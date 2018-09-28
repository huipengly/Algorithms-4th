/* *****************************************************************************
 *  Name:           KdTree.java
 *  Date:           20180928
 *  Description:    使用2d-Tree来解决问题
 *  Author:         huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;

public class KdTree {
    private TreeSet<Node> kdTree;

    /*  Node根据point的xy和rect比较，当rect横向时，比较y，当rect纵向时，比较x。 */
    private class Node implements Comparable<Node> {
        private Point2D p;          // the point
        private RectHV rect;        // the axis-aligned rectangle corresponding to this node，退化成一条线
        private Node lb;            // the left/bottom subtree
        private Node rt;            // the right/top subtree
        /* 只需要子节点，树不要向上找 */

        public Node(Point2D p) {
            this.p = p;
        }

        /* 假设忽略比较的点在rect线上的情况，这种情况也认为是相等的点 */
        @Override
        public int compareTo(Node that) {
            if (rect.xmin() == rect.xmax()) {     // 横向时
                if (this.p.y() < that.p.y()) return -1;
                if (that.p.y() < this.p.y()) return 1;
                if (this.p.x() == that.p.x()) return 0;
            }
            else if (rect.ymin() == rect.ymax()) { // 纵向时
                if (this.p.x() < that.p.y()) return -1;
                if (that.p.x() < this.p.y()) return 1;
                if (this.p.y() == that.p.y()) return 0;
            }
            else
                throw new java.lang.IllegalArgumentException("Node's Rect must be a line");
            return 0;
        }
    }

    public KdTree() {                       // construct an empty set of points
        kdTree = new TreeSet<>();
    }

    public boolean isEmpty() {              // is the set empty?
        return kdTree.isEmpty();
    }

    public int size() {                     // number of points in the set
        return kdTree.size();
    }

    public void insert(
            Point2D p) {                    // add the point to the set (if it is not already in the set)
        kdTree.add(new Node(p));
        kdTree // 更新父节点
    }

    public boolean contains(Point2D p) {    // does the set contain point p?
        return kdTree.contains(new Node(p));        // 新建一个包含p的Node
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
