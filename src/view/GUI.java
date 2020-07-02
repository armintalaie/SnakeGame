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

    private Controller game = new Controller();
    private Scene scene;


    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane pane = new GridPane();
        createGame(pane);
        updateGrid(pane, Controller.Status.CONTINUE);
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
                    updateGrid(pane, game.move(Controller.Dir.UP));
                    break;
                case "DOWN":
                    updateGrid(pane, game.move(Controller.Dir.DOWN));
                    break;
                case "RIGHT":
                    updateGrid(pane, game.move(Controller.Dir.RIGHT));
                    break;
                case "LEFT":
                    updateGrid(pane, game.move(Controller.Dir.LEFT));
                    break;
            }

        });


    }

    private void updateGrid(GridPane pane, Controller.Status status) {

        pane.getChildren().clear();
        if (status == Controller.Status.LOSE) {
            Rectangle rec = new Rectangle(820, 820);
            rec.setFill(Color.RED);
            pane.getChildren().add(rec);
            return;
        } else {
            if (status == Controller.Status.LEVELUP) {
                Rectangle rec = new Rectangle(820, 820);
                rec.setFill(Color.BLUEVIOLET);
                pane.getChildren().add(rec);
                updateGrid(pane, Controller.Status.CONTINUE);
            } else {

                for (int i = 0; i < game.WIDTH; i++)
                    for (int j = 0; j < game.HEIGHT; j++) {
                        Rectangle rec = new Rectangle(40, 40);
                        switch (game.getGameMap().getGrid()[i][j]) {
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
        }


    }

    private void createGame(GridPane pane) throws FileNotFoundException {


        pane.setHgap(1);
        pane.setVgap(1);


        for (int i = 0; i < game.WIDTH; i++)
            for (int j = 0; j < game.HEIGHT; j++) {
                ImageView img = new ImageView(new Image(new FileInputStream("resources/game_cell.jpg")));
                img.setFitWidth(800 / 20);
                img.setFitHeight(800 / 20);
                Rectangle rec = new Rectangle(40, 40);
                rec.setFill(Color.BLACK);
                pane.add(rec, j, i);
            }


    }
}
