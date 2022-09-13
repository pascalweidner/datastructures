import java.util.*;

public class UnionFind<T> {
    private final ArrayList<Integer> size;
    private final ArrayList<Integer> id;
    private final Hashtable<T, Integer> table;
    private final Hashtable<Integer, T> table2;

    private int numComponents;

    public UnionFind(Collection<T> coll) {
        size = new ArrayList<>();
        id = new ArrayList<>();
        table = new Hashtable<>();
        table2 = new Hashtable<>();

        int i = 0;
        for(T elem : coll) {
            id.add(i);
            size.add(1);
            table.put(elem, i);
            table2.put(i, elem);
            numComponents++;
            i++;
        }
    }

    public T find(T elem) {
        int root = table.get(elem);
        while (root != id.get(root)) {
            root = id.get(root);
        }

        int p = table.get(elem);
        while(p != root) {
            int next = id.get(p);
            id.set(p, root);
            p = next;
        }

        return table2.get(root);
    }

    public boolean connected(T p, T q) {
        return find(p) == find(q);
    }

    public int componentSize(T elem) {
        return size.get(table.get(find(elem)));
    }

    public int getNumComponents() {
        return numComponents;
    }

    public void unify(T p, T q) {
        int root1 = table.get(find(p));
        int root2 = table.get(find(q));

        if(root1 == root2) return;

        if(size.get(root1) < size.get(root2)) {
            size.set(root2, size.get(root2) + size.get(root1));
            id.set(root1, root2);
        }
        else {
            size.set(root1, size.get(root2) + size.get(root1));
            id.set(root2, root1);
        }

        numComponents--;
    }


}
