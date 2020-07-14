package view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.SnakeCell;

import java.awt.*;

public class SnakeBody {
    SnakeCell snakeCell;
    SnakeBody next;
    Rectangle shape = new Rectangle(40,40, Color.BLUE);

    SnakeBody(SnakeCell snakeCell, boolean isHeadOrTail) {
        if (true) {
            shape.setStyle("-fx-background-radius: 100 10 10 10; -fx-border-radius: 100 10 10 10;");
            shape.setFill(Color.BLUEVIOLET);
        }
        this.snakeCell = snakeCell;
        shape.setX((snakeCell.getX() ) * 40);
        shape.setY((snakeCell.getY() ) * 40);

    }

}
