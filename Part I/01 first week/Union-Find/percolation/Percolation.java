/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;                   // 二位数组，存储栅格。true表示opened，false表示blocked
    private final int n;
    private final WeightedQuickUnionUF uf, uf2;          // 存储union的
    private int openSiteNumber = 0;
    private final int topNum;
    private final int bottomNum;

    public Percolation(int n)                // create n-by-n grid, with all sites blocked
    {
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException("can't constuct a grid less than zero!");
        }
        this.n = n;
        uf = new WeightedQuickUnionUF(n * n + 2);     // 添加两个点。n*n+1为顶上，n*n+2为底部的点。
        uf2 = new WeightedQuickUnionUF(n * n + 2);
        topNum = n * n + 1 - 1;
        bottomNum = n * n + 2 - 1;
        grid = new boolean[n][n];
        for (int i = 0; i != n; ++i)
            for (int j = 0; j != n; ++j)
                grid[i][j] = false;
    }

    public void open(int row, int col)      // open site (row, col) if it is not open already
    {
        if (row <= 0 || row > n || col <= 0 || col > n) {
            throw new java.lang.IllegalArgumentException("out of range!");
        }
        int trueRow = row - 1, trueCol = col - 1;
        if (!grid[trueRow][trueCol]) {
            ++openSiteNumber;
            grid[trueRow][trueCol] = true;
            if (trueRow != 0 && grid[trueRow - 1][trueCol]) {     // 四边上存在栅格，并且为opened时，连通栅格
                uf.union(getNumber(trueRow, trueCol), getNumber(trueRow - 1, trueCol));
                uf2.union(getNumber(trueRow, trueCol), getNumber(trueRow - 1, trueCol));
            }
            if (trueCol != 0 && grid[trueRow][trueCol - 1]) {
                uf.union(getNumber(trueRow, trueCol), getNumber(trueRow, trueCol - 1));
                uf2.union(getNumber(trueRow, trueCol), getNumber(trueRow, trueCol - 1));
            }
            if (trueRow != n - 1 && grid[trueRow + 1][trueCol]) {
                uf.union(getNumber(trueRow, trueCol), getNumber(trueRow + 1, trueCol));
                uf2.union(getNumber(trueRow, trueCol), getNumber(trueRow + 1, trueCol));
            }
            if (trueCol != n - 1 && grid[trueRow][trueCol + 1]) {
                uf.union(getNumber(trueRow, trueCol), getNumber(trueRow, trueCol + 1));
                uf2.union(getNumber(trueRow, trueCol), getNumber(trueRow, trueCol + 1));
            }

            if (trueRow == 0) {       // 若打开的为顶端或底端，分别和单独的顶端或底端连通
                uf.union(getNumber(trueRow, trueCol), topNum);
                uf2.union(getNumber(trueRow, trueCol), topNum);
            }
            if (trueRow == n - 1)    // uf2不连接底部
                uf.union(getNumber(trueRow, trueCol), bottomNum);
        }

    }

    public boolean isOpen(int row, int col)  // is site (row, col) open?
    {
        if (row <= 0 || row > n || col <= 0 || col > n) {
            throw new java.lang.IllegalArgumentException("out of range!");
        }
        return grid[row - 1][col - 1];
    }

    public boolean isFull(int row, int col)  // is site (row, col) full?
    {
        if (row <= 0 || row > n || col <= 0 || col > n) {
            throw new java.lang.IllegalArgumentException("out of range!");
        }
        return uf2.connected(topNum, getNumber(row - 1, col - 1));
    }

    public int numberOfOpenSites()       // number of open sites
    {
        return openSiteNumber;
    }

    public boolean percolates()              // does the system percolate?
    {
        return uf.connected(topNum, bottomNum);
    }

    private int getNumber(int col, int row) {
        return col * n + row;
    }
}
