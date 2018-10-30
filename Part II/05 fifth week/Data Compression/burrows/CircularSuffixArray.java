/* *****************************************************************************
 *  Name:           CircularSuffixArray.java
 *  Date:           20181030
 *  Description:    CircularSuffixArray
 *  Author:         huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    private final int length;
    private final Integer[] index;
    private final String suffixArray;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new java.lang.IllegalArgumentException("argument s is null.");
        }
        length = s.length();
        suffixArray = s;
        index = new Integer[length];
        for (int i = 0; i != length; ++i) {
            index[i] = i;
        }
        Arrays.sort(index, new Comparator<Integer>() {
            public int compare(Integer i1, Integer i2) {
                for (int i = 0; i != length; ++i) {
                    char c1 = suffixArray.charAt(i1 + i % length);
                    char c2 = suffixArray.charAt(i2 + i % length);
                    if (c1 < c2) {
                        return -1;
                    }
                    if (c1 > c2) {
                        return 1;
                    }
                }
                return 0;
            }
        });
    }

    // length of s
    public int length() {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > length) {
            throw new java.lang.IllegalArgumentException("i must between 0 and " + (length - 1));
        }
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray("ABRACADABRA!");
        StdOut.print(circularSuffixArray.length() + "\n");
        for (int i = 0; i != circularSuffixArray.length; ++i) {
            StdOut.print(circularSuffixArray.index(i) + "\n");
        }
    }
}
