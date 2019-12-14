package ru.itis.tictactoe.server.command;

public class Request<T> {
    private String header;

    private T payload;

    public Request() {
    }

    public Request(String header, T payload) {
        this.header = header;
        this.payload = payload;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
