package ru.itis.tictactoe.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itis.tictactoe.server.command.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Client {
    private static HashMap<String, LiveServerData> serverDataObserver;
    private static PrintWriter writer;
    private static BufferedReader reader;
    private static ObjectMapper objectMapper;
    private static boolean isFinish;
    private static ArrayList<String> unhandledResponse;

    private Client() {
    }

    public static void subscribe(Object observer, LiveServerData onChange) {
        serverDataObserver.put(String.valueOf(observer.hashCode()), onChange);
        if (unhandledResponse.size() != 0) {
            for (String unhandledResponse : unhandledResponse) {
                onChange.onChange(unhandledResponse);
            }
            unhandledResponse.clear();
        }
    }

    public static void unsubscribe(Object observer) {
        serverDataObserver.remove(String.valueOf(observer.hashCode()));
    }

    public static void write(Request request) {
        try {
            writer.println(objectMapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void start(String ip, int port) {
        objectMapper = new ObjectMapper();
        serverDataObserver = new HashMap<>();
        unhandledResponse = new ArrayList<>();
        try {
            Socket socket = new Socket(ip, port);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(() -> {
                try {
                    String line;
                    while (!isFinish && (line = reader.readLine()) != null) {
                        String finalLine = line;
                        if (serverDataObserver.size() == 0) {
                            unhandledResponse.add(finalLine);
                        } else {
                            if (line.length() != 0) {
                                serverDataObserver.forEach((k, v) -> v.onChange(finalLine));
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T decodeJson(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void finished() {
        isFinish = true;
    }
}
