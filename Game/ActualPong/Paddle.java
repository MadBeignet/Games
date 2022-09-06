package ActualPong;

import java.awt.event.KeyEvent;
import java.awt.Point;


public class Paddle {
    
    private int height;
    private int width;
    private Point pos;
    private int[] keys;
    private double velocityY;
    private boolean bothY;
    private boolean decreasing;

    public Paddle(Point p, int x, int y, int[] keys) {
        pos = (p);
        width = x;
        height = y;
        this.keys = keys;
        velocityY = 0;
        bothY = false;
        decreasing = true;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public Point getPos() {
        return pos;
    }
    public double getVelocityY() {
        return velocityY;
    }

    public void keyPressed(KeyEvent e) {
        // every keyboard get has a certain code. get the value of that code from the
        // keyboard event so that we can compare it to KeyEvent constants
        int key = e.getKeyCode();
        // depending on which arrow key was pressed, we're going to move the player by
        // one whole tile for this input
        if (key == keys[0]) {
            if (key == keys[0] && velocityY != Board.VELOCITY && bothY == false) {
                velocityY = -Board.VELOCITY;
            } else if (key == keys[0] && velocityY == Board.VELOCITY) {
                velocityY = 0;
                bothY = true;
            }
        } else if (key == keys[1]) {
            if (key == keys[1] && velocityY != -Board.VELOCITY && bothY == false) {
                velocityY = Board.VELOCITY;
            } else if (key == keys[1] && velocityY == -Board.VELOCITY && bothY == false) {
                velocityY = 0;
                bothY = true;
            }
        } 
        decreasing = false;
    }
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (bothY == false && (key == keys[0] || key == keys[1])) {
            if (key == keys[0]) {
                decreasing = true;
            }
            if (key == keys[1]) {
                decreasing = true;
            }
        } else if (key == keys[0] || key == keys[1]){
            if (key == keys[0])
                velocityY = Board.VELOCITY;
            if (key == keys[1])
                velocityY = -Board.VELOCITY;
            bothY = false;
            
        }

    }
    public void tick() {
        // prevent the player from moving off the edge of the board vertically
        if (pos.y < 0) {
            pos.y = 0;
        } else if (pos.y + height >= Board.ROWS*Board.TILE_SIZE) {
            pos.y = Board.ROWS*Board.TILE_SIZE-1-height;
        }
        move();
    }
    private void move() {
        pos.translate(0, (int)velocityY);
        if (decreasing)
            velocityY = (0.8*velocityY);
    }

}
