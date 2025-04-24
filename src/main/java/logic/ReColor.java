package logic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Handles recoloring of images for demonstration and processing.
 * Currently, supports converting an image to red-channel only.
 */
public class ReColor {

    private BufferedImage img;
    private final String imageName;
    private final String redImageName;
    private final int cube;
    private final int height;
    private final int width;
    private final int colorLimit;

    /**
     * Initializes ReColor with an image and color parameters.
     * @param filename the image file to load
     * @param cube size of color quantization cube (not yet used)
     * @param colorLimit max number of colors (not yet used)
     */
    public ReColor(String filename, int cube, int colorLimit) {
        this.cube = cube;
        this.colorLimit = colorLimit;

        String[] parts = filename.split("\\.");
        this.imageName = parts[0] + colorLimit + "." + parts[1];
        this.redImageName = parts[0] + "Red." + parts[1];

        try {
            File file = new File(filename);
            this.img = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load image: " + filename, e);
        }

        this.width = img.getWidth();
        this.height = img.getHeight();
    }

    /**
     * Creates a red-channel version of the image and saves it to disk.
     */
    public void makeRed() {
        BufferedImage redImg = new BufferedImage(width, height, img.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = img.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;

                int redOnlyPixel = (alpha << 24) | (red << 16);
                redImg.setRGB(x, y, redOnlyPixel);
            }
        }

        try {
            ImageIO.write(redImg, "png", new File(redImageName));
        } catch (IOException e) {
            System.err.println("Failed to write red image: " + redImageName);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String[] files = {"chart.png", "bird.png", "butterfly.png", "cat.png", "dice.png", "flowers.png"};
        int[] colorMax = {5, 100, 100, 25, 6, 40};

        for (int i = 0; i < files.length; i++) {
            ReColor recolor = new ReColor(files[i], 6, colorMax[i]);
            recolor.makeRed();
        }
    }
}
