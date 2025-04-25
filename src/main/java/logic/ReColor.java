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

    public void makeColor(double redFactor, double greenFactor, double blueFactor) {
        BufferedImage colorImg = new BufferedImage(width, height, img.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = img.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                int origRed = (pixel >> 16) & 0xff;
                int origGreen = (pixel >> 8) & 0xff;
                int origBlue = pixel & 0xff;

                // Blend original color with the selected color
                int newRed = (int)(origRed * redFactor);
                int newGreen = (int)(origGreen * greenFactor);
                int newBlue = (int)(origBlue * blueFactor);

                // Clamp values between 0 and 255
                newRed = Math.min(255, Math.max(0, newRed));
                newGreen = Math.min(255, Math.max(0, newGreen));
                newBlue = Math.min(255, Math.max(0, newBlue));

                int blendedPixel = (alpha << 24) | (newRed << 16) | (newGreen << 8) | newBlue;
                colorImg.setRGB(x, y, blendedPixel);
            }
        }

        try {
            String baseFilename = new File(imageName).getName(); // example: test_input16.png
            baseFilename = baseFilename.replaceAll("\\d+", ""); // remove any digits like 16
            baseFilename = baseFilename.replace(".png", ""); // remove .png to avoid double
            String customColorName = baseFilename + "CustomColor.png";

            ImageIO.write(colorImg, "png", new File(customColorName));
        } catch (IOException e) {
            System.err.println("Failed to write custom color image.");
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
