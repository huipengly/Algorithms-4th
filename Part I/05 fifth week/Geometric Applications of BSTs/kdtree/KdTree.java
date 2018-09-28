/* *****************************************************************************
 *  Name:           KdTree.java
 *  Date:           20180928
 *  Description:    使用2d-Tree来解决问题
 *  Author:         huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int size = 0;
    private final int horizon = 0;
    private final int vertical = 1;

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
    // TODO: RectHV怎么更新
    private Node insert(Node n, Point2D p, int i, RectHV re) {
        if (n == null) {
            ++size;                     //添加了一个节点
            return new Node(p, re);
        }

        if (i % 2 == horizon) {         // 横向节点比较y
            if (n.p.y() < p.y())        // p的y小，插入到左节点
                n.lb = insert(n, p, i + 1, new RectHV(re.xmin(), re.ymin(), re.xmax(), n.p.y()));
            else if (n.p.y() > p.y())   // p的y大，插入到右节点
                n.rt = insert(n, p, i + 1, new RectHV(re.xmin(), n.p.y(), re.xmax(), re.ymax()));
        }
        else if (i % 2 == vertical) {   // 纵向比较
            if (n.p.x() < p.x())        // p的x小，插入到左节点
                n.lb = insert(n, p, i + 1, new RectHV(re.xmin(), re.ymin(), n.p.x(), re.ymax()));
            else if (n.p.x() > p.x())   // p的x大，插入到右节点
                n.rt = insert(n, p, i + 1, new RectHV(n.p.y(), re.ymin(), re.xmax(), re.ymax()));
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
            case horizon:
                if (n.p.y() < p.y()) return contains(n.lb, p, i + 1);
                else if (n.p.y() > p.y()) return contains(n.rt, p, i + 1);
                else return true;

            case vertical:
                if (n.p.x() < p.x()) return contains(n.lb, p, i + 1);
                else if (n.p.x() > p.x()) return contains(n.rt, p, i + 1);
                else return true;

            default:    // 这种情况不可能出现，但是不写他就报返回值错误
                return true;
        }
    }

    public void draw() {                    // draw all points to standard draw
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Node n : kdTree) {
            StdDraw.point(n.p.x(), n.p.y());
        }
    }

    public Iterable<Point2D> range(
            RectHV rect) {                  // all points that are inside the rectangle (or on the boundary)
    }

    public Point2D nearest(
            Point2D p) {                    // a nearest neighbor in the set to point p; null if the set is empty
    }

    public static void main(
            String[] args) {                // unit testing of the methods (optional)

    }
}
