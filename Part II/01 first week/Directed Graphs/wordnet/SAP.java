/* *****************************************************************************
 *  Name:           SAP.java
 *  Date:           20181018
 *  Description:    Shortest ancestral path 最短祖先距离
 *  Author:         huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph G;

    private class Ancestor {
        private int length = -1;
        private int ancestor = -1;
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new java.lang.IllegalArgumentException("use a null digraph");
        }
        this.G = new Digraph(G);
    }

    private Ancestor searchSap(int v, int w) {
        Ancestor shortestAncestor = new Ancestor();
        int vNumber = G.V();                    // vertices 顶点个数
        BreadthFirstDirectedPaths vBFS = new BreadthFirstDirectedPaths(G, v);   // BFS计算到每个点的最短距离
        BreadthFirstDirectedPaths wBFS = new BreadthFirstDirectedPaths(G, w);
        for (int i = 0; i != vNumber; ++i) {    // 计算到每个点的最短祖先距离，更新最短始祖
            if (vBFS.hasPathTo(i) && wBFS.hasPathTo(i)) {
                int length = vBFS.distTo(i) + wBFS.distTo(i);
                if (length < shortestAncestor.length || shortestAncestor.length == -1) {
                    shortestAncestor.length = length;
                    shortestAncestor.ancestor = i;
                }
            }
        }

        return shortestAncestor;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        Ancestor shortestAncestor = searchSap(v, w);
        return shortestAncestor.length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        Ancestor shortestAncestor = searchSap(v, w);
        return shortestAncestor.ancestor;
    }

    private Ancestor searchSap(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new java.lang.IllegalArgumentException("use a null Iterator");
        }
        Ancestor shortestAncestor = new Ancestor();
        for (int vv : v) {          // 两个集合，两两计算最短祖先，求出最短的。时间肯定有问题啊！
            for (int ww : w) {
                Ancestor ancestor = searchSap(vv, ww);
                if (ancestor.length < shortestAncestor.length || shortestAncestor.length == -1) {
                    shortestAncestor = ancestor;
                }
            }
        }

        return shortestAncestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        Ancestor shortestAncestor = searchSap(v, w);
        return shortestAncestor.length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        Ancestor shortestAncestor = searchSap(v, w);
        return shortestAncestor.ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("digraph25.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        Stack<Integer> v = new Stack<>();
        v.push(13);
        v.push(23);
        v.push(24);
        Stack<Integer> w = new Stack<>();
        w.push(6);
        w.push(16);
        w.push(17);
        int length = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
}
