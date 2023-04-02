import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Board {

    private int n;
    private int[][] tiles;


    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        this.tiles = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }


    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int hammingDistance = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int currentValue = tiles[i][j];
                int correctValue = i * n + (j + 1);

                if (currentValue != correctValue && currentValue != 0) {
                    hammingDistance++;
                }
            }
        }
        return hammingDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattanDistance = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int currentRow = i;
                int currentColumn = j;
                int currentValue = tiles[i][j];

                int correctRow = currentValue / n;
                if (currentValue % n == 0) {
                    correctRow = currentValue / n - 1;
                }

                int correctColumn = currentValue % n - 1;
                if (correctColumn == -1) {
                    correctColumn = n - 1;
                }

                int columnDistance = (correctColumn - currentColumn);
                int rowDistance = (correctRow - currentRow);
                if (columnDistance < 0) {
                    columnDistance *= -1;
                }
                if (rowDistance < 0) {
                    rowDistance *= -1;
                }

//                StdOut.println("currentValue = " + currentValue);
//                StdOut.println("currentPosition = " + currentRow + " " + currentColumn);
//                StdOut.println("correctPosition = " + correctRow + " " + correctColumn);

                if (currentValue != 0) {
                    manhattanDistance += columnDistance + rowDistance;
                }

            }
        }
        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;

        if (this.hamming() != that.hamming()) return false;
        if (this.manhattan() != that.manhattan()) return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;


//        if (this.tiles != that.manhattan()) return false;

    }

    //     all neighboring boards
    public Iterable<Board> neighbors() {

        ArrayList<Board> neighbors = new ArrayList<Board>();
        int currentValue = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                currentValue = tiles[i][j];

                int[][] newTiles = new int[n][n];

                for (int x = 0; x < n; x++) {
                    for (int y = 0; y < n; y++) {
                        newTiles[x][y] = tiles[x][y];
                    }
                }

//                StdOut.println("currentValue = " + currentValue);
                if (currentValue == 0) {
//                    StdOut.println("i = " + i);
//                    StdOut.println("j = " + j);

                    if (i > 0) {
                        // Top exchange
                        int exch = newTiles[i][j];
                        newTiles[i][j] = newTiles[i - 1][j];
                        newTiles[i - 1][j] = exch;
                        Board exchangeBoard = new Board(newTiles);
                        neighbors.add(exchangeBoard);
//                        StdOut.println(ExchangeBoard);


                        for (int x = 0; x < n; x++) {
                            for (int y = 0; y < n; y++) {
                                newTiles[x][y] = tiles[x][y];
                            }
                        }

                    }

                    if (j > 0) {
                        // Left exchange
                        int exch = newTiles[i][j];
                        newTiles[i][j] = newTiles[i][j - 1];
                        newTiles[i][j - 1] = exch;
                        Board exchangeBoard = new Board(newTiles);
                        neighbors.add(exchangeBoard);
//                        StdOut.println(ExchangeBoard);
                        for (int x = 0; x < n; x++) {
                            for (int y = 0; y < n; y++) {
                                newTiles[x][y] = tiles[x][y];
                            }
                        }
                    }

                    if (i < n - 1) {
                        // Bottom exchange
                        int exch = newTiles[i][j];
                        newTiles[i][j] = newTiles[i + 1][j];
                        newTiles[i + 1][j] = exch;
                        Board exchangeBoard = new Board(newTiles);
                        neighbors.add(exchangeBoard);
//                        StdOut.println(ExchangeBoard);
                        for (int x = 0; x < n; x++) {
                            for (int y = 0; y < n; y++) {
                                newTiles[x][y] = tiles[x][y];
                            }
                        }
                    }
                    if (j < n - 1) {
                        // Right exchange
                        int exch = newTiles[i][j];
                        newTiles[i][j] = newTiles[i][j + 1];
                        newTiles[i][j + 1] = exch;
                        Board exchangeBoard = new Board(newTiles);
                        neighbors.add(exchangeBoard);
//                        StdOut.println(ExchangeBoard);
                        for (int x = 0; x < n; x++) {
                            for (int y = 0; y < n; y++) {
                                newTiles[x][y] = tiles[x][y];
                            }
                        }
                    }

                    break;
                }
            }
            if (currentValue == 0) {
                break;
            }
        }


        return neighbors;

    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {

        int[][] twinTiles = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                twinTiles[i][j] = this.tiles[i][j];
            }
        }
        int currentValue;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                currentValue = this.tiles[i][j];

                if (currentValue != 0) {

                    if (j < n - 1 && twinTiles[i][j + 1] != 0) {
                        // Right exchange
                        int exch = twinTiles[i][j];
                        twinTiles[i][j] = twinTiles[i][j + 1];
                        twinTiles[i][j + 1] = exch;
                        Board twinBoard = new Board(twinTiles);
                        return twinBoard;
                    }
                    if (i < n - 1 && twinTiles[i + 1][j] != 0) {
                        // Bottom exchange
                        int exch = twinTiles[i][j];
                        twinTiles[i][j] = twinTiles[i + 1][j];
                        twinTiles[i + 1][j] = exch;
                        Board twinBoard = new Board(twinTiles);
                        return twinBoard;
                    }

                    break;
                }
            }
        }

        return null;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        for (Board x : initial.neighbors()) {
            StdOut.println(x.toString());
        }
//        StdOut.println(initial.toString());
//        StdOut.println(initial.hamming());
//        StdOut.println(initial.manhattan());
//        int n = 2;
//        int[][] tiles = new int[n][n];
//        tiles[0][0] = 0;
//        tiles[0][1] = 1;
//        tiles[1][0] = 2;
//        tiles[1][1] = 3;
//
//        Board test = new Board(tiles);
//
//        StdOut.println(test.twin().toString());


    }

}
