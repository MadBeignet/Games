package ActualPong;

import java.awt.Point;

public class Ball {

    private double velocityX;
    private double velocityY;
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

    public void setVelocityX(double v) {
        velocityX = v;
    }
    public void setVelocityY(double v) {
        velocityY = v;
    }
    public void moveX() {
        pos.translate((int)velocityX,0);
        velocityX = (velocityX * 0.997);
        //velocityX = Math.abs(velocityX) - 0.01 >= 0 ? (int) (Math.abs(velocityX)/velocityX * (Math.abs(velocityX) - 0.01)) : 0; 
    }
    public void moveY() {
        pos.translate(0, (int) velocityY);
        velocityY = (velocityY * 0.997);
        //velocityY = Math.abs(velocityY) - 0.01 >= 0 ? (int) (Math.abs(velocityY)/velocityY * (Math.abs(velocityY) - 0.01)) : 0; 
    }
    public double getVelocityX() {
        return velocityX;
    }
    public double getVelocityY() {
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
