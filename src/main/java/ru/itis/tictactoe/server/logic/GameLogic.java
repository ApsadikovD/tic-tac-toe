package ru.itis.tictactoe.server.logic;

import ru.itis.tictactoe.model.Cell;

import static ru.itis.tictactoe.util.GameConst.LAYER_SIZE;

public class GameLogic {
    /**
     * true - first player make a move
     * false - second player make a move
     */
    private boolean isFirstPlayerMove;
    private int[][] matrix;

    public GameLogic() {
        isFirstPlayerMove = true;
        matrix = new int[LAYER_SIZE][LAYER_SIZE];
    }

    /**
     * @param cell x, y
     * @return 1 someone win, -1 draw, 0 otherwise
     */
    public int nextStep(Cell cell) {
        matrix[cell.getX()][cell.getY()] = isFirstPlayerMove ? 1 : -1;
        isFirstPlayerMove = !isFirstPlayerMove;

        if (isWin(1) || isWin(-1)) {
            return 1;
        } else if (!isDraw()) {
            return -1;
        } else {
            return 0;
        }
    }

    private boolean isDraw() {
        for (int i = 0; i < LAYER_SIZE; i++) {
            for (int j = 0; j < LAYER_SIZE; j++) {
                if (matrix[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCellEmpty(Cell cell) {
        return matrix[cell.getX()][cell.getY()] == 0;
    }

    public boolean isFirstPlayerMover() {
        return isFirstPlayerMove;
    }

    /**
     * Check game map for winnings
     *
     * @param flag -1 - zero, 1 - cross
     * @return boolean
     */
    private boolean isWin(int flag) {
        /*
         * check horizontal line
         */
        for (int i = 0; i < LAYER_SIZE; i++) {
            boolean isWin = true;
            for (int j = 0; j < LAYER_SIZE; j++) {
                if (matrix[i][j] != flag) {
                    isWin = false;
                }
            }
            if (isWin) return true;
        }

        /*
         * check vertical line
         */
        for (int i = 0; i < LAYER_SIZE; i++) {
            boolean isWin = true;
            for (int j = 0; j < LAYER_SIZE; j++) {
                if (matrix[j][i] != flag) {
                    isWin = false;
                }
            }
            if (isWin) return true;
        }

        /*
         * check main diagonal
         */
        boolean isMainDiagonalWin = true;
        for (int i = 0; i < LAYER_SIZE; i++) {
            if (matrix[i][i] != flag) {
                isMainDiagonalWin = false;
            }
        }
        if (isMainDiagonalWin) return true;

        /*
         * check side diagonal
         */
        boolean isSideDiagonalWin = true;
        for (int i = 0; i < LAYER_SIZE; i++) {
            if (matrix[i][matrix.length - i - 1] != flag) {
                isSideDiagonalWin = false;
            }
        }
        return isSideDiagonalWin;
    }
}
