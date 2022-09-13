import java.util.*;

public class HashTableSeparateChaining<K, V> implements HashTable<K, V>{
    private static final int DEFAULT_CAPACITY = 3;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private final double maxLoadFactor;
    private int capacity;
    private int threshold;
    private int size;

    private LinkedList<Entry<K, V>>[] table;


    public HashTableSeparateChaining() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashTableSeparateChaining(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    public HashTableSeparateChaining(int capacity, double loadFactor) {
        this.maxLoadFactor = loadFactor;
        this.capacity = capacity;
        this.threshold = (int)(capacity * loadFactor);
        table = new LinkedList[capacity];
    }

    private int normalizeIndex(int keyHash) {
        return (keyHash & 0x7FFFFFFF) % capacity;
    }

    private void resizeTable() {
        capacity *= 2;
        threshold = (int)(maxLoadFactor * capacity);
        LinkedList<Entry<K, V>>[] newTable = new LinkedList[capacity];

        for(int i = 0; i < table.length; i++) {
            if(table[i] != null) {
                for(Entry<K, V> entry : table[i]) {
                    int bucketIndex = normalizeIndex(entry.hash);
                    LinkedList<Entry<K, V>> bucket = newTable[bucketIndex];
                    if(bucket == null) newTable[bucketIndex] = bucket = new LinkedList<>();
                    bucket.add(entry);
                }

                table[i].clear();
                table[i] = null;
            }

        }

        table = newTable;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public V get(K key) {
        int bucketIndex = normalizeIndex(key.hashCode());

        LinkedList<Entry<K, V>> bucket = table[bucketIndex];
        if(bucket == null) return null;
        for(Entry<K, V> entry : bucket) {
            if(entry.key.equals(key)) return entry.value;
        }
        return null;
    }

    @Override
    public void set(K key, V value) {
        int bucketIndex = normalizeIndex(key.hashCode());

        LinkedList<Entry<K, V>> bucket = table[bucketIndex];
        if(bucket == null) return;
        for(Entry<K, V> entry : bucket) {
            if(entry.key == key){
                entry.value = value;
                return;
            }
        }
    }

    @Override
    public void add(K key, V value) {
        int bucketIndex = normalizeIndex(key.hashCode());

        LinkedList<Entry<K, V>> bucket = table[bucketIndex];
        if(bucket == null) table[bucketIndex] = bucket = new LinkedList<>();

        Entry<K, V> entry = new Entry<>(key, value);
        if(get(key) == null) {
            bucket.add(entry);
            if(++size > threshold) resizeTable();
        }
    }

    @Override
    public List<K> keys() {
        List<K> keys = new ArrayList<>();
        for(LinkedList<Entry<K, V>> bucket : table) {
            if(bucket != null) {
                for(Entry<K, V> entry : bucket) {
                    keys.add(entry.key);
                }
            }
        }

        return keys;
    }

    @Override
    public List<V> values() {
        List<V> values = new ArrayList<>();
        for(LinkedList<Entry<K, V>> bucket : table) {
            if(bucket != null) {
                for(Entry<K, V> entry : bucket) {
                    values.add(entry.value);
                }
            }
        }

        return values;
    }
}
