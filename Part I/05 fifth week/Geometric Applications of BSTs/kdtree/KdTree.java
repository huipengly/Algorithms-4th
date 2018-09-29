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
        root = insert(root, p, 0, new RectHV(0, 0, 1, 1));
    }

    /* i % 2 表达横竖。0表示横，1表示竖 */
    private Node insert(Node n, Point2D p, int i, RectHV re) {
        if (n == null) {
            ++size;                     //添加了一个节点
            return new Node(p, re);
        }

        double x = n.p.x();
        double y = n.p.y();

        switch (i % 2) {
            case vertical:          // 纵向比较
                if (p.x() < x) {    // p的x小，插入到左节点
                    n.lb = insert(n.lb, p, i + 1,
                                  new RectHV(re.xmin(), re.ymin(), x, re.ymax()));
                }
                else if (p.x() >= x && p.y() != y) {     // p的x大，或p在分割线上，插入到右节点。忽略相同的节点
                    n.rt = insert(n.rt, p, i + 1,
                                  new RectHV(x, re.ymin(), re.xmax(), re.ymax()));
                }
                break;

            case horizon:           // 横向节点比较y
                if (p.y() < y)      // p的y小，插入到左节点
                    n.lb = insert(n.lb, p, i + 1,
                                  new RectHV(re.xmin(), re.ymin(), re.xmax(), y));
                else if (p.y() >= y && p.x() != x)      // p的y大，或p在分割线上，插入到右节点。忽略相同的节点
                    n.rt = insert(n.rt, p, i + 1,
                                  new RectHV(re.xmin(), y, re.xmax(), re.ymax()));
                break;
        }
        return n;
    }

    public boolean contains(Point2D p) {    // does the set contain point p?
        return contains(root, p, 0);
    }

    private boolean contains(Node n, Point2D p, int i) {
        if (n == null)
            return false;
        switch (i % 2) {
            case vertical:
                if (n.p.x() < p.x()) return contains(n.lb, p, i + 1);
                else if (n.p.x() > p.x()) return contains(n.rt, p, i + 1);
                else if (n.p.y() != p.y()) return contains(n.rt, p, i + 1);
                break;

            case horizon:
                if (n.p.y() < p.y()) return contains(n.lb, p, i + 1);
                else if (n.p.y() > p.y()) return contains(n.rt, p, i + 1);
                else if (n.p.x() != p.x()) return contains(n.rt, p, i + 1);
                break;
        }
        return true;
    }

    public void draw() {                    // draw all points to standard draw
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
        Stack<Point2D> points = new Stack<>();
        range(points, root, rect, 0);
        return points;
    }

    private void range(Stack<Point2D> point2DStack, Node n, RectHV rect, int i) {
        if (n.rect.xmin() > rect.xmax() ||
                n.rect.xmax() < rect.xmin() ||
                n.rect.ymin() > rect.ymax() ||
                n.rect.ymax() < rect.xmin())    // range的矩形不在node的包含中
            return;
        double x = n.p.x();
        double y = n.p.y();
        int counter = 0;        // 用来记录满足几个边的条件

        switch (i % 2) {
            case vertical:
                if (rect.xmin() < x) {
                    range(point2DStack, n.lb, rect, i + 1);
                    ++counter;
                }
                if (rect.xmax() > x) {
                    range(point2DStack, n.rt, rect, i + 1);
                    ++counter;
                }
                break;

            case horizon:
                if (rect.ymin() < y) {
                    range(point2DStack, n.lb, rect, i + 1);
                    ++counter;
                }
                if (rect.ymax() > y) {
                    range(point2DStack, n.rt, rect, i + 1);
                    ++counter;
                }
                break;
        }

        if (counter == 4)   // counter等于4说明当前点在范围内
            point2DStack.push(n.p);
    }

    public Point2D nearest(
            Point2D p) {                    // a nearest neighbor in the set to point p; null if the set is empty
        return nearest(p, root, 0);
    }

    private Point2D nearest(Point2D p, Node n, int i) {
        if (n == null)      // 判断节点是否为空
            return null;

        double x = n.p.x();
        double y = n.p.y();
        Point2D ret = null;
        double distance = Math.sqrt(Math.pow(p.x() - x, 2) + Math.pow(p.y() - y, 2));

        switch (i % 2) {
            case vertical:
                // 先向点所在的区域寻找，如果找到未找到点，则再向另一个方向搜索。如果找到点，则另一个方向被剪枝。
                // TODO:这个代码应该可以优化
                if (p.x() < x) {
                    ret = nearest(p, n.lb, i + 1);
                    if (ret == null)
                        ret = nearest(p, n.rt, i + 1);
                    // return ret;
                }
                else {      // p.x() >= x
                    ret = nearest(p, n.rt, i + 1);
                    if (ret == null)
                        ret = nearest(p, n.lb, i + 1);
                    // return ret;
                }
                break;
            case horizon:
                if (p.y() < y) {
                    ret = nearest(p, n.lb, i + 1);
                    if (ret == null)
                        ret = nearest(p, n.rt, i + 1);
                    // return ret;
                }
                else {      // p.y() >= y
                    ret = nearest(p, n.rt, i + 1);
                    if (ret == null)
                        ret = nearest(p, n.lb, i + 1);
                    // return ret;
                }
                break;
        }

        if (ret != null) {
            double retDistance = Math.sqrt(
                    Math.pow(p.x() - ret.x(), 2) + Math.pow(p.y() - ret.y(), 2));
            if (retDistance < distance)
                return ret;
        }
        return n.p;
    }

    public static void main(
            String[] args) {                // unit testing of the methods (optional)
        boolean testNearestNeighbor = true;
        boolean testRangeSearch = false;

        // initialize the two data structures with point from file
        //String filename = args[0];
        In in = new In("point.txt");
        KdTree kdTree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdTree.insert(p);
        }

        if (testNearestNeighbor) {
            // process nearest neighbor queries
            StdDraw.enableDoubleBuffering();
            while (true) {
                // the location (x, y) of the mouse
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                Point2D query = new Point2D(x, y);
                // Point2D testPoint = new Point2D(0.1, 0.1);

                // draw all of the points
                StdDraw.clear();
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(0.01);
                kdTree.draw();

                // draw test point
                StdDraw.setPenRadius(0.03);
                StdDraw.setPenColor(StdDraw.CYAN);
                StdDraw.point(query.x(), query.y());
                // StdDraw.point(testPoint.x(), testPoint.y());

                // draw in red the nearest neighbor (using brute-force algorithm)
                StdDraw.setPenRadius(0.03);
                StdDraw.setPenColor(StdDraw.RED);
                kdTree.nearest(query).draw();
                // kdTree.nearest(testPoint).draw();
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
