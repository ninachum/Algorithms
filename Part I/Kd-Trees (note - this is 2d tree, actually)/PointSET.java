import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.TreeSet;
import java.util.Queue;
import java.util.LinkedList;

public class PointSET {
	private TreeSet<Point2D> setPoints;
	private int numPoints;
	
	public PointSET() {
		setPoints = new TreeSet<Point2D>();
		numPoints = 0;
	}
	
	public boolean isEmpty() {
		return numPoints == 0;
	}
	
	public int size() {
		return numPoints;
	}
	
	public void insert(Point2D p) {
		if (p == null)
			throw new java.lang.IllegalArgumentException();
		if (!setPoints.contains(p)) {
			setPoints.add(p);
			numPoints++;		
		}
	}
	
	public boolean contains(Point2D p) {
		if (p == null)
			throw new java.lang.IllegalArgumentException();
		return setPoints.contains(p);
	}
	
	public void draw() {
		for (Point2D a : setPoints) {
			a.draw();
		}
	}
	
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new java.lang.IllegalArgumentException();
		Queue<Point2D> tempQueue = new LinkedList<Point2D>();
		for (Point2D a : setPoints) {
			if (rect.contains(a))
				tempQueue.add(a);
		}
		return tempQueue;
		
	}
	
	public Point2D nearest(Point2D p) {
		if (p == null)
			throw new java.lang.IllegalArgumentException();
		if (isEmpty())
			return null;
		
		double distance = Double.POSITIVE_INFINITY;
		Point2D current = null;
		for (Point2D a : setPoints) {
			double temp = p.distanceTo(a);
			if (distance > temp) {
				distance = temp;
				current = a;
			}
		}
		return current;
	}
	
	
	
	
}
