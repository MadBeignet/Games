package HorizontalPong;

import javax.swing.*;

public class App {

    private static void initWindow() {
        JFrame window = new JFrame("Pong but you can move horizontally");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Board board = new Board();
        window.add(board);
        window.addKeyListener(board);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public static void main(String[] args) {
        // invokeLater() is used here to prevent our graphics processing from
        // blocking the GUI. https://stackoverflow.com/a/22534931/4655368
        // this is a lot of boilerplate code that you shouldn't be too concerned about.
        // just know that when main runs it will call initWindow() once.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initWindow(); 
            }
        });
    }
}