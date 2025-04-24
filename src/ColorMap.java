import java.awt.*;

public class ColorMap implements Comparable<ColorMap> {
    public int alpha;
    public int red;
    public int green;
    public int blue;
    public int len;
    public int occurCt;
    public Color representativeColor;
    public Color mappedcolor; // This is the color that will actually be on the image based on n number of colors allowed
//    public int distance;

    ColorMap(int alpha, int r, int g, int b,int len) {
        this.alpha = alpha/ len;
        this.red = r/ len;
        this.green = g/ len;
        this.blue = b/ len;
        this.len = len;
        this.occurCt = 0;

        this.representativeColor = new Color(red* len + len /2, green* len + len /2, blue* len + len /2);
        occurCt =1;
    }

    void setMappedColor(ColorMap c){
        this.representativeColor = new Color(c.red* len + len /2, c.green* len + len /2, c.blue* len + len /2);
    }
    Color getRepresentativeColor(){
        return representativeColor;
    }

    public String toString() {
        return "Color  (" + red + "," + green + "," + blue + ") " + occurCt;
    }
    @Override
    // Will allow you to sort the ColorMap by most popular
    public int compareTo(ColorMap c2) {
        return occurCt -c2.occurCt;
    }
    @Override
    // Will allow you to find a previous entry in the HashTable
    public boolean equals (Object o){
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        ColorMap c = (ColorMap)o;
        return red==c.red && blue==c.blue && green==c.green;
    }
//    @Override
//    //Bad hashCode
//    public int hashCode() {
//        return red + green + blue;
//    }
    @Override
    public int hashCode() {
        return 31 * red + 17 * green + blue * 13;  // A more distributed hash code
}

}
