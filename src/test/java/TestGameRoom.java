import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.itis.tictactoe.model.Cell;
import ru.itis.tictactoe.server.player.Player;
import ru.itis.tictactoe.server.player.RealPlayer;
import ru.itis.tictactoe.server.room.GameRoom;

public class TestGameRoom {
    @Test
    public void testEnterRoom() {
        GameRoom gameRoom = new GameRoom();
        Player player1 = new RealPlayer();
        Player player2 = new RealPlayer();
        Player player3 = new RealPlayer();
        gameRoom.join(player1);
        gameRoom.join(player2);
        Assertions.assertThrows(IllegalArgumentException.class, () -> gameRoom.join(player3));
    }

    @Test
    public void testNextStep() {
        GameRoom gameRoom = new GameRoom();
        Player player1 = new RealPlayer();
        Player player2 = new RealPlayer();
        gameRoom.join(player1);
        gameRoom.join(player2);

        if (gameRoom.getFirstPlayer().equals(player1)) {
            Assertions.assertThrows(IllegalArgumentException.class, () -> gameRoom.nextStep(player2, new Cell()));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> gameRoom.nextStep(player1, new Cell()));
        }
    }
}