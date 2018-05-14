package abalone;

public class Pair<K, V> {

    //Key
    private K key;
    
    //Value
    private V value;
    
    public Pair() {}
    
    public Pair(K k, V v) {
        key = k;
        value = v;
    }
    
    public void put(K k, V v) {
        key = k;
        value = v;
    }
    public K getKey() {
        return key;
    }
    
    public V getValue() {
        return value;
    }
    
    public void setKey(K k) {
        key = k;
    }
    
    public void setValue(V v) {
        value = v;
    }
}
