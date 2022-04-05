package Game.Game1;

import java.awt.event.KeyEvent;
import java.awt.Point;
import java.awt.*;
import java.awt.event.*;

public class Paddle {
    
    private int height;
    private int width;
    private Point pos;
    private int[] keys;
    private int velocityX;
    private int velocityY;
    private boolean bothX;
    private boolean bothY;

    public Paddle(Point p, int x, int y, int[] keys) {
        pos = (p);
        width = x;
        height = y;
        this.keys = keys;
        velocityX = 0;
        velocityY = 0;
        bothX = false;
        bothY = false;
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
            if (key == keys[1] && velocityX != -Board.VELOCITY && bothX == false) {
                velocityX = Board.VELOCITY;
            } else if (key == keys[1] && velocityX == -Board.VELOCITY) {
                velocityX = 0;
                bothX = true;
            }
        } else if (key == keys[2]) {
            if (key == keys[2] && velocityY != -Board.VELOCITY && bothY == false) {
                velocityY = Board.VELOCITY;
            } else if (key == keys[2] && velocityY == -Board.VELOCITY && bothY == false) {
                velocityY = 0;
                bothY = true;
            }
        } else if (key == keys[3]) {
            if (key == keys[3] && velocityX != Board.VELOCITY && bothX == false) {
                velocityX = -Board.VELOCITY;
            } else if (key == keys[3] && velocityX == Board.VELOCITY && bothX == false) {
                velocityX = 0;
                bothX = true;
            }
        }
    }
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (bothY == false && (key == keys[0] || key == keys[2])) {
            if (key == keys[0]) {
                velocityY = 0;
            }
            if (key == keys[2]) {
                velocityY = 0;
            }
        } else if (key == keys[0] || key == keys[2]){
            if (key == keys[0])
                velocityY = Board.VELOCITY;
            if (key == keys[2])
                velocityY = -Board.VELOCITY;
            bothY = false;
            
        }

        if (velocityX != 0 && (key == keys[3] || key == keys[1]) && bothX == false) {
            if (key == keys[1]) {
                velocityX = 0;
            }
            if (key == keys[3]) {
                velocityX = 0;
            }
        } else if ((key == keys[3] || key == keys[1]) && bothX == true){
            if (key == keys[1])
                velocityX = -Board.VELOCITY;
            if (key == keys[3])
                velocityX = Board.VELOCITY;
            bothX = false;
        }

    }
    public void tick() {
        if (pos.x < 0) {
            pos.x = 0;
        } else if (pos.x + width >= Board.COLUMNS*Board.TILE_SIZE) {
            pos.x = Board.COLUMNS*Board.TILE_SIZE - 1-width;
        }
        // prevent the player from moving off the edge of the board vertically
        if (pos.y < 0) {
            pos.y = 0;
        } else if (pos.y + height >= Board.ROWS*Board.TILE_SIZE) {
            pos.y = Board.ROWS*Board.TILE_SIZE-1-height;
        }
        move();
    }
    private void move() {
        pos.translate(velocityX, velocityY);
    }
    

    public void draw(Graphics g) {
        // with the Point class, note that pos.getX() returns a double, but 
        // pos.x reliably returns an int. https://stackoverflow.com/a/30220114/4655368
        // this is also where we translate board grid position into a canvas pixel
        // position by multiplying by the tile size.
        g.setColor(new Color(255,0,0));
        g.fillRect(getPos().x, getPos().y, width, height);
    }
    

}
