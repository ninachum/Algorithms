import edu.princeton.cs.algs4.PatriciaSET;
import java.lang.StringBuilder;

public class BoggleSolver {
	private myTrieSET dict;
	private PatriciaSET coll;
	
	public BoggleSolver(String[] dictionary) {
		
		// create a Trie-set from dictionary.
		dict = new myTrieSET();
		for (int i = 0; i < dictionary.length; i++) {
			dict.add(dictionary[i]);
		}
		
	}
	
	public Iterable<String> getAllValidWords(BoggleBoard board) {
		// create a Trie for collecting words from the Boggle.
		coll = new PatriciaSET();
		
		for (int i = 0; i < board.rows(); i++) {
			for (int j = 0; j < board.cols(); j++) {
				boolean[][] marked = new boolean[board.rows()][board.cols()];
				collect(board, i, j, coll, marked, new StringBuilder(), dict.getRoot());
			}
		}
		return coll;
	}
	
	private void collect(BoggleBoard board, int i, int j, PatriciaSET coll, boolean[][] marked, StringBuilder sb, Node curr) {
		
		marked[i][j] = true;
		char currentChar = board.getLetter(i, j);
		if (currentChar == 'Q')
			sb.append("QU");
		else
			sb.append(board.getLetter(i, j));
		
		String tempString = sb.toString();
		if (currentChar == 'Q') {
			curr = dict.nextNode(curr, tempString.charAt(tempString.length() - 2));
			if (curr != null) 
				curr = dict.nextNode(curr, tempString.charAt(tempString.length() - 1));
		}
		else
			curr = dict.nextNode(curr, tempString.charAt(tempString.length() - 1));
		
		if (dict.isThisNodeHasString(curr) && tempString.length() > 2) 
			coll.add(tempString);

		if (dict.needToGoFurther(curr)) {
			if (isValid(board, i, j + 1)	 && !isMarked(marked, i, j + 1)		 && curr != null) 	  collect(board, i, j + 1, coll, marked, sb,  curr);
			if (isValid(board, i + 1, j + 1) && !isMarked(marked, i + 1, j + 1)	 && curr != null)  	  collect(board, i + 1, j + 1, coll, marked, sb, curr);
			if (isValid(board, i + 1, j) 	 && !isMarked(marked, i + 1, j)		 && curr != null) 	  collect(board, i + 1, j, coll, marked, sb, curr);
			if (isValid(board, i + 1, j - 1) && !isMarked(marked, i + 1, j - 1)	 && curr != null)  	  collect(board, i + 1, j - 1, coll, marked, sb, curr);
			if (isValid(board, i, j - 1) 	 && !isMarked(marked, i, j - 1)		 && curr != null) 	  collect(board, i, j - 1, coll, marked, sb, curr);
			if (isValid(board, i - 1, j - 1) && !isMarked(marked, i - 1, j - 1)	 && curr != null)     collect(board, i - 1, j - 1, coll, marked, sb, curr);
			if (isValid(board, i - 1, j) 	 && !isMarked(marked, i - 1, j)		 && curr != null) 	  collect(board, i - 1, j, coll, marked, sb, curr);
			if (isValid(board, i - 1, j + 1) && !isMarked(marked, i - 1, j + 1)	 && curr != null)     collect(board, i - 1, j + 1, coll, marked, sb, curr);
			marked[i][j] = false;
			sb.deleteCharAt(sb.length() - 1);
			if (currentChar == 'Q')
				sb.deleteCharAt(sb.length() - 1);
		}
		else {
			marked[i][j] = false;
			sb.deleteCharAt(sb.length() - 1);
			if (currentChar == 'Q')
				sb.deleteCharAt(sb.length() - 1);
			return;
		}		
	}
	
	private boolean isValid(BoggleBoard board, int i, int j) {
		return (0 <= i && i < board.rows() && 0 <= j && j < board.cols());
	}
	
	private boolean isMarked(boolean[][] marked, int i, int j) {
		return marked[i][j] == true;
	}
	
	/*
	private String qConverter(String word) {
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == 'Q') 
				temp.append("QU");
			else 
				temp.append(word.charAt(i));
		}
		return temp.toString();
	}
	*/
	
	public int scoreOf(String word) {
		if (dict.contains(word)) {
			int len = word.length();
			if (3 <= len && len <= 4)
				return 1;
			if (len == 5)
				return 2;
			if (len == 6)
				return 3;
			if (len == 7)
				return 5;
			if (8 <= len)
				return 11;
		}
		return 0;
	}
	
	/*
	public static void main(String[] args) {
	    In in = new In(args[0]);
	    String[] dictionary = in.readAllStrings();
	    BoggleSolver solver = new BoggleSolver(dictionary);
	    BoggleBoard board = new BoggleBoard(args[1]);
	    int score = 0;
	    for (String word : solver.getAllValidWords(board)) {
	        StdOut.println(word);
	        score += solver.scoreOf(word);
	    }
	    StdOut.println("Score = " + score);
	}
	*/
}
