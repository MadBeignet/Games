package Game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.util.Queue;
import java.util.LinkedList;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;

public class Board extends JPanel implements ActionListener, KeyListener {

    // controls the delay between each tick in ms
    private final int DELAY = 25;
    // controls the size of the board
    public static final int TILE_SIZE = 50;
    public static final int ROWS = 12;
    public static final int COLUMNS = 18;
    // controls how many coins appear on the board
    public static final int NUM_COINS = 5;
    // this is the delay for coins to respawn onto the board after being collected or disappearing
    private static final int MAX_COUNT = 100;
    // this is the max score
    //private static final int MAX_SCORE = 3000;
    private final int MAX_SECONDS = 30;
    // suppress serialization warning
    private static final long serialVersionUID = 490905409104883233L;
    

    // keep a reference to the timer object that triggers actionPerformed() in
    // case we need access to it in another method
    private Timer timer;
    // objects that appear on the game board
    private Player player;
    public ArrayList<Coin> coins;
    private Queue<Count> counts = new LinkedList<Count>();
    

    public Board() {
        // set the game board size
        setPreferredSize(new Dimension(TILE_SIZE * COLUMNS, TILE_SIZE * ROWS));
        // set the game board background color
        setBackground(new Color(232, 232, 232));

        // initialize the game state
        player = new Player();
        coins = populateCoins();

        // this timer will call the actionPerformed() method every DELAY ms
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // this method is called by the timer every DELAY ms.
        // use this space to update the state of your game or animation
        // before the graphics are redrawn.

        // prevent the player from disappearing off the board
        player.tick();

        // give the player points for collecting coins
        collectCoins();

        // updates coins
        updateCoins();


        // calling repaint() will trigger paintComponent() to run again,
        // which will refresh/redraw the graphics.
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // when calling g.drawImage() we can use "this" for the ImageObserver 
        // because Component implements the ImageObserver interface, and JPanel 
        // extends from Component. So "this" Board instance, as a Component, can 
        // react to imageUpdate() events triggered by g.drawImage()

        // draw our graphics.
        drawBackground(g);
        drawScore(g);
        drawMaxScore(g);
        drawTimer(g);
        
        for (Coin coin : coins) {
            coin.draw(g, this);
        }
        player.draw(g, this);

        // this smooths out animations on some systems
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // this is not used but must be defined as part of the KeyListener interface
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // react to key down events
        player.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // react to key up events
    }
    private String timeClock(int n) {
        String numSeconds = String.valueOf(n%60);
        String numMinutes = String.valueOf(n/60);
        if (numSeconds.length() != 2) 
            numSeconds = "0" + numSeconds;
        if (numMinutes.length() != 2) 
            numMinutes = "0" + numMinutes;
        return numMinutes + ":" + numSeconds;
    }

    private void drawTimer(Graphics g) {
        if (player.getCount()/40 >= MAX_SECONDS){
            coins.clear();
            reset();
        }
        String text = timeClock(MAX_SECONDS - player.getCount()/40);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(new Color(0,0,0));
        g2d.setFont(new Font("Lato", Font.BOLD, 25));
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        Rectangle rect = new Rectangle(0, TILE_SIZE * (ROWS - 1), TILE_SIZE * COLUMNS, TILE_SIZE);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        g2d.drawString(text, x, metrics.getHeight());
    }
    private void drawMaxScore(Graphics g) {
        String maxScore = "Max Score: " + String.valueOf(getMaxScore());
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(new Color(237, 133, 155));
        g2d.setFont(new Font("Lato", Font.BOLD, 25));
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        Rectangle rect = new Rectangle(0, TILE_SIZE*(ROWS-1), TILE_SIZE*COLUMNS, TILE_SIZE);
        int x = rect.x + rect.width - metrics.stringWidth(maxScore);
        int y = metrics.getHeight();
        g2d.drawString(maxScore, x, y);

    }

    private void drawBackground(Graphics g) {
        // draw a checkered background
        g.setColor(new Color(214, 214, 214));
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                // only color every other tile
                if ((row + col) % 2 == 1) {
                    // draw a square tile at the current row/column position
                    g.fillRect(
                        col * TILE_SIZE, 
                        row * TILE_SIZE, 
                        TILE_SIZE, 
                        TILE_SIZE
                    );
                }
            }    
        }
    }

    private void drawScore(Graphics g) {
        // set the text to be displayed
        String text = "$" + player.getScore();
        // we need to cast the Graphics to Graphics2D to draw nicer text
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(
            RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(
            RenderingHints.KEY_FRACTIONALMETRICS,
            RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        // set the text color and font
        g2d.setColor(new Color(30, 201, 139));
        g2d.setFont(new Font("Lato", Font.BOLD, 25));
        // draw the score in the bottom center of the screen
        // https://stackoverflow.com/a/27740330/4655368
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        // the text will be contained within this rectangle.
        // here I've sized it to be the entire bottom row of board tiles
        Rectangle rect = new Rectangle(0, TILE_SIZE * (ROWS - 1), TILE_SIZE * COLUMNS, TILE_SIZE);
        // determine the x coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // determine the y coordinate for the text
        // (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // draw the string
        g2d.drawString(text, x, y);
    }

    private ArrayList<Coin> populateCoins() {
        ArrayList<Coin> coinList = new ArrayList<>();
        Random rand = new Random();
        ArrayList<Boolean> bool = new ArrayList<Boolean>();

        // create the given number of coins in random positions on the board.
        // note that there is not check here to prevent two coins from occupying the same NOW THERE IS
        // spot, nor to prevent coins from spawning in the same spot as the player EXCEPT FOR THE PLAYER
        for (int i = 0; i < NUM_COINS - 1; i++) {
            int coinX, coinY;
            boolean matches = true;
            Coin c = new Coin(0,0);
            
            while (matches) {
                coinX = rand.nextInt(COLUMNS);
                coinY = rand.nextInt(ROWS);
                c = new Coin(coinX, coinY);
                for (Coin coin : coinList) {
                    if (coin.getPos().equals(c.getPos())) {
                        bool.add(true);
                        break;
                    } else {
                        bool.add(false);
                    }
                }
                matches = bool.contains(true);
                bool.clear();
            } 
            coinList.add(c);
        }
        Count coun = new Count();
        coun.makeSpecial();
        counts.add(coun);
        return coinList;
    }

    private void collectCoins() {
        // allow player to pickup coins)
        ArrayList<Coin> collectedCoins = new ArrayList<>();
        for (Coin coin : coins) {
            // if the player is on the same tile as a coin, collect it
            if (player.getPos().equals(coin.getPos())) {
                
                // give the player some points for picking this up
                player.addScore(coin.getValue());
                collectedCoins.add(coin);
                Count coun = new Count();
                /*
                boolean special = false;
                for (Coin co : coins) {
                    if (co.isSpecial())
                        special = true;
                }
                for (Count co : counts) {
                    if (co.getSpecial())
                        special = true;
                }*/
                if (coin.getValue() == 500) {
                    coun.makeSpecial();
                    counts.add(coun);
                } 
                else   
                    counts.add(coun);
            }
        }
        // remove collected coins from the board
        coins.removeAll(collectedCoins);
        
        
        if (coins.size() < NUM_COINS && !counts.isEmpty() && counts.peek().getCount() >= MAX_COUNT) {
            addCoin();
            counts.poll();
        } else if (coins.size() < NUM_COINS && !counts.isEmpty() && counts.peek().getCount() < MAX_COUNT) {
            for (Count co : counts) {
                co.updateCount();
            }
        }
        // restart the game if score >= 5000
        //if (Integer.valueOf(player.getScore()) >= 5000) {
        //    coins.clear();
        //    reset();
        //}
    }
    public void reset() {
        writeScore(Integer.valueOf(player.getScore()));
        player = new Player();
        coins = populateCoins();
    }

    private void addCoin() {
        Random rand = new Random();
        int coinX, coinY;
        ArrayList<Boolean> bool = new ArrayList<Boolean>();
        boolean matches = true;
        Coin c = new Coin(0,0);
        while (matches) { // this whole thing makes sure the coins don't overlap
            coinX = rand.nextInt(COLUMNS);
            coinY = rand.nextInt(ROWS);
            c = new Coin(coinX, coinY);
            for (Coin coin : coins) {
                if (coin.getPos().equals(c.getPos())) {
                    bool.add(true);
                    break;
                } else 
                    bool.add(false);
            }
            matches = bool.contains(true);
            bool.clear();
        }
        boolean special = false;
        for (Coin coin : coins) {
            if (coin.isSpecial())
                special = true;
        }
        for (Count couns : counts) {
            if (couns.getSpecial())
                special = true;
        }
        if (!special) {
            c.makeSpecial();
            //System.out.println("made a special coin!");
        }
        coins.add(c);
    }

    private void updateCoins() {
        ArrayList<Coin> timedOutCoins = new ArrayList<Coin>();
        for (Coin c : coins) {
            c.updateCount();
            if (c.getTimeOut()) {
                Count coun = new Count();
                timedOutCoins.add(c);
                boolean special = false;
                //System.out.println("COINS:");
                for (Coin co : coins) {
                    //System.out.println(co.isSpecial());
                    if (co.isSpecial())
                        special = true;
                }
                //System.out.println("COUNTS");
                for (Count co : counts) {
                    //System.out.println(co.getSpecial());
                    if (co.getSpecial())   
                        special = true;
                }
                if (!special) 
                    coun.makeSpecial();
                counts.add(coun);
            }
        }
        coins.removeAll(timedOutCoins);
    }

    private int getMaxScore() {
        int maxScore = 0;
        try {
            File f = new File("Game/scores.txt");
            Scanner s = new Scanner(f);
            if (s.hasNextInt())
            maxScore = s.nextInt();
            while (s.hasNextInt()) {
                int next = s.nextInt();
                if (next > maxScore)
                    maxScore = next;
                
            }
            s.close();
            
        } catch (IOException e) {

        }
        return maxScore;
    }
    private void writeScore(int num) {
        String scores = readScores();
        String score = String.valueOf(num) + "\n";
        try {
            Path fileName = Path.of("Game/scores.txt");
            Files.writeString(fileName, scores + score); 
        } catch (IOException e) {
            System.out.println("Experienced an error!");
        }
    } 
    private String readScores() {
        String scores = "";
        try {
            File f = new File("game/scores.txt");
            Scanner s = new Scanner(f);
            while (s.hasNextLine())
                scores+=s.nextLine() + "\n";
            s.close();
        } catch (IOException e) {

        }
        
        return scores;
    }  

}