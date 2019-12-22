package ru.itis.tictactoe.scene;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ru.itis.tictactoe.client.Client;
import ru.itis.tictactoe.server.command.NewGame;
import ru.itis.tictactoe.server.command.Request;
import ru.itis.tictactoe.server.command.Response;
import ru.itis.tictactoe.server.command.RoomInfo;

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

        Client.subscribe(this, str -> {
            Response response = Client.decodeJson(str, new TypeReference<Response>() {
            });
            if (response.getHeader().equals("ROOM_INFO")) {
                Platform.runLater(() -> {
                    RoomInfo roomInfo = Client.decodeJson(str, new TypeReference<Response<RoomInfo>>() {
                    }).getData();
                    data.put("ROOM_INFO", roomInfo);
                    Client.unsubscribe(MainScene.this);
                    MainScene.this.openStage(stage, data, GameScene.class);
                });
            }
        });

        realPlayer.setOnMouseClicked(event -> {
            Request newGame = new Request<>("NEW_GAME", new NewGame("REAL_PLAYER"));
            Client.write(newGame);
        });

        computerPlayer.setOnMouseClicked(event -> {
            Request newGame = new Request<>("NEW_GAME", new NewGame("COMPUTER_PLAYER"));
            Client.write(newGame);
        });

        stage.setTitle(data.get("name").toString());
        stage.setScene(scene);
        stage.show();
    }
}
