/* *****************************************************************************
 *  Name:           PointSet.java
 *  Date:           20180928
 *  Description:    暴力搜索方法求解问题
 *  author:         huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> point2DTreeSet;

    public PointSET() {                     // construct an empty set of points
        point2DTreeSet = new TreeSet<>();
    }

    public boolean isEmpty() {              // is the set empty?
        return point2DTreeSet.isEmpty();
    }

    public int size() {                     // number of points in the set
        return point2DTreeSet.size();
    }

    public void insert(
            Point2D p) {                    // add the point to the set (if it is not already in the set)
        point2DTreeSet.add(p);
    }

    public boolean contains(Point2D p) {    // does the set contain point p?
        return point2DTreeSet.contains(p);
    }

    public void draw() {                    // draw all points to standard draw
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D p : point2DTreeSet) {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(
            RectHV rect) {                  // all points that are inside the rectangle (or on the boundary)
        Stack<Point2D> pointInRectangle = new Stack<>();
        for (Point2D p : point2DTreeSet) {
            if (p.x() > rect.xmax() && p.x() < rect.xmax() &&
                    p.y() > rect.ymin() && p.y() < rect.ymax())
                pointInRectangle.push(p);
        }
        return pointInRectangle;
    }

    public Point2D nearest(
            Point2D p) {                    // a nearest neighbor in the set to point p; null if the set is empty
        Point2D nearestPoint = point2DTreeSet.first();
        double minDistance = Math.sqrt(
                Math.pow(nearestPoint.x() - p.x(), 2) + Math.pow(nearestPoint.y() - p.y(), 2));
        for (Point2D point : point2DTreeSet) {
            double distance = Math.sqrt(
                    Math.pow(point.x() - p.x(), 2) + Math.pow(point.y() - p.y(), 2));
            if (distance < minDistance) {
                minDistance = distance;     // Hint: 更新最短距离啊！！
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }

    public static void main(
            String[] args) {                // unit testing of the methods (optional)
        boolean testNearestNeighbor = false;
        boolean testRangeSearch = true;

        // initialize the two data structures with point from file
        //String filename = args[0];
        In in = new In("horizontal8.txt");
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }

        if (testNearestNeighbor) {
            // process nearest neighbor queries
            StdDraw.enableDoubleBuffering();
            while (true) {
                // the location (x, y) of the mouse
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                Point2D query = new Point2D(x, y);
                Point2D testPoint = new Point2D(0.5, 0.55);

                // draw all of the points
                StdDraw.clear();
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(0.01);
                brute.draw();

                // draw test point
                StdDraw.setPenRadius(0.03);
                StdDraw.setPenColor(StdDraw.CYAN);
                StdDraw.point(query.x(), query.y());
                // StdDraw.point(testPoint.x(), testPoint.y());

                // draw in red the nearest neighbor (using brute-force algorithm)
                StdDraw.setPenRadius(0.03);
                StdDraw.setPenColor(StdDraw.RED);
                brute.nearest(query).draw();
                // brute.nearest(testPoint).draw();
                StdDraw.setPenRadius(0.02);

                // draw in blue the nearest neighbor (using kd-tree algorithm)
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.show();
                StdDraw.pause(40);
            }
        }

        if (testRangeSearch) {
            double x0 = 0.0, y0 = 0.0;      // initial endpoint of rectangle
            double x1 = 1.0, y1 = 1.0;      // current location of mouse
            boolean isDragging = false;     // is the user dragging a rectangle

            // draw the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            brute.draw();
            StdDraw.show();

            // process range search queries
            StdDraw.enableDoubleBuffering();
            while (true) {

                // // user starts to drag a rectangle
                // if (StdDraw.isMousePressed() && !isDragging) {
                //     x0 = x1 = StdDraw.mouseX();
                //     y0 = y1 = StdDraw.mouseY();
                //     isDragging = true;
                // }
                //
                // // user is dragging a rectangle
                // else if (StdDraw.isMousePressed() && isDragging) {
                //     x1 = StdDraw.mouseX();
                //     y1 = StdDraw.mouseY();
                // }
                //
                // // user stops dragging rectangle
                // else if (!StdDraw.isMousePressed() && isDragging) {
                //     isDragging = false;
                // }

                // draw the points
                StdDraw.clear();
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(0.01);
                brute.draw();

                // draw the rectangle
                RectHV rect = new RectHV(Math.min(x0, x1), Math.min(y0, y1),
                                         Math.max(x0, x1), Math.max(y0, y1));
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius();
                rect.draw();

                // draw the range search results for brute-force data structure in red
                StdDraw.setPenRadius(0.03);
                StdDraw.setPenColor(StdDraw.RED);
                for (Point2D p : brute.range(rect))
                    p.draw();

                StdDraw.show();
                StdDraw.pause(20);
            }
        }
    }
}
