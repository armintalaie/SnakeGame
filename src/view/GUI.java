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
import java.util.ArrayList;

//TODO: add listeners
//TODO: add starting screen
//TODO: cleanup code
//  TODO: constants for width and height
//  TODO: shorten methods, (move some methods out of the GUI class)
//TODO: add transition animation [done]
//-------------------------------
//TODO: show ranking
//-------------------------------
//TODO: add different shapes for snake's tail and head
//TODO: snake eyes
//TODO: win lose and trap sounds
//TODO: bouncy goal
//-------------------------------
//TODO: add top bar for score and lives [done]
//TODO: colorful grid [done]

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


    private ArrayList<ImageView> mapContents = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {

        // initializing GUI and Game map
        borderPane = new BorderPane();
        topBar = new HBox();
        borderPane.setCenter(root);
        borderPane.setTop(topBar);
        createGame(gridMap);

        scene = new Scene(borderPane, PAGEWIDTH, PAGEHEIGHT);


        scene.getStylesheets().add("view/stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.show();

        //playGame();
        root.setPrefWidth(PAGEWIDTH);
        root.setPrefHeight(PAGEHEIGHT);
        //playGame();

        //Instantiating the path class


        showSnake();
        keyboardInput();

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!moving) {
                    updateGameStatus();
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

        KeyValue headX = new KeyValue(newHead.shape.xProperty(), toMove.getX() * 40);
        KeyValue headY = new KeyValue(newHead.shape.yProperty(), toMove.getY() * 40);
        KeyValue tailX = new KeyValue(tailOfSnake.shape.xProperty(), tailOfSnake.next.shape.getX());
        KeyValue tailY = new KeyValue(tailOfSnake.shape.yProperty(), tailOfSnake.next.shape.getY());
        KeyFrame keyFrame = new KeyFrame(Duration.millis(300), headX, headY, tailX, tailY);


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

    private void updateGameStatus() {
        if (gameMap.refreshMap) {
            layoutContent();
            gameMap.refreshMap = false;
        }
        topBar.getChildren().clear();
        int level = gameMap.level;
        int health = gameMap.health;
        Label levelLabel = new Label("LEVEL " + level);
        Label healthLabel = new Label("HEALTH " + health);
        topBar.getChildren().addAll(levelLabel, healthLabel);

    }

    private void keyboardInput() {

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

        if (new_x >= this.WIDTH)
            new_x -= this.WIDTH;

        if (new_x < 0)
            new_x += this.WIDTH;

        if (new_y >= this.HEIGHT)
            new_y -= this.HEIGHT;

        if (new_y < 0)
            new_y += this.HEIGHT;


        SnakeCell cell = new SnakeCell(new_x, new_y);
        gameMap.moveSnake(cell, tail);
        toMove = cell;

        if (!gameMap.levelUp) {

        }

    }


    private void createGame(GridPane pane) throws FileNotFoundException {

        gameMap = new GameMap(WIDTH, HEIGHT);
        makeSnake(4);
        gameMap.fillGrid(4);
        root.getChildren().add(pane);
        layoutMap(pane);
        updateGameStatus();
        layoutContent();

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


    private void layoutMap(GridPane pane) throws FileNotFoundException {

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

        for (int i = 0; i < WIDTH; i++)
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


    private void layoutContent() {
        try {
            removeContent();
            Image prize = new Image(new FileInputStream("resources/cherry.png"));
            Image trap = new Image(new FileInputStream("resources/trap.png"));
            for (int i = 0; i < WIDTH; i++)
                for (int j = 0; j < HEIGHT; j++) {
                    ImageView cherry;
                    if (gameMap.getGrid()[j][i] == GameMap.GOAL)
                        cherry = new ImageView(prize);
                    else {
                        if (gameMap.getGrid()[j][i] == GameMap.TRAP)
                            cherry = new ImageView(trap);
                        else
                            continue;
                    }
                    cherry.setFitWidth(40);
                    cherry.setFitHeight(40);
                    root.getChildren().add(cherry);
                    cherry.setY(40 * j);
                    cherry.setX(40 * i);
                    mapContents.add(cherry);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void removeContent() {
        root.getChildren().removeAll(this.mapContents);
    }

}
