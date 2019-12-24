package ru.itis.tictactoe.model;

public class Line {
    private int x;
    private int y;
    private int type;

    public Line(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public Line() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
