package ru.itis.tictactoe.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itis.tictactoe.model.Cell;
import ru.itis.tictactoe.server.command.*;
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
                        Optional<Map.Entry<Player, GameRoom>> gameRoomEntry = Server.getGameRoomMap().entrySet().stream()
                                .filter(o -> !o.getValue().isStart()).findFirst();
                        if (!gameRoomEntry.isPresent()) {
                            GameRoom gameRoom = new GameRoom();
                            gameRoom.join(player);
                            Server.getGameRoomMap().put(player, gameRoom);

                            write(new Response<>("ROOM_INFO", new RoomInfo("WAIT", null)));
                        } else {
                            GameRoom gameRoom = gameRoomEntry.get().getValue();
                            gameRoom.join(player);
                            Player enemy = gameRoom.getFirstPlayer().equals(player)
                                    ? gameRoom.getSecondPlayer() : gameRoom.getFirstPlayer();
                            write(new Response<>("ROOM_INFO", new RoomInfo("READY", enemy.getName())));

                            ClientThread enemyClient = Server.getClientList().stream()
                                    .filter(client -> client.getPlayer().equals(enemy)).findFirst().get();
                            enemyClient.write(
                                    new Response<>("ROOM_INFO", new RoomInfo("READY", player.getName())));

                            if (gameRoom.getFirstPlayer().equals(player)) {
                                write(new Response<>("FIRST", null));
                                enemyClient.write(new Response<>("SECOND", null));
                            } else {
                                enemyClient.write(new Response<>("FIRST", null));
                                write(new Response<>("SECOND", null));
                            }
                            Server.getGameRoomMap().put(player, gameRoom);
                        }
                        break;
                    case "MOVE":
                        Request<Move> move = objectMapper.readValue(line, new TypeReference<Request<Move>>() {
                        });
                        Cell cell = move.getPayload().getCell();
                        GameRoom gameRoom = Server.getGameRoomMap().get(player);
                        if (gameRoom.isCellFree(cell)) {
                            int res = gameRoom.nextStep(player, cell);

                            Player enemy = gameRoom.getFirstPlayer().equals(player)
                                    ? gameRoom.getSecondPlayer() : gameRoom.getFirstPlayer();
                            ClientThread enemyClient = Server.getClientList().stream()
                                    .filter(client -> client.getPlayer().equals(enemy)).findFirst().get();
                            if (res == 0) {
                                enemyClient.write(new Response<>("ENEMY_MOVE", new Move(cell)));
                                enemyClient.write(new Response<>("NEXT_MOVE", null));
                            } else if (res == -1) {
                                enemyClient.write(new Response<>("DRAW", new Move(cell)));
                                write(new Response<>("DRAW", null));
                                Server.getGameRoomMap().remove(player);
                                Server.getGameRoomMap().remove(enemy);
                            } else {
                                enemyClient.write(new Response<>("LOSE", null));
                                write(new Response<>("WIN", null));
                                Server.getGameRoomMap().remove(player);
                                Server.getGameRoomMap().remove(enemy);
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
