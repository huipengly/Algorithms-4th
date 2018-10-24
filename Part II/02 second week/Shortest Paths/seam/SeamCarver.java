/* *****************************************************************************
 *  Name:           SeamCarver.java
 *  Date:           20181023
 *  Description:    SeamCarver
 *  Author:         huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = picture;
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

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] energeMatrix = new double[height()][width()];

        // 计算能量矩阵
        for (int i = 0; i != height(); ++i) {
            for (int j = 0; j != width(); ++j) {
                energeMatrix[i][j] = energy(j, i);
            }
        }

        // // 输出能量矩阵，调试用
        // for (int i = 0; i != height(); ++i) {
        //     for (int j = 0; j != width(); ++j) {
        //         StdOut.print(energeMatrix[j][i] + "\t");
        //     }
        //     StdOut.print("\n");
        // }

        return findSeam(energeMatrix);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energeMatrix = new double[width()][height()];

        // 计算能量矩阵
        for (int i = 0; i != width(); ++i) {
            for (int j = 0; j != height(); ++j) {
                energeMatrix[i][j] = energy(i, j);
            }
        }

        // // 输出能量矩阵，调试用
        // for (int i = 0; i != height(); ++i) {
        //     for (int j = 0; j != width(); ++j) {
        //         StdOut.print(energeMatrix[j][i] + "\t");
        //     }
        //     StdOut.print("\n");
        // }

        return findSeam(energeMatrix);
    }

    private int[] findSeam(double[][] energeMatrix) {
        int width = energeMatrix.length;
        int height = energeMatrix[0].length;
        double[][] distTo = new double[width][height];
        Pixel[][] edgeTo = new Pixel[width][height];

        // 初始化距离，第一行为1000，其他为无穷大
        for (int i = 0; i != width; ++i) {
            distTo[i][0] = 1000;
        }
        for (int i = 0; i != width; ++i) {
            for (int j = 1; j != height; ++j) {
                distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        // dfs先找出后逆序
        DepthFirstOrder dfs = new DepthFirstOrder(energeMatrix);
        // for (Pixel p : dfs.reversePost()) {
        //     StdOut.print(p.x() + " " + p.y() + "\n");
        // }
        // 根据后逆序，依此松弛点
        for (Pixel p : dfs.reversePost()) {
            relax(energeMatrix, p, distTo, edgeTo);
        }

        // 从最后一行找出最短距离
        int[] seam = new int[height];
        seam[height - 1] = 0;
        double shortest = Double.POSITIVE_INFINITY;
        for (int i = 0; i != width; ++i) {
            if (distTo[i][height - 2] < shortest) {
                shortest = distTo[i][height - 2];
                seam[height - 1] = i;
            }
        }
        // 然后反向寻找最短路径
        for (int i = height - 1; i != 0; --i) {
            seam[i - 1] = edgeTo[seam[i]][i].x();
        }

        return seam;
    }

    // 松弛p点
    void relax(double[][] energeMatrix, Pixel p, double[][] distTo, Pixel[][] edgeTo) {
        int width = distTo.length;
        int height = distTo[0].length;
        int x = p.x();
        int y = p.y();
        if (y != height - 1) {
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
            if (x != width - 1) {
                if (distTo[x + 1][y + 1] > distTo[x][y] + energeMatrix[x + 1][y + 1]) {
                    distTo[x + 1][y + 1] = distTo[x][y] + energeMatrix[x + 1][y + 1];
                    edgeTo[x + 1][y + 1] = p;
                }
            }
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        int width = width();
        int height = height() - 1;
        Picture newPicture = new Picture(width, height);
        for (int i = 0; i != width; ++i) {
            for (int j = 0; j != height; ++j) {
                // 像素在删除点下面，则拷贝的是原图行列下一个坐标
                int row = j >= seam[i] ? j + 1 : j;
                newPicture.set(i, j, picture.get(i, row));
            }
        }
        picture = newPicture;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        int width = width() - 1;
        int height = height();
        Picture newPicture = new Picture(width, height);
        for (int i = 0; i != width; ++i) {
            for (int j = 0; j != height; ++j) {
                // 像素在删除点下面，则拷贝的是原图行列下一个坐标
                int col = i >= seam[j] ? i + 1 : i;
                newPicture.set(i, j, picture.get(col, j));
            }
        }
        picture = newPicture;
    }
}
