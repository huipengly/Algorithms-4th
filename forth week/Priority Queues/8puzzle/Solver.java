/* *****************************************************************************
 *  Name: Slover.java
 *  Date: 20180920
 *  Description: 求解数字推盘游戏
 *  author: huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private int move;
    private Queue<Board> solutions = new Queue<>();

    public Solver(
            Board initial) {                    // find a solution to the initial board (using the A* algorithm)
        solutions.enqueue(initial);
        MinPQ<Board> boardMinPQ = new MinPQ<>(
                Comparator.comparing(Board::hamming));            // 这个怎么根据manhattan比较？
        Board predecessor, searchNode;
        predecessor = initial;
        searchNode = initial;
        while (!searchNode.isGoal()) {
            for (Board board : searchNode.neighbors()) {    // 将当前的搜索点的邻居加入优先队列
                if (!board.equals(predecessor)) {           // critical optimization.新邻居和上一个搜索相同则不加入
                    boardMinPQ.insert(board);
                }
            }
            predecessor = searchNode;                       // 更新上一个搜索点
            searchNode = boardMinPQ.delMin();               // 更新搜索点
            solutions.enqueue(searchNode);                     // 将搜索点加入到求解顺序路线
        }
        move = solutions.size();
    }

    public boolean isSolvable() {               // is the initial board solvable?
        return move != -1;
    }

    public int moves() {                        // min number of moves to solve initial board; -1 if unsolvable
        return move;
    }

    public Iterable<Board> solution() {         // sequence of boards in a shortest solution; null if unsolvable
        return solutions;
    }

    public static void main(String[] args) {    // solve a slider puzzle (given below)
        // create initial board from file
        In in = new In("puzzle01.txt");
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
