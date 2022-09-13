public class List<T> {
    private Node<T> head;

    private static class Node<L> {
        Node next;
        L value;

        public Node(L value, Node next) {
            this.next = next;
            this.value = value;
        }
    }

    public List() {
        head = null;
    }

    public void append(T element) {
        Node<T> temp = new Node<>(element, null);
        if(head == null) {
            head = temp;
            return;
        }

        Node<T> temp2 = head;
        while(temp2.next != null) {
            temp2 = temp2.next;
        }
        temp2.next = temp;
    }

    public int size() {
        if(head == null) {
            return 0;
        }

        int count = 0;
        Node<T> temp = head;
        while(temp.next != null) {
            count++;
            temp = temp.next;
        }

        return count + 1;
    }

    public T get(int index) {
        Node<T> temp = head;
        for(int i = 0; i < index; i++) {
            if(temp.next == null) {
                break;
            }
            temp = temp.next;
        }

        return temp.value;
    }

    public void set(int index, T value) {
        Node<T> temp = head;
        for(int i = 0; i < this.size(); i++) {
            if(i == index) {
                temp.value = value;
                return;
            }
            temp = temp.next;
        }
    }

    public void insert(int index, T element) {
        Node<T> temp = head;
        Node<T> temp1;
        if(index == 0) {
            temp1 = new Node<>(element, temp);
            head = temp1;
            return;
        }

        for(int i = 0; i < index-1; i++) {
            if(temp.next == null) {
                return;
            }
            temp = temp.next;
        }
        Node<T> temp2 = temp.next;
        temp1 = new Node<>(element, temp2);
        temp.next = temp1;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        Node<T> temp = head;
        for(int i = 0; i < this.size(); i++) {
            if(i == 0) {
                builder.append(" ").append(temp.value);
                temp = temp.next;
                continue;
            }
            builder.append(", ").append(temp.value);
            temp = temp.next;
        }

        builder.append(" ]");
        return builder.toString();
    }
}
