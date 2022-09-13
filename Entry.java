import java.util.Objects;

public class Entry <K, V> {
    int hash;
    K key; V value;

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
        this.hash = key.hashCode();
    }

    public boolean equal(Entry<K, V> other) {
        if (hash != other.hash ) return false;
        return Objects.equals(key, other.key);
    }

    @Override
    public String toString() {
        return key + ": " + value;
    }
}
