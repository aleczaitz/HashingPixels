import java.awt.*;

/**
 * Represents a quantized color used for mapping and comparison.
 * Used in image encoding and color reduction.
 */
public class ColorMap implements Comparable<ColorMap> {

    // ==========================
    // Fields
    // ==========================
    public int alpha;
    public int red;
    public int green;
    public int blue;
    public int len;
    public int occurCt;
    public Color representativeColor;
    public Color mappedcolor;

    // ==========================
    // Constructor
    // ==========================
    public ColorMap(int alpha, int r, int g, int b, int len) {
        this.alpha = alpha / len;
        this.red = r / len;
        this.green = g / len;
        this.blue = b / len;
        this.len = len;
        this.occurCt = 1;
        this.representativeColor = new Color(
                red * len + len / 2,
                green * len + len / 2,
                blue * len + len / 2
        );
    }

    // ==========================
    // Public Methods
    // ==========================
    public void setMappedColor(ColorMap c) {
        this.representativeColor = new Color(
                c.red * len + len / 2,
                c.green * len + len / 2,
                c.blue * len + len / 2
        );
    }

    public Color getRepresentativeColor() {
        return representativeColor;
    }

    @Override
    public String toString() {
        return "Color  (" + red + "," + green + "," + blue + ") " + occurCt;
    }

    @Override
    public int compareTo(ColorMap c2) {
        return this.occurCt - c2.occurCt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColorMap c = (ColorMap) o;
        return red == c.red && green == c.green && blue == c.blue;
    }

    @Override
    public int hashCode() {
        return 31 * red + 17 * green + 13 * blue;
    }
}
