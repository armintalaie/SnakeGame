import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Scanner;

public class SnakeGame extends Application {

    public static void main(String[] args) {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        int min = -1;
        int length = 200;
        Scanner scanner = new Scanner(System.in);

        ArrayList<Integer> edges = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            String[] line = scanner.nextLine().split("\\s+");


            // System.out.println(Arrays.toString(line));
            int first = Integer.parseInt(line[0]);

            for (int j = 1; j < line.length; j++) {
                Integer temp = 0;

                if (!edges.contains(temp)) {
                    edges.add(temp);
                }

            }


        }
    }
}
