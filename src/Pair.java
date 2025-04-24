public class Pair<T1,T2> {
    private T1 first;
    private T2 second;

    public T1 get1(){
        return this.first;
    }
    public T2 get2(){
        return this.second;
    }

    public void set2(T2 second) {  // Add setter for the second value
        this.second = second;
    }

    @Override // Equals compares the first element of the pair
            public boolean equals(Object other) {
        Pair<String, Integer> p = (Pair<String, Integer>) other;
        // This will only work with a Pair that is (String, Integer);
        return this.first == p.first;
    }

    @Override // Just hashes the first element in the pair
            public int hashCode() {
        return first.hashCode();
    }

    Pair( T1 a, T2 b){
        this.first = a;
        this.second = b;
      }

    public String toString(){
      return  "(" + first +"," +second +")";
    }

    public static void main(String args[]){
        HashTable<Pair<String, Integer>> H = new HashTable<>();
        H.insert(new Pair<>("Alec", 10));

        Pair<String,Integer> class1 = new Pair<>( "CS", 2420);
        Pair<String,Integer> class2 = new Pair<>( "Math", 2610);
        System.out.println("I am taking " + class1 + class2);
        H.insert(class1);
        H.insert(class2);

        System.out.println(H.find(new Pair<>("Alec", 0)));
    }
}