import edu.princeton.cs.algs4.MinPQ;
import java.util.Collections;
import java.util.Stack;

public class Solver {
	private int leastMoves;
	private Stack<Board> boardStack = new Stack<Board>();
	private boolean solvableFlag;
	private class Node implements Comparable<Node> {
		
		private int moves;
		private int manhattanDist;
		private int priority;
		private Node prev;
		private Board curr;
		
		public Node(int nMoves, Node nPrev, Board nCurr) {
			moves = nMoves;
			curr = nCurr;
			prev = nPrev;
			manhattanDist = curr.manhattan();
			priority = moves + manhattanDist;
		}
		
		public int compareTo(Node that) {
			if (this.priority > that.priority) return 1;
			if (this.priority == that.priority) return 0;
			return -1;
		}
		
	}
	
	public Solver (Board Initial) {
		MinPQ<Node> queue = new MinPQ<Node>();
		MinPQ<Node> oppositeQueue = new MinPQ<Node>();
		
		if (Initial == null)
			throw new java.lang.IllegalArgumentException("Constructor argument is null.");
		
		Node firstNode = new Node(0, null, Initial);
		Node oppositeFirstNode = new Node(0, null, Initial.twin());
		queue.insert(firstNode);
		oppositeQueue.insert(oppositeFirstNode);
		
		while (queue.min().manhattanDist != 0 && oppositeQueue.min().manhattanDist != 0) {
			Node temp = queue.delMin();
			Node oppositeTemp = oppositeQueue.delMin();
		
			doTheWorks(temp, queue);
			doTheWorks(oppositeTemp, oppositeQueue);
		}
		
		if (queue.min().manhattanDist == 0)
			solvableFlag = true;
		else
			solvableFlag = false;
		decideSolution(queue);
	}
	
	private void doTheWorks(Node node, MinPQ<Node> myQueue) {
		if (node.prev != null) {
			for (Board nana : node.curr.neighbors()) {
				if (!nana.equals(node.prev.curr)) {
					Node nunu = new Node(node.moves + 1, node, nana);
					myQueue.insert(nunu);
				}
			}
		}
		else {
			for (Board nana : node.curr.neighbors()) {
				Node nunu = new Node(node.moves + 1, node, nana);
				myQueue.insert(nunu);
			}
		}
	}
	
	public boolean isSolvable() {
		return solvableFlag;
	}
	
	public int moves() {
		if (!isSolvable())
			return -1;
		
		return leastMoves;
	}
	
	public Iterable<Board> solution() {
		if (!isSolvable())  return null;
		return boardStack;
	}
	
	private void decideSolution(MinPQ<Node> myQueue) {
		Node temp = myQueue.min();
		int result = -1; // NOT 0, start from zero, when loop begins.	
		while (temp != null) {
			boardStack.push(temp.curr);
			result++;
			temp = temp.prev;
		}
		Collections.reverse(boardStack);
		leastMoves = result;
	}
	
	
	
	/*
	private class solverIterable implements Iterable<Board> {
		public Iterator<Board> iterator() { return new solverIterator(); }
		
		private class solverIterator implements Iterator<Board> {
			private Node last = queue.min();
			public boolean hasNext() { return last != null; }
			public Board next() { Board temp = last.curr; last = last.prev; return temp; }
		}
	}
	*/
	/*
	public static void main(String[] args) {

	    // create initial board from file
	    In in = new In(args[0]);
	    int n = in.readInt();
	    int[][] blocks = new int[n][n];
	    for (int i = 0; i < n; i++)
	        for (int j = 0; j < n; j++)
	            blocks[i][j] = in.readInt();
	    Board initial = new Board(blocks);

	    // solve the puzzle
	    Solver solver = new Solver(initial);

	    // print solution to standard output
	    if (!solver.isSolvable())
	        StdOut.println("No solution possible");
	    else {
	        StdOut.println("Minimum number of moves = " + solver.moves());
	        for (Board board : solver.solution())
	            StdOut.println(board);
	    }
	}
	*/
	
}
