package ru.itis.tictactoe;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.itis.tictactoe.scene.GameScene;
import ru.itis.tictactoe.util.GameConst;

import java.util.HashMap;

public class App extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        HashMap<String, Object> data = new HashMap<>();
        data.put("name", GameConst.GAME_NAME);
        GameScene gameStage = new GameScene(primaryStage, data);
        gameStage.start();
    }
}
