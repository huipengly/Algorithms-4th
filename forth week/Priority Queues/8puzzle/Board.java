/* *****************************************************************************
 *  Name: Board.java
 *  Date: 20180920
 *  Description: 推盘类
 *  author: huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private final int dimension;
    private final int hamming;      // 提示初始化为0时多余的
    private final int manhattan;
    private final boolean isGoal;
    private final int[][] blocks;   // java二维数组不能用clone，然后泛型也有问题
    private int blankCol;           // 记录空白块位置
    private int blankRow;

    public Board(int[][] blocks) {           // construct a board from an n-by-n array of blocks
        dimension = blocks.length;
        this.blocks = new int[dimension][dimension];
        my2DimensionArrayCopy(this.blocks, blocks);
        hamming = calculateHamming();
        manhattan = calculateManhattan();
        isGoal = calculateIsGoal();
    }

    // 判断当前块里的值和预想值是否相同，不同就加hamming
    private int calculateHamming() {
        int tempHamming = 0;
        for (int i = 0; i != dimension; ++i) {
            for (int j = 0; j != dimension; ++j) {
                // if (blocks[i][j] != (i * dimension + j + 1) && blocks[i][j] != 0)
                //     tempHamming++;
                if (blocks[i][j] == 0) {    // 记录空白块位置
                    blankRow = i;
                    blankCol = j;
                }
                else if (blocks[i][j] != (i * dimension + j + 1))   // 判断是否相同
                    tempHamming++;
            }
        }
        return tempHamming;
    }

    // 当前块里实际的数字和这个数字应该在的位置。两个的行差和列差绝对值的和，就是一个块的曼哈顿距离
    private int calculateManhattan() {
        int tempManhattan = 0;
        for (int i = 0; i != dimension; ++i) {
            for (int j = 0; j != dimension; ++j) {
                if (blocks[i][j] != 0) {        // 非空白才判断
                    // 除以维度就是应在的行，除维度的余数是应在的列
                    int desiredRow = (blocks[i][j] - 1) / dimension;
                    int desiredCol = (blocks[i][j] - 1) % dimension;    // 数从1开始记录的，所以-1再余数
                    tempManhattan += Math.abs(desiredRow - i) + Math.abs(desiredCol - j);
                }
            }
        }
        return tempManhattan;
    }

    private boolean calculateIsGoal() {
        for (int i = 0; i != dimension; ++i) {
            for (int j = 0; j != dimension; ++j) {
                if (blocks[i][j] != (i * dimension + j + 1) && blocks[i][j] != 0)
                    return false;
            }
        }
        return true;
    }

    private void my2DimensionArrayCopy(int[][] lhs, int[][] rhs) {
        if (lhs == null || rhs == null)       // 空数组返回，和数组维度不等的返回
            throw new java.lang.NullPointerException();
        assert (lhs.length != rhs.length || lhs[0].length != rhs[0].length);
        for (int i = 0; i != lhs.length; ++i) {
            for (int j = 0; j != lhs[0].length; ++j) {
                lhs[i][j] = rhs[i][j];
            }
        }
    }

    // (where blocks[i][j] = block in row i, column j)
    public int dimension() {                // board dimension n
        return dimension;
    }

    public int hamming() {                  // number of blocks out of place
        return hamming;
    }

    public int manhattan() {                 // sum of Manhattan distances between blocks and goal
        return manhattan;
    }

    public boolean isGoal() {               // is this board the goal board?
        return isGoal;
    }

    private void swap(int[][] arr, int i1, int j1, int i2, int j2) {
        int temp = arr[i1][j1];
        arr[i1][j1] = arr[i2][j2];
        arr[i2][j2] = temp;
    }

    public Board twin() {                   // a board that is obtained by exchanging any pair of blocks 这里没有说随机交换，我就只在前三个中交换非空的两个
        int[][] twinBlocks = new int[dimension][dimension];
        my2DimensionArrayCopy(twinBlocks, blocks);
        if (twinBlocks[0][0] != 0 && twinBlocks[0][1] != 0) {
            swap(twinBlocks, 0, 0, 0, 1);
        }
        else if (twinBlocks[0][0] != 0 && twinBlocks[0][2] != 0) {
            swap(twinBlocks, 0, 0, 0, 2);
        }
        else if (twinBlocks[0][1] != 0 && twinBlocks[0][2] != 0) {
            swap(twinBlocks, 0, 1, 0, 2);
        }

        return new Board(twinBlocks);
    }

    public boolean equals(Object y) {       // does this board equal y?
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        // 这么判断对么？
        if (this.dimension() != that.dimension()) return false;
        if (this.hamming() != that.hamming()) return false;
        if (this.manhattan() != that.manhattan()) return false;
        return true;
    }

    public Iterable<Board> neighbors() {    // all neighboring boards
        Stack<Board> neighborStack = new Stack<Board>();
        if (blankRow != 0)  // 判断某个方向是否在边界，不在就可移动这个方向上的块
            neighborStack.push(moveBlankSquare(-1, 0));
        if (blankRow != dimension - 1)
            neighborStack.push(moveBlankSquare(1, 0));
        if (blankCol != 0)
            neighborStack.push(moveBlankSquare(0, -1));
        if (blankCol != dimension - 1)
            neighborStack.push(moveBlankSquare(0, 1));
        return neighborStack;
    }

    private Board moveBlankSquare(int row, int col) {         // 移动空白块，row和col分别表示向上下左右移动，只能取+-1和0
        assert row > 1 || row < -1 || col > 1 || col < -1;      // 行列移动超过一行，错误
        assert (Math.abs(row) + Math.abs(col)) < 1;             // 行列都没有移动，错误
        int[][] movedBlocks = new int[dimension][dimension];
        my2DimensionArrayCopy(movedBlocks, blocks);
        swap(movedBlocks, blankRow + row, blankCol + col, blankRow, blankCol);        // 交换移动行和空白行
        return new Board(movedBlocks);
    }

    public String toString() {              // string representation of this board (in the output format specified below)
        String str = dimension + "\n";
        for (int i = 0; i != dimension; ++i) {
            for (int j = 0; j != dimension; ++j) {
                if (blocks[i][j] != 0)
                    str += String.valueOf(blocks[i][j]) + " ";
                else
                    str += "  ";
            }
            str += "\n";
        }
        return str;
    }

    public static void main(String[] args) { // unit tests (not graded)
        In in = new In("puzzle011.txt");
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        StdOut.print(initial.toString());
        for (Board b : initial.neighbors()) {
            StdOut.print(b.toString());
        }
        StdOut.print(initial.twin().toString());
    }
}
