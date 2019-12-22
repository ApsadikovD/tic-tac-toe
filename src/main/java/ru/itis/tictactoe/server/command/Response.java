package ru.itis.tictactoe.server.command;

public class Response<T> {
    private String header;

    private T data;

    public Response() {
    }

    public Response(String header, T data) {
        this.header = header;
        this.data = data;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
