import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class FastCollinearPoints {
	private int numSegs;
	private myStack segmentsArr;
	
	public FastCollinearPoints(Point[] points) {
		if (points == null)
			throw new java.lang.IllegalArgumentException("creating BruteCollinearPoints failed : null argument");
		
		for (int i = 0; i < points.length; i++) {
			if (points[i] == null)
				throw new java.lang.IllegalArgumentException("null element detected");
		}
		
		if (isRepeated(points))
			throw new java.lang.IllegalArgumentException("repeated element detected");
		
		numSegs = 0;
		segmentsArr = new myStack();
		
		Arrays.sort(points);

		
		for (int i = 0; i < points.length; i++) {
			// mappedSlope[] auxArr is used to map a point and its scope, when auxArr is sorted BY_SLOPE.
			mappedSlope[] auxArr = new mappedSlope[points.length];

			for (int j = 0; j < points.length; j++) {
				auxArr[j] = new mappedSlope(points[j].slopeTo(points[i]), points[j]);
			}
			
			//Arrays.sort(auxArr, mappedSlope.BY_POINT);	// uses MergeSort automatically(mappedSlope is an object), which is stable.

			Arrays.sort(auxArr, mappedSlope.BY_SLOPE);	// uses MergeSort automatically(mappedSlope is an object), which is stable.
			
			// finds points that have same slope with points[i].
			int numOfSameSlopes = 1;
			for (int j = 0; j < points.length - 1; j++) {
				if (auxArr[j].slope == auxArr[j + 1].slope) {
					numOfSameSlopes++;
					// corner case : when auxArr ends with numOfSameSlopes is >=3.
					if (j == points.length - 2) {
						if (numOfSameSlopes >= 3) { // NOT 4
							LineSegment temp = segmentTopBottomPoint(auxArr, auxArr[0], (j + 1) - (numOfSameSlopes - 1), j + 1); // NEGATIVE_INFINITY is always first, when sorted BY_SLOPE.
							if (temp != null) {	// core part for performance. check segmentTopBottomPoint()
								segmentsArr.push(temp);
								numSegs++;
							}
							
						}
						numOfSameSlopes = 1; // initialize for next i iteration
					}
				}
				else {
					if (numOfSameSlopes >= 3) { // NOT 4
						LineSegment temp = segmentTopBottomPoint(auxArr, auxArr[0], j - (numOfSameSlopes - 1), j); // NEGATIVE_INFINITY is always first, when sorted BY_SLOPE.
						if (temp != null) {
							segmentsArr.push(temp);
							numSegs++;
						}
					}
					numOfSameSlopes = 1;

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
	
	// defensive copy of an returning array
	public LineSegment[] segments() {
		LineSegment[] tempForReturn = new LineSegment[numSegs];
		for (int i = 0; i < numSegs; i++) {
			tempForReturn[i] = segmentsArr.stack[i];
		}
		return tempForReturn;
	}
		
	// stack that only grows (size 0 -> 1 -> 2 -> 3... (by 1 each time))
	private class myStack {

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
	private LineSegment segmentTopBottomPoint(mappedSlope[] a, mappedSlope standard, int lo, int hi) {
		mappedSlope[] aux = new mappedSlope[(hi - lo + 1) + 1]; // addition 1 element : for i th point (current standard for slope)
		
		aux[0] = standard;
		for (int i = 1; i < aux.length; i++) {
			aux[i] = a[lo + i - 1];
		}

		
		// return null if the slope elements are not in order (which means it is an already-seen LineSegment.)
		// the fact that auxArr(in constructor) is sorted with points, then sorted with slopes reduces the number of comparison from (number of LineSegments in stackArr.stack) to 1.  
		if (aux[0].origin.compareTo(aux[1].origin) > 0)
			return null;
		// this loop replaces isAlreadyIn method (which takes N^4 if input points are in grid form. LineSegment 
		/*
		for (int i = 0; i < aux.length - 1; i++) {
			if (aux[i].origin.compareTo(aux[i + 1].origin) > 0)
				return null;
		}
		*/
		
		// check if there's already a same LineSegment element, using String Pool.
		/*
		private boolean isAlreadyIn(LineSegment temp) {
			for (int i = 0; i < segmentsArr.stack.length; i++) {
				if (temp.toString().equals(segmentsArr.stack[i].toString()))
					return true;
			}
			return false;
		}
		*/

		//OLD : Arrays.sort(aux, mappedSlope.BY_POINT); ------>> bottleneck (it takes N lg N) ....=> improved to 2N
		mappedSlope minPoint = aux[0];
		for (int i = 1; i < aux.length; i++) {
			if (minPoint.origin.compareTo(aux[i].origin) > 0)
				minPoint = aux[i];
		}
		
		mappedSlope maxPoint = aux[0];
		for (int i = 1; i < aux.length; i++) {
			if (maxPoint.origin.compareTo(aux[i].origin) < 0)
				maxPoint = aux[i];
		}
		
		
		return new LineSegment(minPoint.origin, maxPoint.origin); // left argument is the smallest, right is the greatest point (for comparison standard : check Point.java)
	}
	
	
	// check if there's a duplicated point, using String Pool.
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
	    FastCollinearPoints collinear = new FastCollinearPoints(points);
	    for (LineSegment segment : collinear.segments()) {
	        StdOut.println(segment);
	        segment.draw();
	    }
	    StdDraw.show();
	}
	*/
	
}

// a data type that maps a point & its relative slope with point[i]
class mappedSlope {
	public static final Comparator<mappedSlope> BY_SLOPE = new BySlope();
	public static final Comparator<mappedSlope> BY_POINT = new ByPoint();
	public double slope;
	public Point origin;
	
	public mappedSlope(double newSlope, Point newOrigin) {
		slope = newSlope;
		origin = newOrigin;
	}
	
	private static class BySlope implements Comparator<mappedSlope> {
		public int compare(mappedSlope a, mappedSlope b) {
			if (a.slope > b.slope)
				return +1;
			else if (a.slope == b.slope)
				return 0;
			else
				return -1;
		}
	}
	
	private static class ByPoint implements Comparator<mappedSlope> {
		public int compare(mappedSlope a, mappedSlope b) {
			return a.origin.compareTo(b.origin); 
		}
	}
	
}