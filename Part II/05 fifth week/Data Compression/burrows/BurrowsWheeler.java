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
        // while (!BinaryStdIn.isEmpty()) {
        //     char c = BinaryStdIn.readChar();
        //     originalStringBuilder.append(c);
        // }
        // String originalString = originalStringBuilder.toString();
        String originalString = BinaryStdIn.readString();
        // StdOut.print(originalString + "\n");
        CircularSuffixArray csa = new CircularSuffixArray(originalString);
        for (int i = 0; i != csa.length(); ++i) {   // 写入first，first是个int！
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
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

    private static int[] constructeNext(String t) {
        int R = 256;
        int length = t.length();
        int[] next = new int[length];
        int[] count = new int[R + 1];
        int[] firstCol = new int[length];

        for (int i = 0; i != length; ++i) {
            ++count[t.charAt(i) + 1];
        }
        for (int i = 1; i != length; ++i) {
            count[i] += count[i - 1];
        }
        for (int i = 0; i != length; ++i) {
            char c = t.charAt(i);
            int pos = count[c]++;
            firstCol[pos] = c;
            next[pos] = i;
        }

        return next;
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    // 假设输入流一定正确，不需要检测
    public static void inverseTransform() {
        int first;
        StringBuilder transformedStringBuilder = new StringBuilder();
        // 从流中构造原始字符串
        // 读取first
        if (!BinaryStdIn.isEmpty()) {
            first = BinaryStdIn.readInt();
        }
        else {
            throw new java.lang.IllegalArgumentException("invalid input.");
        }
        // 读取编码
        String t = BinaryStdIn.readString();
        // while (!BinaryStdIn.isEmpty()) {
        //     char c = BinaryStdIn.readChar();
        //     transformedStringBuilder.append(c);
        // }
        // String t = transformedStringBuilder.toString();
        // char[] originalChar = new char[t.length()];
        int[] next = constructeNext(t);
        // int[] next = { 3, 0, 6, 7, 8, 9, 10, 11, 5, 2, 1, 4 };

        // 还原回原始信息，并输出
        for (int i = 0; i != t.length(); ++i) {
            // originalChar[i] = t.charAt(next[first]);
            BinaryStdOut.write(t.charAt(next[first]));
            first = next[first];
        }

        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        }
        else if (args[0].equals("+")) {
            inverseTransform();
        }
    }
}
