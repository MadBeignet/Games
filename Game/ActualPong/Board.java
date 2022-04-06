package Game.ActualPong;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import javax.swing.*;


public class Board extends JPanel implements ActionListener, KeyListener{
    
    // make delay
    private final int DELAY = 25;
    // tile size
    public static final int TILE_SIZE = 25;
    // #rows
    public static final int ROWS = 27;
    // #columns
    public static final int COLUMNS = 48;
    private final int PADDLE_HEIGHT = 100;
    private final int PADDLE_WIDTH = 20;
    public static final int VELOCITY = 10;
    private int player1Score;
    private int player2Score;
    private boolean player1 = false;
    private boolean player2 = false;
    Color col;
    // Timer
    Timer timer;
    Ball ball;
    Paddle leftPaddle;
    Paddle rightPaddle;
    private int[] leftKeys = {KeyEvent.VK_W, KeyEvent.VK_S};
    private int[] rightKeys = {KeyEvent.VK_UP, KeyEvent.VK_DOWN};
    // implement two paddles
  
    public Board() {
        col = new Color(0,0,0);
        // set the game board size
        setPreferredSize(new Dimension(TILE_SIZE * COLUMNS, TILE_SIZE * ROWS));
        // set the game board background color
        //setBackground(new Color(232, 232, 232));
        //setBackground(new Color(0,0,0));
        ball = new Ball(10,10);
        ball.setVelocityX(VELOCITY*pickRandom());
        // initialize the game state
        leftPaddle = new Paddle(new Point(0,(ROWS*TILE_SIZE-PADDLE_HEIGHT)/2), PADDLE_WIDTH, PADDLE_HEIGHT, leftKeys);
        rightPaddle = new Paddle(new Point(COLUMNS*TILE_SIZE-PADDLE_WIDTH, (ROWS*TILE_SIZE-PADDLE_HEIGHT)/2),PADDLE_WIDTH, PADDLE_HEIGHT, rightKeys);
        player1Score = 0;
        player2Score = 0;

        // this timer will call the actionPerformed() method every DELAY ms
        timer = new Timer(DELAY, this);
        timer.start();
    }
    private int pickRandom() {
        return (int) (Math.random()*2)  == 1 ? 1 : -1;
    }
    public void actionPerformed(ActionEvent e) {
        // this method is called by the timer every DELAY ms.
        // use this space to update the state of your game or animation
        // before the graphics are redrawn.

        // prevent the player from disappearing off the board
        //backgroundIncrease();
        leftPaddle.tick();
        rightPaddle.tick();
        // give the player points for collecting coins
        hitBall();
        ball.moveX();
        ball.moveY();
        
        //System.out.println("x: " + ball.getVelocityX());
        //System.out.println("y: " + ball.getVelocityY());

        // updates coins
        


        // calling repaint() will trigger paintComponent() to run again,
        // which will refresh/redraw the graphics.
        repaint();
    }
    public Color backgroundIncrease(Graphics g, Color c) {
        int[] rgb = {c.getRed(), c.getGreen(), c.getBlue()};
        if (rgb[2] < 255-4) {
            rgb[2] = rgb[2] + 5;
        } else
            rgb[2] = 150;
        if (rgb[1] < 255 - 2) {
            rgb[1] = rgb[1] + 3;
        } else
            rgb[1] = 150;
        if (rgb[0] < 255) {
            rgb[0] = rgb[0] + 1;
        } else
            rgb[0] = 100;
       
        g.setColor(new Color(rgb[0], rgb[1], rgb[2]));
        g.fillRect(0,0,COLUMNS*TILE_SIZE,ROWS*TILE_SIZE);
        return new Color(rgb[0], rgb[1], rgb[2]);
    }
    @Override
    public void keyTyped(KeyEvent e) {
        // this is not used but must be defined as part of the KeyListener interface
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // react to key down events
        leftPaddle.keyPressed(e);
        rightPaddle.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // react to key up events
        leftPaddle.keyReleased(e);
        rightPaddle.keyReleased(e);
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
       
        drawPaddles(g);
        drawBall(g);
        
        drawScore(g);
        
        
        

        // this smooths out animations on some systems
        Toolkit.getDefaultToolkit().sync();
        
    }
    private void drawBackground(Graphics g) {
        // draw a checkered background
        g.setColor(new Color(214, 214, 214));
        g.fillRect(0,0,COLUMNS*TILE_SIZE,ROWS*TILE_SIZE);
    }
    private void drawBall(Graphics g) {
        g.setColor(new Color(0,0,0));
        g.fillRect(ball.getX(), ball.getY(),ball.getWidth(), ball.getHeight());
    }
    private void drawPaddles(Graphics g) {
        
        g.setColor(new Color(255,0,0));
        g.fillRect(leftPaddle.getPos().x,leftPaddle.getPos().y, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillRect(rightPaddle.getPos().x, rightPaddle.getPos().y, PADDLE_WIDTH, PADDLE_HEIGHT);
        
    }
    private void drawScore(Graphics g) {
        String text = player1Score + " vs " + player2Score;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(0,0,0));
        g2d.setFont(new Font("Lato", Font.BOLD,25));
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        Rectangle rect = new Rectangle(0, TILE_SIZE * (ROWS - 1), TILE_SIZE * COLUMNS, TILE_SIZE);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g2d.drawString(text,x,y);
    }
    private void nextRound() {
        ball = new Ball(10,10);
        ball.setVelocityX(VELOCITY*pickRandom());
        leftPaddle = new Paddle(new Point(0,(ROWS*TILE_SIZE-PADDLE_HEIGHT)/2), PADDLE_WIDTH, PADDLE_HEIGHT, leftKeys);
        rightPaddle = new Paddle(new Point(COLUMNS*TILE_SIZE-PADDLE_WIDTH, (ROWS*TILE_SIZE-PADDLE_HEIGHT)/2),PADDLE_WIDTH, PADDLE_HEIGHT, rightKeys);

    }
    private void hitBall() {
        // horizontal
        // leftPaddle right side
        if (leftPaddle.getPos().x + leftPaddle.getWidth() >= ball.getX() && leftPaddle.getPos().x + leftPaddle.getWidth() - ball.getWidth() <= ball.getX() && ball.getY() >= leftPaddle.getPos().y && ball.getY() <= leftPaddle.getPos().y + leftPaddle.getHeight()){
            ball.setVelocityX(VELOCITY*2 + ball.getVelocityX());
            ball.setVelocityY((int) (ball.getVelocityY() + 0.4 * leftPaddle.getVelocityY()));
        }
        // leftPaddle left side
        if (leftPaddle.getPos().x <= ball.getX() + ball.getWidth() && leftPaddle.getPos().x + ball.getWidth() >= ball.getX() && ball.getY() + ball.getHeight() >= leftPaddle.getPos().y && ball.getY() <= leftPaddle.getPos().y + leftPaddle.getHeight()) {
            ball.setVelocityX(-VELOCITY*2 + ball.getVelocityX());
            ball.setVelocityY((int) (ball.getVelocityY() + 0.4 * leftPaddle.getVelocityY()));
        }
        // rightPaddle left side
        if (rightPaddle.getPos().x <= ball.getX() + ball.getWidth() && rightPaddle.getPos().x + ball.getWidth() >= ball.getX() + ball.getWidth() && ball.getY() + ball.getHeight() >= rightPaddle.getPos().y && ball.getY() <= rightPaddle.getPos().y + rightPaddle.getHeight()){
            ball.setVelocityX(-VELOCITY*2 + ball.getVelocityX());
            ball.setVelocityY((int) (ball.getVelocityY() + 0.4 * rightPaddle.getVelocityY()));
        }
        // rightPaddle right side
        if (rightPaddle.getPos().x + rightPaddle.getWidth() >= ball.getX() && rightPaddle.getPos().x + rightPaddle.getWidth() - ball.getWidth() <= ball.getX() && ball.getY() + ball.getHeight() >= rightPaddle.getPos().y && ball.getY() <= rightPaddle.getPos().y + rightPaddle.getHeight()) {
            ball.setVelocityX(VELOCITY*2 + ball.getVelocityX());
            ball.setVelocityY((int) (ball.getVelocityY() + 0.4 * rightPaddle.getVelocityY()));
        }
        // vertical 
        // leftPaddle top
        if (leftPaddle.getPos().y <= ball.getY() + ball.getHeight() && leftPaddle.getPos().y + ball.getHeight() >= ball.getY() + ball.getHeight() && ball.getX() + ball.getWidth() >= leftPaddle.getPos().x && ball.getX() <= leftPaddle.getPos().x + leftPaddle.getWidth())
            ball.setVelocityY(-VELOCITY);
        // leftPaddle bottom
        if (leftPaddle.getPos().y + leftPaddle.getHeight() >= ball.getY() && leftPaddle.getPos().y + leftPaddle.getHeight() - ball.getHeight() <= ball.getY() && ball.getX() + ball.getWidth() >= leftPaddle.getPos().x && ball.getX() <= leftPaddle.getPos().x + leftPaddle.getWidth())
            ball.setVelocityY(VELOCITY);
        // rightPaddle top
        if (rightPaddle.getPos().y <= ball.getY() + ball.getHeight() && rightPaddle.getPos().y + ball.getHeight() >= ball.getY() && ball.getX() + ball.getWidth() >= rightPaddle.getPos().x && ball.getX() <= rightPaddle.getPos().x + rightPaddle.getWidth())
            ball.setVelocityY(-VELOCITY);
        // rightPaddle bottom
        if (rightPaddle.getPos().y + rightPaddle.getHeight() >= ball.getY() && rightPaddle.getPos().y + rightPaddle.getHeight() - ball.getHeight() <= ball.getY() && ball.getX() + ball.getWidth() >= rightPaddle.getPos().x && ball.getX() <= rightPaddle.getPos().x + rightPaddle.getWidth())
            ball.setVelocityY(VELOCITY);
        if (ball.getX() < 0) {
            player2 = true;
            player2Score++;
            nextRound();
        } 
        if (ball.getX() + ball.getWidth() > COLUMNS*TILE_SIZE ) {
            player2 = true;
            player1Score++;
            nextRound();
        } 
        if (ball.getY() <= 0)
            ball.setVelocityY(-ball.getVelocityY());
        if (ball.getY() + ball.getHeight() >= ROWS*TILE_SIZE)
            ball.setVelocityY(-ball.getVelocityY());



    }


}
