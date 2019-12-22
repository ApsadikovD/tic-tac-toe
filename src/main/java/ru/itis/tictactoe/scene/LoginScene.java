package ru.itis.tictactoe.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.itis.tictactoe.client.Client;
import ru.itis.tictactoe.server.command.Login;
import ru.itis.tictactoe.server.command.Request;

import java.io.IOException;
import java.util.HashMap;

public class LoginScene extends AbstractScene {
    public LoginScene(Stage stage, HashMap<String, Object> data) {
        super(stage, data);
    }

    @Override
    public void start() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(root);

        TextField username = (TextField) root.lookup("#name");
        Button login = (Button) root.lookup("#login");

        login.setOnMouseClicked(event -> {
            Request<Login> loginRequest = new Request<>("LOGIN", new Login(username.getText()));
            Client.write(loginRequest);
            LoginScene.this.openStage(stage, data, MainScene.class);
        });

        stage.setTitle(data.get("name").toString());
        stage.setScene(scene);
        stage.show();
    }
}
