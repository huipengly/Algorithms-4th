/* *****************************************************************************
 *  Name: Deque.java
 *  Date: 2018/09/10
 *  Description: 使用双向链表实现的双向队列
 *  note: 单向链表在addLast时间不符合O(n)，边长数组的内存使用不符合，8N-56N。
 *  author: huipengly
 **************************************************************************** */

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int size;

    private class Node {
        Item item;
        Node pre;
        Node next;
    }

    public Deque() {                           // construct an empty deque
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() {                 // is the deque empty?
        return first == null;
    }

    public int size() {                        // return the number of items on the deque
        return size;
    }

    public void addFirst(Item item) {          // add the item to the front
        if (item == null)
            throw new java.lang.IllegalArgumentException("add null item.");
        Node newFirst = new Node();
        newFirst.item = item;
        newFirst.pre = null;
        newFirst.next = null;
        if (isEmpty()) {
            first = newFirst;
            last = newFirst;
        }
        else {
            newFirst.next = first;
            first.pre = newFirst;
            first = newFirst;
        }
    }

    public void addLast(Item item) {           // add the item to the end
        if (item == null)
            throw new java.lang.IllegalArgumentException("add null item.");
        Node newLast = new Node();
        newLast.item = item;
        newLast.pre = null;
        newLast.next = null;
        if (isEmpty()) {
            first = newLast;
            last = newLast;
        }
        else {
            last.next = newLast;
            newLast.pre = last;
            last = newLast;
        }
    }

    public Item removeFirst() {                // remove and return the item from the front
        if (isEmpty())
            throw new java.util.NoSuchElementException("remove from an empty deque.");
        Item item = first.item;
        first = first.next;
        if (isEmpty())
            last = null;
        else {
            first.pre = null;
        }
        return item;
    }

    public Item removeLast() {                 // remove and return the item from the end
        if (isEmpty())
            throw new java.util.NoSuchElementException("remove from an empty.");
        Item item = last.item;
        last = last.pre;
        if (isEmpty())
            first = null;
        else {
            last.next = null;
        }
        return item;
    }

    public Iterator<Item> iterator() {         // return an iterator over items in order from front to end
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException("can't remove.");
        }

        public Item next() {
            if (hasNext())
                throw new java.util.NoSuchElementException("doesn't have Next.");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args)   // unit testing (optional)
    {
    }
}
