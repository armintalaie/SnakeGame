package model;

import java.util.LinkedList;
import java.util.Random;


public class GameMap {

    public final int EMPTY = 0;
    public final int TRAP = 1;
    public final int GOAL = 2;
    public final int SNAKECELL = 3;


    private int WIDTH;
    private int HEIGHT;
    private int[][] grid;




    public GameMap(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        initializeGrid();

    }



    public void initializeGrid() {
        this.grid = new int[this.WIDTH][this.HEIGHT];

        for (int i = 0; i < this.WIDTH; i++)
            for (int j = 0; j < this.HEIGHT; j++)
                grid[i][j] = EMPTY;
    }

    public void fillGrid(int trapNum) {
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

    }

    public int[][] getGrid() {
        return grid;
    }
}
