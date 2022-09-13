import java.util.Arrays;
import java.util.List;

public class FenwickTree {
    private final int size;
    private long[] tree;

    public FenwickTree(int sz) {
        tree = new long[(size = sz + 1)];
    }

    public FenwickTree(long[] values) {
        size = values.length;
        values[0] = 0L;

        tree = values.clone();

        for(int i = 1; i < size; i++) {
            int parent = i + LSB(i);
            if(parent < size) tree[parent] += tree[i];

        }

    }

    private static int LSB(int i) {
        return Integer.lowestOneBit(i);
    }

    private long prefixSum(int i){
        long sum = 0;
        while(i != 0) {
            sum += tree[i];
            i = i - LSB(i);
        }
        return sum;
    }

    public int size() {
        return size;
    }

    public long sum(int left, int right) {
        return prefixSum(right) - prefixSum(left - 1);
    }

    public void add(int i, long x) {
        while (i < size) {
            tree[i] += x;
            i = i + LSB(i);
        }
    }

    public long get(int i) {
        return sum(i, i);
    }

    @Override
    public String toString() {
        return Arrays.toString(tree);
    }


}
