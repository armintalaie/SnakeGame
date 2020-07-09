package view;

import controller.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
import model.GameMap;
import model.Snake;
import model.SnakeCell;

import java.awt.*;
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

    private Scene scene;
    private GameMap gameMap;
    GridPane pane = new GridPane();
    private final int WIDTH = 15;
    private final int HEIGHT = 15;
    private SnakeCell head;
    private SnakeCell tail;
    boolean resume = true;
    private Dir curr = Dir.NONE;
    Label level;
    HBox topBar;
    BorderPane borderPane;
    Pane root = new Pane();

    @Override
    public void start(Stage primaryStage) throws Exception {

        borderPane = new BorderPane();
        topBar = new HBox();
        createGame(pane);
        updateGrid(pane);
        scene = new Scene(borderPane, 600, 670);
        scene.getStylesheets().add("view/stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.show();
        playGame();

        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {

                while (true) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (gameMap.health > 0)
                                move();
                        }
                    });
                    Thread.sleep(150);
                }

            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

        Task task2 = new Task<Void>() {
            @Override
            public Void call() throws Exception {

                while (true) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                updateGrid(pane);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            level = new Label("LEVEL: " + Integer.toString(gameMap.level));
                            Label health = new Label("HEALTH: " + Integer.toString(gameMap.health));
                            level.setPrefSize(300, 70);
                            health.setPrefSize(300, 70);
                            topBar.getChildren().clear();
                            topBar.getChildren().add(level);
                            topBar.getChildren().add(health);
                            borderPane.setTop(topBar);

                        }
                    });
                    Thread.sleep(20);
                }

            }
        };
        Thread th2 = new Thread(task2);

        th2.start();

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

    private void move() {

        Dir dir = this.curr;
        if (dir == Dir.NONE)
            return;

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
        head.setNext(cell);
        head = cell;
        gameMap.moveSnake(head, tail);

        if (!gameMap.levelUp) {
            tail = tail.getNext();
        }

        if (gameMap.levelUp || gameMap.refreshMap) {
            gameMap.levelUp = false;
            gameMap.initializeGrid();
            gameMap.fillGrid(4);

        }


    }

    private void updateGrid(GridPane pane) throws FileNotFoundException {

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
        Image headS = new Image(new FileInputStream("resources/game_cell.jpg"));

        for (int i = 0; i < WIDTH; i++)
            for (int j = 0; j < HEIGHT; j++) {
                boolean ok = false;
                Rectangle rec = new Rectangle(40, 40);
                ImageView img = new ImageView(temp);
                if ((i + j) % 2 == 0) {
                    img.setEffect(colorAdjust);
                    rec.setEffect(colorAdjust);
                }
                img.setFitWidth(40);
                img.setFitHeight(40);
                rec.setFill(Color.GREEN);

                switch (gameMap.getGrid()[i][j]) {
                    case GameMap.EMPTY:
                        ok = true;
                        break;
                    case GameMap.GOAL:
                        rec.setFill(Color.BLUEVIOLET);
                        break;
                    case GameMap.SNAKECELL:
                        StackPane pane1 = new StackPane();
                        rec.setFill(Color.BLUE);
                        if (head.getY() == i && head.getX() == j) {
                            ImageView im2 = new ImageView(headS);
                            im2.setStyle("-fx-background-color: red;");
                            im2.setFitWidth(40);
                            im2.setFitHeight(40);
                            pane1.getChildren().add(im2);
                            pane.add(pane1, j, i);
                            break;
                        }

                        break;
                    case GameMap.TRAP:
                        rec.setFill(Color.ORANGE);
                }
                if (head.getY() == i && head.getX() == j) {
                    continue;
                }

                if (ok)
                    pane.add(img, j, i);
                else
                    pane.add(rec, j, i);
            }

    }


    private void createGame(GridPane pane) throws FileNotFoundException {

        gameMap = new GameMap(WIDTH, HEIGHT);
        makeSnake(4);
        gameMap.fillGrid(4);
        root.getChildren().add(pane);
        borderPane.setCenter(root);

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

}
