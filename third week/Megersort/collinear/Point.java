/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;

import java.util.Arrays;
import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point. Formally, if the two points are
     * (x0, y0) and (x1, y1), then the slope is (y1 - y0) / (x1 - x0). For completeness, the slope
     * is defined to be +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical; and Double.NEGATIVE_INFINITY if
     * (x0, y0) and (x1, y1) are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
        if (this.x == that.x && this.y == that.y)
            return Double.NEGATIVE_INFINITY;
        else if (this.y == that.y)
            return 0;
        else if (this.x == that.x)
            return Double.POSITIVE_INFINITY;
        else {
            return ((double) that.y - this.y) / ((double) that.x - this.x);
        }
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate. Formally, the invoking
     * point (x0, y0) is less than the argument point (x1, y1) if and only if either y0 < y1 or if
     * y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument point (x0 = x1 and y0 =
     * y1); a negative integer if this point is less than the argument point; and a positive integer
     * if this point is greater than the argument point
     */
    public int compareTo(Point that) {
        /* YOUR CODE HERE */
        if (this.x == that.x && this.y == that.y) {
            return 0;
        }
        if (this.y < that.y || (this.y == that.y && this.x < that.x)) {
            return -1;
        }
        return 1;
    }

    /**
     * Compares two points by the slope they make with this point. The slope is defined as in the
     * slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new SlopeOrder(this);
    }

    /**
     * 此comparator提供两个点关于参照点的比较方法，主要供排序方法使用 invokePoint就是参照点，两个待比较的Point的大小，这就是排序方法中要用的排序依据
     *
     * @author evasean www.cnblogs.com/evasean/
     */
    private class SlopeOrder implements Comparator<Point> {
        private final Point p0;

        public SlopeOrder(Point invokePoint) {
            this.p0 = invokePoint;
        }

        @Override
        public int compare(Point o1, Point o2) {
            // TODO Auto-generated method stub
            double slope1 = p0.slopeTo(o1);
            double slope2 = p0.slopeTo(o2);
            return Double.compare(slope1, slope2); //double不推荐用==直接比大小，采用这种方式比较好
        }
    }


    /**
     * Returns a string representation of this point. This method is provide for debugging; your
     * program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        Point p1 = new Point(0, 0);
        Point p2 = new Point(2, 8);
        Point p3 = new Point(5, 5);
        Point p4 = new Point(0, 10);
        Point p5 = new Point(10, 0);

        p1.compareTo(p2);

        Point[] parr = new Point[5];
        parr[0] = p1;
        parr[1] = p2;
        parr[2] = p3;
        parr[3] = p4;
        parr[4] = p5;
        Arrays.sort(parr);

        Arrays.sort(parr, p1.slopeOrder());
    }
}
