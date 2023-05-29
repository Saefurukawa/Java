package onTheRoad;
/**
 * Testing code for graph algorithms
 */

import java.util.ArrayList;
import java.util.List;

public class TestGraphs {
	/**
	 * Testing code for graph algorithms
	 * @param args  ignored
	 */
	public static void main(String[] args) {
		// Build a test graph
		EdgeWeightedDigraph testGraph = new EdgeWeightedDigraph(4);
		List<String> vertices = new ArrayList<String>();
		vertices.add("Irvine");
		vertices.add("Costa Mesa");
		vertices.add("Chino Hills");
		vertices.add("Claremont");
		testGraph.addEdge(new DirectedEdge(0, 1, 1.0));
		testGraph.addEdge(new DirectedEdge(0, 2, 1.0));
		testGraph.addEdge(new DirectedEdge(1, 3, 3.0));
		testGraph.addEdge(new DirectedEdge(2, 3, 2.0));
		System.out.println(testGraph);
		// rev is graph with reversed edges
		EdgeWeightedDigraph rev = GraphAlgorithms.graphEdgeReversal(testGraph);
		System.out.println(rev);
		// print out edges in reversed graph
		for (DirectedEdge edge: rev.edges()) {
			System.out.println(edge + " " + edge.weight());
		}
		
		// Run breadth-first search and see if everything visited
		GraphAlgorithms.breadthFirstSearch(testGraph, 0);
		for (int vertex=0; vertex<testGraph.V(); vertex++) {
			System.out.println(vertex + " is visited: "
					+ testGraph.isVisited(vertex));
		}
		// Check to see if testGraph is strongly-connected.
		if (GraphAlgorithms.isStronglyConnected(testGraph)) {
			System.out.println("Strongly connected");
		} else {
			System.out.println("not strongly connected");
		}
		// Add another vertex and an edges
		testGraph.addEdge(new DirectedEdge(3,0, 3.0));
		// See if it is now strongly connected
		if (GraphAlgorithms.isStronglyConnected(testGraph)) {
			System.out.println("Strongly connected");
		} else {
			System.out.println("not strongly connected");
		}
		// Find the shortest path from "a" to "e" and print it.
		ArrayList<DirectedEdge> result = 
				GraphAlgorithms.getShortestPath(testGraph, 0, 3); 
		System.out.println("Shortest path from "+ vertices.get(0) + " to " + vertices.get(3) + " is");
		GraphAlgorithms.printShortestPath(result, true, vertices);
		GraphAlgorithms.printShortestPath(result, false, vertices);
	}

/*
4 4
0: 0->2  1.00  0->1  1.00  
1: 1->3  3.00  
2: 2->3  2.00  
3: 

3->2  2.00 2.0
3->1  3.00 3.0
2->0  1.00 1.0
1->0  1.00 1.0
0 is visited: true
1 is visited: true
2 is visited: true
3 is visited: true
not strongly connected
Strongly connected
Shortest path from Irvine to Claremont is
 Begin at Irvine
 Continue to Chino Hills (1.0)
 Continue to Claremont (2.0)
Total distance: 3.0 miles
 Begin at Irvine
 Continue to Chino Hills (1 hours 0 minutes 0.0 seconds)
 Continue to Claremont (2 hours 0 minutes 0.0 seconds)
Total time: 3 hours 0 minutes 0.0 seconds
*/
}
