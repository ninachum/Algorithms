import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.LinkedQueue;

public class BurrowsWheeler {
	
	// do Burrows-Wheeler tramsform.
	public static void transform() {
		String input = BinaryStdIn.readString();
		CircularSuffixArray x = new CircularSuffixArray(input); // sorted suffixes of input
		int len = input.length();
		int first = -1;							// 32-bit integer value
		char[] t = new char[len];				// last column of sorted suffix array 	
		for (int i = 0; i < len; i++) {
			int temp = x.index(i);				// 
			if (temp != 0)
				t[i] = input.charAt(temp - 1);
			else {
				t[i] = input.charAt(len - 1);
				first = i;		
			}
		}
		
		BinaryStdOut.write(first);
		for (int i = 0; i < len; i++) {
			BinaryStdOut.write(t[i]);
		}
		
		BinaryStdIn.close();
		BinaryStdOut.close();
		
	}
	
	public static void inverseTransform() {
		int R = 256;
		int first = BinaryStdIn.readInt();
		String input = BinaryStdIn.readString();
		int len = input.length();
		Object[] queue = new Object[R];
		for (int i = 0; i < R; i++) {
			queue[i] = new LinkedQueue<Integer>();
		}
		for (int i = 0; i < len; i++) {
			((LinkedQueue<Integer>) queue[input.charAt(i)]).enqueue(i);
		}
		
		char[] x = input.toCharArray();
		MyLSD.sort(x);
		int[] next = new int[len];
		
		for (int i = 0; i < len; i++) {
			next[i] = ((LinkedQueue<Integer>)queue[x[i]]).dequeue();			
		}
		
		for (int i = 0, j = first; i < len; i++) {
			BinaryStdOut.write(x[j]);
			j = next[j];
		}
		
		BinaryStdIn.close();
		BinaryStdOut.close();
		
		
	}
	
	public static void main(String[] args) {
		if (args[0].contentEquals("-"))
			BurrowsWheeler.transform();
		else if (args[0].contentEquals("+"))
			BurrowsWheeler.inverseTransform();
		
	}
}
