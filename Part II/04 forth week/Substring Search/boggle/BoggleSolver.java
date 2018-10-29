/* *****************************************************************************
 *  Name:           BoggleSolver.java
 *  Date:           20181029
 *  Description:    求解Boggle
 *  Author:         huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.TST;

public class BoggleSolver {
    private final TST<Integer> tst;
    private final String[] dictionary;

    private final int[] points = { 0, 0, 0, 1, 1, 2, 3, 5 };

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.dictionary = dictionary.clone();
        tst = new TST<>();
        for (int i = 0; i < this.dictionary.length; ++i) {
            tst.put(this.dictionary[i], i);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {

    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        return word.length() >= 8 ? 11 : points[word.length()];
    }

    public static void main(String[] args) {

    }
}
