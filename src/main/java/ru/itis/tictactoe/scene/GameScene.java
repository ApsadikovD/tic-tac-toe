package ru.itis.tictactoe.scene;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.itis.tictactoe.client.Client;
import ru.itis.tictactoe.graphics.GameMapDrawer;
import ru.itis.tictactoe.model.Cell;
import ru.itis.tictactoe.server.command.Move;
import ru.itis.tictactoe.server.command.Request;
import ru.itis.tictactoe.server.command.Response;
import ru.itis.tictactoe.server.command.RoomInfo;
import ru.itis.tictactoe.util.GameMapHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import static ru.itis.tictactoe.util.GameConst.LAYER_SIZE;

public class GameScene extends AbstractScene {

    public GameScene(Stage stage, HashMap<String, Object> data) {
        super(stage, data);
    }

    @Override
    public void start() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/tic-tac-toe.fxml"));
        Scene scene = new Scene(root);

        Canvas canvas = (Canvas) root.lookup("#canvas");
        Text text = (Text) root.lookup("#text");
        Text playerMove = (Text) root.lookup("#player");
        AtomicReference<Boolean> mapDisable = new AtomicReference<>(true);
        AtomicReference<Boolean> first = new AtomicReference<>(false);
        int[][] matrix = new int[LAYER_SIZE][LAYER_SIZE];

        GraphicsContext context = canvas.getGraphicsContext2D();

        GameMapDrawer gameMapDrawer = new GameMapDrawer(context);
        gameMapDrawer.drawMap();

        RoomInfo roomInfo = (RoomInfo) data.get("ROOM_INFO");
        if (roomInfo.getGameStatus().equals("READY")) {
            text.setText("You play with " + roomInfo.getEnemy());
        }

        Client.subscribe(this, str -> {
            Response response = Client.decodeJson(str, new TypeReference<>() {
            });
            if (response.getHeader().equals("ROOM_INFO")) {
                Platform.runLater(() -> {
                    String enemy = Client.decodeJson(str, new TypeReference<Response<RoomInfo>>() {
                    }).getData().getEnemy();
                    if (enemy != null) text.setText("You play with " + enemy);
                });
            } else if (response.getHeader().equals("NEXT_MOVE")) {
                Platform.runLater(() -> {
                    mapDisable.set(false);
                    playerMove.setText("Your move");
                });
            } else if (response.getHeader().equals("ENEMY_MOVE")) {
                Move move = Client.decodeJson(str, new TypeReference<Response<Move>>() {
                }).getData();
                Platform.runLater(() -> {
                    matrix[move.getCell().getX()][move.getCell().getY()] = 1;
                    gameMapDrawer.drawImage(!first.get() ? "cross.png" : "zero.png",
                            move.getCell().getX(), move.getCell().getY());
                });
            } else if (response.getHeader().equals("FIRST")) {
                first.set(true);
                Platform.runLater(() -> {
                    mapDisable.set(false);
                    playerMove.setText("Your move");
                });
            } else if (response.getHeader().equals("SECOND")) {
                first.set(false);
            } else if (response.getHeader().equals("DRAW")) {
                Platform.runLater(() -> {
                    data.put("text", "Draw");
                    Client.unsubscribe(GameScene.this);
                    GameScene.this.openStage(stage, data, GameOverScene.class);
                });
            } else if (response.getHeader().equals("WIN")) {
                Platform.runLater(() -> {
                    data.put("text", "You win");
                    Client.unsubscribe(GameScene.this);
                    GameScene.this.openStage(stage, data, GameOverScene.class);
                });
            } else if (response.getHeader().equals("LOSE")) {
                Platform.runLater(() -> {
                    data.put("text", "You lose");
                    Client.unsubscribe(GameScene.this);
                    GameScene.this.openStage(stage, data, GameOverScene.class);
                });
            }
        });

        canvas.setOnMouseClicked(event -> {
            if (!mapDisable.get()) {
                Cell cell = GameMapHelper.defineCell(event.getX(), event.getY());
                if (GameMapHelper.isCellFree(matrix, cell)) {
                    matrix[cell.getX()][cell.getY()] = 1;
                    gameMapDrawer.drawImage(first.get() ? "cross.png" : "zero.png", cell.getX(), cell.getY());
                    Client.write(new Request<>("MOVE", new Move(cell)));
                    playerMove.setText("Expect the opponent's move");
                    mapDisable.set(true);
                }
            }
        });

        stage.setTitle(data.get("name").toString());
        stage.setScene(scene);
        stage.show();
    }
}
