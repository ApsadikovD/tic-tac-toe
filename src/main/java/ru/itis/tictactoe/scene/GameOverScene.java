package ru.itis.tictactoe.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.itis.tictactoe.util.GameConst;

import java.io.IOException;
import java.util.HashMap;

public class GameOverScene extends AbstractScene {

    public GameOverScene(Stage stage, HashMap<String, Object> data) {
        super(stage, data);
    }

    @Override
    public void start() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/game-over.fxml"));
        Scene scene = new Scene(root);

        stage.setTitle(data.get("name").toString());
        stage.setScene(scene);
        stage.show();
    }
}
