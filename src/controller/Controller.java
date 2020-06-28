package controller;

import model.GameMap;
import model.SnakeCell;



public class Controller {

    public final int WIDTH = 20;
    public final int HEIGHT = 20;
    public enum Dir {UP, LEFT, RIGHT, DOWN}

    public GameMap getGameMap() {
        return gameMap;
    }

    private GameMap gameMap;
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

    public void move(Dir dir) {
        int new_x = head.getX();
        int new_y = head.getY();

        switch (dir){
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
        System.out.println("meow");
        SnakeCell cell = new SnakeCell(new_x, new_y);
        head.setNext(cell);
        head = cell;
        gameMap.moveSnake(head, tail);
        tail = tail.getNext();


    }
}
