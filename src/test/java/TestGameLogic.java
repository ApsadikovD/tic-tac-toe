
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.itis.tictactoe.server.logic.GameLogic;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestGameLogic {
    @Test
    public void testGameStatusDraw() {
        GameLogic gameLogic = new GameLogic();
        try {
            Method method = gameLogic.getClass().getDeclaredMethod("isDraw");
            method.setAccessible(true);
            Field field = gameLogic.getClass().getDeclaredField("matrix");
            field.setAccessible(true);
            int[][] matrix = (int[][]) field.get(gameLogic);

            matrix[0][0] = 1;
            matrix[0][1] = 2;
            matrix[0][2] = 1;
            matrix[0][3] = 2;
            matrix[0][4] = 1;

            matrix[1][0] = 2;
            matrix[1][1] = 1;
            matrix[1][2] = 2;
            matrix[1][3] = 1;
            matrix[1][4] = 2;

            matrix[2][0] = 1;
            matrix[2][1] = 2;
            matrix[2][2] = 1;
            matrix[2][3] = 2;
            matrix[2][4] = 1;

            matrix[3][0] = 2;
            matrix[3][1] = 1;
            matrix[3][2] = 2;
            matrix[3][3] = 1;
            matrix[3][4] = 2;

            matrix[4][0] = 1;
            matrix[4][1] = 2;
            matrix[4][2] = 1;
            matrix[4][3] = 2;
            matrix[4][4] = 1;

            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) {
                    System.out.print(matrix[i][j] + " ");
                }
                System.out.println();
            }

            Assertions.assertEquals(true, method.invoke(gameLogic));
        } catch (NoSuchMethodException | IllegalAccessException | NoSuchFieldException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGameStatusWin() {
        GameLogic gameLogic = new GameLogic();
        try {
            Method method = gameLogic.getClass().getDeclaredMethod("isWin", int.class);
            method.setAccessible(true);
            Field field = gameLogic.getClass().getDeclaredField("matrix");
            field.setAccessible(true);
            int[][] matrix = (int[][]) field.get(gameLogic);

            matrix[0][0] = 1;
            matrix[0][1] = -1;
            matrix[0][2] = 1;
            matrix[0][3] = 1;
            matrix[0][4] = 1;

            matrix[1][0] = -1;
            matrix[1][3] = 1;
            matrix[1][4] = -1;

            matrix[2][3] = 1;

            matrix[3][0] = -1;
            matrix[3][1] = 1;
            matrix[3][2] = 1;
            matrix[3][3] = 1;
            matrix[3][4] = -1;

            matrix[4][3] = 1;

            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) {
                    System.out.print(matrix[i][j] + "   ");
                }
                System.out.println();
            }

            Assertions.assertEquals(true, method.invoke(gameLogic, 1));
        } catch (NoSuchMethodException | IllegalAccessException | NoSuchFieldException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGameStatusProgress() {
        GameLogic gameLogic = new GameLogic();
        try {
            Method method = gameLogic.getClass().getDeclaredMethod("isWin", int.class);
            method.setAccessible(true);
            Field field = gameLogic.getClass().getDeclaredField("matrix");
            field.setAccessible(true);
            int[][] matrix = (int[][]) field.get(gameLogic);

            matrix[0][0] = 1;
            matrix[0][1] = -1;
            matrix[0][2] = 1;
            matrix[0][3] = 1;
            matrix[0][4] = 1;

            matrix[1][0] = -1;
            matrix[1][3] = 1;
            matrix[1][4] = -1;

            matrix[2][3] = 1;

            matrix[4][3] = 1;

            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) {
                    System.out.print(matrix[i][j] + "   ");
                }
                System.out.println();
            }

            Assertions.assertEquals(false, method.invoke(gameLogic, 1));
        } catch (NoSuchMethodException | IllegalAccessException | NoSuchFieldException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
