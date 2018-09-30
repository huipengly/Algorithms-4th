/* *****************************************************************************
 *  Name:           KdTree.java
 *  Date:           20180928
 *  Description:    使用2d-Tree来解决问题
 *  Author:         huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int size = 0;
    private final int horizon = 1;
    private final int vertical = 0;

    private class Node {
        private Point2D p;          // the point
        private RectHV rect;        // the axis-aligned rectangle corresponding to this node
        private Node lb;            // the left/bottom subtree
        private Node rt;            // the right/top subtree

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    public KdTree() {                       // construct an empty set of points

    }

    public boolean isEmpty() {              // is the set empty?
        return size == 0;
    }

    public int size() {                     // number of points in the set
        return size;
    }

    public void insert(
            Point2D p) {                    // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new java.lang.IllegalArgumentException("point is null");
        if (root == null) {
            ++size;
            root = new Node(p, new RectHV(0, 0, 1, 1));
        }
        else
            root = insert(root, p, 0);
    }

    /* i % 2 表达横竖。0表示横，1表示竖 */
    private Node insert(Node n, Point2D p, int i) {
        double x = n.p.x();
        double y = n.p.y();

        switch (i % 2) {
            case vertical:          // 纵向比较
                if (p.x() < x) {    // p的x小，插入到左节点
                    if (n.lb == null) {
                        n.lb = new Node(p, new RectHV(n.rect.xmin(), n.rect.ymin(),
                                                      x, n.rect.ymax()));
                        ++size;
                        return n;
                    }
                    n.lb = insert(n.lb, p, i + 1);
                }
                else if (p.x() > x ||
                        (p.x() == x && p.y() != y)) {     // p的x大，或p在分割线上，插入到右节点。忽略相同的节点
                    if (n.rt == null) {
                        n.rt = new Node(p, new RectHV(x, n.rect.ymin(),
                                                      n.rect.xmax(), n.rect.ymax()));
                        ++size;
                        return n;
                    }
                    n.rt = insert(n.rt, p, i + 1);
                }
                break;

            case horizon:           // 横向节点比较y
                if (p.y() < y) {      // p的y小，插入到左节点
                    if (n.lb == null) {
                        n.lb = new Node(p, new RectHV(n.rect.xmin(), n.rect.ymin(),
                                                      n.rect.xmax(), y));
                        ++size;
                        return n;
                    }
                    n.lb = insert(n.lb, p, i + 1);
                }
                else if (p.y() > y ||
                        (p.y() == y && p.x() != x)) {      // p的y大，或p在分割线上，插入到右节点。忽略相同的节点
                    if (n.rt == null) {
                        n.rt = new Node(p, new RectHV(n.rect.xmin(), y,
                                                      n.rect.xmax(), n.rect.ymax()));
                        ++size;
                        return n;
                    }
                    n.rt = insert(n.rt, p, i + 1);
                }
                break;
        }
        return n;
    }

    public boolean contains(Point2D p) {    // does the set contain point p?
        if (p == null)
            throw new java.lang.IllegalArgumentException("point is null");
        return contains(root, p, 0);
    }

    private boolean contains(Node n, Point2D p, int i) {
        if (n == null)
            return false;
        double x = n.p.x();
        double y = n.p.y();
        switch (i % 2) {
            case vertical:
                if (p.x() < x) return contains(n.lb, p, i + 1);
                else if (p.x() > x ||
                        (p.x() == x && p.y() != y))
                    return contains(n.rt, p, i + 1);
                break;

            case horizon:
                if (p.y() < y) return contains(n.lb, p, i + 1);
                else if (p.y() > y ||
                        (p.y() == y && p.x() != x))
                    return contains(n.rt, p, i + 1);
                break;
        }
        return true;
    }

    public void draw() {                    // draw all points to standard draw
        if (isEmpty())
            return;
        draw(root, 0);
    }

    // 递归思想，先画左侧节点，然后画右侧节点。
    // 先一直向左走，走到子节点均为空的节点，画。然后返回，向这个节点父节点的右节点画，也是找到这个右节点中均为空的节点。然后画父节点，再返回继续画上面的父节点
    private void draw(Node n, int i) {
        if (n.lb != null)
            draw(n.lb, i + 1);
        if (n.rt != null)
            draw(n.rt, i + 1);

        StdDraw.setPenRadius();
        switch (i % 2) {
            case vertical:
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
                break;
            case horizon:
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
                break;
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(n.p.x(), n.p.y());
    }

    public Iterable<Point2D> range(
            RectHV rect) {                  // all points that are inside the rectangle (or on the boundary)
        if (rect == null)
            throw new java.lang.IllegalArgumentException("rectangle is null");
        Stack<Point2D> points = new Stack<>();
        range(points, root, rect);
        return points;
    }

    private void range(Stack<Point2D> point2DStack, Node n, RectHV rect) {
        if (n == null)      // 记得判断空节点
            return;

        if (!n.rect.intersects(rect))    // range的矩形不在node的包含中
            return;

        if (n.lb != null && rect.intersects(n.lb.rect))     // 如果和左/下边矩形相交
            range(point2DStack, n.lb, rect);
        if (n.rt != null && rect.intersects(n.rt.rect))     // 如果和右/上边矩形相交
            range(point2DStack, n.rt, rect);

        if (rect.contains(n.p))   // 当前节点在range的矩形内
            point2DStack.push(n.p);
    }

    public Point2D nearest(
            Point2D p) {                    // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null)
            throw new java.lang.IllegalArgumentException("point is null");
        if (isEmpty())
            return null;
        return nearest(p, root.p, root, 0);
    }

    // np是指节点的point，p是query点，i用来记录方向
    private boolean lowerSide(Point2D np, Point2D p, int i) {
        switch (i % 2) {
            case vertical:  // 垂直分割
                return np.x() > p.x();
            default:        // 横向分割
                return np.y() > p.y();
        }
    }

    private boolean higherSide(Point2D np, Point2D p, int i) {
        return !lowerSide(np, p, i);
    }

    private double distanceToRect(Point2D np, Point2D p, int i) {
        switch (i % 2) {
            case vertical:
                return p.distanceTo(new Point2D(np.x(), p.y()));
            default:
                return p.distanceTo(new Point2D(p.x(), np.y()));
        }
    }

    private int compare(Point2D p1, Point2D query, int i) {
        switch (i % 2) {
            case vertical:
                return query.x() < p1.x() ? -1 : 1;
            case horizon:
                return query.y() < p1.y() ? -1 : 1;
        }
        return 0;
    }

    private Point2D nearest(Point2D p, Point2D nearestPoint, Node n, int i) {

        // double x = n.p.x();
        // double y = n.p.y();

        double nearestDistance = p.distanceTo(nearestPoint);      // 当前最短距离
        double distanceToNode = p.distanceTo(n.p);
        if (distanceToNode < nearestDistance) {                 // 比较当前点距离和最短距离
            nearestPoint = n.p;
            nearestDistance = distanceToNode;
        }

        int cmp = compare(n.p, p, i);           // 判断在那边包含

        if (cmp < 0) {
            if (n.lb != null) {
                nearestPoint = nearest(p, nearestPoint, n.lb, i + 1);
                nearestDistance = p.distanceTo(nearestPoint);
            }
            if (n.rt != null) {
                if (nearestDistance > n.rt.rect.distanceTo(p)) {
                    nearestPoint = nearest(p, nearestPoint, n.rt, i + 1);
                    // nearestDistance = p.distanceTo(nearestPoint);
                }
            }
        }
        else {
            if (n.rt != null) {
                nearestPoint = nearest(p, nearestPoint, n.rt, i + 1);
                nearestDistance = p.distanceTo(nearestPoint);
            }
            if (n.lb != null) {
                if (nearestDistance > n.lb.rect.distanceTo(p)) {
                    nearestPoint = nearest(p, nearestPoint, n.lb, i + 1);
                    // nearestDistance = p.distanceTo(nearestPoint);
                }
            }
        }

        return nearestPoint;
    }

    public static void main(
            String[] args) {                // unit testing of the methods (optional)
        boolean testNearestNeighbor = true;
        boolean testRangeSearch = false;

        Point2D p1 = new Point2D(0.206107, 0.904508);
        Point2D p2 = new Point2D(0.5, 1);
        // 0.206107 0.904508
        // 0.5 1

        // initialize the two data structures with point from file
        //String filename = args[0];
        In in = new In("point.txt");
        KdTree kdTree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdTree.insert(p);
            kdTree.draw();
        }

        boolean a = kdTree.contains(new Point2D(0.92, 0.5));

        if (testNearestNeighbor) {
            // process nearest neighbor queries
            StdDraw.enableDoubleBuffering();
            while (true) {
                // the location (x, y) of the mouse
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                // Point2D query = new Point2D(x, y);
                Point2D testPoint = new Point2D(0.41, 0.5);

                // StdOut.print(testPoint.distanceTo(p1) + "\n");
                // StdOut.print(testPoint.distanceTo(p2) + "\n");

                // draw all of the points
                StdDraw.clear();
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(0.01);
                kdTree.draw();

                // draw test point
                StdDraw.setPenRadius(0.03);
                StdDraw.setPenColor(StdDraw.CYAN);
                // StdDraw.point(query.x(), query.y());
                StdDraw.point(testPoint.x(), testPoint.y());

                // draw in red the nearest neighbor (using brute-force algorithm)
                StdDraw.setPenRadius(0.03);
                StdDraw.setPenColor(StdDraw.RED);
                // kdTree.nearest(query).draw();
                kdTree.nearest(testPoint).draw();
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
            kdTree.draw();
            StdDraw.show();

            // process range search queries
            StdDraw.enableDoubleBuffering();
            while (true) {

                // user starts to drag a rectangle
                if (StdDraw.isMousePressed() && !isDragging) {
                    x0 = x1 = StdDraw.mouseX();
                    y0 = y1 = StdDraw.mouseY();
                    isDragging = true;
                }

                // user is dragging a rectangle
                else if (StdDraw.isMousePressed() && isDragging) {
                    x1 = StdDraw.mouseX();
                    y1 = StdDraw.mouseY();
                }

                // user stops dragging rectangle
                else if (!StdDraw.isMousePressed() && isDragging) {
                    isDragging = false;
                }

                // draw the points
                StdDraw.clear();
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(0.01);
                kdTree.draw();

                // draw the rectangle
                RectHV rect = new RectHV(Math.min(x0, x1), Math.min(y0, y1),
                                         Math.max(x0, x1), Math.max(y0, y1));
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius();
                rect.draw();

                // draw the range search results for brute-force data structure in red
                StdDraw.setPenRadius(0.03);
                StdDraw.setPenColor(StdDraw.RED);
                for (Point2D p : kdTree.range(rect))
                    p.draw();

                StdDraw.show();
                StdDraw.pause(20);
            }
        }
    }
}
