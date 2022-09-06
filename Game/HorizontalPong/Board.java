package HorizontalPong;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class Board extends JPanel implements ActionListener, KeyListener{
    
    // make delay
    private final int DELAY = 10;
    // tile size
    public static final int TILE_SIZE = 25;
    // #rows
    public static final int ROWS = 27;
    // #columns
    public static final int COLUMNS = 48;
    private final int PADDLE_HEIGHT = 100;
    private final int PADDLE_WIDTH = 50;
    public static final int VELOCITY = 5;
    private final int RANGE = 300;
    private int player1Score;
    private int player2Score;
    // Timer
    Timer timer;
    Ball ball;
    Paddle leftPaddle;
    Paddle rightPaddle;
    private int[] leftKeys = {KeyEvent.VK_W, KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_A};
    private int[] rightKeys = {KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT};
    // implement two paddles
  
    public Board() {
        // set the game board size
        setPreferredSize(new Dimension(TILE_SIZE * COLUMNS, TILE_SIZE * ROWS));
        // set the game board background color
        setBackground(new Color(232, 232, 232));
        ball = new Ball(10,10);
        // initialize the game state
        leftPaddle = new Paddle(new Point(0,(ROWS*TILE_SIZE-PADDLE_HEIGHT)/2), PADDLE_WIDTH, PADDLE_HEIGHT, leftKeys);
        rightPaddle = new Paddle(new Point(COLUMNS*TILE_SIZE-PADDLE_WIDTH, (ROWS*TILE_SIZE-PADDLE_HEIGHT)/2),PADDLE_WIDTH, PADDLE_HEIGHT, rightKeys);
        player1Score = 0;
        player2Score = 0;

        // this timer will call the actionPerformed() method every DELAY ms
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void actionPerformed(ActionEvent e) {
        // this method is called by the timer every DELAY ms.
        // use this space to update the state of your game or animation
        // before the graphics are redrawn.

        // prevent the player from disappearing off the board
        
        leftPaddle.tick();
        rightPaddle.tick();
        // give the player points for collecting coins
        hitBall();
        ball.moveX();
        ball.moveY();

        // updates coins
        


        // calling repaint() will trigger paintComponent() to run again,
        // which will refresh/redraw the graphics.
        repaint();
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
        drawBall(g);
        drawPaddles(g);
        drawScore(g);
        
        

        // this smooths out animations on some systems
        Toolkit.getDefaultToolkit().sync();
        leftPaddle.draw(g);
    }
    private void drawBackground(Graphics g) {
        // draw a checkered background
        g.setColor(new Color(214, 214, 214));
        g.fillRect(0,0,COLUMNS*TILE_SIZE,ROWS*TILE_SIZE);
        g.setColor(new Color(0,0,0));
        g.fillRect(0,(ROWS*TILE_SIZE-RANGE)/2, TILE_SIZE, TILE_SIZE/5);
        g.fillRect(0,(ROWS*TILE_SIZE + RANGE)/2 - TILE_SIZE/5, TILE_SIZE, TILE_SIZE/5);
        g.fillRect(COLUMNS*TILE_SIZE - TILE_SIZE, (ROWS*TILE_SIZE - RANGE)/2,TILE_SIZE, TILE_SIZE/5);
        g.fillRect(COLUMNS*TILE_SIZE - TILE_SIZE, (ROWS*TILE_SIZE + RANGE)/2, TILE_SIZE, TILE_SIZE/5);
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
        leftPaddle = new Paddle(new Point(0,(ROWS*TILE_SIZE-PADDLE_HEIGHT)/2), PADDLE_WIDTH, PADDLE_HEIGHT, leftKeys);
        rightPaddle = new Paddle(new Point(COLUMNS*TILE_SIZE-PADDLE_WIDTH, (ROWS*TILE_SIZE-PADDLE_HEIGHT)/2),PADDLE_WIDTH, PADDLE_HEIGHT, rightKeys);

    }
    private void hitBall() {
        // horizontal
        // leftPaddle right side
        if (leftPaddle.getPos().x + leftPaddle.getWidth() >= ball.getX() && leftPaddle.getPos().x + leftPaddle.getWidth() - ball.getWidth() <= ball.getX() && ball.getY() >= leftPaddle.getPos().y && ball.getY() <= leftPaddle.getPos().y + leftPaddle.getHeight())
            ball.setVelocityX(VELOCITY);
        // leftPaddle left side
        if (leftPaddle.getPos().x <= ball.getX() + ball.getWidth() && leftPaddle.getPos().x + ball.getWidth() >= ball.getX() && ball.getY() + ball.getHeight() >= leftPaddle.getPos().y && ball.getY() <= leftPaddle.getPos().y + leftPaddle.getHeight())
            ball.setVelocityX(-VELOCITY);
        // rightPaddle left side
        if (rightPaddle.getPos().x <= ball.getX() + ball.getWidth() && rightPaddle.getPos().x + ball.getWidth() >= ball.getX() + ball.getWidth() && ball.getY() + ball.getHeight() >= rightPaddle.getPos().y && ball.getY() <= rightPaddle.getPos().y + rightPaddle.getHeight())
            ball.setVelocityX(-VELOCITY);
        // rightPaddle right side
        if (rightPaddle.getPos().x + rightPaddle.getWidth() >= ball.getX() && rightPaddle.getPos().x + rightPaddle.getWidth() - ball.getWidth() <= ball.getX() && ball.getY() + ball.getHeight() >= rightPaddle.getPos().y && ball.getY() <= rightPaddle.getPos().y + rightPaddle.getHeight())
            ball.setVelocityX(VELOCITY);
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
        if (ball.getX() < 0 && ball.getY() >= (ROWS*TILE_SIZE - RANGE)/2 && ball.getY() <= (ROWS*TILE_SIZE + RANGE)/2) {
            nextRound();
            player2Score++;
        } else if (ball.getX() < 0) {
            ball.setVelocityX(VELOCITY);
        }
        if (ball.getX() + ball.getWidth() > COLUMNS*TILE_SIZE && ball.getY() >= (ROWS*TILE_SIZE - RANGE)/2 && ball.getY() <= (ROWS*TILE_SIZE + RANGE)/2) {
            nextRound();
            player1Score++;
        } else if (ball.getX() + ball.getWidth() > COLUMNS*TILE_SIZE) {
            ball.setVelocityX(-VELOCITY);
        }
        if (ball.getY() <= 0)
            ball.setVelocityY(VELOCITY);
        if (ball.getY() + ball.getHeight() >= ROWS*TILE_SIZE)
            ball.setVelocityY(-VELOCITY);



    }


}
