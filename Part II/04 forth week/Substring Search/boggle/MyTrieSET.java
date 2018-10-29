/* *****************************************************************************
 *  Name:           MyTrieSET.java
 *  Date:           20181029
 *  Description:    26-ways trie
 *  Author:         huipengly
 **************************************************************************** */

public class MyTrieSET {
    private static final int R = 26;
    private Node root;
    private int n;

    private class Node {
        private boolean isString;
        private Node[] next = new Node[R];
    }

    public MyTrieSET() {
    }

    public void add(String key) {
        if (key == null) {
            throw new java.lang.IllegalArgumentException("argument to add() is null");
        }
        root = add(root, key, 0);
    }

    private Node add(Node x, String key, int d) {
        if (x == null) {
            x = new Node();
        }
        // 单词位置为true
        if (d == key.length()) {
            if (!x.isString) {
                ++n;
            }
            x.isString = true;
        }
        else {
            int c = key.charAt(d) - 'A';
            x.next[c] = add(x.next[c], key, d + 1);
        }
        return x;
    }

    public boolean contains(String key) {
        if (key == null) {
            throw new java.lang.IllegalArgumentException("argument to contains() is null");
        }
        Node x = get(root, key, 0);
        if (x == null) {
            return false;
        }
        return x.isString;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) {
            return null;
        }
        if (d == key.length()) {
            return x;
        }
        int c = key.charAt(d) - 'A';
        return get(x.next[c], key, d + 1);
    }

    public boolean hasKeysWithPrefix(String prefix) {
        if (prefix == null) {
            throw new java.lang.IllegalArgumentException("argument to contains() is null");
        }
        Node x = get(root, prefix, 0);
        return x != null;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return n;
    }

    public static void main(String[] args) {

    }
}
