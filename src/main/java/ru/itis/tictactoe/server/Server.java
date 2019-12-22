package ru.itis.tictactoe.server;

import ru.itis.tictactoe.server.player.Player;
import ru.itis.tictactoe.server.room.GameRoom;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {
    private static List<ClientThread> clientList = new ArrayList<>();
    private static HashMap<Player, GameRoom> gameRoomMap = new HashMap<>();

    private Server() {
    }

    public static synchronized HashMap<Player, GameRoom> getGameRoomMap() {
        return gameRoomMap;
    }

    public static synchronized List<ClientThread> getClientList() {
        return clientList;
    }

    public static void main(String[] args) {
        new Server().start(8001);
    }

    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(8001);
            while (true) {
                new ClientThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
