import java.util.ArrayList;
import java.util.Comparator;


// 2 * 5 + 2 = (12 - 1) / 2 = 5
//Left child index: 2i + 1
//Right child index: 2i + 2
//if the index is even, then the element is the right child else the element is the left child
public class PriorityQueue<T extends Comparable<T>> {
    private final ArrayList<T> heap;
    private final Comparator<T> comparator;

    public PriorityQueue() {
        heap = new ArrayList<>();
        comparator = Comparable::compareTo;
    }

    public PriorityQueue(Comparator<T> comp) {
        heap = new ArrayList<>();
        comparator = comp;
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    public T peek() {
        if (heap.isEmpty()) return null;
        return heap.get(0);
    }

    public void push(T element) {
        heap.add(element);
        int index = heap.size() - 1;

        while(true) {
            int parent_node;
            if(index == 0) {
                break;
            }
            else if(index % 2 == 0) {
                parent_node = (index - 2) / 2;
            }
            else {
                parent_node = (index - 1)  / 2;
            }

            if(comparator.compare(heap.get(parent_node), element) > 0) {
                heap.set(index, heap.get(parent_node));
                heap.set(parent_node, element);
                index = parent_node;
            }
            else {
                break;
            }
        }
    }

    public T pop() {
        T element = heap.get(0);
        heap.set(0, heap.get(heap.size() - 1));
        heap.remove(heap.size() - 1);

        if (heap.size() == 0) {
            return element;
        }

        T parent = heap.get(0);
        int index = 0;

        while(true) {
            if(heap.size()-1 < index * 2 + 1) {
                break;
            }
            else if(heap.size()-1 < index * 2 + 2) {
                if (comparator.compare(parent, heap.get(index * 2 + 1) ) > 0) {
                    heap.set(index, heap.get(index * 2 + 1));
                    heap.set(index * 2 + 1, parent);
                    index = index * 2 + 1;
                }
                else {
                    break;
                }
            }
            else {
                int child;

                if(comparator.compare(heap.get(index * 2 + 1), heap.get(index * 2 + 2)) <= 0) {
                    child = index * 2 + 1;
                }
                else {
                    child = index * 2 + 2;
                }


                if (comparator.compare(parent, heap.get(child)) > 0) {
                    heap.set(index, heap.get(child));
                    heap.set(child, parent);
                    index = child;
                }
                else {
                    break;
                }


            }
        }

        return element;
    }
}
