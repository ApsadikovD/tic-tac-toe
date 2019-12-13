package ru.itis.tictactoe.base;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import static ru.itis.tictactoe.util.GameConst.*;


public class GameMapDrawer implements IDrawer {
    private GraphicsContext context;

    public GameMapDrawer(GraphicsContext context) {
        this.context = context;
    }

    @Override
    public void drawMap() {
        context.setFill(Color.WHITE);
        context.setStroke(Color.GRAY);
        for (int i = 0; i < LAYER_SIZE; i++) {
            for (int j = 0; j < LAYER_SIZE; j++) {
                context.fillRect(CELL_SIZE * i, CELL_SIZE * j, CELL_SIZE, CELL_SIZE);
                context.strokeRect(CELL_SIZE * i, CELL_SIZE * j, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    @Override
    public void drawImage(String imgName, int cellX, int cellY) {
        Image image = new Image(getClass().getResourceAsStream("/image/" + imgName));
        context.drawImage(image,
                (CELL_SIZE - IMAGE_SIZE) / 2 + CELL_SIZE * cellX,
                (CELL_SIZE - IMAGE_SIZE) / 2 + CELL_SIZE * cellY,
                IMAGE_SIZE, IMAGE_SIZE);
    }
}
