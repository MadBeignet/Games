package Game;

import java.awt.event.KeyEvent;


public class Player extends Parent{

    // image that represents the player's position on the board
    // current position of the player on the board grid
    // keep track of the player's score
    private int score;
    private Count count;

    public Player() {
        super("Game/images/player.png",0,0);
        // load the assets

        // initialize the state
        score = 0;
        count = new Count();
    }
    
    public void keyPressed(KeyEvent e) {
        // every keyboard get has a certain code. get the value of that code from the
        // keyboard event so that we can compare it to KeyEvent constants
        int key = e.getKeyCode();
        // depending on which arrow key was pressed, we're going to move the player by
        // one whole tile for this input
        if (key == KeyEvent.VK_W) {
            translatePlayer(0, -1);
        }
        if (key == KeyEvent.VK_D) {
            translatePlayer(1, 0);
        }
        if (key == KeyEvent.VK_S) {
            translatePlayer(0, 1);
        }
        if (key == KeyEvent.VK_A) {
            translatePlayer(-1, 0);
        }
    }

    public void tick() {
        // this gets called once every tick, before the repainting process happens.
        // so we can do anything needed in here to update the state of the player.
        // prevent the player from moving off the edge of the board sideways
        if (getPos().x < 0) {
            getPos().x = Board.COLUMNS-1;
        } else if (getPos().x >= Board.COLUMNS) {
            getPos().x = 0;
        }
        // prevent the player from moving off the edge of the board vertically
        if (getPos().y < 0) {
            getPos().y = Board.ROWS-1;
        } else if (getPos().y >= Board.ROWS) {
            getPos().y = 0;
        }
        count.updateCount();
        
    }

    public String getScore() {
        return String.valueOf(score);
    }

    public void addScore(int amount) {
        score += amount;
    }

    public int getCount() {
        return count.getCount();
    }

}
