import edu.princeton.cs.algs4.In;

public class Outcast {
	
	private WordNet wordNet;
	private int[] distances;
	
	public Outcast(WordNet wordnet) {
		this.wordNet = wordnet;
	}
	
	public String outcast(String[] nouns) {
		distances = new int[nouns.length];
		for (int i = 0; i < distances.length; i++) {
			int tempDist = 0;
			for (int j = 0; j < distances.length; j++) 
				if (i != j) 
					tempDist += wordNet.distance(nouns[i], nouns[j]);		
			distances[i] = tempDist;
		}
		
		int maxDistNounIdx = 0;
		int maxDist = distances[0];
		for (int i = 1; i < distances.length; i++) {
			if (maxDist < distances[i]) {
				maxDist = distances[i];
				maxDistNounIdx = i;		
			}
		}
		
		return nouns[maxDistNounIdx];
	}
	/*
	public static void main(String[] args) {
	    WordNet wordnet = new WordNet(args[0], args[1]);
	    Outcast outcast = new Outcast(wordnet);
	    for (int t = 2; t < args.length; t++) {
	        In in = new In(args[t]);
	        String[] nouns = in.readAllStrings();
	        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
	    }
	}
	*/
}
