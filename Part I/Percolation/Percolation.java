import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	
	// variables...
	private boolean[][] sites;          // false for closed, true for open.
	private WeightedQuickUnionUF wq;	// connected to both virtual top and bottom. for percolates(). 
	private WeightedQuickUnionUF wq_2;  // connected to only virtual top. for isFull(). -----> solves backwash problem.
	private int opens;
	private int size;
	
	// methods...
	public Percolation(int n)
	{
		if (n <= 0) {
			throw new IllegalArgumentException("Initialization failed : n is less than 1.");
		}
		
		size = n;
		sites = new boolean[n][n];		// JAVA : automatically initialized to zero. ******************

		wq = new WeightedQuickUnionUF(n * n + 2);
		wq_2 = new WeightedQuickUnionUF(n * n + 2);
		
		opens = 0;
	}
	
	public void open(int row, int col)
	{
		if ((row - 1 < 0) || (row - 1 >= size) || (col - 1 < 0) || (col - 1 >= size)) {
			throw new IllegalArgumentException("open() failed : out of bound");
		}
		
		if (sites[row - 1][col - 1] == true)
			return;
		
		sites[row - 1][col - 1] = true;
		
		if (row - 1 == 0) {
			wq.union((row - 1) * size + (col - 1),  size * size);
			wq_2.union((row - 1) * size + (col - 1),  size * size);
		}
		if (row - 1 == size - 1) {
			wq.union((row - 1) * size + (col - 1),  size * size + 1);
			// wq_2 is connected to virtual top only, not virtual bottom.
		}
		
		
		if (row - 1 - 1 >= 0 && sites[row - 1 - 1][col - 1] == true) {
			wq.union((row - 1 - 1) * size + col - 1, (row - 1) * size + (col - 1));
			wq_2.union((row - 1 - 1) * size + col - 1, (row - 1) * size + (col - 1));
		}
		if (col - 1 - 1 >= 0 && sites[row - 1][col - 1 - 1] == true) {
			wq.union((row - 1) * size + (col - 1 - 1), (row - 1) * size + (col - 1));
			wq_2.union((row - 1) * size + (col - 1 - 1), (row - 1) * size + (col - 1));
		}
		if (row + 1 - 1 < size && sites[row + 1 - 1][col - 1] == true) {
			wq.union((row + 1 - 1) * size + col - 1, (row - 1) * size + (col - 1));
			wq_2.union((row + 1 - 1) * size + col - 1, (row - 1) * size + (col - 1));
		}
		if (col + 1 - 1 < size && sites[row - 1][col + 1 - 1] == true) {
			wq.union((row - 1) * size + (col + 1 - 1), (row - 1) * size + (col - 1));
			wq_2.union((row - 1) * size + (col + 1 - 1), (row - 1) * size + (col - 1));
		}
		
		opens++;
	}
	
	public boolean isOpen(int row, int col)
	{
		if (row - 1 < 0 || row - 1 >= size || col - 1 < 0 || col - 1 >= size) {
			throw new IllegalArgumentException("isOpen() failed : out of bound");
		}
		return sites[row - 1][col - 1];
	}
	
	public boolean isFull(int row, int col)
	{
		if (row - 1 < 0 || row - 1 >= size || col - 1 < 0 || col - 1 >= size) {
			throw new IllegalArgumentException("isFull() failed : out of bound");
		}
		
		return wq_2.connected((row - 1) * size + (col - 1), size * size); // use wq_2 for isFull(), not wq. this prevents backwash problem.
		
	}
	
	public int numberOfOpenSites()
	{
		return opens;
	}
	
	public boolean percolates()
	{
		return wq.connected(size * size, size * size + 1); // use wq for percolates(). this prevents backwash problem.
	}
	
	public static void main(String[] args) {

        int n = StdIn.readInt();
		Percolation nina = new Percolation(n);
		
		while (!StdIn.isEmpty()) {

            int p = StdIn.readInt();
            int q = StdIn.readInt();
            nina.open(p,  q);
            
		}
		
		System.out.println(nina.percolates());
	}
	
	
}