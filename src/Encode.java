import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

public class Encode {
    BufferedImage img;
    String imageName;
    String encodedImageName;
    int cube;
    int height = 0;
    int width = 0;
    int colorLimit = 0;

    /**
     * Set up the Encode Class
     * @param filename  File containing the original image
     * @param cube Size of cube for which will accumulate colors
     * @param colorLimit
     */
    Encode(String filename, int cube, int colorLimit) { // Makes a new image to mess with
        img = null;
        this.cube = cube;
        this.colorLimit = colorLimit;
        File f = null;
        String[] p = filename.split("\\.");
        System.out.println("File name " + filename);
        imageName = p[0] + colorLimit + "." + p[1];
        encodedImageName = p[0] + "Encoded." + p[1];
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

    // This method makes a hashtable of colorMaps based on the input image, then sorts them and creates a list
    // of colors to be used in the new image
    public void makeEncoded(int len, int colormax) {
        HashTable<ColorMap> H = new HashTable<>(); // create the hashtable

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) { // for each pixel
                int p = img.getRGB(x, y); // 010010 101101 101110 101110
                int alpha = (p >> 24) & 0xff; //transparency measure
                int r = (p >> 16) & 0xff;  //red
                int g = (p >> 8) & 0xff;   //green
                int b = p & 0xff;          //blue
                ColorMap cm = new ColorMap(alpha, r, g, b, len); // create color map from pixel
                ColorMap exist = H.find(cm); // Add each colorMap to a hashtable, count how many times they come up
                if (exist == null) { // If the ColorMap DOESN'T exist in the table
                    H.insert(cm); // Insert it
                } else {
                    exist.occurCt++; // Mark that it occurred again
                }
            }
        }
        ArrayList<ColorMap> keys = H.getAll(); // complete list of color mapped pixels in order of the picture
        keys.sort(Comparator.reverseOrder()); // Sorted list

        ArrayList<Color> ColorstoMapto = new ArrayList<>(); // List of Colors to map to
        int MaxLength = Math.min(colormax, keys.size());

        for (int i = 0; i < MaxLength; i++) {
            Color color = keys.get(i).getRepresentativeColor();
            ColorstoMapto.add(color);
        }

        BufferedImage img2 = new BufferedImage(width, height, img.getType()); //makes new image
        // Iterate over every pixel in the original image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x, y);
                int r = (p >> 16) & 0xff;  //red
                int g = (p >> 8) & 0xff;   //green
                int b = p & 0xff;          //blue

                Color color = new Color(r, g, b, 255);
                Color newcolor = new Color(255, 255, 255, 255);

                int lastdistance = Integer.MAX_VALUE;
                for (Color i : ColorstoMapto) {
                    int thisDistance = findDistance(color, i); // sets the distance between each key and each chosen color
                    if (thisDistance < lastdistance) {
                        newcolor = i;

                        lastdistance = thisDistance;
                    }
                }
                img2.setRGB(x, y, newcolor.getRGB());

            }
        }
        try {
            File f = new File(encodedImageName);
            ImageIO.write(img2, "png", f);
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Average Probe count: " + H.getAverageProbeCount());
    }
    public static int findDistance(Color c1, Color c2) {
        return (int) Math.sqrt(
                Math.pow(c2.getRed() - c1.getRed(), 2) +
                        Math.pow(c2.getGreen() - c1.getGreen(), 2) +
                        Math.pow(c2.getBlue() - c1.getBlue(), 2)
        );
    }
    public static void main(String[] args) {
        String[] files = {"chart.png", "bird.png", "butterfly.png", "cat.png", "dice.png", "flowers.png"};
        int[] colorMax = {5, 100, 100, 25, 6, 40};

        for (int i = 0; i < files.length; i++) {

            Encode r = new Encode(files[i], 6, colorMax[i]);
            r.makeEncoded(6, 5);

        }
    }
}