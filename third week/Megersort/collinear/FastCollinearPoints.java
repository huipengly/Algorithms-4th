/* *****************************************************************************
 *  Name: FastCollinearPoints.java
 *  Date: 20180918
 *  Description: 快速查找共线点方法
 *  author: huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private int number = 0;
    private Point[] points;
    private ArrayList<LineSegment> segmentList = new ArrayList<LineSegment>();

    public FastCollinearPoints(
            Point[] points) {    // finds all line segments containing 4 or more points
        if (points == null)
            throw new java.lang.IllegalArgumentException("argument to the constructor is null.");
        for (int i = 0; i != points.length; ++i) {
            if (points[i] == null)
                throw new java.lang.IllegalArgumentException("include null Point.");
        }
        this.points = points.clone();
        Arrays.sort(this.points);
        for (int i = 0; i != this.points.length - 1; ++i) {
            if (this.points[i].equals(this.points[i + 1]))
                throw new java.lang.IllegalArgumentException("include same Point.");
        }
        if (this.points.length > 3)
            findLine();
    }

    private void findLine() {
        //double[] slope = new double[points.length - 1];
        //Point[] temp_points = points.clone();
        for (int i = 0; i != points.length; ++i) {
            Point[] tempPoints = points.clone();
            Arrays.sort(tempPoints, points[i].slopeOrder());                   // 按照关于i点的斜率排序
            int counter = 0;
            for (int j = 0; j != points.length - 1; ++j) {
                if (Double.compare(points[i].slopeTo(tempPoints[j]),
                                   points[i].slopeTo(tempPoints[j + 1]))
                        == 0)       // 斜率相同增加计数值，不同就清零
                    counter++;
                else {
                    if (counter > 1 && (points[i].compareTo(tempPoints[j - counter])
                            // 与的条件是判断是否为线段最开始的点，因为首先是根据点的大小排序，再根据相对斜率排序的，排序是稳定的，不会打乱点的大小。判断计算斜率的参考点和同斜率最小的点，就知道是否是起始点。这里最开始没想到是觉得斜率A-B和B-A不一样，傻了。
                            <= 0)) {  // 共线点超过4个，既有超过1对的相等
                        // for (int k = 0; k != counter - 1; --k) {
                        //     segmentList.add(new LineSegment(points[j - k], points[j - k - 1]));
                        // }
                        segmentList.add(new LineSegment(points[i], tempPoints[j]));
                    }
                    counter = 0;
                }
                if (j == points.length - 2) {   // 处理最后一段相等
                    if (counter > 1 && (points[i].compareTo(tempPoints[j + 1 - counter])
                            <= 0)) {  // 共线点超过4个，既有超过1对的相等
                        segmentList.add(new LineSegment(points[i], tempPoints[j + 1]));
                    }
                    counter = 0;
                }
            }
        }
        number = segmentList.size();
    }


    public int numberOfSegments() {        // the number of line segments
        return number;
    }

    public LineSegment[] segments() {                // the line segments
        LineSegment[] segments = new LineSegment[number];
        int i = 0;
        for (LineSegment seg : segmentList) {
            segments[i++] = seg;
        }
        return segments;
    }

    public static void main(String[] args) {
        // read the n points from a file
        //In in = new In(args[0]);
        In in = new In("input20.txt"); //本地测试使用
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenRadius(0.01);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
