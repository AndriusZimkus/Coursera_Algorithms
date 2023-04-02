import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private int numberOfMoves = 0;
    private Stack<Board> solution = new Stack<Board>();

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Solver argument is null");
        }

        MinPQ<SearchNode> priorityQueue = new MinPQ<SearchNode>(manhattanPriorityOrder());
        MinPQ<SearchNode> priorityQueueTwin = new MinPQ<SearchNode>(manhattanPriorityOrder());


        SearchNode initialSearchNode = new SearchNode(initial, 0, null);
        SearchNode initialSearchNodeTwin = new SearchNode(initial.twin(), 0, null);

        priorityQueue.insert(initialSearchNode);
        priorityQueueTwin.insert(initialSearchNodeTwin);
        int numberOfMovesTwin;

        while (true) {

            SearchNode currentNode = priorityQueue.delMin();
            SearchNode currentNodeTwin = priorityQueueTwin.delMin();


            numberOfMoves = currentNode.numberOfMoves;
            numberOfMovesTwin = currentNodeTwin.numberOfMoves;

            if (currentNode.nodeBoard.isGoal()) {
                // Construct solution
                solution.push(currentNode.nodeBoard);
                while (currentNode.previousSearchNode != null) {
                    currentNode = currentNode.previousSearchNode;
                    solution.push(currentNode.nodeBoard);
                }
                break;
            } else if (currentNodeTwin.nodeBoard.isGoal()) {
                solution = null;
                break;
            }

            numberOfMoves++;
            numberOfMovesTwin++;
            for (Board x : currentNode.nodeBoard.neighbors()) {

                SearchNode newNode = new SearchNode(x, numberOfMoves, currentNode);
                if (currentNode.previousSearchNode == null || !x.equals(currentNode.previousSearchNode.nodeBoard)) {
                    priorityQueue.insert(newNode);
                }

            }
            for (Board x : currentNodeTwin.nodeBoard.neighbors()) {

                SearchNode newNode = new SearchNode(x, numberOfMovesTwin, currentNodeTwin);

                if (currentNodeTwin.previousSearchNode == null || !x.equals(currentNodeTwin.previousSearchNode.nodeBoard)) {
                    priorityQueueTwin.insert(newNode);
                }


            }

        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.solution() != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) {
            return -1;
        } else return numberOfMoves;
    }

    private class SearchNode {
        Board nodeBoard;
        int numberOfMoves;
        SearchNode previousSearchNode;
        int manhattanPriority;

        public SearchNode(Board board, int numberOfMoves, SearchNode previousSearchNode) {
            this.nodeBoard = board;
            this.numberOfMoves = numberOfMoves;
            this.previousSearchNode = previousSearchNode;
            this.manhattanPriority = this.manhattanPriority();
        }

        public int hammingPriority() {
            return this.nodeBoard.hamming() + this.numberOfMoves;
        }

        public int manhattanPriority() {
            return this.nodeBoard.manhattan() + this.numberOfMoves;
        }
    }

    private Comparator<SearchNode> manhattanPriorityOrder() {
        return new ManhattanPriorityOrder();
    }

    private class ManhattanPriorityOrder implements Comparator<SearchNode> {
        public int compare(SearchNode o1, SearchNode o2) {
//            int firstManhattan = o1.manhattanPriority();
//            int secondManhattan = o2.manhattanPriority();
            int firstManhattan = o1.manhattanPriority;
            int secondManhattan = o2.manhattanPriority;
//            StdOut.println("firstManhattan = " + firstManhattan + " secondManhattan = " + secondManhattan);
            if (firstManhattan < secondManhattan) {
                return -1;
            } else if (firstManhattan > secondManhattan) {
                return 1;
            } else {
                return 0;
            }

        }
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return this.solution;
    }

    // test client (see below)
    public static void main(String[] args) {
//        create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();

//        int n = 3;
//        int[][] tiles = new int[n][n];
//        tiles[0][0] = 8;
//        tiles[0][1] = 1;
//        tiles[0][2] = 3;
//        tiles[1][0] = 4;
//        tiles[1][1] = 0;
//        tiles[1][2] = 2;
//        tiles[2][0] = 7;
//        tiles[2][1] = 6;
//        tiles[2][2] = 5;


        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

//        print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }

}
