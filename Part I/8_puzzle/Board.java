import java.util.Stack;
import java.lang.StringBuilder;

public class Board {
	private int[][] board;
	private int dimension;
	private Stack<Board> neighborsArr;
	private int zero_row = -1;	 // position of zero(row, col)
	private int zero_col = -1;
	
	public Board(int [][] blocks) {
		dimension = blocks.length; // num of rows (n-by-n)
		board = new int[dimension][dimension];
	
		// copies blocks to board.
		copyBoard(blocks, board, dimension);
		// initialize neighboring board.
		find_zero();
	}
	
	private void find_zero() {
		// find coordinate of zero.
		outerloop:
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (board[i][j] == 0) {
					zero_row = i;
					zero_col = j;
					break outerloop;		
				}
			}
		}
		if (zero_row == -1 || zero_col == -1)
			throw new java.lang.IllegalArgumentException();
	}
	
	private void copyBoard(int[][] from, int[][] to, int dimension) {
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				to[i][j] = from[i][j];
			}
		}
	}
	
	public int dimension() {
		return dimension;
	}
	
	public int hamming() {
		
		int count = 0; // number of blocks out of place
		
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (board[i][j] == 0)
					continue;
				if (board[i][j] != i * dimension + j + 1)
					count++;
			}
		}		
		return count;		
	}
	
	public int manhattan() {
		
		int sum = 0; // sum of Manhattan distances between blocks and goal
		
		outerloop:
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (i == dimension - 1 && j == dimension - 1)
					break outerloop;
				if (board[i][j] != i * dimension + j + 1) {
					int temp = findManDist(i, j);
					sum += temp;
				}
			}
		}
		return sum;
		
	}
	
	private int findManDist(int row, int col) {
		
		int resultRow = 0, resultCol = 0;
		outerloop:
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (board[i][j] == row * dimension + col + 1) {
					resultRow = i; resultCol = j;
					break outerloop;
				}
			}
		}
		
		return (Math.abs(row - resultRow) + Math.abs(col - resultCol));
	}
	
	// precompute...?
	public boolean isGoal() {
		
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (i == dimension - 1 && j == dimension - 1)
					return true;
				if (board[i][j] != i * dimension + j + 1)
					return false;
			}
		}
		// not expected
		return false;
	}
	
	public Board twin() {
		int[][] tempBoard = new int[dimension][dimension];
		
		// copies board to tempBoard
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				tempBoard[i][j] = board[i][j];
			}
		}
		
		// change easy-to-pick two element([0][0], [0][1]). (if either of two is zero, do it in an alternative way.)
		if (tempBoard[0][0] == 0) exch(tempBoard, 0, 1, 1, 0);
		
		else if (tempBoard[0][1] == 0) exch(tempBoard, 0, 0, 1, 0);
	
		else exch(tempBoard, 0, 0, 0, 1);
		
		return new Board(tempBoard);
	}
	
	private void exch(int[][] a, int fromRow, int fromCol, int toRow, int toCol) {
		int t = a[fromRow][fromCol];
		a[fromRow][fromCol] = a[toRow][toCol];
		a[toRow][toCol] = t;
	}
	
	public boolean equals(Object y) {
		if (y == this) return true;
		
		if (y == null) return false;
		
		if (y.getClass() != this.getClass())
			return false;
		
		Board that = (Board) y;
		
		if (this.dimension != that.dimension)
			return false;
		
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (this.board[i][j] != that.board[i][j])
					return false;
			}
		}
		
		return true;
	}
	
	public Iterable<Board> neighbors() { decideNeighbors(); return neighborsArr; }
	/*
	private class neighborIterable implements Iterable<Board> {
		public Iterator<Board> iterator() { return new neighborIterator(); }
		
		public class neighborIterator implements Iterator<Board> {
			private int current = 0;
			public boolean hasNext() { return current != neighborsArr.length; }
			public Board next() { return neighborsArr[current++]; }
		}
	}
	*/
	private void decideNeighbors() {
		
		//int numOfNeighbors = 0;		
		int n = 0; // indicates index of a new neighbor.
		/*
		// initial scan to decide size of Board[] neighbors.
		if (zero_row - 1 >= 0) numOfNeighbors++;
		if (zero_row + 1 < dimension) numOfNeighbors++;
		if (zero_col - 1 >= 0) numOfNeighbors++;
		if (zero_col + 1 < dimension) numOfNeighbors++;
		*/
		neighborsArr = new Stack<Board>();
		
		// fill Board[].
		if (zero_row - 1 >= 0) 
			makeNewNeighbor(zero_row, zero_col, zero_row - 1, zero_col, n++);
		
		if (zero_row + 1 < dimension) 
			makeNewNeighbor(zero_row, zero_col, zero_row + 1, zero_col, n++);
		
		if (zero_col - 1 >= 0) 
			makeNewNeighbor(zero_row, zero_col, zero_row, zero_col - 1, n++);
		
		if (zero_col + 1 < dimension) 
			makeNewNeighbor(zero_row, zero_col, zero_row, zero_col + 1, n++);
		
	}
	
	private void makeNewNeighbor(int fromRow, int fromCol, int toRow, int toCol, int entryIndex) {
		int[][] temp = new int[dimension][dimension];
		copyBoard(board, temp, dimension);
		exch(temp, fromRow, fromCol, toRow, toCol);
		neighborsArr.push(new Board(temp));
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder(dimension + "\n");
		String result;
		
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				sb.append(board[i][j] + " ");
				if (j == dimension - 1)
					sb.append('\n');
			}
		}
		result = sb.toString();
		
		return result;
	}
	
}
