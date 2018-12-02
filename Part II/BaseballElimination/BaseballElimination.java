import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import java.lang.StringBuilder;
import java.util.HashMap;

public class BaseballElimination {
	private int teams;	// number of teams
	private HashMap<String, Integer> names; // maps teamnames to numbers.
	private int[] wins;
	private int[] losses;
	private int[] remaining;
	private int[][] gamesVersus; // games left to be played by two(row, col) teams.
	private boolean[] eliminated; // is the team eliminated?
	private Object[] certOfElim; // eliminated by who? -- this is Queue<String> certOfElim. (generic array) 
	private String[] numberToName; // maps numbers to teamnames.
	
	public BaseballElimination(String filename) {
		
		In readFile = new In(filename);
		String[] allLines = readFile.readAllLines();
		teams = Integer.parseInt(allLines[0]);
		initializeArrays();
		
		// read in data
		for (int i = 1; i <= teams; i++) {
			allLines[i] = allLines[i].trim();
			String[] rowInfo = allLines[i].split("\\s+");
			names.put(rowInfo[0], i - 1);
			numberToName[i - 1] = rowInfo[0];
			wins[i - 1] = Integer.parseInt(rowInfo[1]);
			losses[i - 1] = Integer.parseInt(rowInfo[2]);
			remaining[i - 1] = Integer.parseInt(rowInfo[3]);
			
			for (int j = 0; j < teams; j++) {
				gamesVersus[i - 1][j] = Integer.parseInt(rowInfo[4 + j]);
			}
		}
		
		// for each team, first do a simple test (no maxflow needed). if it is not eliminated, do a harder test (maxflow)
		for (int i = 0; i < teams; i++) {
			doSimpleTest(i);
			if (!eliminated[i])
				testElimination(i);	
		}
		
	}
	
	private void initializeArrays() {
		names = new HashMap<String, Integer>();
		wins = new int[teams];
		losses = new int[teams];
		remaining = new int[teams];
		gamesVersus = new int[teams][teams];
		eliminated = new boolean[teams];
		certOfElim = new Object[teams]; // couldn't create generic array. instead, cast to Queue<String>, whenever it needs to.
		numberToName = new String[teams];
	}
	
	/*	<content,   vertex number,                                                                          alias>
	 *  source 		0																						"source"
	 *  game vertex 1... (teams - 1) * (teams - 2) / 2														"0-1", "0-2"...
	 *  team vertex (teams - 1) * (teams - 2) / 2 + 1 ... (teams - 1) * (teams - 2) / 2 + (teams - 1)		"0", "1"...
	 *  sink	    (teams - 1) * (teams - 2) / 2 + (teams - 1) + 1											"sink"
	 */
	private void testElimination(int teamNo) {
		// note that the vertex for a team indicated by 'teamNo' is excluded in this flow network. (see assignment description.)
		
		int numOfVertex = 1 + (teams - 1) * (teams - 2) / 2 + (teams - 1) + 1;
		
		HashMap<String, Integer> stringToVertex = new HashMap<String, Integer>();	// maps string(alias for vertex) to vertex
		String[] vertexToString = new String[numOfVertex]; // maps vertex to string(alias for vertex)
		
		// map "source" to vertex 0, mutually.
		stringToVertex.put("source", 0);
		vertexToString[0] = "source";
		
		// map game vertex to its alias, mutually.
		int k = 1;
		for (int j = 0; j < teams; j++) {
			if (j == teamNo)
				continue;
			for (int i = j + 1; i < teams; i++) {
				if (i == teamNo)
					continue;
				StringBuilder temp = new StringBuilder();
				temp.append(j);
				temp.append("-");
				temp.append(i);
				stringToVertex.put(temp.toString(), k);
				vertexToString[k++] = temp.toString();
			}
		}
		
		// map team vertex to its alias, mutually.
		for (int i = 0; i < teams; i++) {
			if (i == teamNo)
				continue;
			StringBuilder temp = new StringBuilder();
			temp.append(i);
			stringToVertex.put(temp.toString(), k);
			vertexToString[k++] = temp.toString();
		}
		
		// map "sink" to last vertex, mutually.
		stringToVertex.put("sink", k);
		vertexToString[1 + (teams - 1) * (teams - 2) / 2 + (teams - 1)] = "sink";

		// initialize flow network with these vertices.
		FlowNetwork flowNet = new FlowNetwork(numOfVertex);
		
		// for game vertices...
		for (int i = 1; i <= (teams - 1) * (teams - 2) / 2; i++) {
			// first, add an edge from source to each game vertex. gamesLeft() uses the alias to read proper capacity from gamesVersus[][].
			flowNet.addEdge(new FlowEdge(stringToVertex.get("source"), i, gamesLeft(vertexToString[i])));
			// second, add two edges from each game vertex to its corresponding teams (which is two).
			String[] twoTeams = targetTeams(vertexToString[i]);
			flowNet.addEdge(new FlowEdge(i, stringToVertex.get(twoTeams[0]), Double.POSITIVE_INFINITY));
			flowNet.addEdge(new FlowEdge(i, stringToVertex.get(twoTeams[1]), Double.POSITIVE_INFINITY));
		}
		
		// for team vertices...
		for (int i = 0; i < teams; i++) {
			if (i == teamNo)
				continue;
			// add an edge from each team vertex to sink vertex. the cases that (wins[teamNo] + remaining[teamNo] - wins[i] < 0) is excluded by first doSimpleTest().
			// so there will be no exception.
			flowNet.addEdge(new FlowEdge(stringToVertex.get("" + i), stringToVertex.get("sink"), wins[teamNo] + remaining[teamNo] - wins[i]));
		}
		
		// do FF algorithm.
		FordFulkerson ff = new FordFulkerson(flowNet, 0 , stringToVertex.get("sink"));
		
		// check if all edges from source is full. if it is not full, the team is eliminated. (see assignment description for detail.)
		for (FlowEdge e : flowNet.adj(0)) {
			if (e.flow() != e.capacity()) {
				eliminated[teamNo] = true;
				break;
			}
		}
		
		// if it is eliminated, make a certification of elimination (a queue that indicates by who it is eliminated.)
		if (eliminated[teamNo] == true) {
			certOfElim[teamNo] = new Queue<String>();
			for (int i = 0; i < teams; i++) {
				if (i == teamNo)
					continue;
				if (ff.inCut(stringToVertex.get("" + i)))
					((Queue<String>) certOfElim[teamNo]).enqueue(numberToName[i]);
			}
		}
	}
	
	private void doSimpleTest(int teamNo) {
		for (int i = 0; i < teams; i++) {
			if (i == teamNo)
				continue;
			if (wins[teamNo] + remaining[teamNo] < wins[i]) {
				if (!eliminated[teamNo]) {
					eliminated[teamNo] = true;
					certOfElim[teamNo] = new Queue<String>();	
				}
				((Queue<String>) certOfElim[teamNo]).enqueue(numberToName[i]);				
			}
		}
	}
	
	
	private int gamesLeft(String nodename) {
		String[] temp = nodename.split("-");
		int first = Integer.parseInt(temp[0]);
		int second = Integer.parseInt(temp[1]);
		
		return gamesVersus[second][first];
	}
	
	private String[] targetTeams(String gamename) {
		String[] temp = gamename.split("-");
		return temp;
	}
	
	
	public int numberOfTeams() {
		return teams;
	}
	
	public Iterable<String> teams() {
		return names.keySet();
	}
	
	public int wins(String team) {
		if (!names.containsKey(team))
			throw new java.lang.IllegalArgumentException();
		return wins[names.get(team)];
	}
	
	public int losses(String team) {
		if (!names.containsKey(team))
			throw new java.lang.IllegalArgumentException();
		return losses[names.get(team)];
	}
	
	public int remaining(String team) {
		if (!names.containsKey(team))
			throw new java.lang.IllegalArgumentException();
		return remaining[names.get(team)];
	}
	
	public int against(String team1, String team2) {
		if (!names.containsKey(team1) || !names.containsKey(team2))
			throw new java.lang.IllegalArgumentException();
		return gamesVersus[names.get(team1)][names.get(team2)];
	}
	
	public boolean isEliminated(String team) {
		if (!names.containsKey(team))
			throw new java.lang.IllegalArgumentException();
		return eliminated[names.get(team)];
	}
	
	public Iterable<String> certificateOfElimination(String team) {
		if (!names.containsKey(team))
			throw new java.lang.IllegalArgumentException();
		return (Iterable<String>) certOfElim[names.get(team)];
	}
	
	/*
	public static void main(String[] args) {
	    BaseballElimination division = new BaseballElimination(args[0]);
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) {
	                StdOut.print(t + " ");
	            }
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
	}
	*/
}
