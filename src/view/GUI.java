package view;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private final int GUI_WIDTH = WIDTH * 40;
    private final int GUI_HEIGHT = 600;

    private Scene scene;
    private HBox topBar;
     VBox borderPane;
    private Pane root = new Pane();
    private GridPane gridMap = new GridPane();
    private HBox bottom = new HBox();

    boolean resume = true;
    private Dir curr = Dir.NONE;


    private GameMap gameMap;
    // head and tail of snake shapes on GUI
    private SnakeBody headOfSnake;
    private SnakeBody tailOfSnake;

    // snake head and tail cells
    private SnakeCell head;
    private SnakeCell tail;

    // variables to track moving transitions
    private SnakeCell toMove = null;
    private boolean moving = false;
    private int switchColor = 1;
    private boolean colorDir = true;
    Stage stage;

    private ArrayList<ImageView> mapContents = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {

        // initializing GUI and Game map
        borderPane = new VBox();
        topBar = new HBox();
        topBar.setPrefSize(GUI_WIDTH, 30);


        this.pauseAndQuit();
        borderPane.getChildren().addAll(topBar,root,bottom);
        createGame(gridMap);

        scene = new Scene(borderPane, GUI_WIDTH, GUI_HEIGHT + 100);

        stage = primaryStage;
        stage.setTitle("SNAKE GAME");


       scene.getStylesheets().add("view/stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.show();

        //playGame();
        root.setPrefWidth(WIDTH * 40);
        root.setPrefHeight(HEIGHT * 40);
        //playGame();

        //Instantiating the path class



        keyboardInput();

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!moving && resume) {
                    move();
                    updateGameStatus();
                    animateSnake();
                }
            }
        };

        animationTimer.start();


    }

    private void pauseAndQuit() {

        Button quit = new Button("Quit Game");
        Button pause = new Button("Pause");

        quit.setOnMouseClicked(event -> {
            stage.close();
        });


        pause.setOnMouseClicked(event -> {
            if (this.resume)
                pause.setText("Resume");
            else
                pause.setText("Pause");
                resume = !this.resume;
        });
        bottom.getStyleClass().add("hbox");
        bottom.getChildren().add(pause);
        bottom.getChildren().add(quit);

    }

    private void animateSnake() {

        if (toMove == null)
            return;
        moving = true;
        SnakeBody newHead = new SnakeBody(head, switchColor);

        if (this.colorDir) {
            if(this.switchColor < 40)
                this.switchColor += 3;
            else
                this.colorDir = false;
        } else {
            if(this.switchColor > 10)
                this.switchColor -= 3;
            else
                this.colorDir = true;
        }


        root.getChildren().add(newHead.shape);

        KeyValue headX = new KeyValue(newHead.shape.xProperty(), toMove.getX() * 40);
        KeyValue headY = new KeyValue(newHead.shape.yProperty(), toMove.getY() * 40);
        KeyValue tailX = new KeyValue(tailOfSnake.shape.xProperty(), tailOfSnake.next.shape.getX());
        KeyValue tailY = new KeyValue(tailOfSnake.shape.yProperty(), tailOfSnake.next.shape.getY());
        KeyFrame movingHead = new KeyFrame(Duration.millis(250 - gameMap.level * 3), headX, headY);
        KeyFrame movingTail = new KeyFrame(Duration.millis(250 - gameMap.level * 3), tailX, tailY);


        Timeline timeline = new Timeline();
        if (this.gameMap.levelUp) {
            movingHead = new KeyFrame(Duration.millis(200 - gameMap.level * 3), headX, headY);
            timeline.getKeyFrames().add(movingHead);

        } else
            timeline.getKeyFrames().addAll(movingHead, movingTail);
        timeline.setCycleCount(1);
        timeline.play();


        timeline.setOnFinished(event -> {
            head.setNext(toMove);
            head = toMove;
            newHead.snakeCell = head;
            headOfSnake.next = newHead;
            headOfSnake = newHead;
            if (!gameMap.levelUp) {
                root.getChildren().remove(tailOfSnake.shape);
                tailOfSnake = tailOfSnake.next;
                tail = tail.getNext();

            } else
                gameMap.levelUp = false;

            toMove = null;
            moving = false;

        });

    }

    private void updateGameStatus() {
        if (gameMap.health <= 0)
            this.losingScreen();

        if (gameMap.refreshMap) {
            if (this.gameMap.levelUp) {
                this.gameMap.initializeGrid(false);
            } else {
                this.toMove = null;
                this.curr = Dir.NONE;
                this.gameMap.initializeGrid(false);
            }

            this.gameMap.fillGrid(4);
            this.layoutContent();
        }

        this.topBar.getStyleClass().add("top");
        this.topBar.getChildren().clear();
        Label levelLabel = new Label("Level " + this.gameMap.level);
        levelLabel.setPrefSize((double) GUI_WIDTH / 2, 30);
        Label healthLabel = new Label("Health ");
        //healthLabel.setPrefSize((double) GUI_WIDTH / 2, 30);
        this.topBar.getChildren().addAll(levelLabel, healthLabel);
        try {
            Image image = new Image(new FileInputStream("resources/star.png"));
            for (int i = 1 ; i <= 5; i++) {

                ImageView img = new ImageView(image);
                img.setFitWidth(30);
                img.setFitHeight(30);
                if (i > this.gameMap.health) {
                    ColorAdjust colorAdjust = new ColorAdjust();
                    colorAdjust.setBrightness(0.8);
                    img.setEffect(colorAdjust);
                }
                this.topBar.getChildren().add(img);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void losingScreen() {
        Intro intro = new Intro(this);
        this.resume = false;


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
        SnakeCell body = tail;
        SnakeBody prev = null;


        while (body != null) {
            if (body.getNext() == null) {
                headOfSnake = new SnakeBody(body, 1);
                root.getChildren().add(headOfSnake.shape);
                prev.next = headOfSnake;
            } else {


                SnakeBody others = new SnakeBody(body, 1);
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

    private void resetSnake() {
        SnakeBody body = this.tailOfSnake;
        this.headOfSnake = null;


        while (body != null) {
            this.root.getChildren().remove(body.shape);
            body = body.next;
        }
        this.makeSnake(4);

    }

    private void move() {

        Dir dir = this.curr;
        if (dir == Dir.NONE || toMove != null)
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


        SnakeCell cell = new SnakeCell(new_x, new_y);
        gameMap.moveSnake(cell, tail);
        toMove = cell;


    }


    private void createGame(GridPane pane) throws FileNotFoundException {

        this.gameMap = new GameMap(WIDTH, HEIGHT);
        this.makeSnake(4);
        this.gameMap.fillGrid(4);
        this.root.getChildren().add(pane);
        this.layoutMap(pane);
        this.updateGameStatus();
        this.layoutContent();
        showSnake();

    }

    private void makeSnake(int length) {

        SnakeCell next = null;
        SnakeCell head = null;

        int x = this.WIDTH / 2;
        int y = this.HEIGHT / 2;

        for (int i = 0; i < length; i++) {
            SnakeCell cell = new SnakeCell(x - i, y);
            this.gameMap.getGrid()[y][x - i] = GameMap.SNAKECELL;
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
            for (int i = 0; i < this.WIDTH; i++)
                for (int j = 0; j < this.HEIGHT; j++) {
                    ImageView cherry;
                    if (this.gameMap.getGrid()[j][i] == GameMap.GOAL)
                        cherry = new ImageView(prize);
                    else {
                        if (this.gameMap.getGrid()[j][i] == GameMap.TRAP)
                            cherry = new ImageView(trap);
                        else
                            continue;
                    }
                    cherry.setFitWidth(40);
                    cherry.setFitHeight(40);
                    this.root.getChildren().add(cherry);
                    cherry.setX(40 * i);
                    cherry.setY(40 * j);
                    this.mapContents.add(cherry);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void removeContent() {
        for(ImageView imageView: this.mapContents)
            this.root.getChildren().remove(imageView);
        //root.getChildren().removeAll(this.mapContents);
        this.mapContents.clear();
    }

}
