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

                // Use the original red channel as brightness (like your makeRed)
                int brightness = origRed;

                // Now apply the selected color multiplied by brightness
                int newRed = (int)(brightness * redFactor);
                int newGreen = (int)(brightness * greenFactor);
                int newBlue = (int)(brightness * blueFactor);

                // Clamp to [0, 255]
                newRed = Math.min(255, Math.max(0, newRed));
                newGreen = Math.min(255, Math.max(0, newGreen));
                newBlue = Math.min(255, Math.max(0, newBlue));

                int tintedPixel = (alpha << 24) | (newRed << 16) | (newGreen << 8) | newBlue;
                colorImg.setRGB(x, y, tintedPixel);
            }
        }

        try {
            String baseFilename = new File(imageName).getName();
            baseFilename = baseFilename.replaceAll("\\d+", "");
            baseFilename = baseFilename.replace(".png", "");
            String customColorName = baseFilename + "CustomColor.png";

            ImageIO.write(colorImg, "png", new File(customColorName));
        } catch (IOException e) {
            System.err.println("Failed to write custom color image.");
            e.printStackTrace();
        }
    }

    public BufferedImage makeColorImage(double redFactor, double greenFactor, double blueFactor) {
        BufferedImage colorImg = new BufferedImage(width, height, img.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = img.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                int origRed = (pixel >> 16) & 0xff;

                int brightness = origRed;

                int newRed = (int)(brightness * redFactor);
                int newGreen = (int)(brightness * greenFactor);
                int newBlue = (int)(brightness * blueFactor);

                newRed = Math.min(255, Math.max(0, newRed));
                newGreen = Math.min(255, Math.max(0, newGreen));
                newBlue = Math.min(255, Math.max(0, newBlue));

                int tintedPixel = (alpha << 24) | (newRed << 16) | (newGreen << 8) | newBlue;
                colorImg.setRGB(x, y, tintedPixel);
            }
        }

        return colorImg;
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
