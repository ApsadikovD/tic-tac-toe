
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.itis.tictactoe.model.Cell;
import ru.itis.tictactoe.util.GameMapHelper;

public class TestGameMapHelper {
    @Test
    public void testDefineCell00() {
        Cell expected = new Cell();
        expected.setX(0);
        expected.setY(0);
        Cell result = GameMapHelper.defineCell(25, 25);
        Assertions.assertEquals(expected.getX(), result.getX());
        Assertions.assertEquals(expected.getY(), result.getY());
    }

    @Test
    public void testDefineCell23() {
        Cell expected = new Cell();
        expected.setX(2);
        expected.setY(3);
        Cell result = GameMapHelper.defineCell(175, 125);
        Assertions.assertEquals(expected.getX(), result.getX());
        Assertions.assertEquals(expected.getY(), result.getY());
    }
}
