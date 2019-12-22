package ru.itis.tictactoe.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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

        Text text = (Text) root.lookup("#text");
        Button main = (Button) root.lookup("#main");

        text.setText(data.get("text").toString());

        main.setOnMouseClicked(event -> {
            GameOverScene.this.openStage(stage, data, MainScene.class);
        });

        stage.setTitle(data.get("name").toString());
        stage.setScene(scene);
        stage.show();
    }
}
