package ru.itis.tictactoe.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itis.tictactoe.server.command.Login;
import ru.itis.tictactoe.server.command.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private BufferedReader reader;
    private PrintWriter writer;
    private ObjectMapper objectMapper;
    private String userName;
    private boolean isLogout;

    public ClientThread(Socket socket) throws IOException {
        objectMapper = new ObjectMapper();
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
        new Thread(() -> {
            String line;
            try {
                while (!isLogout && (line = reader.readLine()) != null) {
                    Request request = objectMapper.readValue(line, Request.class);
                    switch (request.getHeader()) {
                        case "login":
                            Request<Login> login = objectMapper.readValue(line, new TypeReference<Request<Login>>() {
                            });
                            userName = login.getPayload().getLogin();
                            Server.getClientList().add(this);
                            break;
                        case "logout":
                            Server.getClientList().remove(this);
                            isLogout = true;
                            break;
                    }
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }).start();
    }
}
