package ru.itis.tictactoe.server.command;

import ru.itis.tictactoe.server.player.Player;

public class RoomInfo {
    private String gameStatus;

    private String enemy;

    public RoomInfo() {
    }

    public RoomInfo(String gameStatus, String enemy) {
        this.gameStatus = gameStatus;
        this.enemy = enemy;
    }

    public RoomInfo(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getEnemy() {
        return enemy;
    }

    public void setEnemy(String enemy) {
        this.enemy = enemy;
    }
}
