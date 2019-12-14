package ru.itis.tictactoe.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ru.itis.tictactoe.client.Client;
import ru.itis.tictactoe.server.command.Request;

import java.io.IOException;
import java.util.HashMap;

public class GameRoomScene extends AbstractScene {
    public GameRoomScene(Stage stage, HashMap<String, Object> data) {
        super(stage, data);
    }

    @Override
    public void start() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/game-room.fxml"));
        Scene scene = new Scene(root);

        Button logout = (Button) root.lookup("#logout");

        logout.setOnMouseClicked(event -> {
            Request logoutRequest = new Request<>("logout", null);
            Client.write(logoutRequest);
            Client.finished();
            stage.close();
        });

        stage.setTitle(data.get("name").toString());
        stage.setScene(scene);
        stage.show();
    }
}
