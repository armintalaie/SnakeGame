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


public class GUI extends Application {

    Controller game = new Controller();


    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane pane = new GridPane();
        createGame(pane);
        updateGrid(pane);
        Scene scene = new Scene(pane, 820, 820);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, (key)-> {
            switch (key.getCode().toString()){
                case "UP":
                    game.move(Controller.Dir.UP);
                    updateGrid(pane);
                    break;
                case "DOWN":
                    game.move(Controller.Dir.DOWN);
                    updateGrid(pane);
                    break;
                case "RIGHT":
                    game.move(Controller.Dir.RIGHT);
                    updateGrid(pane);
                    break;
                case "LEFT":
                    game.move(Controller.Dir.LEFT);
                    updateGrid(pane);
                    break;


            }
                });
        primaryStage.setScene(scene);
        primaryStage.show();








    }

    private void updateGrid(GridPane pane) {
        pane.getChildren().clear();

        for(int i = 0 ; i < game.WIDTH; i++)
            for (int j = 0 ; j < game.HEIGHT; j++) {
                Rectangle rec = new Rectangle(40,40);
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

    private void createGame(GridPane pane) throws FileNotFoundException {


        pane.setHgap(1);
        pane.setVgap(1);



        for(int i = 0 ; i < game.WIDTH; i++)
            for (int j = 0 ; j < game.HEIGHT; j++) {
                ImageView img = new ImageView(new Image(new FileInputStream("resources/game_cell.jpg")));
                img.setFitWidth(800/20);
                img.setFitHeight(800/20);
                Rectangle rec = new Rectangle(40,40);
                rec.setFill(Color.BLACK);
                pane.add(rec, j, i);
            }


    }
}
