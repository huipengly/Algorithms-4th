/* *****************************************************************************
 *  Name: Permutation.java
 *  Date: 2018/09/10
 *  Description: client
 *  author: huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> sQueue = new RandomizedQueue<String>();
        int k = Integer.parseInt(args[0]);
        if (k == 0)
            return;             // 不需要输出的时候直接返回
        int counter = k;
        for (int i = 0; i != k; ++i) {
            if (!StdIn.isEmpty()) {
                String str = StdIn.readString();
                sQueue.enqueue(str);
            }
        }
        while (!StdIn.isEmpty()) {
            String str = StdIn.readString();
            ++counter;
            if (StdRandom.uniform(counter) < k) {
                sQueue.dequeue();
                sQueue.enqueue(str);
            }
        }
        for (int i = 0; i != k; ++i) {
            StdOut.print(sQueue.dequeue() + "\n");
        }
    }
}
