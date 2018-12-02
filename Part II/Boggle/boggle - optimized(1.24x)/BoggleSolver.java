import edu.princeton.cs.algs4.PatriciaSET;
import edu.princeton.cs.algs4.Bag;
import java.lang.StringBuilder;

public class BoggleSolver {
	private myTrieSET dict;
	private PatriciaSET coll;
	private int boardRows;
	private int boardCols;
	
	public BoggleSolver(String[] dictionary) {
		
		// create a 26-Way Trie( which is SET, not ST(map) ) from dictionary.
		dict = new myTrieSET();
		for (int i = 0; i < dictionary.length; i++) {
			dict.add(dictionary[i]);
		}
		
	}
	
	private class Coordinate {
		public int row;
		public int col;
		
		public Coordinate(int i, int j) {
			row = i;
			col = j;
		}
	}
	
	
	public Iterable<String> getAllValidWords(BoggleBoard board) {
		// create a PatriciaTrie for collecting words from the Boggle.
		coll = new PatriciaSET();
		boardRows = board.rows();
		boardCols = board.cols();
		
		Object[][] adjMat = new Object[boardRows][boardCols];
		for (int i = 0; i < boardRows; i++) {
			for (int j = 0; j < boardCols; j++) {
				adjMat[i][j] = (Object) new Bag<Coordinate>();
				if (isValid(i, j + 1)) ((Bag<Coordinate>) adjMat[i][j]).add(new Coordinate(i, j + 1));
				if (isValid(i + 1, j + 1)) ((Bag<Coordinate>) adjMat[i][j]).add(new Coordinate(i + 1, j + 1));
				if (isValid(i + 1, j)) ((Bag<Coordinate>) adjMat[i][j]).add(new Coordinate(i + 1, j));
				if (isValid(i + 1, j - 1)) ((Bag<Coordinate>) adjMat[i][j]).add(new Coordinate(i + 1, j - 1));
				if (isValid(i, j - 1)) ((Bag<Coordinate>) adjMat[i][j]).add(new Coordinate(i, j - 1));
				if (isValid(i - 1, j - 1)) ((Bag<Coordinate>) adjMat[i][j]).add(new Coordinate(i - 1, j - 1));
				if (isValid(i - 1, j)) ((Bag<Coordinate>) adjMat[i][j]).add(new Coordinate(i - 1, j));
				if (isValid(i - 1, j + 1)) ((Bag<Coordinate>) adjMat[i][j]).add(new Coordinate(i - 1, j + 1));
			}
		}
		
		// collects word, starting from each dice.
		for (int i = 0; i < boardRows; i++) {
			for (int j = 0; j < boardCols; j++) {
				// initialize an array marked[][] to record visited path.
				boolean[][] marked = new boolean[boardRows][boardCols];
				collect(board, i, j, coll, marked, new StringBuilder(), dict.getRoot(), adjMat);
			}
		}
		return coll;
	}
	

	/**
	 * 
	 * @param board : current Boggle board
	 * @param i : current row in the board
	 * @param j : current column in the board
	 * @param coll : PatriciaTrie for collecting words from the board
	 * @param marked : array for recording visited path
	 * @param sb : word constructed from each recursive call. (example : A -> AP -> APP -> APPL -> APPLE)
	 * @param curr : current Node in dictionary.
	 * @param adjMat : adjacency matrix for position [i][j]
	 */
	private void collect(BoggleBoard board, int i, int j, PatriciaSET coll, boolean[][] marked, StringBuilder sb, Node curr, Object[][] adjMat) {
		if (curr == null) return;
		
		// mark current position as visited
		marked[i][j] = true;
		
		// append current character to current word. 
		char currentChar = board.getLetter(i, j);
		if (currentChar == 'Q')
			sb.append("QU");
		else
			sb.append(board.getLetter(i, j));
		
		String tempString = sb.toString();
		// move between Nodes in dictionary Trie, according to current character, 
		if (currentChar == 'Q') {
			curr = dict.nextNode(curr, tempString.charAt(tempString.length() - 2));
			if (curr != null) 
				curr = dict.nextNode(curr, tempString.charAt(tempString.length() - 1));
		}
		else
			curr = dict.nextNode(curr, tempString.charAt(tempString.length() - 1));
		
		// moving is finished. if current Node is has a String, add current word to coll. (equivalent to dict.contains(tempString), but more fast version.)
		if (dict.isThisNodeHasString(curr) && tempString.length() > 2) 
			coll.add(tempString);

		// for valid child of valid current Node, do recursive calls.
		if (curr != null) {
			for (Coordinate x : (Bag<Coordinate>) adjMat[i][j]) {
				if (curr != null)
					if (!isMarked(marked, x.row, x.col) && curr.next[board.getLetter(x.row, x.col) - 'A'] != null) {
					collect(board , x.row, x.col, coll, marked, sb, curr, adjMat);
					}
			}
		}
			
		
		// restore marked[i][j] and current word, for further recursive call.
		// I M P O R T A N T ! !
		marked[i][j] = false;
		sb.deleteCharAt(sb.length() - 1);
		if (currentChar == 'Q')
			sb.deleteCharAt(sb.length() - 1);
		/*
		// if current Node has a child, do recursive calls.
		if (dict.needToGoFurther(curr)) {
			for (Coordinate x : (Bag<Coordinate>) adjMat[i][j]) {
				if (!isMarked(marked, x.row, x.col)) {
					collect(board , x.row, x.col, coll, marked, sb, curr, adjMat);
				}
			}
			
			//if (isValid(board, i, j + 1)	 && !isMarked(marked, i, j + 1)) 	 	  collect(board, i, j + 1, coll, marked, sb,  curr, adjMat);
			//if (isValid(board, i + 1, j + 1) && !isMarked(marked, i + 1, j + 1))  	  collect(board, i + 1, j + 1, coll, marked, sb, curr, adjMat);
			//if (isValid(board, i + 1, j) 	 && !isMarked(marked, i + 1, j)) 	      collect(board, i + 1, j, coll, marked, sb, curr, adjMat);
			//if (isValid(board, i + 1, j - 1) && !isMarked(marked, i + 1, j - 1))  	  collect(board, i + 1, j - 1, coll, marked, sb, curr, adjMat);
			//if (isValid(board, i, j - 1) 	 && !isMarked(marked, i, j - 1)) 	 	  collect(board, i, j - 1, coll, marked, sb, curr, adjMat);
			//if (isValid(board, i - 1, j - 1) && !isMarked(marked, i - 1, j - 1))      collect(board, i - 1, j - 1, coll, marked, sb, curr, adjMat);
			//if (isValid(board, i - 1, j) 	 && !isMarked(marked, i - 1, j)) 	 	  collect(board, i - 1, j, coll, marked, sb, curr, adjMat);
			//if (isValid(board, i - 1, j + 1) && !isMarked(marked, i - 1, j + 1))      collect(board, i - 1, j + 1, coll, marked, sb, curr, adjMat);
			
			
			// restore marked[i][j] and current word, for further recursive call.
			// I M P O R T A N T ! !
			marked[i][j] = false;
			sb.deleteCharAt(sb.length() - 1);
			if (currentChar == 'Q')
				sb.deleteCharAt(sb.length() - 1);
		}
		else {			
			// restore marked[i][j] and current word, for further recursive call.
			// I M P O R T A N T ! !
			marked[i][j] = false;
			sb.deleteCharAt(sb.length() - 1);
			if (currentChar == 'Q')
				sb.deleteCharAt(sb.length() - 1);
			return;
		}		
		*/
	}
	
	private boolean isValid(int i, int j) {
		return (0 <= i && i < boardRows && 0 <= j && j < boardCols);
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
