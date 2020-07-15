package view;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.GameMap;
import model.SnakeCell;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

//TODO: add listeners
//TODO: add starting screen
//TODO: cleanup code
//  TODO: constants for width and height
//  TODO: shorten methods, (move some methods out of the GUI class)
//TODO: add transition animation
//-------------------------------
//TODO: show ranking
//-------------------------------
//TODO: add different shapes for snake's tail and head
//TODO: snake eyes
//TODO: win lose and trap sounds
//TODO: bouncy goal
//-------------------------------
//TODO: add top bar for score and lives [done]
//  TODO: colorful grid [done]

public class GUI extends Application {

    public enum Dir {UP, LEFT, RIGHT, DOWN, NONE}

    private final int WIDTH = 15;
    private final int HEIGHT = 15;
    private final int PAGEWIDTH = 600;
    private final int PAGEHEIGHT = 600;

    private Scene scene;
    private GameMap gameMap;
    GridPane gridMap = new GridPane();

    boolean resume = true;
    private Dir curr = Dir.NONE;
    Label level;
    HBox topBar;
    BorderPane borderPane;
    Pane root = new Pane();

    // head and tail of snake shapes on GUI
    private SnakeBody headOfSnake;
    private SnakeBody tailOfSnake;

    // snake head and tail cells
    private SnakeCell head;
    private SnakeCell tail;

    // variables to track moving transitions
    private SnakeCell toMove = null;
    private boolean moving = false;

    @Override
    public void start(Stage primaryStage) throws Exception {

        borderPane = new BorderPane();
        topBar = new HBox();

        borderPane.setCenter(root);
        borderPane.setTop(topBar);


        //updateGrid(pane);
        scene = new Scene(borderPane, PAGEWIDTH, PAGEHEIGHT);
        createGame(gridMap);


        scene.getStylesheets().add("view/stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.show();

        //playGame();
        root.setPrefWidth(PAGEWIDTH);
        root.setPrefHeight(PAGEHEIGHT);
        //playGame();

        //Instantiating the path class


        showSnake();
        playGame();

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!moving) {

                    move();
                    animateSnake();
                }
            }
        };

        animationTimer.start();


    }

    private void animateSnake() {

        if (toMove == null)
            return;
        moving = true;
        SnakeBody newHead = new SnakeBody(head, true);
        root.getChildren().add(newHead.shape);

        KeyValue keyValue = new KeyValue(newHead.shape.xProperty(), toMove.getX() * 40);
        KeyValue keyValue2 = new KeyValue(newHead.shape.yProperty(), toMove.getY() * 40);
        KeyValue keyValue3 = new KeyValue(tailOfSnake.shape.xProperty(), tailOfSnake.next.shape.getX());
        KeyValue keyValue4 = new KeyValue(tailOfSnake.shape.yProperty(), tailOfSnake.next.shape.getY());
        KeyFrame keyFrame = new KeyFrame(Duration.millis(300), keyValue, keyValue2, keyValue3, keyValue4);


        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(1);
        timeline.play();


        timeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                head.setNext(toMove);
                head = toMove;
                newHead.snakeCell = head;
                headOfSnake.next = newHead;
                headOfSnake = newHead;
                root.getChildren().remove(tailOfSnake.shape);
                tailOfSnake = tailOfSnake.next;
                tail = tail.getNext();
                toMove = null;
                moving = false;

            }
        });

    }

    private void playGame() {

        scene.addEventFilter(KeyEvent.KEY_PRESSED, (key) -> {
            switch (key.getCode().toString()) {
                case "UP":
                    if (this.curr == Dir.DOWN)
                        break;
                    this.curr = Dir.UP;
                    break;
                case "DOWN":
                    if (this.curr == Dir.UP)
                        break;
                    this.curr = Dir.DOWN;
                    break;
                case "RIGHT":
                    if (this.curr == Dir.LEFT)
                        break;
                    this.curr = Dir.RIGHT;
                    break;
                case "LEFT":
                    if (this.curr == Dir.RIGHT)
                        break;
                    this.curr = Dir.LEFT;
                    break;
            }

        });
    }

    private void showSnake() {
        boolean isHead = false;
        SnakeCell body = tail;
        SnakeBody prev = null;

        while (body != null) {
            if (body.getNext() == null) {
                headOfSnake = new SnakeBody(body, isHead);
                root.getChildren().add(headOfSnake.shape);
                prev.next = headOfSnake;
            } else {


                SnakeBody others = new SnakeBody(body, false);
                if (prev != null)
                    prev.next = others;
                else
                    tailOfSnake = others;
                root.getChildren().add(others.shape);

                prev = others;
            }

            body = body.getNext();
        }
    }

    private void move() {


        Dir dir = this.curr;
        if (dir == Dir.NONE || toMove != null)
            return;

        resume = false;

        int new_x = head.getX();
        int new_y = head.getY();


        switch (dir) {
            case UP:
                new_y--;
                break;
            case DOWN:
                new_y++;
                break;
            case LEFT:
                new_x--;
                break;
            case RIGHT:
                new_x++;
                break;
        }

        if (new_x >= this.WIDTH) {
            new_x -= this.WIDTH;
        }

        if (new_x < 0) {
            new_x += this.WIDTH;
        }

        if (new_y >= this.HEIGHT) {
            new_y -= this.HEIGHT;
        }

        if (new_y < 0) {
            new_y += this.HEIGHT;
        }


        SnakeCell cell = new SnakeCell(new_x, new_y);
        gameMap.moveSnake(cell, tail);
        toMove = cell;

        if (!gameMap.levelUp) {

        }

        /*if (gameMap.levelUp || gameMap.refreshMap) {
            gameMap.levelUp = false;
            gameMap.initializeGrid();
            gameMap.fillGrid(4);

        }*/


    }


    private void createGame(GridPane pane) throws FileNotFoundException {

        gameMap = new GameMap(WIDTH, HEIGHT);
        makeSnake(4);
        gameMap.fillGrid(-1);
        root.getChildren().add(pane);
        layoutGrid(pane);

    }

    public void makeSnake(int length) {

        SnakeCell next = null;
        SnakeCell head = null;

        int x = this.WIDTH / 2;
        int y = this.HEIGHT / 2;

        for (int i = 0; i < length; i++) {
            SnakeCell cell = new SnakeCell(x - i, y);
            gameMap.getGrid()[y][x - i] = gameMap.SNAKECELL;
            cell.setNext(next);
            next = cell;


            if (head == null)
                head = cell;
        }

        this.head = head;
        this.tail = next;
    }


    private void layoutGrid(GridPane pane) throws FileNotFoundException {

        pane.getChildren().clear();

        if (gameMap.health <= 0) {
            Rectangle rec = new Rectangle(620, 620);
            rec.setFill(Color.RED);
            pane.getChildren().add(rec);
            return;
        }

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.1);

        Image temp = new Image(new FileInputStream("resources/green_cell.jpg"));

        for (int i = 1; i < WIDTH; i++)
            for (int j = 0; j < HEIGHT; j++) {
                ImageView img = new ImageView(temp);
                if ((i + j) % 2 == 0) {
                    img.setEffect(colorAdjust);
                }
                img.setFitWidth(40);
                img.setFitHeight(40);
                pane.add(img, j, i);
            }

    }

}
