/* *****************************************************************************
 *  Name: FastCollinearPoints.java
 *  Date: 20180918
 *  Description: 快速查找共线点方法
 *  author: huipengly
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private int number = 0;
    private Point[] points;
    private ArrayList<LineSegment> segmentList = new ArrayList<LineSegment>();

    public FastCollinearPoints(
            Point[] points) {    // finds all line segments containing 4 or more points
        this.points = points;
        if (this.points == null)
            throw new java.lang.IllegalArgumentException("argument to the constructor is null.");
        for (int i = 0; i != this.points.length; ++i) {
            if (points[i] == null)
                throw new java.lang.IllegalArgumentException("include null Point.");
        }
        Arrays.sort(this.points);
        for (int i = 0; i != this.points.length - 1; ++i) {
            if (this.points[i].equals(this.points[i + 1]))
                throw new java.lang.IllegalArgumentException("include same Point.");
        }
        findLine();
    }

    public void findLine() {
        double[] slope = new double[points.length - 1];
        for (int i = 0; i != points.length - 1; ++i) {
            Arrays.sort(points, points[i].slopeOrder());                   // 按照关于i点的斜率排序
            int counter = 0;
            for (int j = 0; j != points.length - 1; ++j) {
                if (Double.compare(points[i].slopeTo(points[j]), points[i].slopeTo(points[j + 1]))
                        == 0)       // 斜率相同增加计数值，不同就清零
                    counter++;
                else {
                    if (counter >= 4) {  // 共线点超过4个
                        for (int k = 0; k != counter - 1; --k) {
                            segmentList.add(new LineSegment(points[j - k], points[j - k - 1]));
                        }
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

    }
}
