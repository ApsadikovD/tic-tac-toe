package ru.itis.tictactoe;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.itis.tictactoe.client.Client;
import ru.itis.tictactoe.scene.LoginScene;
import ru.itis.tictactoe.util.GameConst;

import java.util.HashMap;

public class App extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        Client.start("127.0.0.1", 8001);

        HashMap<String, Object> data = new HashMap<>();
        data.put("name", GameConst.GAME_NAME);
        LoginScene loginScene = new LoginScene(primaryStage, data);
        loginScene.start();

    }
}
