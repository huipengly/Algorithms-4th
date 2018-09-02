/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    public PercolationStats(int n,
                            int trials)    // perform trials independent experiments on an n-by-n grid
    {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        double[] proportionOfOpenSites = new double[trials];

        for (int i = 0; i != trials; ++i) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                if (n != 1)
                    percolation.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
                else
                    percolation.open(1, 1);
            }
            proportionOfOpenSites[i] = (double) percolation.numberOfOpenSites() / (n * n);
        }

        mean = StdStats.mean(proportionOfOpenSites);
        stddev = StdStats.stddev(proportionOfOpenSites);

        /* 计算置信区间 */
        double temp = 1.96 * stddev / java.lang.Math.sqrt(trials);
        confidenceLo = mean - temp;
        confidenceHi = mean + temp;
    }

    public double mean()                          // sample mean of percolation threshold
    {
        return mean;
    }

    public double stddev()                        // sample standard deviation of percolation threshold
    {
        return stddev;
    }

    public double confidenceLo()                  // low  endpoint of 95% confidence interval
    {
        return confidenceLo;
    }

    public double confidenceHi()                  // high endpoint of 95% confidence interval
    {
        return confidenceHi;
    }

    public static void main(String[] args)        // test client (described below)
    {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);
        StdOut.printf("mean                    = %f\n", percolationStats.mean());
        StdOut.printf("stddev                  = %f\n", percolationStats.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]", percolationStats.confidenceLo(),
                      percolationStats.confidenceHi());
    }
}
