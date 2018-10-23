/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:    对能量矩阵进行深度优先搜索
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class DepthFirstOrder {
    private double[][] p;
    private boolean[][] marked;
    private int width;
    private int height;
    private Stack<Pixel> reversePost = new Stack<>();

    // p是一个二维像素矩阵，代表矩阵能量
    public DepthFirstOrder(double[][] p) {
        this.p = p;
        width = p.length;
        height = p[0].length;
        marked = new boolean[width][height];
        for (boolean[] m : marked) {
            for (boolean mm : m) {
                mm = false;
            }
        }
        // 这里不认为第一行的点特殊，认为有一个虚拟起点。第一个for是要遍历第一行
        for (int i = 0; i != width; ++i)
            if (!marked[i][0]) dfs(i, 0);
    }

    Iterable<Pixel> reversePost() {
        return reversePost;
    }

    private void dfs(int x, int y) {
        marked[x][y] = true;
        // 判断是否在最后一行了
        if (y != height - 1) {
            // 左下
            if (x != 0 && !marked[x - 1][y + 1]) {
                dfs(x - 1, y + 1);
            }
            // 下方
            if (!marked[x][y + 1]) {
                dfs(x, y + 1);
            }
            // 右下
            if (x != width - 1 && !marked[x + 1][y + 1]) {
                dfs(x + 1, y + 1);
            }
        }
        reversePost.push(new Pixel(x, y));
    }

    public static void main(String[] args) {
        double[][] test = new double[3][3];
        DepthFirstOrder dfs = new DepthFirstOrder(test);
        for (Pixel p : dfs.reversePost()) {
            StdOut.print(p.x() + " " + p.y() + "\n");
        }
    }
}
