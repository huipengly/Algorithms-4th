/* *****************************************************************************
 *  Name:           BoggleSolver.java
 *  Date:           20181029
 *  Description:    求解Boggle
 *  Author:         huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieSET;

public class BoggleSolver {
    private final MyTrieSET dict;
    // private final String[] dictionary;
    private TrieSET validWords;         // 这里使用set，单词不会被二次添加
    private boolean[][] mark;
    // MyTrieSET.Node node;

    private final int[] points = { 0, 0, 0, 1, 1, 2, 3, 5 };

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        // this.dictionary = dictionary.clone();
        dict = new MyTrieSET();
        for (int i = 0; i < dictionary.length; ++i) {
            dict.add(dictionary[i]);
        }
    }

    // 深度优先搜索找到可能的字母组合
    private void dfs(BoggleBoard board, int m, int n, StringBuilder sb, MyTrieSET.Node x) {
        mark[m][n] = true;
        // 处理QU情况
        String letter = String.valueOf(board.getLetter(m, n));
        if (letter.equals("Q")) {
            letter = "QU";
        }
        sb.append(letter);

        // 判断是否需要剪枝
        MyTrieSET.Node node = dict.firstNodeWithPrefix(sb, x);
        if (node == null) {
            // 返回前取消字母的标记
            mark[m][n] = false;
            // 返回前将最后一个字母删除
            sb.deleteCharAt(sb.length() - 1);
            return;
        }

        // 判断是否出现有效的单词，既长度大于2，且在字典中，有的话添加进set
        if (sb.length() > 2 && dict.contains(sb.toString())) {
            validWords.add(sb.toString());
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
                    dfs(board, m + i, n + j, sb, node);
                }
            }
        }

        // 返回时将此骰子置为false
        mark[m][n] = false;
        // 返回前将最后一个字母删除
        sb.deleteCharAt(sb.length() - 1);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        validWords = new TrieSET();
        mark = new boolean[board.rows()][board.cols()];
        for (int row = 0; row != board.rows(); ++row) {
            for (int col = 0; col != board.cols(); ++col) {
                mark[row][col] = false;
            }
        }

        for (int row = 0; row != board.rows(); ++row) {
            for (int col = 0; col != board.cols(); ++col) {
                dfs(board, row, col, new StringBuilder(), null);
            }
        }
        return validWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        // 先要判断是否在字典中
        if (!dict.contains(word)) {
            return 0;
        }
        return word.length() >= 8 ? 11 : points[word.length()];
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        // BoggleBoard board = new BoggleBoard("board-points26539.txt");
        // BoggleBoard board = new BoggleBoard(1000, 1000);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
