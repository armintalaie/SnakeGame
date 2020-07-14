package model;



import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Random;


//TODO: combine width and height
//TODO: add enum for cell types
//-------------------------------
//TODO: implement score [done]
//TODO: implement life [done]


public class GameMap {

    public static final int EMPTY = 0;
    public static final int TRAP = 1;
    public static final int GOAL = 2;
    public static final int SNAKECELL = 3;


    public int level = 1;
    public int health = 5;
    public boolean levelUp = false;
    public boolean refreshMap = true;
    private int WIDTH;
    private int HEIGHT;
    private int[][] grid;


    public GameMap(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.grid = new int[this.WIDTH][this.HEIGHT];
        initializeGrid();

    }


    public void initializeGrid() {

        for (int i = 0; i < this.WIDTH; i++)
            for (int j = 0; j < this.HEIGHT; j++) {
                if (grid[j][i] != SNAKECELL)
                    grid[j][i] = EMPTY;
            }

    }

    public void fillGrid(int trapNum) {
        trapNum = trapNum + this.level;
        Random random = new Random(System.currentTimeMillis());
        boolean chosenGoal = false;

        while (trapNum > 0 || !chosenGoal) {
            int x = random.nextInt(this.WIDTH);
            int y = random.nextInt(this.HEIGHT);

            if (this.grid[x][y] == EMPTY) {
                if (chosenGoal) {
                    this.grid[x][y] = TRAP;
                    trapNum--;
                } else {
                    this.grid[x][y] = GOAL;
                    chosenGoal = true;
                }

            }

        }
        this.refreshMap = false;

    }

    public int[][] getGrid() {
        return grid;
    }

    public void moveSnake(SnakeCell head, SnakeCell tail) {

        lose(head);

        if (this.health <= 0)
            return;

        if (grid[head.getY()][head.getX()] == GOAL) {
            this.refreshMap = true;
            this.levelUp = true;
            this.level++;

        } else {
            grid[tail.getY()][tail.getX()] = EMPTY;
        }

        grid[head.getY()][head.getX()] = SNAKECELL;



    }


    private void lose(SnakeCell head) {

        if (grid[head.getY()][head.getX()] == TRAP || head.getY() > WIDTH || head.getY() < 0 ||
                head.getX() > HEIGHT || head.getX() < 0) {
            this.health--;
            this.refreshMap = true;
            return;
        }
        if (grid[head.getY()][head.getX()] == SNAKECELL) {
            this.health--;
            this.refreshMap = true;
        }


    }
}
