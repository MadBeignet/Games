package Game0;

import java.io.File;
import java.io.IOException;
import java.awt.Point;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;


public class Parent {
    
    private BufferedImage image;
    public Point pos;
    private String fileName;

    public Parent(String fileName, int x, int y) {
        this.fileName = fileName;
        loadImage();
        pos = new Point(x,y);
    }
    public void setFileName(String f) {
        fileName = f;
        loadImage();
    }

    protected void loadImage() {
        try {
            // you can use just the filename if the image file is in your
            // project folder, otherwise you need to provide the file path.
            image = ImageIO.read(new File(fileName));
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
    }
    public void draw(Graphics g, ImageObserver observer) {
        // with the Point class, note that pos.getX() returns a double, but 
        // pos.x reliably returns an int. https://stackoverflow.com/a/30220114/4655368
        // this is also where we translate board grid position into a canvas pixel
        // position by multiplying by the tile size.
        g.drawImage(
            image, 
            pos.x * Board.TILE_SIZE, 
            pos.y * Board.TILE_SIZE, 
            observer
        );
    }

    public Point getPos() {
        return pos;
    }

    public void translatePlayer(int x, int y) {
        pos.translate(x,y);
    }
}
