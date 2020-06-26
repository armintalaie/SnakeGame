import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Scanner;

public class SnakeGame extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane pane = new GridPane();

        Scene scene = new Scene(pane, 1200, 800);

        primaryStage.setScene(scene);
        primaryStage.show();










    }
}
