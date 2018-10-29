/* *****************************************************************************
 *  Name:           MyTrieSET.java
 *  Date:           20181029
 *  Description:    26-ways trie
 *  Author:         huipengly
 **************************************************************************** */

public class MyTrieSET {
    private static final int R = 26;
    private Node root;
    // private Node lastNode;
    private int n;

    public class Node {
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

    public Node firstNodeWithPrefix(StringBuilder prefix, Node x) {
        if (prefix == null) {
            throw new java.lang.IllegalArgumentException("argument to contains() is null");
        }
        // 第一次查找这个单词，从0深度开始
        if (x == null) {
            return get(root, prefix.toString(), 0);
        }
        else {  // 接着上一次的深度开始查找
            int c = prefix.charAt(prefix.length() - 1) - 'A';
            return get(x.next[c], prefix.toString(), prefix.length());
        }
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
