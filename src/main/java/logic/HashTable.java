package logic;// QuadraticProbing Hash table class
//
// CONSTRUCTION: an approximate initial size or default of 101
//
// ******************PUBLIC OPERATIONS*********************
// bool insert( x )       --> Insert x
// bool remove( x )       --> Remove x
// bool contains( x )     --> Return true if x is present
// void makeEmpty( )      --> Remove all items


import java.util.ArrayList;

/**
 * Probing table implementation of hash tables using quadratic probing.
 * Note that all "matching" is based on the equals method.
 */
public class HashTable<E> {

    // ==========================
    // Fields
    // ==========================
    private static final int DEFAULT_TABLE_SIZE = 101;
    private HashEntry<E>[] array; // The array of elements
    private int occupiedCt;       // Number of occupied cells
    private int currentActiveEntries; // Active entries
    private int totalProbes = 0;  // Total number of probes
    private int totalInsertions = 0; // Total insertions

    // ==========================
    // Constructors
    // ==========================
    public HashTable() {
        this(DEFAULT_TABLE_SIZE);
    }

    public HashTable(int size) {
        allocateArray(size);
        doClear();
    }

    // ==========================
    // Public Methods
    // ==========================
    public boolean insert(E x) {
        int currentPos = findPos(x);
        if (isActive(currentPos)) return false;

        array[currentPos] = new HashEntry<>(x, true);
        currentActiveEntries++;
        totalInsertions++;

        if (++occupiedCt > array.length / 2) rehash();

        return true;
    }

    public boolean remove(E x) {
        if (x == null) return false;
        int currentPos = findPos(x);
        if (isActive(currentPos)) {
            array[currentPos].isActive = false;
            currentActiveEntries--;
            return true;
        }
        return false;
    }

    public boolean contains(E x) {
        int currentPos = findPos(x);
        return isActive(currentPos);
    }

    public E find(E x) {
        int currentPos = findPos(x);
        return isActive(currentPos) ? array[currentPos].element : null;
    }

    public int size() {
        return currentActiveEntries;
    }

    public int capacity() {
        return array.length;
    }

    public void makeEmpty() {
        doClear();
    }

    public ArrayList<E> getAll() {
        ArrayList<E> list = new ArrayList<>();
        for (HashEntry<E> entry : array)
            if (entry != null && entry.isActive)
                list.add(entry.element);
        return list;
    }

    public String toString(int limit) {
        StringBuilder sb = new StringBuilder();
        int ct = 0;
        for (int i = 0; i < array.length && ct < limit; i++) {
            if (array[i] != null && array[i].isActive) {
                sb.append(i + ": " + array[i].element + "\n");
                ct++;
            }
        }
        return sb.toString();
    }

    public int getAverageProbeCount() {
        return totalInsertions == 0 ? 0 : totalProbes / totalInsertions;
    }

    public double getProbeCount() {
        return totalProbes;
    }

    // ==========================
    // Private Methods
    // ==========================
    private void rehash() {
        HashEntry<E>[] oldArray = array;
        allocateArray(2 * oldArray.length);
        occupiedCt = 0;
        currentActiveEntries = 0;

        for (HashEntry<E> entry : oldArray)
            if (entry != null && entry.isActive)
                insert(entry.element);
    }

    private int findPos(E x) {
        int offset = 1;
        int currentPos = myhash(x);
        int probes = 1;

        while (array[currentPos] != null && !array[currentPos].element.equals(x)) {
            currentPos += offset;
            offset += 2;
            probes++;
            if (currentPos >= array.length) currentPos -= array.length;
        }

        totalProbes += probes;
        return currentPos;
    }

    private boolean isActive(int currentPos) {
        return array[currentPos] != null && array[currentPos].isActive;
    }

    private void doClear() {
        occupiedCt = 0;
        currentActiveEntries = 0;
        for (int i = 0; i < array.length; i++)
            array[i] = null;
    }

    private int myhash(E x) {
        int hashVal = x.hashCode();
        hashVal %= array.length;
        if (hashVal < 0) hashVal += array.length;
        return hashVal;
    }

    private void allocateArray(int size) {
        array = new HashEntry[nextPrime(size)];
    }

    private static int nextPrime(int n) {
        if (n % 2 == 0) n++;
        while (!isPrime(n)) n += 2;
        return n;
    }

    private static boolean isPrime(int n) {
        if (n == 2 || n == 3) return true;
        if (n == 1 || n % 2 == 0) return false;
        for (int i = 3; i * i <= n; i += 2)
            if (n % i == 0)
                return false;
        return true;
    }

    // ==========================
    // Static Nested Class
    // ==========================
    private static class HashEntry<E> {
        public E element;
        public boolean isActive;

        public HashEntry(E e) {
            this(e, true);
        }

        public HashEntry(E e, boolean i) {
            element = e;
            isActive = i;
        }
    }
}
