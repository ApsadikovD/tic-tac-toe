package ru.itis.tictactoe.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itis.tictactoe.model.Cell;
import ru.itis.tictactoe.server.command.*;
import ru.itis.tictactoe.server.player.ComputerPlayer;
import ru.itis.tictactoe.server.player.Player;
import ru.itis.tictactoe.server.player.RealPlayer;
import ru.itis.tictactoe.server.room.GameRoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Optional;

public class ClientThread extends Thread {
    private BufferedReader reader;
    private PrintWriter writer;
    private ObjectMapper objectMapper;
    private Player player;
    private boolean isLogout;

    public ClientThread(Socket socket) throws IOException {
        objectMapper = new ObjectMapper();
        writer = new PrintWriter(socket.getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private void write(Object o) {
        try {
            writer.println(objectMapper.writeValueAsString(o));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String line;
        try {
            while (!isLogout && (line = reader.readLine()) != null) {
                Request request = objectMapper.readValue(line, Request.class);
                switch (request.getHeader()) {
                    case "LOGIN":
                        Request<Login> login = objectMapper.readValue(line, new TypeReference<Request<Login>>() {
                        });
                        player = new RealPlayer(login.getPayload().getLogin());
                        Server.getClientList().add(this);
                        break;
                    case "LOGOUT":
                        Server.getClientList().remove(this);
                        writer.println();
                        isLogout = true;
                        break;
                    case "NEW_GAME":
                        if (Server.getGameRoomMap().get(player) != null) return;

                        Request<NewGame> newGame = objectMapper.readValue(line, new TypeReference<Request<NewGame>>() {
                        });

                        Optional<Map.Entry<Player, GameRoom>> gameRoomEntry = Server.getGameRoomMap().entrySet().stream()
                                .filter(o -> !o.getValue().isStart()).findFirst();

                        if (!gameRoomEntry.isPresent()) {
                            GameRoom gameRoom = new GameRoom();
                            gameRoom.join(player);
                            Server.getGameRoomMap().put(player, gameRoom);

                            if (newGame.getPayload().getGameType().equals("COMPUTER_PLAYER")) {
                                ComputerPlayer computerPlayer = new ComputerPlayer();
                                gameRoom.join(computerPlayer);

                                write(new Response<>("ROOM_INFO",
                                        new RoomInfo("READY", computerPlayer.getName())));

                                if (gameRoom.getFirstPlayer().equals(player)) {
                                    write(new Response<>("FIRST"));
                                } else {
                                    computerPlayer.setComputerFirstPlayer(true);
                                    Cell computerStep = computerPlayer.getNextStep();
                                    write(new Response<>("SECOND"));
                                    write(new Response<>("ENEMY_MOVE", new Move(computerStep)));
                                    write(new Response<>("NEXT_MOVE"));

                                    gameRoom.nextStep(gameRoom.getFirstPlayer(), computerStep);
                                    computerPlayer.setCell(computerStep);
                                }
                            } else {
                                write(new Response<>("ROOM_INFO", new RoomInfo("WAIT", null)));
                            }
                        } else {
                            GameRoom gameRoom = gameRoomEntry.get().getValue();
                            gameRoom.join(player);
                            Server.getGameRoomMap().put(player, gameRoom);

                            Player enemy = gameRoom.getFirstPlayer().equals(player)
                                    ? gameRoom.getSecondPlayer() : gameRoom.getFirstPlayer();
                            ClientThread enemyClient = Server.getClientList().stream()
                                    .filter(client -> client.getPlayer().equals(enemy)).findFirst().get();

                            write(new Response<>("ROOM_INFO", new RoomInfo("READY", enemy.getName())));

                            enemyClient.write(
                                    new Response<>("ROOM_INFO", new RoomInfo("READY", player.getName())));

                            if (gameRoom.getFirstPlayer().equals(player)) {
                                write(new Response<>("FIRST"));
                                enemyClient.write(new Response<>("SECOND"));
                            } else {
                                enemyClient.write(new Response<>("FIRST"));
                                write(new Response<>("SECOND"));
                            }
                        }
                        break;
                    case "MOVE":
                        Request<Move> move = objectMapper.readValue(line, new TypeReference<Request<Move>>() {
                        });
                        Cell cell = move.getPayload().getCell();
                        GameRoom gameRoom = Server.getGameRoomMap().get(player);
                        if (gameRoom.isCellEmpty(cell)) {
                            int res = gameRoom.nextStep(player, cell);


                            Player enemy = gameRoom.getFirstPlayer().equals(player)
                                    ? gameRoom.getSecondPlayer() : gameRoom.getFirstPlayer();
                            if (enemy instanceof ComputerPlayer) {
                                if (res == 0) {
                                    ((ComputerPlayer) enemy).setCell(cell);
                                    Cell computerStep = ((ComputerPlayer) enemy).getNextStep();
                                    res = gameRoom.nextStep(enemy, computerStep);
                                    ((ComputerPlayer) enemy).setCell(computerStep);
                                    if (res == 0) {
                                        write(new Response<>("ENEMY_MOVE", new Move(computerStep)));
                                        write(new Response<>("NEXT_MOVE"));
                                    } else if (res == -1) {
                                        write(new Response<>("DRAW", new Move(computerStep)));
                                        Server.getGameRoomMap().remove(player);
                                        Server.getGameRoomMap().remove(enemy);
                                    } else {
                                        write(new Response<>("LOSE", new Move(computerStep)));
                                    }
                                } else if (res == -1) {
                                    write(new Response<>("DRAW"));
                                    Server.getGameRoomMap().remove(player);
                                } else {
                                    write(new Response<>("WIN"));
                                    Server.getGameRoomMap().remove(player);
                                    Server.getGameRoomMap().remove(enemy);
                                }
                            } else {
                                ClientThread enemyClient = Server.getClientList().stream()
                                        .filter(client -> client.getPlayer().equals(enemy)).findFirst().get();
                                if (res == 0) {
                                    enemyClient.write(new Response<>("ENEMY_MOVE", new Move(cell)));
                                    enemyClient.write(new Response<>("NEXT_MOVE"));
                                } else if (res == -1) {
                                    enemyClient.write(new Response<>("DRAW", new Move(cell)));
                                    write(new Response<>("DRAW"));
                                    Server.getGameRoomMap().remove(player);
                                    Server.getGameRoomMap().remove(enemy);
                                } else {
                                    enemyClient.write(new Response<>("LOSE", new Move(cell)));
                                    write(new Response<>("WIN"));
                                    Server.getGameRoomMap().remove(player);
                                    Server.getGameRoomMap().remove(enemy);
                                }
                            }
                        }
                        break;
                }
            }
        } catch (IOException e) {
            Server.getClientList().remove(this);
            Server.getGameRoomMap().remove(player);
        }
    }

    public Player getPlayer() {
        return player;
    }
}
