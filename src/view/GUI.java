package view;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.GameMap;
import model.Snake;
import model.SnakeCell;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

//TODO: add transition animation for leveling up or losing
//TODO: add different shapes for snake's tail and head
//TODO: add top bar for score and lives
//TODO: show ranking
//TODO: add cute animation and graphics
//  TODO: snake eyes
//  TODO: win lose and trap sounds
//  TODO: bouncy goal
//  TODO: colorful grid
//  TODO: move in direction until changed

public class GUI extends Application {

    public enum Dir {UP, LEFT, RIGHT, DOWN, NONE}

    //private Controller game = new Controller();
    private Scene scene;
    private GameMap gameMap;
    GridPane pane = new GridPane();
    public final int WIDTH = 20;
    public final int HEIGHT = 20;
    SnakeCell head;
    SnakeCell tail;


    @Override
    public void start(Stage primaryStage) throws Exception {

        createGame(pane);
        updateGrid(pane);
        scene = new Scene(pane, 820, 820);


        primaryStage.setScene(scene);
        primaryStage.show();
        playGame(pane);


    }

    private void playGame(GridPane pane) {


        boolean resume = true;
        scene.addEventFilter(KeyEvent.KEY_PRESSED, (key) -> {
            switch (key.getCode().toString()) {
                case "UP":
                    move(Dir.UP);
                    //updateGrid(pane);
                    break;
                case "DOWN":
                    move(Dir.DOWN);
                    //updateGrid(pane, game.move(Controller.Dir.DOWN));
                    break;
                case "RIGHT":
                    move(Dir.RIGHT);
                    //updateGrid(pane, game.move(Controller.Dir.RIGHT));
                    break;
                case "LEFT":
                    move(Dir.LEFT);
                    //updateGrid(pane, game.move(Controller.Dir.LEFT));
                    break;
            }

        });


    }

    private void move(Dir dir) {

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
        head.setNext(cell);
        head = cell;
        gameMap.moveSnake(head, tail);

        if(!gameMap.levelUp) {
            tail = tail.getNext();
        }


        if (gameMap.levelUp || gameMap.refreshMap) {
            gameMap.levelUp = false;
            gameMap.initializeGrid();
            gameMap.fillGrid(4);
        }
        System.out.println("sss");
        updateGrid(pane);


    }

    private void updateGrid(GridPane pane) {

        pane.getChildren().clear();

        if (gameMap.health <= 0) {
            Rectangle rec = new Rectangle(820, 820);
            rec.setFill(Color.RED);
            pane.getChildren().add(rec);
            return;
        }


        for (int i = 0; i < WIDTH; i++)
            for (int j = 0; j < HEIGHT; j++) {
                Rectangle rec = new Rectangle(40, 40);
                switch (gameMap.getGrid()[i][j]) {
                    case GameMap.EMPTY:
                        rec.setFill(Color.BLACK);
                        break;
                    case GameMap.GOAL:
                        rec.setFill(Color.BLUEVIOLET);
                        break;
                    case GameMap.SNAKECELL:
                        rec.setFill(Color.GREEN);
                        break;
                    case GameMap.TRAP:
                        rec.setFill(Color.ORANGE);
                }

                pane.add(rec, j, i);
            }


    }


    private void createGame(GridPane pane) throws FileNotFoundException {

        gameMap = new GameMap(WIDTH, HEIGHT);
        makeSnake(8);
        gameMap.fillGrid(4);


        pane.setHgap(1);
        pane.setVgap(1);


        for (int i = 0; i < WIDTH; i++)
            for (int j = 0; j < HEIGHT; j++) {
                ImageView img = new ImageView(new Image(new FileInputStream("resources/game_cell.jpg")));
                img.setFitWidth(800 / 20);
                img.setFitHeight(800 / 20);
                Rectangle rec = new Rectangle(40, 40);
                rec.setFill(Color.BLACK);
                pane.add(rec, j, i);
            }


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
