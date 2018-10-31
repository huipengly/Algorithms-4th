/* *****************************************************************************
 *  Name:           MoveToFront.java
 *  Date:           20181030
 *  Description:    MoveToFront
 *  Author:         huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        int R = 256;
        char[] sequences = new char[R];
        for (char i = 0; i != R; ++i) {
            sequences[i] = i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char before = sequences[0];
            for (int i = 0; i != R; ++i) {
                if (sequences[i] == c) {
                    sequences[0] = c;
                    sequences[i] = before;
                    BinaryStdOut.write((char) i);
                    break;
                }
                char temp = sequences[i];
                sequences[i] = before;
                before = temp;
            }
        }

        // close output stream
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        int R = 256;
        char[] sequences = new char[R];
        for (char i = 0; i != R; ++i) {
            sequences[i] = i;
        }
        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readChar();
            char c = sequences[index];
            BinaryStdOut.write(c);
            char before = sequences[0];
            // 这里i到index + 1是因为这个index从0开始计算的
            for (int i = 1; i != index + 1; ++i) {
                char temp = sequences[i];
                sequences[i] = before;
                before = temp;
            }
            sequences[0] = c;
        }

        // close output stream
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        }
        else if (args[0].equals("+")) {
            decode();
        }
    }
}
