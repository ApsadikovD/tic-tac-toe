package ru.itis.tictactoe.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ru.itis.tictactoe.client.Client;
import ru.itis.tictactoe.server.command.NewGame;
import ru.itis.tictactoe.server.command.Request;

import java.io.IOException;
import java.util.HashMap;

public class MainScene extends AbstractScene {
    public MainScene(Stage stage, HashMap<String, Object> data) {
        super(stage, data);
    }

    @Override
    public void start() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        Scene scene = new Scene(root);

        Button logout = (Button) root.lookup("#logout");
        Button realPlayer = (Button) root.lookup("#realPlayer");
        Button computerPlayer = (Button) root.lookup("#computer");

        logout.setOnMouseClicked(event -> {
            Request logoutRequest = new Request<>("LOGOUT", null);
            Client.write(logoutRequest);
            Client.finished();
            stage.close();
        });

        realPlayer.setOnMouseClicked(event -> {
            Request newGame = new Request<>("NEW_GAME", new NewGame("REAL_PLAYER"));
            Client.write(newGame);
            MainScene.this.openStage(stage, data, GameScene.class);
        });

        computerPlayer.setOnMouseClicked(event -> {
            Request newGame = new Request<>("NEW_GAME", new NewGame("COMPUTER_PLAYER"));
            Client.write(newGame);
            MainScene.this.openStage(stage, data, GameScene.class);
        });

        stage.setTitle(data.get("name").toString());
        stage.setScene(scene);
        stage.show();
    }
}
