import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.itis.tictactoe.model.Cell;
import ru.itis.tictactoe.server.player.ComputerPlayer;

import java.lang.reflect.Field;

public class TestComputerPlayer {
    @Test
    public void testComputerPlayerStep() {
        try {
            ComputerPlayer computerPlayer = new ComputerPlayer();
            computerPlayer.setComputerFirstPlayer(true);

            Field field = computerPlayer.getClass().getDeclaredField("matrix");
            field.setAccessible(true);
            int[][] matrix = (int[][]) field.get(computerPlayer);

            matrix[0][0] = -1;
            matrix[0][4] = -1;
            matrix[0][2] = -1;
            matrix[0][3] = -1;

            Cell cell = computerPlayer.getNextStep();
            Assertions.assertEquals(0, cell.getX());
            Assertions.assertEquals(1, cell.getY());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
