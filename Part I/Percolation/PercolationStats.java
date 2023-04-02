import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double[] trialResults;
    private final int trials;
    private final double confidenceConstant = 1.96;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("");
        }

        Percolation percObject;
        int currentRow;
        int currentColumn;
        this.trials = trials;
        trialResults = new double[trials];

        for (int i = 0; i < trials; i++) {
            percObject = new Percolation(n);
            while (!percObject.percolates()) {

                int randomSite = StdRandom.uniform(1, n * n + 1);

                currentRow = (randomSite - 1) / n + 1;
                if (randomSite % n == 0) {
                    currentColumn = n;
                } else {
                    currentColumn = randomSite % n;
                }
                percObject.open(currentRow, currentColumn);
            }
            trialResults[i] = (double) percObject.numberOfOpenSites() / (n * n);
        }

    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(trialResults);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(trialResults);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - confidenceConstant * stddev() / Math.sqrt(this.trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (confidenceConstant * stddev()) / Math.sqrt(this.trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats percObject = new PercolationStats(n, trials);

        System.out.println("mean = " + percObject.mean());
        System.out.println("stddev = " + percObject.stddev());
        System.out.println("95% confidence interval = [" + percObject.confidenceLo() + " , " + percObject.confidenceHi() + "]");
    }

}
