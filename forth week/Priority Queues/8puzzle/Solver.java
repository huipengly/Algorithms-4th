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

public class Solver {
    private int move;
    private Stack<Board> solutions = new Stack<>();

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private SearchNode pre;
        private int move;
        private int priority;

        public SearchNode(Board board) {
            this.board = board;
            pre = null;
            move = 0;
            priority = move + this.board.manhattan();
        }

        public SearchNode(Board board, SearchNode pre) {
            this.board = board;
            this.pre = pre;
            move = pre.move + 1;
            priority = move + this.board.manhattan();
        }

        public int getMove() {
            return move;
        }

        public int compareTo(SearchNode that) {
            if (this.priority < that.priority) return -1;
            if (that.priority < this.priority) return 1;
            return 0;
        }

    }

    public Solver(
            Board initial) {                    // find a solution to the initial board (using the A* algorithm)
        SearchNode searchNode = new SearchNode(initial);        // 搜索节点初始化
        SearchNode twinSearchNode = new SearchNode(initial.twin());
        MinPQ<SearchNode> searchNodeMinPQ = new MinPQ<>();      // 搜索节点优先队列，按优先级小的
        MinPQ<SearchNode> twinSearchNodeMinPQ = new MinPQ<>();
        while (!searchNode.board.isGoal() && !twinSearchNode.board.isGoal()) { // 判断搜索节点是否找到解
            for (Board board : searchNode.board.neighbors()) {  // 添加搜索节点的邻居到优先队列
                /* 如果前一个节点为空节点，则添加所有的邻居节点 */
                /* 如果邻居和当前搜索节点的前一个节点相同，则不添加。 题目中第一个优化项 */
                if (searchNode.pre == null || !board.equals(searchNode.pre.board)) {
                    searchNodeMinPQ.insert(new SearchNode(board, searchNode));
                }
            }
            searchNode = searchNodeMinPQ.delMin();              // 将优先级最低的作为下一个搜索节点

            for (Board board : twinSearchNode.board.neighbors()) {
                if (twinSearchNode.pre == null || !board.equals(twinSearchNode.pre.board)) {
                    twinSearchNodeMinPQ.insert(new SearchNode(board, twinSearchNode));
                }
            }
            twinSearchNode = twinSearchNodeMinPQ.delMin();
        }
        if (!twinSearchNode.board.isGoal()) {
            move = searchNode.getMove();
            solutions.push(initial);
            while (searchNode.pre != null) {                        // 记录搜索过程
                solutions.push(searchNode.board);
                searchNode = searchNode.pre;
            }
        }
        else
            move = -1;
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
        In in = new In("puzzle04.txt");
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
