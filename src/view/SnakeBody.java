package view;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.SnakeCell;

import java.awt.*;

public class SnakeBody {
    SnakeCell snakeCell;
    SnakeBody next;
    Rectangle shape;

    SnakeBody(SnakeCell snakeCell, int switchColor) {
        shape = new Rectangle(40, 40, Color.rgb(24, 87, 122));

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness((double) switchColor / 90);
        Effect e = createShadowedBox(5, 5, 5, 5, 40);
        colorAdjust.setInput(e);
        this.shape.setEffect(colorAdjust);
        this.snakeCell = snakeCell;
        shape.setX((snakeCell.getX()) * 40);
        shape.setY((snakeCell.getY()) * 40);

    }

    private Effect createShadowedBox(double shadowWidth, double shadowHeight, double offsetX, double offsetY, double radius) {
        DropShadow e = new DropShadow();
        e.setColor(Color.rgb(24, 87, 122, 0.8));
        e.setWidth(shadowWidth);
        e.setHeight(shadowHeight);
        e.setOffsetX(offsetX);
        e.setOffsetY(offsetY);
        e.setRadius(radius);
        return e;
    }

}
