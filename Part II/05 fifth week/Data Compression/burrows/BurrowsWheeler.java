/* *****************************************************************************
 *  Name:           BurrowsWheeler.java
 *  Date:           20181030
 *  Description:    BurrowsWheeler
 *  Author:         huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        char startFlag = 0;     // 起始位
        StringBuilder originalStringBuilder = new StringBuilder();
        // 从流中构造原始字符串
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            originalStringBuilder.append(c);
        }
        String originalString = originalStringBuilder.toString();
        // StdOut.print(originalString + "\n");
        CircularSuffixArray csa = new CircularSuffixArray(originalString);
        BinaryStdOut.write(startFlag);
        BinaryStdOut.write(startFlag);
        BinaryStdOut.write(startFlag);              // 写入三个起始位
        for (int i = 0; i != csa.length(); ++i) {   // 写入first
            if (csa.index(i) == 0) {
                BinaryStdOut.write((char) i);
            }
        }
        // StdOut.print("\n");
        // for (int i = 0; i != csa.length(); ++i) {
        //     StdOut.print(csa.index(i) + "\t");
        // }
        for (int i = 0; i != csa.length(); ++i) {   // 写入数据
            // BinaryStdOut.write(originalString.charAt(csa.index(i)));
            int index = csa.index(i);
            // 取index的最后一个字母，既index前一个。如果index为0，则取最后一个
            int endCharIndex = index == 0 ? (csa.length() - 1) : (index - 1);
            // StdOut.print(originalString.charAt(endCharIndex));
            BinaryStdOut.write(originalString.charAt(endCharIndex));

        }

        BinaryStdOut.close();
    }

    // private static int[] constructeNext() {
    // }
    //
    // // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    // public static void inverseTransform() {
    //     char[] originalString;
    //     char[] t;
    //     Queue<Character> strQueue = new Queue<>();
    //     enum Read
    //     while (!BinaryStdIn.isEmpty()) {
    //         char c = BinaryStdIn.readChar();
    //         switch (c) {
    //             case 0:
    //
    //         }
    //     }
    //
    // }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        }
        // else if (args[0].equals("+")) {
        //     inverseTransform();
        // }
    }
}
