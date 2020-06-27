package model;

import java.util.Random;

public class GameMap {

    private int WIDTH;
    private int HEIGHT;
    private CellStatus[][] grid;

    enum CellStatus {SNAKE,
    TRAP,
    EMPTY,
    GOAL}


    GameMap(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        initializeGrid();
        fillGrid(5);


    }

    private void initializeGrid() {
        this.grid = new CellStatus[this.WIDTH][this.HEIGHT];

        for (int i = 0; i < this.WIDTH; i++)
            for (int j = 0; j < this.HEIGHT; j++)
                grid[i][j] = CellStatus.EMPTY;
    }

    public void fillGrid(int trapNum){
        Random random = new Random(System.currentTimeMillis());
        boolean chosenGoal = false;

        while(trapNum > 0 || !chosenGoal) {
            int x = random.nextInt(this.WIDTH);
            int y = random.nextInt(this.HEIGHT);

            if (this.grid[x][y] == CellStatus.EMPTY){
                if (chosenGoal) {
                    this.grid[x][y] = CellStatus.TRAP;
                    trapNum--;
                } else {
                    this.grid[x][y] = CellStatus.GOAL;
                    chosenGoal = true;
                }

            }

        }

    }

}
