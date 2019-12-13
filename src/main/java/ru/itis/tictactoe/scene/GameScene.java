package ru.itis.tictactoe.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import ru.itis.tictactoe.base.GameMapDrawer;
import ru.itis.tictactoe.model.Cell;
import ru.itis.tictactoe.util.GameMapHelper;

import java.io.IOException;
import java.util.HashMap;

public class GameScene extends AbstractScene {

    public GameScene(Stage stage, HashMap<String, Object> data) {
        super(stage, data);
    }

    @Override
    public void start() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/tic-tac-toe.fxml"));
        Scene scene = new Scene(root);

        Canvas canvas = (Canvas) root.lookup("#canvas");
        GraphicsContext context = canvas.getGraphicsContext2D();

        GameMapDrawer gameMapDrawer = new GameMapDrawer(context);
        gameMapDrawer.drawMap();

        final Integer[] i = {0};

        canvas.setOnMouseClicked(event -> {
            Cell cell = GameMapHelper.defineCell(event.getX(), event.getY());
            gameMapDrawer.drawImage("cross.png", cell.getX(), cell.getY());
            i[0] += 1;
            if (i[0] >= 1) {
                GameScene.this.openStage(stage, data, GameOverScene.class);
            }
        });

        stage.setTitle(data.get("name").toString());
        stage.setScene(scene);
        stage.show();
    }
}
