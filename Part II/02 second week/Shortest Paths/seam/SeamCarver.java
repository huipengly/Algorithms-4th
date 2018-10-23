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
                picture.getRGB(x - 1, y), picture.getRGB(x + 1, y),
                picture.getRGB(x, y - 1), picture.getRGB(x + 1, y)
        };

        double rx = ((rgb[1] >> 16) & 0xFF) - ((rgb[0] >> 16) & 0xFF);
        double gx = ((rgb[1] >> 8) & 0xFF) - ((rgb[0] >> 8) & 0xFF);
        double bx = ((rgb[1] >> 0) & 0xFF) - ((rgb[0] >> 0) & 0xFF);

        double ry = ((rgb[3] >> 16) & 0xFF) - ((rgb[2] >> 16) & 0xFF);
        double gy = ((rgb[3] >> 8) & 0xFF) - ((rgb[2] >> 8) & 0xFF);
        double by = ((rgb[3] >> 0) & 0xFF) - ((rgb[2] >> 0) & 0xFF);

        return Math.sqrt(rx * rx + gx * gx + bx * bx + ry * ry + gy * gy + by * by);
    }

    // // sequence of indices for horizontal seam
    // public int[] findHorizontalSeam() {
    //
    // }
    //
    // // sequence of indices for vertical seam
    // public int[] findVerticalSeam() {
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
