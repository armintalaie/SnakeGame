package controller;

import model.GameMap;
import model.SnakeCell;



public class Controller {

    public final int WIDTH = 20;
    public final int HEIGHT = 20;
    GameMap gameMap;
    SnakeCell head;
    SnakeCell tail;

    public Controller() {
        gameMap = new GameMap(WIDTH, HEIGHT);
        makeSnake(8);
        gameMap.fillGrid(4);
        printMap();
        System.out.println(head);
    }

    public void runGame() {


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

    public void printMap() {

        for (int i = 0; i < this.WIDTH; i++) {
            for (int j = 0; j < this.HEIGHT; j++)
                System.out.print(gameMap.getGrid()[i][j] + " ");
            System.out.println();
        }


    }
}
