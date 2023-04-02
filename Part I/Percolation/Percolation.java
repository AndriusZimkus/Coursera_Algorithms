import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {


    private boolean[] percolationArrayIsOpen;
    private int openSites = 0;
    private final WeightedQuickUnionUF matrixUF;
    private final WeightedQuickUnionUF matrixUF_NoBottomNode;
    private final int dimensions;


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("");
        }

        dimensions = n;
        percolationArrayIsOpen = new boolean[n * n + 2];

        for (int i = 0; i < n * n + 2; i++) {
            percolationArrayIsOpen[i] = false;
        }

        matrixUF = new WeightedQuickUnionUF(n * n + 2);
        matrixUF_NoBottomNode = new WeightedQuickUnionUF(n * n + 1);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isIndexValid(row, col)) {
            throw new IllegalArgumentException("");
        }

        if (!isOpen(row, col)) {
            percolationArrayIsOpen[getIndex(row, col)] = true;
            openSites += 1;

            if (row == 1) {
                matrixUF.union(0, getIndex(row, col));
                matrixUF_NoBottomNode.union(0, getIndex(row, col));
            }
            if (row == dimensions) {
                matrixUF.union(getIndex(row, col), dimensions * dimensions + 1);
            }

            // left
            if (col > 1 && isOpen(row, col - 1)) {
                matrixUF.union(getIndex(row, col), getIndex(row, col - 1));
                matrixUF_NoBottomNode.union(getIndex(row, col), getIndex(row, col - 1));
            }
            if (row > 1 && isOpen(row - 1, col)) {
                // up
                matrixUF.union(getIndex(row, col), getIndex(row - 1, col));
                matrixUF_NoBottomNode.union(getIndex(row, col), getIndex(row - 1, col));
            }
            if (col < dimensions && isOpen(row, col + 1)) {
                // right
                matrixUF.union(getIndex(row, col), getIndex(row, col + 1));
                matrixUF_NoBottomNode.union(getIndex(row, col), getIndex(row, col + 1));
            }
            if (row < dimensions && isOpen(row + 1, col)) {
                // down
                matrixUF.union(getIndex(row, col), getIndex(row + 1, col));
                matrixUF_NoBottomNode.union(getIndex(row, col), getIndex(row + 1, col));
            }

        }


    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isIndexValid(row, col)) {
            throw new IllegalArgumentException("");
        }
        return percolationArrayIsOpen[getIndex(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isIndexValid(row, col)) {
            throw new IllegalArgumentException("");
        }

        return isOpen(row, col) && matrixUF.find(getIndex(row, col)) == matrixUF.find(0) && matrixUF_NoBottomNode.find(getIndex(row, col)) == matrixUF_NoBottomNode.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }


    // does the system percolate?
    public boolean percolates() {
        return matrixUF.find(0) == matrixUF.find(dimensions * dimensions + 1);
    }

    private int getIndex(int row, int col) {
        return (row - 1) * dimensions + col;
    }

    private boolean isIndexValid(int row, int col) {
        if (row < 1 || col < 1 || row > dimensions || col > dimensions) {
            return false;
        }

        int indexForValidation = getIndex(row, col);
        return indexForValidation >= 0 && (indexForValidation <= dimensions * dimensions + 1);
    }

    // test client (optional)
    public static void main(String[] args) {

    }


}
