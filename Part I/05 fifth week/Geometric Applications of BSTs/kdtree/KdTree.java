/* *****************************************************************************
 *  Name:           KdTree.java
 *  Date:           20180928
 *  Description:    使用2d-Tree来解决问题
 *  Author:         huipengly
 **************************************************************************** */

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

        switch (i % 2) {
            case vertical:          // 纵向比较
                if (n.p.x() < p.x()) {          // p的x小，插入到左节点
                    n.lb = insert(n, p, i + 1,
                                  new RectHV(re.xmin(), re.ymin(), n.p.x(), re.ymax()));
                }
                else if (n.p.x() > p.x()) {     // p的x大，插入到右节点
                    n.rt = insert(n, p, i + 1,
                                  new RectHV(n.p.y(), re.ymin(), re.xmax(), re.ymax()));
                }
                else if (n.p.y() != p.y()) {    // 相当于在分割线上，这种情况认为在分割线上侧
                    n.rt = insert(n, p, i + 1,
                                  new RectHV(n.p.y(), re.ymin(), re.xmax(), re.ymax()));
                }
                // else if (n.p.y() == p.y()) {  // 相同节点忽略
                break;

            case horizon:           // 横向节点比较y
                if (n.p.y() < p.y())        // p的y小，插入到左节点
                    n.lb = insert(n, p, i + 1,
                                  new RectHV(re.xmin(), re.ymin(), re.xmax(), n.p.y()));
                else if (n.p.y() > p.y())   // p的y大，插入到右节点
                    n.rt = insert(n, p, i + 1,
                                  new RectHV(re.xmin(), n.p.y(), re.xmax(), re.ymax()));
                else if (n.p.x() != p.x())   // 相当于在分割线上，这种情况认为在分割线右侧
                    n.rt = insert(n, p, i + 1,
                                  new RectHV(re.xmin(), n.p.y(), re.xmax(), re.ymax()));
                // 相同的节点忽略
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
        draw(root);
    }

    // 递归思想，先画左侧节点，然后画右侧节点。
    // 先一直向左走，走到子节点均为空的节点，画。然后返回，向这个节点父节点的右节点画，也是找到这个右节点中均为空的节点。然后画父节点，再返回继续画上面的父节点
    private void draw(Node n) {         // 不用判断节点方向，因为是要画整个矩形

        if (n.lb != null)
            draw(n.lb);
        if (n.rt != null)
            draw(n.rt);

        // if (n.lb == null && n.rt == null) {
        StdDraw.setPenRadius();
        if (n.rect.ymin() != 0) {    // 判断矩形下边不在边界上
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.rect.xmin(), n.rect.ymin(), n.rect.xmax(), n.rect.ymin());
        }
        if (n.rect.ymax() != 1) {   // 判断矩形上边不在边界上
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.rect.xmin(), n.rect.ymax(), n.rect.xmax(), n.rect.ymax());
        }
        if (n.rect.xmin() != 0) {   // 判断矩形左边不在边界上
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.rect.xmin(), n.rect.ymin(), n.rect.xmin(), n.rect.ymax());
        }
        if (n.rect.xmax() != 0) {   // 判断矩形右边不在边界上
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.rect.xmax(), n.rect.ymin(), n.rect.xmax(), n.rect.ymax());
        }

        //     return;
        // }
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
    }

    public static void main(
            String[] args) {                // unit testing of the methods (optional)

    }
}
