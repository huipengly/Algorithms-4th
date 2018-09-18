/* *****************************************************************************
 *  Name: 暴力判断是否共线
 *  Date: 20180917
 *  Description:
 *  author: huipengly
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BruteCollinearPoints {
    //Point[] points;
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
        Arrays.sort(points);
        for (int i = 0; i != points.length - 1; ++i) {
            if (points[i].equals(points[i + 1]))
                throw new java.lang.IllegalArgumentException("include same Point.");
        }
        //this.points = points;
        findLine(points);
    }

    public void findLine(Point[] points) {    // 寻找连成线的四个点
        for (int i = 0; i != points.length - 3; ++i) {
            for (int j = i + 1; j != points.length - 2; ++j) {
                for (int k = j + 1; k != points.length - 1; ++k) {
                    Comparator<Point> comparator = points[i]
                            .slopeOrder();      // 与point[i]点连线的斜率作比较
                    if (comparator.compare(points[j], points[k]) == 0) {    // 斜率相同说明此i,j,k共线，寻找下一个l
                        for (int i1 = k + 1; i1 != points.length; ++i1) {
                            if (comparator.compare(points[k], points[i1]) == 0) {
                                segmentList.add(new LineSegment(points[i], points[j]));
                                segmentList.add(new LineSegment(points[j], points[k]));
                                segmentList.add(new LineSegment(points[k], points[i1]));
                                ++number;
                            }
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {        // the number of line segments
        return number;
    }

    public LineSegment[] segments() {               // the line segments
        LineSegment[] segments = new LineSegment[segmentList.size()];
        int i = 0;
        for (LineSegment seg : segmentList) {
            segments[i++] = seg;
        }
        return segments;
    }

    public static void main(String[] args) {
        Point[] p = new Point[6];
        p[0] = new Point(19000, 10000);
        p[1] = new Point(18000, 10000);
        p[2] = new Point(32000, 10000);
        p[3] = new Point(21000, 10000);
        p[4] = new Point(1234, 5678);
        p[5] = new Point(14000, 10000);
        BruteCollinearPoints bcp = new BruteCollinearPoints(p);
    }
}
