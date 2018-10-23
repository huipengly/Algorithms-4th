/* *****************************************************************************
 *  Name:           SeamCarver.java
 *  Date:           20181023
 *  Description:    SeamCarver
 *  Author:         huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
    private Picture picture;
    private double[][] distTo;
    private Pixel[][] edgeTo;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = picture;
        distTo = new double[picture.width()][picture.height()];
        for (int i = 0; i != width(); ++i) {
            distTo[i][0] = 1000;
        }
        for (int i = 0; i != width(); ++i) {
            for (int j = 1; j != height(); ++j) {
                distTo[i][j] = 1e10;
            }
        }
        edgeTo = new Pixel[picture.width()][picture.height()];
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return 1000;
        }
        // 存储上下左右四个点的rgb备用
        int[] rgb = new int[] {
                picture.getRGB(x, y - 1),
                picture.getRGB(x, y + 1),
                picture.getRGB(x - 1, y),
                picture.getRGB(x + 1, y)
        };

        double rx = ((rgb[1] >> 16) & 0xFF) - ((rgb[0] >> 16) & 0xFF);
        double gx = ((rgb[1] >> 8) & 0xFF) - ((rgb[0] >> 8) & 0xFF);
        double bx = ((rgb[1] >> 0) & 0xFF) - ((rgb[0] >> 0) & 0xFF);

        double ry = ((rgb[3] >> 16) & 0xFF) - ((rgb[2] >> 16) & 0xFF);
        double gy = ((rgb[3] >> 8) & 0xFF) - ((rgb[2] >> 8) & 0xFF);
        double by = ((rgb[3] >> 0) & 0xFF) - ((rgb[2] >> 0) & 0xFF);

        return Math.sqrt(rx * rx + gx * gx + bx * bx + ry * ry + gy * gy + by * by);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energeMatrix = new double[width()][height()];
        for (int i = 0; i != width(); ++i) {
            for (int j = 0; j != height(); ++j) {
                energeMatrix[i][j] = energy(i, j);
            }
        }

        DepthFirstOrder dfs = new DepthFirstOrder(energeMatrix);
        for (Pixel p : dfs.reversePost()) {
            StdOut.print(p.x() + " " + p.y() + "\n");
        }
        for (Pixel p : dfs.reversePost()) {
            relax(energeMatrix, p);
        }

        int seamEndY = 0;
        double shortest = 1e10;
        for (int i = 0; i != width(); ++i) {
            if (distTo[i][height() - 2] < shortest) {
                shortest = distTo[i][height() - 2];
                seamEndY = i;
            }
        }

        int[] seam = new int[height()];
        // 首尾根据最短路径决定
        seam[height() - 1] = seamEndY;
        seam[height() - 2] = seamEndY;
        for (int i = height() - 3; i != 0; --i) {
            seam[i] = edgeTo[seam[i + 1]][i].x();
        }

        seam[0] = seam[1];

        return seam;
    }

    // 松弛p1到p2点
    void relax(double[][] energeMatrix, Pixel p) {
        int x = p.x();
        int y = p.y();
        if (y != height() - 1) {
            // 左下
            if (x != 0) {
                if (distTo[x - 1][y + 1] > distTo[x][y] + energeMatrix[x - 1][y + 1]) {
                    distTo[x - 1][y + 1] = distTo[x][y] + energeMatrix[x - 1][y + 1];
                    edgeTo[x - 1][y + 1] = p;
                }
            }
            // 下方
            if (distTo[x][y + 1] > distTo[x][y] + energeMatrix[x][y + 1]) {
                distTo[x][y + 1] = distTo[x][y] + energeMatrix[x][y + 1];
                edgeTo[x][y + 1] = p;
            }
            // 右下
            if (x != width() - 1) {
                if (distTo[x + 1][y + 1] > distTo[x][y] + energeMatrix[x + 1][y + 1]) {
                    distTo[x + 1][y + 1] = distTo[x][y] + energeMatrix[x + 1][y + 1];
                    edgeTo[x + 1][y + 1] = p;
                }
            }
        }
    }

    // // sequence of indices for horizontal seam
    // public int[] findHorizontalSeam() {
    //
    // }
    //
    // // remove horizontal seam from current picture
    // public void removeHorizontalSeam(int[] seam) {
    //
    // }
    //
    // // remove vertical seam from current picture
    // public void removeVerticalSeam(int[] seam)
}
