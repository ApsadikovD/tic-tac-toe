package ru.itis.tictactoe.scene;

import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public abstract class AbstractScene {
    protected Stage stage;
    protected HashMap<String, Object> data;

    public AbstractScene(Stage stage, HashMap<String, Object> data) {
        this.stage = stage;
        this.data = data;
    }

    public abstract void start() throws IOException;

    public void openStage(Stage stage, HashMap<String, Object> data,  Class<? extends AbstractScene> scene) {
        try {
            scene.getConstructor(Stage.class, HashMap.class).newInstance(stage, data).start();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            System.out.println(e.toString());
            throw new IllegalArgumentException("AbstractScene not contains default constructor");
        } catch (IOException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
}
