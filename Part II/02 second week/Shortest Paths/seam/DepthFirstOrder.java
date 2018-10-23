/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:    对能量矩阵进行深度优先搜索
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class DepthFirstOrder {
    private int[][] p;
    private boolean[][] marked;
    private int width;
    private int height;
    private Stack<Point> reversePost = new Stack<>();

    // p是一个二维像素矩阵，代表矩阵能量
    public DepthFirstOrder(int[][] p) {
        this.p = p;
        width = p[0].length;
        height = p.length;
        marked = new boolean[height][width];
        for (boolean[] m : marked) {
            for (boolean mm : m) {
                mm = false;
            }
        }
        // 这里不认为第一行的点特殊，认为有一个虚拟起点。第一个for是要遍历第一行
        for (int i = 0; i != width; ++i)
            if (!marked[0][i]) dfs(0, i);
    }

    Iterable<Point> reversePost() {
        return reversePost;
    }

    private class Point {
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private void dfs(int x, int y) {
        marked[x][y] = true;
        // 判断是否在最后一行了
        if (x != height - 1) {
            // 左下
            if (y != 0 && !marked[x + 1][y - 1]) {
                dfs(x + 1, y - 1);
            }
            // 下方
            if (!marked[x + 1][y]) {
                dfs(x + 1, y);
            }
            // 右下
            if (y != width - 1 && !marked[x + 1][y + 1]) {
                dfs(x + 1, y + 1);
            }
        }
        reversePost.push(new Point(x, y));
    }

    public static void main(String[] args) {
        int[][] test = new int[3][3];
        DepthFirstOrder dfs = new DepthFirstOrder(test);
        for (Point p : dfs.reversePost()) {
            StdOut.print(p.x + " " + p.y + "\n");
        }
    }
}
