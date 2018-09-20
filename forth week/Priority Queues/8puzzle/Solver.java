/* *****************************************************************************
 *  Name: Slover.java
 *  Date: 20180920
 *  Description: 求解数字推盘游戏
 *  author: huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private int move;
    private Stack<Board> solutions = new Stack<>();

    public Solver(
            Board initial) {                    // find a solution to the initial board (using the A* algorithm)
        Stack<Board> calculateSolution = new Stack<>(); // 使用stack是因为中间会有些不合适的方向，要出栈
        calculateSolution.push(initial);
        MinPQ<Board> boardMinPQ = new MinPQ<>(
                Comparator.comparing(Board::manhattan));            // 这个怎么根据manhattan比较？
        Board predecessor, searchNode;
        predecessor = initial;
        searchNode = initial;
        while (!searchNode.isGoal()) {
            for (Board board : searchNode.neighbors()) {    // 将当前的搜索点的邻居加入优先队列
                if (!board.equals(predecessor)) {           // critical optimization.新邻居和上一个搜索相同则不加入
                    boardMinPQ.insert(board);
                }
            }
            //predecessor = searchNode;                       // 更新上一个搜索点
            searchNode = boardMinPQ.delMin();               // 更新搜索点
            boolean isNeighbor = false;
            for (Board pre : calculateSolution) {          // 在走过的路中寻找他的父节点，这里最开始用了pop的方法，是错误的，不能删除当时没有用的过程，可能之后还要用
                predecessor = pre;
                for (Board board : pre.neighbors()) {      // 判断当前搜索点是否是上一个点的子节点
                    if (searchNode.equals(board)) {
                        isNeighbor = true;
                        break;
                    }
                }
                if (isNeighbor)                             // 找到父节点退出寻找
                    break;
            }
            calculateSolution.push(searchNode);             // 将搜索点加入到求解顺序路线
        }

        Board end = calculateSolution.pop();
        solutions.push(end);
        for (Board pre : calculateSolution) {               // 寻找整个还原过程，从寻找过程的最后向initial节点寻找
            for (Board board : pre.neighbors()) {
                if (end.equals(board)) {
                    end = pre;                              // 如果是此节点的子节点，将此节点更新为end
                    solutions.push(end);
                    break;
                }
            }
        }

        move = solutions.size() - 1;                        // 包含初始broad，所以减1
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
        In in = new In("puzzle3x3-11.txt");
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
