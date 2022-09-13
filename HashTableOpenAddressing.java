import java.util.ArrayList;
import java.util.List;

public class HashTableOpenAddressing<K, V> implements HashTable<K, V>{
    private static final int DEFAULT_CAPACITY = 8;
    private static final double DEFAULT_LOAD_FACTOR = 0.45;

    private int capacity, threshold, modificationCount = 0;
    private double loadFactor;
    private K [] keyTable;
    private V [] valueTable;

    private int usedBuckets, size;

    private boolean containsFlag = false;

    private final K TOMBSTONE = (K) (new Object());

    public HashTableOpenAddressing() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashTableOpenAddressing(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    public HashTableOpenAddressing(int capacity, double loadFactor) {
        this.loadFactor = loadFactor;
        this.capacity = Math.max(DEFAULT_CAPACITY, next2Power(capacity));
        threshold = (int) (this.capacity * loadFactor);

        keyTable = (K[]) new Object[this.capacity];
        valueTable = (V[]) new Object[this.capacity];
    }

    private static int next2Power(int n) {
        return Integer.highestOneBit(n) << 1;
    }

    private static int P(int x) {
        return (x*x + x) >> 1;
    }

    private int normalizeIndex(int keyHash) {
        return (keyHash & 0x7FFFFFFF) % capacity;
    }

    private void resizeTable() {
        capacity *= 2;
        threshold = (int) (capacity * loadFactor);

        K[] oldKeyTable = (K[]) new Object[capacity];
        V[] oldValueTable = (V[]) new Object[capacity];

        K[] keyTableTmp = keyTable;
        keyTable = oldKeyTable;
        oldKeyTable = keyTableTmp;

        V[] valueTableTmp = valueTable;
        valueTable = oldValueTable;
        oldValueTable = valueTableTmp;

        size = usedBuckets = 0;

        for(int i = 0; i < oldKeyTable.length; i++) {
            if(oldKeyTable[i] != null && oldKeyTable[i] != TOMBSTONE) {
                add(oldKeyTable[i], oldValueTable[i]);
            }
            oldValueTable[i] = null;
            oldKeyTable[i] = null;
        }
    }

    public void clear() {
        for(int i = 0; i < capacity; i++) {
            keyTable[i] = null;
            valueTable[i] = null;
        }
        size = usedBuckets = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public V get(K key) {
        final int hash = normalizeIndex(key.hashCode());
        int i = hash, j = -1, x = 1;

        do {
            if (keyTable[i] == TOMBSTONE) {
                if (j == -1) i = j;
            } else if (keyTable[i] != null) {
                if (keyTable[i].equals(key)) {
                    containsFlag = true;

                    if (j != -1) {
                        keyTable[j] = keyTable[i];
                        valueTable[j] = valueTable[i];
                        keyTable[i] = TOMBSTONE;
                        valueTable[i] = null;

                        return valueTable[j];
                    } else {
                        return valueTable[i];
                    }
                }
            } else {
                containsFlag = false;
                return null;
            }

            i = normalizeIndex(hash + P(x++));
        } while( true );
    }

    @Override
    public void set(K key, V value) {
        if(usedBuckets >= threshold) resizeTable();

        final int hash = normalizeIndex(key.hashCode());
        int i = hash, j = -1, x = 1;

        do {
            if(keyTable[i] == TOMBSTONE) {
                if(j == -1) j = i;
            }
            else if(keyTable[i] != null) {
                if(keyTable[i].equals(key)) {
                    if (j == - 1) {
                        valueTable[i] = value;
                    } else {
                        keyTable[i] = TOMBSTONE;
                        valueTable[i] = null;
                        keyTable[j] = key;
                        valueTable[j] = value;
                    }

                    modificationCount++;
                    return;
                }
            }
            else {
                modificationCount++;
                return;
            }

            i = normalizeIndex(hash + P(x++));
        } while(true);
    }

    @Override
    public void add(K key, V value) {
        if(usedBuckets >= threshold) resizeTable();

        final int hash = normalizeIndex(key.hashCode());
        int i = hash, j = -1, x = 1;

        do {
            if(keyTable[i] == TOMBSTONE) {
                if(j == -1) j = i;
            }
            else if(keyTable[i] != null) {
                if(keyTable[i].equals(key)) {
                    V oldValue = valueTable[i];
                    if (j == - 1) {
                        valueTable[i] = oldValue;
                    } else {
                        keyTable[i] = TOMBSTONE;
                        valueTable[i] = null;
                        keyTable[j] = key;
                        valueTable[j] = oldValue;
                    }

                    modificationCount++;
                    return;
                }
            }
            else {
                if (j == -1) {
                    usedBuckets++;
                    size++;
                    keyTable[i] = key;
                    valueTable[i] = value;
                }
                else {
                    size++;
                    keyTable[j] = key;
                    valueTable[j] = value;
                }

                modificationCount++;
                return;
            }

            i = normalizeIndex(hash + P(x++));
        } while(true);
    }

    public void remove(K key) {
        final int hash = normalizeIndex(key.hashCode());
        int i = hash, x = 1;

        for(;; i = normalizeIndex(hash + P(x++))) {
            if(keyTable[i] == TOMBSTONE) continue;
            if (keyTable[i] == null) return;
            if(keyTable[i].equals(key)) {
                size--;
                modificationCount++;
                keyTable[i] = TOMBSTONE;
                valueTable[i] = null;
                return;
            }
        }
    }

    @Override
    public List<K> keys() {
        List<K> keys = new ArrayList<>(size);
        for(int i = 0; i < capacity; i++) {
            if(keyTable[i] != null && keyTable[i] != TOMBSTONE) {
                keys.add(keyTable[i]);
            }
        }
        return keys;
    }

    @Override
    public List<V> values() {
        List<V> values = new ArrayList<>(size);
        for(int i = 0; i < capacity; i++) {
            if(valueTable[i] != null) {
                values.add(valueTable[i]);
            }
        }
        return values;
    }
}
