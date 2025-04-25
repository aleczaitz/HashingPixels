package logic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Encodes an image by reducing the color palette using a hash-based quantization technique.
 */
public class Encode {

    private BufferedImage img;
    private final String imageName;
    private final String encodedImageName;
    private final int cube;
    private final int width;
    private final int height;
    private final int colorLimit;

    /**
     * Initializes the encoder with the given image and parameters.
     * @param filename the path to the image
     * @param cube size of quantization cube
     * @param colorLimit maximum number of colors to map to
     */
    public Encode(String filename, int cube, int colorLimit) {
        this.cube = cube;
        this.colorLimit = colorLimit;

        String[] parts = filename.split("\\.");
        this.imageName = parts[0] + colorLimit + "." + parts[1];
        this.encodedImageName = parts[0] + "Encoded." + parts[1];

        try {
            this.img = ImageIO.read(new File(filename));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image: " + filename, e);
        }

        this.width = img.getWidth();
        this.height = img.getHeight();
    }

    /**
     * Applies color quantization and saves an encoded image with fewer representative colors.
     * @param len quantization cube size
     * @param colormax maximum number of colors to use in final image
     */
    public void makeEncoded(int len, int colormax) {

        HashTable<ColorMap> hashTable = new HashTable<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x, y);
                int alpha = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                ColorMap cm = new ColorMap(alpha, r, g, b, len);
                ColorMap existing = hashTable.find(cm);

                if (existing == null) {
                    hashTable.insert(cm);
                } else {
                    existing.occurCt++;
                }
            }
        }

        ArrayList<ColorMap> sortedColors = hashTable.getAll();
        sortedColors.sort(Comparator.reverseOrder());

        ArrayList<Color> topColors = new ArrayList<>();
        int limit = Math.min(colormax, sortedColors.size());
        for (int i = 0; i < limit; i++) {
            topColors.add(sortedColors.get(i).getRepresentativeColor());
        }

        BufferedImage encoded = new BufferedImage(width, height, img.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x, y);
                Color original = new Color((p >> 16) & 0xff, (p >> 8) & 0xff, p & 0xff);
                Color closest = topColors.get(0);
                int minDist = Integer.MAX_VALUE;

                for (Color candidate : topColors) {
                    int dist = findDistance(original, candidate);
                    if (dist < minDist) {
                        closest = candidate;
                        minDist = dist;
                    }
                }

                encoded.setRGB(x, y, closest.getRGB());
            }
        }

        try {
            ImageIO.write(encoded, "png", new File(encodedImageName));
        } catch (IOException e) {
            System.err.println("Failed to write encoded image: " + encodedImageName);
            e.printStackTrace();
        }

        System.out.println("Average Probe Count: " + hashTable.getAverageProbeCount());
    }

    /**
     * Calculates the Euclidean distance between two colors in RGB space.
     */
    public static int findDistance(Color c1, Color c2) {
        return (int) Math.sqrt(
                Math.pow(c2.getRed() - c1.getRed(), 2) +
                        Math.pow(c2.getGreen() - c1.getGreen(), 2) +
                        Math.pow(c2.getBlue() - c1.getBlue(), 2)
        );
    }

    public static void main(String[] args) {
        String[] files = {"chart.png", "bird.png", "butterfly.png", "cat.png", "dice.png", "flowers.png",};
        int[] colorMax = {5, 100, 100, 25, 6, 40};

        for (int i = 0; i < files.length; i++) {
            Encode encoder = new Encode(files[i], 6, colorMax[i]);
            encoder.makeEncoded(6, 15);
        }
    }
}
