import java.util.List;

public interface HashTable<K, V> {
    boolean isEmpty();

    int size();

    V get(K key);

    void set(K key, V value);

    void add(K key, V value);

    List<K> keys();

    List<V> values();
}
