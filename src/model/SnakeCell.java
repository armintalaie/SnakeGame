package model;

public class SnakeCell {
    int x;
    int y;
    SnakeCell next;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public SnakeCell getNext() {
        return next;
    }

    public void setNext(SnakeCell next) {
        this.next = next;
    }

    public SnakeCell(int x, int y){
        this.x = x;
        this.y = y;
    }
}
