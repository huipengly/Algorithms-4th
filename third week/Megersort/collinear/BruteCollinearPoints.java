/* *****************************************************************************
 *  Name: 暴力判断是否共线
 *  Date: 20180917
 *  Description:
 *  author: huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BruteCollinearPoints {
    private Point[] points;
    private int number = 0;
    //LineSegment[] lineSegments = new LineSegment[3];
    private ArrayList<LineSegment> segmentList = new ArrayList<LineSegment>();

    public BruteCollinearPoints(Point[] points) {    // finds all line segments containing 4 points
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
        //this.points = points;
        if (this.points.length > 3)
            findLine();
    }

    private void findLine() {    // 寻找连成线的四个点
        for (int i = 0; i != points.length - 3; ++i) {
            for (int j = i + 1; j != points.length - 2; ++j) {
                for (int k = j + 1; k != points.length - 1; ++k) {
                    Comparator<Point> comparator = points[i]
                            .slopeOrder();      // 与point[i]点连线的斜率作比较
                    if (comparator.compare(points[j], points[k]) == 0) {    // 斜率相同说明此i,j,k共线，寻找下一个l
                        for (int i1 = k + 1; i1 != points.length; ++i1) {
                            if (comparator.compare(points[k], points[i1]) == 0) {
                                // segmentList.add(new LineSegment(points[i], points[j]));
                                // segmentList.add(new LineSegment(points[j], points[k]));
                                // segmentList.add(new LineSegment(points[k], points[i1]));

                                segmentList.add(new LineSegment(points[i], points[i1]));
                            }
                        }
                    }
                }
            }
        }
        number = segmentList.size();
    }

    public int numberOfSegments() {        // the number of line segments
        return number;
    }

    public LineSegment[] segments() {               // the line segments
        LineSegment[] segments = new LineSegment[number];
        int i = 0;
        for (LineSegment seg : segmentList) {
            segments[i++] = seg;
        }
        return segments;
    }

    public static void main(String[] args) {
        // read the n points from a file
        // In in = new In(args[0]);
        In in = new In("input8.txt"); //本地测试使用
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
