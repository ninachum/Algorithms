
public class BruteCollinearPoints {
	private int numSegs;
	private final myStack segmentsArr;
	
	public BruteCollinearPoints(Point[] points) {
		if (points == null)
			throw new java.lang.IllegalArgumentException("creating BruteCollinearPoints failed : null argument");
		
		for (int i =0; i < points.length; i++) {
			if (points[i] == null)
				throw new java.lang.IllegalArgumentException("null element detected");
		}
		
		if (isRepeated(points))
			throw new java.lang.IllegalArgumentException("repeated element detected");
		
		
		
		numSegs = 0;
		segmentsArr = new myStack();
		
		// for all 4-combinations, assuming there's no 5 or more collinear points.
		for (int i = 0; i < points.length - 3; i++) {
			for (int j = i + 1; j < points.length - 2; j++) {
				for (int k = j + 1; k < points.length - 1; k++) {
					for (int l = k + 1; l < points.length; l++) {
						if (isCollinear(points[i], points[j], points[k], points[l])) {
							segmentsArr.push(new LineSegment(segmentTopPoint(points[i], points[j], points[k], points[l]),
												 			 segmentBottomPoint(points[i], points[j], points[k], points[l])));
							numSegs++;
						}
					}
				}
			}
		}
		LineSegment[] tempStack = new LineSegment[numSegs];
		for (int i = 0; i < numSegs; i++) {
			tempStack[i] = segmentsArr.stack[i];
		}
		segmentsArr.stack = tempStack;
		
	}
	
	public int numberOfSegments() {
		return numSegs;
	}
	
	public LineSegment[] segments() {
		LineSegment[] tempForReturn = new LineSegment[numSegs];
		for (int i = 0; i < numSegs; i++) {
			tempForReturn[i] = segmentsArr.stack[i];
		}
		return tempForReturn;
	}
	
	private boolean isCollinear(Point p, Point q, Point r, Point s) {
		double slopeTo_q = p.slopeTo(q);
		double slopeTo_r = p.slopeTo(r);
		double slopeTo_s = p.slopeTo(s);
		
		if (slopeTo_q == slopeTo_r && slopeTo_r == slopeTo_s)
			return true;
		else return false;
		/*
		myStack<Double> tempStack = new myStack<Double>();c
		int numOfNonNI = 0;
		
		if (slopeTo_q != Double.NEGATIVE_INFINITY) {S
			tempStack.push(slopeTo_q);
			numOfNonNI++;
		}
		if (slopeTo_r != Double.NEGATIVE_INFINITY) {
			tempStack.push(slopeTo_r);
			numOfNonNI++;
		}
		if (slopeTo_s != Double.NEGATIVE_INFINITY) {
			tempStack.push(slopeTo_s);
			numOfNonNI++;
		}
		
		// tests three elements are all equal (skip NEGATIVE_INFINITY element)
		if (numOfNonNI == 3) {
			if (slopeTo_q == slopeTo_r && slopeTo_r == slopeTo_s)
				return true;
			else return false;
		}
		if (numOfNonNI == 2) {
			if (tempStack.pop() == tempStack.pop())
				return true;
			else return false;
		}
		if (numOfNonNI == 1) {
			return true;
		}
		
		return true;
		*/
	}
		
	// stack that only grows
	private final class myStack {

		private int top;
		private LineSegment[] stack;
		
		public myStack() {
			top = 0;
			stack = new LineSegment[0];
		}
		
		public void push(LineSegment item) {
			if (item == null)
				throw new java.lang.IllegalArgumentException("push failed : argument is null.");
			
			if (isEmpty())
				stack = new LineSegment[1];
			
			if (isFull())
				resize(stack.length * 2);
			
			stack[top++] = item;
		}
		
		public LineSegment pop() {
			if (isEmpty())
				throw new java.util.NoSuchElementException("pop : empty stack.");
			
			LineSegment result = stack[--top];
			stack[top] = null;
			return result;
		}
		
		public boolean isFull() {
			return top == stack.length;
		}
		
		public boolean isEmpty() {
			return top == 0;
		}
		
		private void resize(int stackSize) {
			 LineSegment[] newStack = new LineSegment[stackSize];
			
			for (int i = 0; i < stack.length; i++) {
				newStack[i] = stack[i];
			}
			
			stack = newStack;
		}
	}
	// needs an helper function that returns two end points.
	private Point segmentTopPoint(Point p, Point q, Point r, Point s) {
		Point winner1 = p;
		Point winner2 = r;
		
		if (p.compareTo(q) >= 0)
			winner1 = p;
		else
			winner1 = q;
		
		if (r.compareTo(s) >= 0)
			winner2 = r;
		else
			winner2 = s;
		
		if (winner1.compareTo(winner2) >= 0)
			return winner1;
		else
			return winner2;
	}
	
	private Point segmentBottomPoint(Point p, Point q, Point r, Point s) {
		Point loser1 = p;
		Point loser2 = r;
		
		if (p.compareTo(q) >= 0)
			loser1 = q;
		else
			loser1 = p;
		
		if (r.compareTo(s) >= 0)
			loser2 = s;
		else
			loser2 = r;
		
		if (loser1.compareTo(loser2) >= 0)
			return loser2;
		else
			return loser1;
	}
	
	private boolean isRepeated(Point[] points) {
		for (int i = 0; i < points.length - 1; i++) {
			for (int j = i + 1; j < points.length; j++) {
				if (points[i].slopeTo(points[j]) == Double.NEGATIVE_INFINITY)
					return true;
			}
		}
		return false;
	}
	/*
	public static void main(String[] args) {

	    // read the n points from a file
	    In in = new In(args[0]);
	    int n = in.readInt();
	    Point[] points = new Point[n];
	    for (int i = 0; i < n; i++) {
	        int x = in.readInt();
	        int y = in.readInt();
	        points[i] = new Point(x, y);
	    }

	    // draw the points
	    StdDraw.enableDoubleBuffering();
	    StdDraw.setXscale(0, 32768);
	    StdDraw.setYscale(0, 32768);
	    for (Point p : points) {
	        p.draw();
	    }
	    StdDraw.show();

	    // print and draw the line segments
	    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
	    for (LineSegment segment : collinear.segments()) {
	        StdOut.println(segment);
	        segment.draw();
	    }
	    StdDraw.show();
	    collinear.segmentsArr.stack[3] = new LineSegment(new Point(3,4), new Point(4,5)); // why?
	}
	*/
	
}
