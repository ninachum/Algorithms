import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	
	private double[] results;
	private double mean;
	private double stddev;
	
	public PercolationStats(int n, int trials) {
		
		if (n <= 0 || trials <= 0 ) {
			throw new IllegalArgumentException("n and trials must be greater than 0.");
		}
		results = new double[trials];
		
		for (int i = 0; i < trials; i++) {
			Percolation sample = new Percolation(n);
			while (!sample.percolates()) {
				sample.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1); // plus 1 each, because index base requirement is 1.
			}
			results[i] = ((double) sample.numberOfOpenSites()) / ((double) (n * n));
		}
		
		mean = mean();
		stddev = stddev();
	}
	
	public double mean() {
		return StdStats.mean(results);
	}
	
	public double stddev() {
		return StdStats.stddev(results);
	}
	
	public double confidenceLo() {
		return mean - (1.96 * stddev) / (Math.sqrt(results.length));
	}
	
	public double confidenceHi() {
		return mean + (1.96 * stddev) / (Math.sqrt(results.length));
	}
	
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage : java PercolationStats size trials");
			return;
		}
		int size = Integer.parseInt(args[0]), trials = Integer.parseInt(args[1]);

		PercolationStats nina = new PercolationStats(size, trials);
		System.out.println("mean = " + nina.mean());
		System.out.println("stddev = " + nina.stddev());
		System.out.println("95% confidence interval = [" + nina.confidenceLo() + ", " + nina.confidenceHi() + "]");
		
	}
}

