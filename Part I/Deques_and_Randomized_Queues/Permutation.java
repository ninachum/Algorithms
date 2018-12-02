import edu.princeton.cs.algs4.StdIn;


public class Permutation {
	public static void main(String[] args) {
		RandomizedQueue<String> rdQueue = new RandomizedQueue<String>();
		
		while (!StdIn.isEmpty())
			rdQueue.enqueue(StdIn.readString());
		
		int i = 0;
		for (String s : rdQueue) {
			if (i == Integer.parseInt(args[0]))
				break;
			System.out.println(s);
			i++;
		}
		
	}
}
