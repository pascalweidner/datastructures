public class AVLTree<T extends Comparable<T>> {
    public class Node {
        public int bf;
        public T value;
        public int height;
        public Node left, right;

        public Node(T value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    public Node root;
    private int nodeCount = 0;

    public int height() {
        if(root == null) return 0;
        return root.height;
    }

    public int size() {
        return nodeCount;
    }

    public boolean isEmpty() {
        return nodeCount == 0;
    }

    public boolean contains(T value) {
        return contains(root, value);
    }

    private boolean contains(Node node, T value) {
        if(node == null) return false;

        int cmp = value.compareTo(node.value);
        if(cmp < 0) return contains(node.left, value);
        if(cmp > 0) return contains(node.right, value);
        return true;
    }

    public void insert(T value) {
        if(value == null) return;
        if(!contains(root, value)) {
            root = insert(root, value);
            nodeCount++;
        }
    }

    private Node insert(Node node, T value) {
        if(node == null) return new Node(value);

        int cmp = value.compareTo(node.value);
        if(cmp < 0) {
            node.left = insert(node.left, value);
        }
        else {
            node.right = insert(node.right, value);
        }

        update(node);

        return balance(node);
    }

    private void update(Node node) {
        int leftNodeHeight = (node.left == null) ? -1 : node.left.height;
        int rightNodeHeight = (node.right == null) ? -1: node.right.height;

        node.height = 1 + Math.max(leftNodeHeight, rightNodeHeight);
        node.bf = rightNodeHeight - leftNodeHeight;
    }

    private Node balance(Node node) {
        if(node.bf == -2) {
            if(node.left.bf <= 0) {
                return leftLeftCase(node);
            }
            else  {
                return leftRightCase(node);
            }
        } else if(node.bf == 2) {
            if(node.right.bf >= 0) {
                return rightRightCase(node);
            }
            else {
                return rightLeftCase(node);
            }
        }

        return node;
    }

    private Node leftLeftCase(Node node) {
        return rightRotation(node);
    }

    private Node leftRightCase(Node node) {
        node.left = leftRotation(node.left);
        return leftLeftCase(node);
    }

    private Node rightRightCase(Node node) {
        return leftRotation(node);
    }

    private Node rightLeftCase(Node node) {
        node.right = rightRotation(node.right);
        return rightRightCase(node);
    }

    private Node leftRotation(Node node) {
        Node newParent = node.right;
        node.right = newParent.left;
        newParent.left = node;
        update(node);
        update(newParent);
        return newParent;
    }

    private Node rightRotation(Node node) {
        Node newParent = node.left;
        node.left = newParent.right;
        newParent.right = node;
        update(node);
        update(newParent);
        return newParent;
    }
}
