package view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class Intro extends Application {
    private GUI gui;
    private Scene scene;
     Stage stage;
    private final int WIDTH = 700;
    private final int HEIGHT = 700;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.scene = new Scene(new Group(), WIDTH,HEIGHT);

        this.stage = primaryStage;
        if (gui == null)
            introScreen();

        scene.getStylesheets().add("view/stylesheet.css");

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("SNAKE GAME");


    }

     void loseScreen(GUI gui) {

        StackPane stackPane = new StackPane();
        stackPane.setBackground(Background.EMPTY);
        Button main = new Button("Main menu");
        main.getStyleClass().add("introbutton");
        Rectangle rectangle = new Rectangle(this.WIDTH, this.HEIGHT,  Color.web("#c91c1c", 0.6));
        stackPane.getChildren().addAll(gui.borderPane,rectangle,main);
        gui.scene.setRoot(stackPane);
        this.gui = gui;
        this.scene = gui.scene;
        this.stage = gui.stage;


         main.setOnMouseClicked(event -> {
             introScreen();
         });

     }


    private void introScreen() {
        BorderPane group = new BorderPane();
        Label label = new Label("  Timmy The Snake");
        group.setTop(label);

        VBox options = new VBox();
        Button button = new Button("Start");

        options.getChildren().add(button);
        group.setCenter(options);
        button.setOnMouseClicked(event -> {
            this.gui = new GUI();
            try {
                this.gui.start(this.stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });



        group.getStyleClass().add("introborderpane");
        options.getStyleClass().add("introvbox");
        options.setSpacing(50);

        Button quit = new Button("Quit");
        options.getChildren().add(quit);
        this.scene.setRoot(group);


        quit.setOnMouseClicked(event -> {
            stage.close();
        });

        label.getStyleClass().add("introlabel");
        button.getStyleClass().add("introbutton");
        quit.getStyleClass().add("introbutton");


    }
}
