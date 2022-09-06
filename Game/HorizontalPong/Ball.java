package HorizontalPong;

import java.awt.Point;

public class Ball {

    private int velocityX;
    private int velocityY;
    private Point pos;
    private int height;
    private int width;

    public Ball(int x, int y) {
        height = y;
        width = x;
        velocityX = 0;
        velocityY = 0;
        pos = new Point((Board.COLUMNS * Board.TILE_SIZE-width)/2, (Board.ROWS*Board.TILE_SIZE - height)/2);
    }

    public void setVelocityX(int v) {
        velocityX = v;
    }
    public void setVelocityY(int v) {
        velocityY = v;
    }
    public void moveX() {
        pos.translate(velocityX,0);
    }
    public void moveY() {
        pos.translate(0, velocityY);
    }
    public int getVelocityX() {
        return velocityX;
    }
    public int getVelocityY() {
        return velocityY;
    }
    public Point getPos() {
        return pos;
    }
    public int getX() {
        return pos.x;
    }
    public int getY() {
        return pos.y;
    }
    public void translate(int x, int y) {
        pos.translate(x, y);
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    
}
