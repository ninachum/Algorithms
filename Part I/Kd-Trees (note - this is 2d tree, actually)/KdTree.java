import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.Queue;
import java.util.LinkedList;

// KdTree uses (unbalanced) binary tree. check WEEK_5_99GeometricSearch for details.
// to use balanced KdTree, google K-D-B tree..
public class KdTree {
	private int nan = 0;
	private int numNodes;
	private Node root;
	private static class Node {
		private Point2D point;
		private RectHV rect;
		private Node lb;
		private Node rt;
		
		public Node(Point2D p) {
			point = p;
		}
		
		@Override
	    public boolean equals(Object other) {
	        if (other == this) return true;
	        if (other == null) return false;
	        if (other.getClass() != this.getClass()) return false;
	        Node that = (Node) other;
	        return this.point.equals(that.point) && this.rect.equals(that.rect);
	    }
	}
	// RectHV that can be updated (min, max coordinates)
	private static class myRectHV {
		private double xmin;
		private double ymin;
		private double xmax;
		private double ymax;
		
		public myRectHV(double Nxmin, double Nymin, double Nxmax, double Nymax) {
			xmin = Nxmin;
			ymin = Nymin;
			xmax = Nxmax;
			ymax = Nymax;
		}
		
		public double xmin() {
			return xmin;
		}
		
		public double ymin() {
			return ymin;
		}
		
		public double xmax() {
			return xmax;
		}
		
		public double ymax() {
			return ymax;
		}
		
		public RectHV createRectHV() {
			return new RectHV(xmin, ymin, xmax, ymax);
		}
		
		public void update(double Nxmin, double Nymin, double Nxmax, double Nymax) {
			xmin = Nxmin;
			ymin = Nymin;
			xmax = Nxmax;
			ymax = Nymax;
		}
		
	}
	
	public boolean isEmpty() {
		return numNodes == 0;
	}
	
	public int size() {
		return numNodes;
	}
	
	public void insert(Point2D p) {
		if (p == null)
			throw new java.lang.IllegalArgumentException();
		Node tempNode = new Node(p);
		myRectHV tempRect = new myRectHV(0, 0, 1, 1);
		root = insertNode(root, tempNode, 0, tempRect);
	}
	
	// updating current node's rectangle with [new RectHV()] is too expensive (accumulated debt for garbage collector)
	// instead, create updatable RectHV class 'myRectHV', and update myRectHV instance each time insertNode is called.
	private Node insertNode(Node root, Node toInsert, int dimension, myRectHV rect) {
		if (root == null) { toInsert.rect = rect.createRectHV(); numNodes++; return toInsert; }
		if (root.point.equals(toInsert.point)) return root; // if toInsert already exists, do nothing and return.
		
		int currDim = dimension % 2;
		if (currDim == 0) { // if current dimension is 0 ( horizontal split )
			if (toInsert.point.x() < root.point.x()) { // to left
				rect.update(rect.xmin(), rect.ymin(), root.point.x(), rect.ymax());//instead of ... RectHV newRect = new RectHV(rect.xmin(), rect.ymin(), root.point.x(), rect.ymax());
				root.lb = insertNode(root.lb, toInsert, dimension + 1, rect);
			}
			else { // if equal or greater, to right
				rect.update(root.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
				root.rt = insertNode(root.rt, toInsert, dimension + 1, rect); // when x is same, goto 'right' subtree
			}
		}
		else { // if current dimension is 1 ( horizontal split )
			if (toInsert.point.y() < root.point.y()) { // to bottom
				rect.update(rect.xmin(), rect.ymin(), rect.xmax(), root.point.y());
				root.lb = insertNode(root.lb, toInsert, dimension + 1, rect);
			}
			else { // if equal or greater, to top
				rect.update(rect.xmin(), root.point.y(), rect.xmax(), rect.ymax());
				root.rt = insertNode(root.rt, toInsert, dimension + 1, rect); // when y is same, goto 'top' subtree
			}
		}
		return root;
	}
	
	public boolean contains(Point2D p) {
		if (p == null)
			throw new java.lang.IllegalArgumentException();
		Node tempNode = new Node(p);
		return (searchNode(root, tempNode, 0) != null);
	}
	
	private Node searchNode(Node root, Node toSearch, int dimension) {
		if (root == null) return null;
		if (root.point.equals(toSearch.point)) return root;
		int currDim = dimension % 2;
		if (currDim == 0) {
			if (toSearch.point.x() < root.point.x()) return searchNode(root.lb, toSearch, dimension + 1);
			else									 return searchNode(root.rt, toSearch, dimension + 1); // when x is same, goto 'right' subtree
		}
		else {
			if (toSearch.point.y() < root.point.y()) return searchNode(root.lb, toSearch, dimension + 1);
			else									 return searchNode(root.rt, toSearch, dimension + 1); // when y is same, goto 'top' subtree
		}
	}
	
	public void draw() {
		draw(root, 0);
	}
	
	private void draw (Node x, int dimension) {
		if (x == null) return;
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.01);
		StdDraw.point(x.point.x(), x.point.y());
		
		int currDim = dimension % 2;
		StdDraw.setPenRadius();
		if (currDim == 0) {
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(x.point.x(), x.rect.ymin(), x.point.x(), x.rect.ymax());
		}
		else {
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.line(x.rect.xmin(), x.point.y(), x.rect.xmax(), x.point.y());	
		}
		draw(x.lb, dimension + 1);
		draw(x.rt, dimension + 1);
	}
	
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new java.lang.IllegalArgumentException();
		Queue<Point2D> tempQueue = new LinkedList<Point2D>();
		range(root, tempQueue, rect);
		return tempQueue;
	}
	
	private void range(Node x, Queue<Point2D> tempQueue, RectHV queryRect) {
		if (x == null) return;
		if (x.rect.intersects(queryRect)) {
			if (queryRect.contains(x.point))
				tempQueue.add(x.point);
			range(x.lb, tempQueue, queryRect);
			range(x.rt, tempQueue, queryRect);
		}
	}
	
	//
	public Point2D nearest(Point2D p) {
		if (p == null)
			throw new java.lang.IllegalArgumentException();
		//double initDistance = root.point.distanceTo(p);
		if (!isEmpty()) {
			Queue<Point2D> tempQueue = new LinkedList<Point2D>();
			tempQueue.add(root.point);
			nearest(root, p, tempQueue, 0);	
			return tempQueue.remove();
		}
		else
			return null;
	}
	
	/*
	private void nearest(Node x, Point2D p, Queue<Point2D> tempQueue) {
		if (x == null) return;
		if (x.rect.distanceTo(p) >= tempQueue.peek().distanceTo(p))
			return;
		else {
			double tempDist = x.point.distanceTo(p);
			if (tempDist < tempQueue.peek().distanceTo(p)) {
				tempQueue.remove();
				tempQueue.add(x.point);
			}
			nearest(x.lb, p, tempQueue);
			nearest(x.rt, p, tempQueue);
		}
	}
	*/
	
	private void nearest(Node x, Point2D p, Queue<Point2D> tempQueue, int dimension) {
		nan++;
		double tempDist = x.point.distanceTo(p);	     // distance (current point to query point) 
		//double temPest = tempQueue.peek().distanceTo(p); // closest point's distance
		if (tempDist < tempQueue.peek().distanceTo(p)) {
			tempQueue.remove();
			tempQueue.add(x.point);
		}
		
		// to fast-search nearest point(and prune as many as subtree possible), search toward query point first.
		int currDim = dimension % 2;
		if (currDim == 0 && x.point.x() <= p.x()) {
			if (x.rt != null && tempQueue.peek().distanceTo(p) > x.rt.rect.distanceTo(p)) nearest(x.rt, p, tempQueue, dimension + 1);
			if (x.lb != null && tempQueue.peek().distanceTo(p) > x.lb.rect.distanceTo(p)) nearest(x.lb, p, tempQueue, dimension + 1);		
		}
		else if (currDim == 0 && x.point.x() > p.x()) {
			if (x.lb != null && tempQueue.peek().distanceTo(p) > x.lb.rect.distanceTo(p)) nearest(x.lb, p, tempQueue, dimension + 1);
			if (x.rt != null && tempQueue.peek().distanceTo(p) > x.rt.rect.distanceTo(p)) nearest(x.rt, p, tempQueue, dimension + 1);		
		}
		else if (currDim == 1 && x.point.y() <= p.y()) {
			if (x.rt != null && tempQueue.peek().distanceTo(p) > x.rt.rect.distanceTo(p)) nearest(x.rt, p, tempQueue, dimension + 1);
			if (x.lb != null && tempQueue.peek().distanceTo(p) > x.lb.rect.distanceTo(p)) nearest(x.lb, p, tempQueue, dimension + 1);		
		}
		else if (currDim == 1 && x.point.y() > p.y()) {
			if (x.lb != null && tempQueue.peek().distanceTo(p) > x.lb.rect.distanceTo(p)) nearest(x.lb, p, tempQueue, dimension + 1);
			if (x.rt != null && tempQueue.peek().distanceTo(p) > x.rt.rect.distanceTo(p)) nearest(x.rt, p, tempQueue, dimension + 1);		
		}
		
		//if (x.lb != null && tempQueue.peek().distanceTo(p) > x.lb.rect.distanceTo(p)) nearest(x.lb, p, tempQueue); 
		//if (x.rt != null && tempQueue.peek().distanceTo(p) > x.rt.rect.distanceTo(p)) nearest(x.rt, p, tempQueue);
	}
	
	
	/*
	public static void main(String args[]) {
		Point2D pt = new Point2D(0.7, 0.2);
		Point2D pt2 = new Point2D(0.5 , 0.4);
		Point2D pt3 = new Point2D(0.2 , 0.3);
		Point2D pt4 = new Point2D(0.4 , 0.7);
		Point2D pt5 = new Point2D(0.9 , 0.6);
		KdTree kd = new KdTree();
		
		kd.insert(pt);
		kd.insert(pt2);
		kd.insert(pt3);
		kd.insert(pt4);
		kd.insert(pt5);
		kd.draw();
		kd.nearest(new Point2D(0.712, 0.963));
	}
	*/
}