import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

public class ReColor {
    BufferedImage img;
    String imageName;
    String redImageName;
    int cube;
    int height = 0;
    int width = 0;
    int colorLimit = 0;

    /**
     * Set up the ReColor Class
     * @param filename  File containing the original image
     * @param cube Size of cube for which will accumulate colors
     * @param colorLimit
     */
    ReColor(String filename, int cube, int colorLimit) {
        img = null;
        this.cube = cube;
        this.colorLimit = colorLimit;
        File f = null;
        String[] p = filename.split("\\.");
        System.out.println("File name " + filename);
        imageName = p[0] + colorLimit + "." + p[1];
        redImageName = p[0] + "Red." + p[1];
        try {
            f = new File(
                    filename);
            img = ImageIO.read(f);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        width = img.getWidth();
        height = img.getHeight();
    }

    public void makeRed() {
        BufferedImage img2 = new BufferedImage(width, height, img.getType()); //makes new image
        for (int y = 0; y < height; y++) { // for each row
            for (int x = 0; x < width; x++) { // for each column
                int pixel = img.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff; //gets the transparency measure section
                int r = (pixel >> 16) & 0xff;  //gets the red section
                int g = (pixel >> 8) & 0xff;   // gets the green section
                int b = pixel & 0xff;          //gets the blue section
                // set new RGB keeping the alpha and the color wanted. Setting others to 0.
                pixel = (alpha << 24) | (r << 16) | (0 << 8) | 0; // sets 0s for G and B values (101011 101101 000000 000000)
                img2.setRGB(x, y, pixel); // sets the color of the pixel
            }
        }
        try {
            File f = new File(
                    redImageName);
            ImageIO.write(img2, "png", f);
        } catch (Exception e) {
            System.out.println(e);
        }
    }





    public static void main(String[] args) {
        String[] files = {"chart.png", "bird.png", "butterfly.png", "cat.png", "dice.png", "flowers.png"};
        int[] colorMax = {5, 100, 100, 25, 6, 40};

        for (int i = 0; i < files.length; i++) {

            ReColor r = new ReColor(files[i], 6, colorMax[i]);
            r.makeRed();
            //r.grabColors();
        }
    }
}
