import java.util.Arrays;

public class CircularSuffixArray {
	
	private String str;
	private Suffix[] index;
	
	// since String.substring() takes linear time, explicit suffix (which are Strings) cannot be made. (which results in n^2 time)
	// instead, create instances that has differing start point.  ex) "APPLE" has 5 Suffix instances - starts with position 0, 1, 2, 3, 4. 
	private class Suffix implements Comparable<Suffix> {
		int start;
		int length;
		int idxNo;			// original position of the sorted suffix.
		
		public Suffix(int st, int len, int iNo) {
			start = st;
			length = len;
			idxNo = iNo;
		}
		
		public int compareTo(Suffix that) {
			int countThis = 0;
			int countThat = 0;
			for (int i = this.start, j = that.start;
					countThis < this.length && countThat < that.length;
					i++, j++, countThis++, countThat++) {
				if (i >= this.length)
					i = 0;
				if (j >= that.length)
					j = 0;
				
				if (str.charAt(i) > str.charAt(j)) {
					return 1;
				}
				else if (str.charAt(i) < str.charAt(j)) {
					return -1;
				}
			}
			return 0;
		}
		
		public String toString() {
			StringBuilder a = new StringBuilder();
			int count = 0;
			for (int i = this.start; count < this.length; i++, count++) {
				if (i >= this.length)
					i = 0;
				a.append(str.charAt(i));
			}
			return a.toString();
		}
	}
	
	public CircularSuffixArray(String s) {
		if (s == null) {
			throw new java.lang.IllegalArgumentException();
		}
		str = s;
		int len = s.length();		
		if (len == 0) 						// avoid zero-length String ""
			return;
		
		index = new Suffix[len];			// create original suffix array 
		index[0] = new Suffix(0, len, 0);	
		for (int i = 1; i < len; i++) {
			index[i] = new Suffix(i, len, i);
		}
		
		Arrays.sort(index);
		// NOT Quick.sort(index) -- for redundant character comparison (according to Coursera submission result)
		// Arrays.sort uses 'tuned' Quicksort (???!) 
		
	}
	
	public int length() {
		return str.length();
	}
	
	public int index(int i) {
		if (!(0 <= i && i < str.length())) 
			throw new java.lang.IllegalArgumentException();
		return index[i].idxNo;		// return the entry's original position.
	}	
	
	
	
	public static void main(String[] args) {
		CircularSuffixArray x = new CircularSuffixArray("");
		int a = 3 + 3;
	}
	
}
