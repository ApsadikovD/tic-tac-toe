package ru.itis.tictactoe.server.command;

import ru.itis.tictactoe.model.Cell;

public class Move {
    private Cell cell;

    public Move() {
    }

    public Move(Cell cell) {
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }
}
