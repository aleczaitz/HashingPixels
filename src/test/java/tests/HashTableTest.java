package tests;

import logic.HashTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class HashTableTest {
    private HashTable<String> table;

    @BeforeEach
    public void setUp() {
        table = new HashTable<>();
    }

    @Test
    public void testInsertAndContains() {
        assertTrue(table.insert("apple"));
        assertFalse(table.insert("apple")); // no duplicate
        assertTrue(table.contains("apple"));
    }

    @Test
    public void testRemove() {
        table.insert("banana");
        assertTrue(table.remove("banana"));
        assertFalse(table.contains("banana"));
        assertFalse(table.remove("banana")); // already removed
    }

    @Test
    public void testFind() {
        table.insert("cherry");
        assertEquals("cherry", table.find("cherry"));
        assertNull(table.find("missing"));
    }

    @Test
    public void testSize() {
        table.insert("a");
        table.insert("b");
        assertEquals(2, table.size());
        table.remove("a");
        assertEquals(1, table.size());
    }

    @Test
    public void testCapacity() {
        assertTrue(table.capacity() >= 101); // default size is at least 101
    }

    @Test
    public void testMakeEmpty() {
        table.insert("x");
        table.insert("y");
        table.makeEmpty();
        assertEquals(0, table.size());
        assertFalse(table.contains("x"));
    }

    @Test
    public void testGetAll() {
        table.insert("dog");
        table.insert("cat");
        List<String> all = table.getAll();
        assertTrue(all.contains("dog"));
        assertTrue(all.contains("cat"));
        assertEquals(2, all.size());
    }

    @Test
    public void testToString() {
        table.insert("zebra");
        String output = table.toString(5);
        assertTrue(output.contains("zebra"));
    }

    @Test
    public void testAverageProbeCountAndProbes() {
        table.insert("mango");
        table.insert("melon");
        double probes = table.getProbeCount();
        int avg = table.getAverageProbeCount();
        assertTrue(probes >= 2); // at least one probe per insert
        assertTrue(avg >= 1);
    }
}
