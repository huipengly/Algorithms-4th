/* *****************************************************************************
 *  Name: Permutation.java
 *  Date: 2018/09/10
 *  Description: client
 *  author: huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> sQueue = new RandomizedQueue<String>();
        int out_number = Integer.parseInt(args[0]);
        while (!StdIn.isEmpty()) {
            String str = StdIn.readString();
            sQueue.enqueue(str);
            // StdOut.print(str);
        }
        for (int i = 0; i != out_number; ++i) {
            StdOut.print(sQueue.dequeue() + "\n");
        }
    }
}
