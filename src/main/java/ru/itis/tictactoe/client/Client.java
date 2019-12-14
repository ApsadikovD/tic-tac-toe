package ru.itis.tictactoe.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itis.tictactoe.server.command.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class Client {
    private static HashMap<String, LiveServerData> serverDataObserver;
    private static PrintWriter printWriter;
    private static BufferedReader bufferedReader;
    private static ObjectMapper objectMapper;
    private static boolean isFinish;

    private Client() {
    }

    public static void subscribe(Object observer, LiveServerData onChange) {
        serverDataObserver.put(String.valueOf(observer.hashCode()), onChange);
    }

    public static void unsubscribe(Object observer) {
        serverDataObserver.remove(String.valueOf(observer.hashCode()));
    }

    public static void write(Request request) {
        try {
            printWriter.println(objectMapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void start(String ip, int port) {
        objectMapper = new ObjectMapper();
        serverDataObserver = new HashMap<>();
        try {
            Socket socket = new Socket(ip, port);
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(() -> {
                try {
                    String line;
                    while (!isFinish && (line = bufferedReader.readLine()) != null) {
                        String finalLine = line;
                        serverDataObserver.forEach((k, v) -> v.onChange(finalLine));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void finished() {
        isFinish = true;
    }
}
