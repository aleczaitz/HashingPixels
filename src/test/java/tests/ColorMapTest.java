package tests;

import logic.ColorMap;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;

public class ColorMapTest {

    @Test
    public void testConstructorAndRepresentativeColor() {
        ColorMap c = new ColorMap(255, 100, 150, 200, 10);
        assertEquals(10, c.len);
        assertEquals(new Color(100 + 5, 150 + 5, 200 + 5), c.getRepresentativeColor());
        assertEquals(1, c.occurCt); // occurs once by default
    }

    @Test
    public void testSetMappedColor() {
        ColorMap base = new ColorMap(255, 5, 10, 15, 10);
        ColorMap target = new ColorMap(255, 10, 20, 22, 10);
        base.setMappedColor(target);

        Color expected = new Color(15, 25, 25);  // match computed result
        assertEquals(expected, base.getRepresentativeColor());
    }



    @Test
    public void testCompareTo() {
        ColorMap c1 = new ColorMap(255, 10, 10, 10, 10);
        ColorMap c2 = new ColorMap(255, 10, 10, 10, 10);
        c2.occurCt = 5;
        assertTrue(c1.compareTo(c2) < 0);
    }

    @Test
    public void testEqualsAndHashCode() {
        ColorMap a = new ColorMap(255, 100, 150, 200, 10);
        ColorMap b = new ColorMap(255, 100, 150, 200, 10);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testNotEqualsDifferentColor() {
        ColorMap a = new ColorMap(255, 100, 150, 200, 10);
        ColorMap b = new ColorMap(255, 120, 150, 200, 10); // 120 / 10 = 12 ≠ 10
        assertNotEquals(a, b);
    }
}
