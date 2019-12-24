package ru.itis.tictactoe.server.player;

import ru.itis.tictactoe.model.Cell;
import ru.itis.tictactoe.model.Line;

import static ru.itis.tictactoe.util.GameConst.LAYER_SIZE;

public class ComputerPlayer extends Player {
    private int[][] matrix;
    private int enemyFlag;
    private int flag;
    private boolean isComputerFirstPlayer;
    private boolean isComputerMove;

    public ComputerPlayer() {
        super("Computer");
        matrix = new int[LAYER_SIZE][LAYER_SIZE];
        enemyFlag = isComputerFirstPlayer ? -1 : 1;
        flag = isComputerFirstPlayer ? 1 : -1;
        isComputerMove = flag == 1;
    }

    public void setComputerFirstPlayer(boolean computerFirstPlayer) {
        isComputerFirstPlayer = computerFirstPlayer;
    }

    public Cell getNextStep() {
        if (isMapEmpty()) {
            return getRandomCell();
        }

        Line winLine = getWinLine(flag, enemyFlag, 4);
        if (winLine != null) {
            return getFirstFreeCellOnLine(winLine);
        }

        Line enemyWinLine = getWinLine(enemyFlag, flag, 4);
        if (enemyWinLine != null) {
            return getFirstFreeCellOnLine(enemyWinLine);
        }

        winLine = getWinLine(flag, enemyFlag, 0);
        if (winLine != null) {
            return getFirstFreeCellOnLine(winLine);
        }

        return getFirstEmptyCell();
    }

    private Cell getFirstFreeCellOnLine(Line line) {
        Cell cell = new Cell();
        if (line.getType() == 1) {
            for (int i = 0; i < LAYER_SIZE; i++) {
                if (matrix[line.getX()][i] == 0) {
                    cell.setX(line.getX());
                    cell.setY(i);
                    return cell;
                }
            }
        } else if (line.getType() == 2) {
            for (int i = 0; i < LAYER_SIZE; i++) {
                if (matrix[i][line.getY()] == 0) {
                    cell.setX(i);
                    cell.setY(line.getY());
                    return cell;
                }
            }
        } else if (line.getType() == 3) {
            for (int i = 0; i < LAYER_SIZE; i++) {
                if (matrix[i][i] == 0) {
                    cell.setY(i);
                    cell.setX(i);
                    return cell;
                }
            }
        } else {
            for (int i = 0; i < LAYER_SIZE; i++) {
                if (matrix[i][LAYER_SIZE - 1 - i] == 0) {
                    cell.setY(LAYER_SIZE - 1 - i);
                    cell.setX(i);
                    return cell;
                }
            }
        }
        return null;
    }

    private boolean isMapEmpty() {
        for (int i = 0; i < LAYER_SIZE; i++) {
            for (int j = 0; j < LAYER_SIZE; j++) {
                if (matrix[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private Cell getFirstEmptyCell() {
        Cell cell = new Cell();
        for (int i = 0; i < LAYER_SIZE; i++) {
            for (int j = 0; j < LAYER_SIZE; j++) {
                if (matrix[i][j] == 0) {
                    cell.setX(i);
                    cell.setY(j);
                    return cell;
                }
            }
        }
        return cell;
    }

    private Cell getRandomCell() {
        Cell cell = new Cell();
        cell.setX((int) (Math.random() * 4 + 0.5));
        cell.setY((int) (Math.random() * 4 + 0.5));
        while (!isCellFree(cell)) {
            cell.setX((int) (Math.random() * 4 + 0.5));
            cell.setY((int) (Math.random() * 4 + 0.5));
        }
        return cell;
    }

    private boolean isCellFree(Cell cell) {
        return matrix[cell.getX()][cell.getY()] == 0;
    }

    private Line getWinLine(int flag, int enemyFlag, int threshold) {
        Line winLine = new Line();
        boolean isWinLineExist = false;
        int lastCountMove = threshold;
        for (int i = 0; i < LAYER_SIZE; i++) {
            boolean isLineWin = true;
            int countMove = 0;
            for (int j = 0; j < LAYER_SIZE; j++) {
                if (matrix[i][j] == enemyFlag) {
                    isLineWin = false;
                } else if (matrix[i][j] == flag) {
                    countMove += 1;
                }
            }
            if (isLineWin && lastCountMove <= countMove) {
                lastCountMove = countMove;
                winLine.setX(i);
                winLine.setY(0);
                winLine.setType(1);
                isWinLineExist = true;
            }
        }

        for (int i = 0; i < LAYER_SIZE; i++) {
            boolean isLineWin = true;
            int moveOnLine = 0;
            for (int j = 0; j < LAYER_SIZE; j++) {
                if (matrix[j][i] == enemyFlag) {
                    isLineWin = false;
                } else if (matrix[j][i] == flag) {
                    moveOnLine += 1;
                }
            }
            if (isLineWin && lastCountMove <= moveOnLine) {
                lastCountMove = moveOnLine;
                winLine.setX(0);
                winLine.setY(i);
                winLine.setType(2);
                isWinLineExist = true;
            }
        }

        boolean isLineWin = true;
        int moveOnLine = 0;
        for (int i = 0; i < LAYER_SIZE; i++) {
            if (matrix[i][i] == enemyFlag) {
                isLineWin = false;
            } else if (matrix[i][i] == flag) {
                moveOnLine += 1;
            }
        }
        if (isLineWin && lastCountMove <= moveOnLine) {
            winLine.setY(0);
            lastCountMove = moveOnLine;
            isWinLineExist = true;
            winLine.setX(0);
            winLine.setType(3);
        }

        moveOnLine = 0;
        for (int i = 0; i < LAYER_SIZE; i++) {
            if (matrix[LAYER_SIZE - 1 - i][i] == enemyFlag) {
                isLineWin = false;
            } else if (matrix[LAYER_SIZE - 1 - i][i] == flag) {
                moveOnLine += 1;
            }
        }
        if (isLineWin && lastCountMove <= moveOnLine) {
            winLine.setY(LAYER_SIZE - 1);
            winLine.setX(0);
            winLine.setType(4);
            isWinLineExist = true;
        }

        return isWinLineExist ? winLine : null;
    }

    public void setCell(Cell cell) {
        matrix[cell.getX()][cell.getY()] = isComputerMove ? flag : enemyFlag;
        isComputerMove = !isComputerMove;
    }
}
