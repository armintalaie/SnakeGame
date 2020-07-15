package view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.SnakeCell;

import java.awt.*;

public class SnakeBody {
    SnakeCell snakeCell;
    SnakeBody next;
    Rectangle shape;

    SnakeBody(SnakeCell snakeCell, boolean isHeadOrTail) {
        shape = new Rectangle(40,40, Color.rgb(83, 99, 219));
        if (true) {

            shape.setStyle("-fx-background-radius: 10px 10px 10px 10px;");
        }
        this.snakeCell = snakeCell;
        shape.setX((snakeCell.getX() ) * 40);
        shape.setY((snakeCell.getY() ) * 40);

    }

}
