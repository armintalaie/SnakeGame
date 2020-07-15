package view;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Intro extends Application {
    private GUI gui;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Group group = new Group();

        StackPane stackPane = new StackPane();
        stackPane.setBackground(Background.EMPTY);
        Rectangle rectangle = new Rectangle(600, 600 + 100,  Color.web("#ff5121", 0.4));
        stackPane.getChildren().addAll(gui.borderPane,rectangle);
        Scene scene = new Scene(stackPane, 600, 600 + 100);
        scene.getStylesheets().add("view/stylesheet.css");
        primaryStage.setScene(scene);


    }

    Intro(GUI gui) {
        this.gui = gui;
        try {
            this.start(gui.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
