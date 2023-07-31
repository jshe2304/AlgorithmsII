/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    private final Digraph G;
    private int cacheV;
    private int cacheW;
    private BreadthFirstDirectedPaths bfsV;
    private BreadthFirstDirectedPaths bfsW;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.G = new Digraph(G);
    }

    private void updateV(int v) {
        cacheV = v;
        bfsV = new BreadthFirstDirectedPaths(G, v);
    }

    private void updateW(int w) {
        cacheW = w;
        bfsW = new BreadthFirstDirectedPaths(G, w);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v > G.V() || w < 0 || w >= G.V()) {
            throw new IllegalArgumentException();
        }

        if (v != cacheV || bfsV == null) updateV(v);
        if (w != cacheW || bfsW == null) updateW(w);

        int min = G.V() + G.E();
        for (int i = 0; i < G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int len = bfsV.distTo(i) + bfsW.distTo(i);
                if (len < min) min = len;
            }
        }

        if (min == G.V() + G.E()) min = -1;
        return min;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v > G.V() || w < 0 || w >= G.V()) {
            throw new IllegalArgumentException();
        }

        if (v != cacheV || bfsV == null) updateV(v);
        if (w != cacheW || bfsW == null) updateW(w);

        int min = G.V() + G.E();
        int sap = -1;
        for (int i = 0; i < G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int len = bfsV.distTo(i) + bfsW.distTo(i);
                if (len < min) {
                    min = len;
                    sap = i;
                }
            }
        }

        return sap;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();

        for (Integer V : v) {
            if (V == null || V < 0 || V >= G.V()) throw new IllegalArgumentException();
            updateV(V);
            for (Integer W : w) {
                if (W == null || W < 0 || W >= G.V()) throw new IllegalArgumentException();
                if (bfsV.hasPathTo(W)) {
                    updateW(W);
                    return length(V, W);
                }
            }
        }
        return -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();

        for (Integer V : v) {
            if (V == null || V < 0 || V >= G.V()) throw new IllegalArgumentException();
            updateV(V);
            for (Integer W : w) {
                if (W == null || W < 0 || W >= G.V()) throw new IllegalArgumentException();
                if (bfsV.hasPathTo(W)) {
                    updateW(W);
                    return ancestor(V, W);
                }
            }
        }
        return -1;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("txt/digraph-wordnet.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
