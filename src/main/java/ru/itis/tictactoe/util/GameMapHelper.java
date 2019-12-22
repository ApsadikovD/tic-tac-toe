package ru.itis.tictactoe.util;

import ru.itis.tictactoe.model.Cell;

import static ru.itis.tictactoe.util.GameConst.CELL_SIZE;

public class GameMapHelper {
    public static Cell defineCell(double x, double y) {
        Cell cell = new Cell();
        cell.setX(defineCellByCoordinate(x));
        cell.setY(defineCellByCoordinate(y));
        return cell;
    }

    private static int defineCellByCoordinate(double coordinate) {
        int cell = 0;
        while (coordinate - CELL_SIZE > 0) {
            coordinate = coordinate - CELL_SIZE;
            cell++;
        }
        return cell;
    }

    public static boolean isCellFree(int[][] matrix, Cell cell) {
        return matrix[cell.getX()][cell.getY()] == 0;
    }
}
