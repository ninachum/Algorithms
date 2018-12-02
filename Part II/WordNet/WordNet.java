import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;
import java.util.HashSet;
import java.util.Iterator;
import java.lang.StringBuilder;

public class WordNet {

	private Digraph wordNet;
	private ST<String, HashSet<Integer>> stringToNum; // ST is balanced BST.. HashMap would be faster.!!!
	private Object[] numToString; //	private TreeSet<String>[] numToString; 
	private SAP sapEntry;
	
	public WordNet(String synsets, String hypernyms) {
		if (synsets == null || hypernyms == null)
			throw new java.lang.IllegalArgumentException(); 
		
		In synsetsInput = new In(synsets);
		String[] synsetsAllLines = synsetsInput.readAllLines();
		
		wordNet = new Digraph(synsetsAllLines.length);
		
		stringToNum = new ST<String, HashSet<Integer>>();
		numToString = new Object[synsetsAllLines.length]; //
		for (int i = 0; i < synsetsAllLines.length; i++) 
			numToString[i] = new HashSet<String>(); //
		
		for (int i = 0; i < synsetsAllLines.length; i++) {
			String line = synsetsAllLines[i];
			String[] words = line.split(",");
			String[] synonyms = words[1].split(" ");
			
			for (int k = 0; k < synonyms.length; k++)  {
				if (!stringToNum.contains(synonyms[k])) {
					HashSet<Integer> tempSET = new HashSet<Integer>();
					tempSET.add(i);
					stringToNum.put(synonyms[k], tempSET);
				}
				else {
					stringToNum.get(synonyms[k]).add(i);
				}

				((HashSet<String>)numToString[i]).add(synonyms[k]); //
			}
		}
		
		In hypernymsInput = new In(hypernyms);
		String[] hypernymsAllLines = hypernymsInput.readAllLines();
		
		for (int i = 0; i < hypernymsAllLines.length; i++) {
			String line = hypernymsAllLines[i];
			String[] numbers = line.split(",");
			
			for (int k = 1; k < numbers.length; k++) {
				wordNet.addEdge(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[k]));
			}
		}
		
		// these operations must be called after wordNet construction is complete.
		if (!isRootedDAG(wordNet))
			throw new java.lang.IllegalArgumentException(); 
		sapEntry = new SAP(wordNet);
	}
	
	private boolean isRootedDAG(Digraph G) {
		return (isSingleRooted(G) && isDAG(G));
	}
	
	private boolean isSingleRooted(Digraph G) {
		
		int numOfRoot = 0;
		
		for (int i = 0; i < G.V(); i++) {
			if (G.outdegree(i) == 0) {
				numOfRoot++;
				if (numOfRoot > 1)
					return false;
			}
		}
		
		if (numOfRoot == 1)
			return true;
		else
			return false;
	}
	
	private boolean isDAG(Digraph G) {
		DirectedCycle temp = new DirectedCycle(G);
		return !temp.hasCycle();
	}
	
	public Iterable<String> nouns() {
		return stringToNum.keys();
		/*
		Queue<String> queue = new Queue<String>();
		for (int i = 0; i < numToString.length; i++) {
			for (String x : createIterable_S(((HashSet<String>) numToString[i]).iterator())) { //
				queue.enqueue(x);
			}
		}
		return queue;
		*/
	}
	
	public boolean isNoun(String word) {
		if (word == null)
			throw new java.lang.IllegalArgumentException(); 
		return stringToNum.contains(word);
	}
	
	public int distance(String nounA, String nounB) {
		if (!isNoun(nounA) || !isNoun(nounB))
			throw new java.lang.IllegalArgumentException(); 
		
		HashSet<Integer> vertexA = stringToNum.get(nounA);
		HashSet<Integer> vertexB = stringToNum.get(nounB);
		
		Iterable<Integer> IterableA = createIterable_I(vertexA.iterator());
		Iterable<Integer> IterableB = createIterable_I(vertexB.iterator());
		
		return sapEntry.length(IterableA, IterableB);
	}
	
	public String sap(String nounA, String nounB) {
		if (!isNoun(nounA) || !isNoun(nounB))
			throw new java.lang.IllegalArgumentException(); 
	
		HashSet<Integer> vertexA = stringToNum.get(nounA);
		HashSet<Integer> vertexB = stringToNum.get(nounB);
		
		Iterable<Integer> IterableA = createIterable_I(vertexA.iterator());
		Iterable<Integer> IterableB = createIterable_I(vertexB.iterator());
		
		int resultVertex = sapEntry.ancestor(IterableA, IterableB);
		if (!(0 <= resultVertex && resultVertex < wordNet.V()))
			return null;
		
		StringBuilder tempSB = new StringBuilder();
		for (String x: createIterable_S(((HashSet<String>)numToString[resultVertex]).iterator())) {	 //
			tempSB.append(x);
			tempSB.append(" ");
		}
		String tempS = tempSB.toString();
		tempS = tempS.substring(0, tempS.length() - 1);
		return tempS;
	}
	
	private Iterable<Integer> createIterable_I(Iterator<Integer> x) {
		Queue<Integer> queue = new Queue<Integer>();
		while(x.hasNext()) {
			queue.enqueue(x.next());
		}
		return queue;
	}
	
	private Iterable<String> createIterable_S(Iterator<String> x) {
		Queue<String> queue = new Queue<String>();
		while(x.hasNext()) {
			queue.enqueue(x.next());
		}
		return queue;
	}
	
	public static void main(String[] args) {
		
	}
	
}
