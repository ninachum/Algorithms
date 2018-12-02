import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class SAP {
	
	private Digraph dg;
	
	public SAP(Digraph G) {
		if (G == null)
			throw new java.lang.IllegalArgumentException(); 
		dg = new Digraph(G);
	}
	
	public int length(int v, int w) {
		if (inRange(v,w))
			throw new java.lang.IllegalArgumentException(); 
		
		MyBFS bfsV = new MyBFS(dg, v);
		MyBFS bfsW = new MyBFS(dg, w);
		
		boolean[] markedV = bfsV.returnMarked();
		boolean[] markedW = bfsW.returnMarked();
		
		int minDistance = Integer.MAX_VALUE;
		for(int i = 0; i < markedV.length; i++) {
			if (markedV[i] == true && markedW[i] == true ) { // && i != v && i != w
				int tempDist = (bfsV.distTo(i) + bfsW.distTo(i));
				if (minDistance > tempDist) {
					minDistance = tempDist;
				}
			}
		}
		
		if (minDistance == Integer.MAX_VALUE)
			return -1;
		else
			return minDistance;

	}
	
	private boolean inRange(int v, int w) {
		return (!(0 <= v && v < dg.V()) || !((0 <= w && w < dg.V())));
	}
	
	public int ancestor(int v, int w) {
		if (inRange(v,w))
			throw new java.lang.IllegalArgumentException(); 
		MyBFS bfsV = new MyBFS(dg, v);
		MyBFS bfsW = new MyBFS(dg, w);
		
		boolean[] markedV = bfsV.returnMarked();
		boolean[] markedW = bfsW.returnMarked();
		
		int sa = -1;
		int minDistance = Integer.MAX_VALUE;
		for(int i = 0; i < markedV.length; i++) {
			if (markedV[i] == true && markedW[i] == true ) { // && i != v && i != w
				int tempDist = (bfsV.distTo(i) + bfsW.distTo(i));
				if (minDistance > tempDist) {
					minDistance = tempDist;
					sa = i;
				}
			}
		}
		
		if (minDistance == Integer.MAX_VALUE)
			return -1;
		else
			return sa;
	}
	
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		if (v == null || w == null)
			throw new java.lang.IllegalArgumentException(); 
		MyBFS bfsV = new MyBFS(dg, v);
		MyBFS bfsW = new MyBFS(dg, w);
		
		boolean[] markedV = bfsV.returnMarked();
		boolean[] markedW = bfsW.returnMarked();
		
		int minDistance = Integer.MAX_VALUE;
		for(int i = 0; i < markedV.length; i++) {
			// assuming contains() is done within constant bound.
			if (markedV[i] == true && markedW[i] == true ) { // && !setV.contains(i) && !setW.contains(i)
				int tempDist = (bfsV.distTo(i) + bfsW.distTo(i));
				if (minDistance > tempDist) {
					minDistance = tempDist;
				}
			}
		}
		
		if (minDistance == Integer.MAX_VALUE)
			return -1;
		else
			return minDistance;
	}
	
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		if (v == null || w == null)
			throw new java.lang.IllegalArgumentException(); 
		MyBFS bfsV = new MyBFS(dg, v);
		MyBFS bfsW = new MyBFS(dg, w);
		
		boolean[] markedV = bfsV.returnMarked();
		boolean[] markedW = bfsW.returnMarked();
				
		int sa = -1;
		int minDistance = Integer.MAX_VALUE;
		for(int i = 0; i < markedV.length; i++) {
			// assuming contains() is done within constant bound.
			if (markedV[i] == true && markedW[i] == true ) { // && !setV.contains(i) && !setW.contains(i))
				int tempDist = (bfsV.distTo(i) + bfsW.distTo(i));
				if (minDistance > tempDist) {
					minDistance = tempDist;
					sa = i;
				}
			}
		}
		
		if (minDistance == Integer.MAX_VALUE)
			return -1;
		else
			return sa;
		
	}
	
	/*
	public static void main(String[] args) {
	    In in = new In(args[0]);
	    Digraph G = new Digraph(in);
	    SAP sap = new SAP(G);
	    while (!StdIn.isEmpty()) {
	        int v = StdIn.readInt();
	        int w = StdIn.readInt();
	        int length   = sap.length(v, w);
	        int ancestor = sap.ancestor(v, w);
	        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
	    }
	}
	*/
}
