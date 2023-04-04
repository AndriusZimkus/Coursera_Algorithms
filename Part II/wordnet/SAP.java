import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.LinkedList;

public class SAP {

    private Digraph G;
    private boolean[] markedV;
    private int[] lengthsV;
    private boolean[] markedW;
    private int[] lengthsW;

    private LinkedList<Integer> usedIntegers;


    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("Null Digraph");
        }
        this.G = new Digraph(G);

        markedV = new boolean[G.V()];
        lengthsV = new int[G.V()];

        markedW = new boolean[G.V()];
        lengthsW = new int[G.V()];
        usedIntegers = new LinkedList<>();

    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v >= G.V()) {
            throw new IllegalArgumentException("Illegal argument, v outside range");
        }
        if (w < 0 || w >= G.V()) {
            throw new IllegalArgumentException("Illegal argument, w outside range");
        }

        ArrayList<Integer> vList = new ArrayList<>();
        vList.add(v);
        ArrayList<Integer> wList = new ArrayList<>();
        wList.add(w);

        return BFSDeluxe(vList, wList)[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= G.V()) {
            throw new IllegalArgumentException("Illegal argument, v outside range");
        }
        if (w < 0 || w >= G.V()) {
            throw new IllegalArgumentException("Illegal argument, w outside range");
        }

        ArrayList<Integer> vList = new ArrayList<>();
        vList.add(v);
        ArrayList<Integer> wList = new ArrayList<>();
        wList.add(w);

        return BFSDeluxe(vList, wList)[1];

    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null) {
            throw new IllegalArgumentException("Illegal argument, v null");
        }
        if (w == null) {
            throw new IllegalArgumentException("Illegal argument, w null");
        }

        for (Integer x : v) {
            if (x == null || x < 0 || x >= G.V()) {
                throw new IllegalArgumentException("Illegal argument, v outside range");
            }
        }

        for (Integer x : w) {
            if (x == null || x < 0 || x >= G.V()) {
                throw new IllegalArgumentException("Illegal argument, w outside range");
            }
        }

        return BFSDeluxe(v, w)[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null) {
            throw new IllegalArgumentException("Illegal argument, v null");
        }
        if (w == null) {
            throw new IllegalArgumentException("Illegal argument, w null");
        }
        for (Integer x : v) {
            if (x == null || x < 0 || x >= G.V()) {
                throw new IllegalArgumentException("Illegal argument, v outside range");
            }
        }

        for (Integer x : w) {
            if (x == null || x < 0 || x >= G.V()) {
                throw new IllegalArgumentException("Illegal argument, w outside range");
            }
        }

        return BFSDeluxe(v, w)[1];

    }

    private int[] BFSDeluxe(Iterable<Integer> v, Iterable<Integer> w) {

        // Alternating BFS v, w
        int[] result = new int[2];
        result[0] = -1;
        result[1] = -1;

        if (!usedIntegers.isEmpty()) {
            for (int x : usedIntegers) {
                markedV[x] = false;
                lengthsV[x] = 0;
                markedW[x] = false;
                lengthsW[x] = 0;
            }
        }

        usedIntegers.clear();

        Queue<Integer> queueV = new Queue<Integer>();
//        boolean[] markedV = new boolean[G.V()];
//        int[] lengthsV = new int[G.V()];

        Queue<Integer> queueW = new Queue<Integer>();
//        boolean[] markedW = new boolean[G.V()];
//        int[] lengthsW = new int[G.V()];

        for (int currentV : v) {
            usedIntegers.add(currentV);
            markedV[currentV] = true; // Mark the source
            lengthsV[currentV] = 0;
            queueV.enqueue(currentV);
        }
        for (int currentW : w) {
            usedIntegers.add(currentW);
            markedW[currentW] = true; // Mark the source
            lengthsW[currentW] = 0;
            queueW.enqueue(currentW);
        }

        while (!queueV.isEmpty() || !queueW.isEmpty()) {

            // V queue actions

            if (!queueV.isEmpty()) {
                int next = queueV.dequeue(); // Remove next vertex from the queue.

                if (markedW[next] && (lengthsV[next] + lengthsW[next] < result[0] || result[0] == -1)) {
                    // shortest ancestor found - it is next
                    result[0] = lengthsV[next] + lengthsW[next];
                    result[1] = next;

                }

                for (int nextAdjacent : G.adj(next))
                    if (!markedV[nextAdjacent]) // For every unmarked adjacent vertex,
                    {
                        usedIntegers.add(nextAdjacent);
                        markedV[nextAdjacent] = true; // mark it because path is known,
                        lengthsV[nextAdjacent] = lengthsV[next] + 1;
                        queueV.enqueue(nextAdjacent); // and add it to the queue.


                        if (markedW[nextAdjacent] && (lengthsV[nextAdjacent] + lengthsW[nextAdjacent] < result[0] || result[0] == -1)) {
                            // shortest ancestor found - it is nextAdjacent
                            result[0] = lengthsV[nextAdjacent] + lengthsW[nextAdjacent];
                            result[1] = nextAdjacent;

                        }
                    }
            }


            // W queue actions
            if (!queueW.isEmpty()) {
                int next = queueW.dequeue(); // Remove next vertex from the queue.

                if (markedV[next] && (lengthsV[next] + lengthsW[next] < result[0] || result[0] == -1)) {
                    // shortest ancestor found - it is next
                    result[0] = lengthsV[next] + lengthsW[next];
                    result[1] = next;
                }

                for (int nextAdjacent : G.adj(next))
                    if (!markedW[nextAdjacent]) // For every unmarked adjacent vertex,
                    {
                        usedIntegers.add(nextAdjacent);
                        markedW[nextAdjacent] = true; // mark it because path is known,
                        lengthsW[nextAdjacent] = lengthsW[next] + 1;
                        queueW.enqueue(nextAdjacent); // and add it to the queue.

                        if (markedV[nextAdjacent] && (lengthsV[nextAdjacent] + lengthsW[nextAdjacent] < result[0] || result[0] == -1)) {

                            // shortest ancestor found - it is nextAdjacent
                            result[0] = lengthsV[nextAdjacent] + lengthsW[nextAdjacent];
                            result[1] = nextAdjacent;

                        }
                    }
            }


        }

        return result;

    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        StdOut.print(G.toString());
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);

            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
            StdOut.println();
        }
    }
}
