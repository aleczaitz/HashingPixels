// QuadraticProbing Hash table class
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
 * Probing table implementation of hash tables.
 * Note that all "matching" is based on the equals method.
 * @author Mark Allen Weiss
 */
public class HashTable<E>
{
    private int totalProbes = 0; // keep track of the probes
    private int totalInsertions = 0; // keep track of how many insertions


    /**
     * Construct the hash table.
     */
    public HashTable( )
    {
        this( DEFAULT_TABLE_SIZE );
    }

    /**
     * Construct the hash table.
     * @param size the approximate initial size.
     */
    public HashTable( int size )
    {
        allocateArray( size );
        doClear( );
    }

    /**
     * Insert into the hash table. If the item is
     * already present, do nothing.
     * @param x the item to insert.
     */
    public boolean insert( E x )
    {
        // Insert x as active
        int currentPos = findPos( x );
        if( isActive( currentPos ) )
            return false;

        array[ currentPos ] = new HashEntry<>( x, true );
        currentActiveEntries++;
        totalInsertions++;

        // Rehash if more than half the table is occupied
        if( ++occupiedCt > array.length / 2 )
            rehash( );

        return true;
    }


    // get all will be patterned just like this but will bring back an arraylist instead of a string
    public String toString (int limit){
        StringBuilder sb = new StringBuilder();
        int ct=0;
        for (int i=0; i < array.length && ct < limit; i++){
            if (array[i]!=null && array[i].isActive) {
                sb.append( i + ": " + array[i].element + "\n" );
                ct++;
            }
        }
        return sb.toString();
    }

    public ArrayList<E> getAll (){ // looks at the first set number of real entries and prints them out.
        ArrayList<E> hashList = new ArrayList<>();
        for (int i=0; i < array.length; i++){
            if (array[i]!=null && array[i].isActive) {
                hashList.add(array[i].element);
            }
        }
        return hashList;
    }


    /**
     * Expand the hash table.
     */
    private void rehash( )
    {
        HashEntry<E> [ ] oldArray = array;

        // Create a new double-sized, empty table
        allocateArray( 2 * oldArray.length );
        occupiedCt = 0;
        currentActiveEntries = 0;

        // Copy table over
        for( HashEntry<E> entry : oldArray )
            if( entry != null && entry.isActive )
                insert( entry.element );
    }

    /**
     * Method that performs quadratic probing resolution.
     * @param x the item to search for.
     * @return the position where the search terminates.
     * Never returns an inactive location.
     */
    private int findPos( E x )
    {
        int offset = 1;
        int currentPos = myhash( x );
        int probes = 1; // Start with the first attempt

        while( array[ currentPos ] != null &&
                !array[ currentPos ].element.equals( x ) )
        {
            currentPos += offset;  // Compute ith probe
            offset += 2;
            probes++; // count the current probe

            if( currentPos >= array.length )
                currentPos -= array.length;
        }
        totalProbes += probes; // add the probes form this operation to the total
        return currentPos;
    }

    /**
     * Remove from the hash table.
     * @param x the item to remove.
     * @return true if item removed
     */
    public boolean remove( E x )
    {
        if (x == null) {
            return false; // Added this in case find() passed a null pointer
        }
        int currentPos = findPos( x );
        if( isActive( currentPos ) )
        {
            array[ currentPos ].isActive = false;
            currentActiveEntries--;
            return true;
        }
        else
            return false;
    }

    /**
     * Get current size.
     * @return the size.
     */
    public int size( )
    {
        return currentActiveEntries;
    }

    /**
     * Get length of internal table.
     * @return the size.
     */
    public int capacity( )
    {
        return array.length;
    }

    /**
     * Find an item in the hash table.
     * @param x the item to search for.
     * @return true if item is found
     */
    public boolean contains( E x )
    {
        int currentPos = findPos( x );
        return isActive( currentPos );
    }

    /**
     * Find an item in the hash table.
     * @param x the item to search for.
     * @return the matching item.
     */
    public E find( E x )
    {
        int currentPos = findPos( x );
        if (!isActive( currentPos )) {
            return null;
        }
        else {
            return array[currentPos].element;
        }
    }


    /**
     * Return true if currentPos exists and is active.
     * @param currentPos the result of a call to findPos.
     * @return true if currentPos is active.
     */
    private boolean isActive( int currentPos )
    {
        return array[ currentPos ] != null && array[ currentPos ].isActive;
    }

    /**
     * Make the hash table logically empty.
     */
    public void makeEmpty( )
    {
        doClear( );
    }

    private void doClear( )
    {
        occupiedCt = 0;
        for( int i = 0; i < array.length; i++ )
            array[ i ] = null;
    }

    private int myhash( E x )
    {
        int hashVal = x.hashCode( );

        hashVal %= array.length;
        if( hashVal < 0 )
            hashVal += array.length;

        return hashVal;
    }

    private static class HashEntry<E>
    {
        public E  element;   // the element
        public boolean isActive;  // false if marked deleted

        public HashEntry( E e )
        {
            this( e, true );
        }

        public HashEntry( E e, boolean i )
        {
            element  = e;
            isActive = i;
        }
    }

    private static final int DEFAULT_TABLE_SIZE = 101;

    private HashEntry<E> [ ] array; // The array of elements
    private int occupiedCt;         // The number of occupied cells: active or deleted
    private int currentActiveEntries;                  // Current size

    /**
     * Internal method to allocate array.
     * @param arraySize the size of the array.
     */
    private void allocateArray( int arraySize )
    {
        array = new HashEntry[ nextPrime( arraySize ) ];
    }

    /**
     * Internal method to find a prime number at least as large as n.
     * @param n the starting number (must be positive).
     * @return a prime number larger than or equal to n.
     *
     */
    private static int nextPrime( int n )
    {
        if( n % 2 == 0 )
            n++;

        for( ; !isPrime( n ); n += 2 )
            ;

        return n;
    }

    /**
     * Internal method to test if a number is prime.
     * Not an efficient algorithm.
     * @param n the number to test.
     * @return the result of the test.
     */
    private static boolean isPrime( int n )
    {
        if( n == 2 || n == 3 )
            return true;

        if( n == 1 || n % 2 == 0 )
            return false;

        for( int i = 3; i * i <= n; i += 2 )
            if( n % i == 0 )
                return false;

        return true;
    }

    public int getAverageProbeCount() { // returns total insertions/total probes
        if (totalInsertions == 0) {
            return 0; //don't divide by zero
        }
        return totalProbes / totalInsertions;
    }

    public double getProbeCount() { // returns total probes
        return totalProbes;
    }


    // Simple main to test the hash table
    public static void main( String [ ] args ) {
        // Test code here

        HashTable<Pair<String, Integer>> H = new HashTable<>(); // Created the hashtable of pairs

        Pair<String,Integer> class1 = new Pair<>( "CS", 2420);
        Pair<String,Integer> class2 = new Pair<>( "Math", 2610);
        Pair<String,Integer> class3 = new Pair<>( "Phys", 2220);
        Pair<String,Integer> class4 = new Pair<>( "Eng", 2110);
        Pair<String,Integer> class5 = new Pair<>( "Gov", 2150);
        Pair<String,Integer> class6 = new Pair<>( "Econ", 3410);


        H.insert(class1);
        H.insert(class2);
        H.insert(class3);
        H.insert(class4);
        System.out.println("Testing inserting:\n" + H.getAll()); // Tested inserting Pairs

        H.remove(H.find(new Pair<>("Phys", 0)));
        System.out.println("Testing removing:\n" + H.getAll()); //Tested removing a Pair

        System.out.println("Testing finding: " + H.find(new Pair<>("CS", 0)) + "\n"); //Tested finding values

        System.out.println("Testing changing the second value:\nBefore: " + H.find(new Pair<>("CS", 0)));
        System.out.println(H.getAll());
        class1.set2(3000);
        System.out.println("After: " + H.find(new Pair<>("CS", 0)));
        System.out.println(H.getAll()); // testing changing the second value of an element

        System.out.println("Initial capacity: " + H.capacity());
        H.allocateArray(5);
        System.out.println("After allocating a smaller size: " + H.capacity());

        // trying to delete something that doesn't exist in the table
        System.out.println("Trying to delete an element that isn't there: "
                + H.remove(H.find(new Pair<>("bruh", 0))));

        System.out.println("\nInitial Capacity: " + H.capacity() + "\n");

        H.insert(class1);
        System.out.println("Capacity: " + H.capacity());
        System.out.println(H.getAll());

        H.insert(class2);
        System.out.println("Capacity: " + H.capacity());
        System.out.println(H.getAll());

        H.insert(class3);
        System.out.println("Capacity: " + H.capacity());
        System.out.println(H.getAll());

        H.insert(class4);
        System.out.println("Capacity: " + H.capacity());
        System.out.println(H.getAll());

        H.insert(class5);
        System.out.println("Capacity: " + H.capacity());
        System.out.println(H.getAll());

        H.insert(class6);
        System.out.println("Capacity: " + H.capacity());
        System.out.println(H.getAll());

        System.out.println("\nAverage probes: " + H.getAverageProbeCount());
        System.out.println("Probes: " + H.getProbeCount());

    }
}

