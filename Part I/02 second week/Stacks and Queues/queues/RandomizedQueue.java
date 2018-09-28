/* *****************************************************************************
 *  Name: RandomizedQueue.java
 *  Date: 2018/09/10
 *  Description: 变长数组实现随机队列
 *  note: 链表在查找随机数是时间不满足O(n)
 *  author: huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] s;
    private int N;

    public RandomizedQueue() {                 // construct an empty randomized queue
        N = 0;
        s = (Item[]) new Object[1];
    }

    public boolean isEmpty() {                 // is the randomized queue empty?
        return N == 0;
    }

    public int size() {                        // return the number of items on the randomized queue
        return N;
    }

    public void enqueue(Item item) {          // add the item
        if (item == null)
            throw new java.lang.IllegalArgumentException("add null item.");
        if (size() == s.length) {
            resize(2 * s.length);
        }
        s[N++] = item;
    }

    private void resize(int capacity) {     // resize the array
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i != size(); ++i) {
            copy[i] = s[i];
        }
        s = copy;
    }

    public Item dequeue() {                     // remove and return a random item
        if (isEmpty())
            throw new java.util.NoSuchElementException("queue is empty. can't dequeue.");
        int i = StdRandom.uniform(0, size());
        /* 由于dequeue不需要考虑顺序，且此随机队列中没有操作需要保持顺序，所以删除的时候不考虑顺序 */
        Item temp = s[i];
        s[i] = s[N - 1];                        // 将最后一位（是N-1不是N）和随机的要删除位调换，删除最后位。
        s[N - 1] = null;                        // 释放存储对象
        --N;
        if (N > 0 && N == s.length / 4)         // 这里size不是数组的大小，s.length才是数组的大小
            resize(s.length / 2);
        return temp;
    }

    public Item sample() {                     // return a random item (but do not remove it)
        if (isEmpty())
            throw new java.util.NoSuchElementException("queue is empty. can't sample.");
        int i = StdRandom.uniform(size());
        return s[i];
    }

    public Iterator<Item> iterator() {        // return an independent iterator over items in random order
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int current;
        private int[] iteration_order;

        public RandomizedQueueIterator() {  // 构造函数，生成一个[0, size())的均匀随机数列
            current = 0;
            iteration_order = new int[size()];
            iteration_order = StdRandom.permutation(size());
        }

        public boolean hasNext() {
            return current != size();
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException("can't remove.");
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException("doesn't have Next.");
            }
            return s[iteration_order[current++]];
        }
    }

    public static void main(String[] args) {   // unit testing (optional)
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        for (int i = 0; i != 4096; ++i) {
            rq.enqueue(0);
        }
        for (int i = 0; i != 4000; ++i) {
            rq.dequeue();
        }
    }
}
