/* *****************************************************************************
 *  Name:           BoggleSolver.java
 *  Date:           20181029
 *  Description:    求解Boggle
 *  Author:         huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

public class BoggleSolver {
    private final TST<Integer> tst;
    // private final String[] dictionary;
    private SET<String> validWords;         // 这里使用set，单词不会被二次添加
    private boolean[][] mark;

    private final int[] points = { 0, 0, 0, 1, 1, 2, 3, 5 };

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        // this.dictionary = dictionary.clone();
        tst = new TST<>();
        for (int i = 0; i < dictionary.length; ++i) {
            tst.put(dictionary[i], i);
        }
    }

    // 深度优先搜索找到可能的字母组合
    private void dfs(BoggleBoard board, int m, int n, String str) {
        mark[m][n] = true;
        // 处理QU情况
        String letter = String.valueOf(board.getLetter(m, n));
        if (letter.equals("Q")) {
            letter = "QU";
        }
        str += letter;

        // 判断是否需要剪枝
        if (tst.keysWithPrefix(str) == null) {
            // 返回前取消字母的标记
            mark[m][n] = false;
            return;
        }

        // 判断是否出现有效的单词，既长度大于2，且在字典中，有的话添加进set
        if (str.length() > 2 && tst.contains(str)) {
            validWords.add(str);
        }

        // 在八个方向未使用过的骰子进行dfs
        for (int i = -1; i != 2; ++i) {
            // 边界条件
            if ((m == 0 && i == -1) || (m == board.rows() - 1 && i == 1)) {
                continue;
            }
            for (int j = -1; j != 2; ++j) {
                // 边界条件
                if ((n == 0 && j == -1) || (n == board.cols() - 1 && j == 1)) {
                    continue;
                }
                if (!mark[m + i][n + j]) {
                    dfs(board, m + i, n + j, str);
                }
            }
        }

        // 返回时将此骰子置为false
        mark[m][n] = false;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        validWords = new SET<>();
        mark = new boolean[board.rows()][board.cols()];
        for (int row = 0; row != board.rows(); ++row) {
            for (int col = 0; col != board.cols(); ++col) {
                mark[row][col] = false;
            }
        }

        for (int row = 0; row != board.rows(); ++row) {
            for (int col = 0; col != board.cols(); ++col) {
                dfs(board, row, col, "");
            }
        }
        return validWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        return word.length() >= 8 ? 11 : points[word.length()];
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
