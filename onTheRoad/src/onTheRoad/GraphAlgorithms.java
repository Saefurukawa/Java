package onTheRoad;

/**
 * Common algorithms for Graphs. 
 * They all assume working with a EdgeWeightedDirected graph.
 */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class GraphAlgorithms {

	/**
	 * Reverses the edges of a graph
	 * 
	 * @param g
	 *            edge weighted directed graph
	 * @return graph like g except all edges are reversed
	 */
	public static EdgeWeightedDigraph graphEdgeReversal(EdgeWeightedDigraph g) {
		//Create a new graph for reversal
		EdgeWeightedDigraph reverseGraph = new EdgeWeightedDigraph(g.V());
	

		//loop through each vertice in the original graph
		for (int i = 0; i <g.V(); i++){
			//Loop thorugh each edge incident from vertex i
			for (DirectedEdge edge : g.adj(i)){
				//Get where to
				int to = edge.to();
				//Get weight
				double weight = edge.weight();
				//Put a new edge in reverse graph
				reverseGraph.addEdge(new DirectedEdge(to, i, weight));
			}
		}

		return reverseGraph;
	}

	/**
	 * Performs breadth-first search of g from vertex start.
	 * 
	 * @param g
	 *            directed edge weighted graph
	 * @param start
	 *            index of starting vertex for search
	 */
	public static void breadthFirstSearch(EdgeWeightedDigraph g, int start) {
		//Reset the graph
		g.reset();
		//Use priority queue
		Deque <Integer> queue = new ArrayDeque<>();

		//Add a starting vertex to the queue
		queue.offer(start);
		//Create a edge from start to start, and visit the starting vertex
		g.visit(new DirectedEdge(start, start, 0.0), 0.0);

		//while queue is not empty
		while (!queue.isEmpty()){
			//Get the vertex at front of queue
			int top = queue.pollFirst();
		
			//Loop through all adjacent edges
			for (DirectedEdge edge : g.adj(top)){
				//get where to
				int w = edge.to();
				//If the destination vertex is not visited
				if (!g.isVisited(w)){
					//Update the weight
					double distance = g.getDist(top)+edge.weight();
					//Visit the edge
					g.visit(edge, distance);
					//Put the vertex on the queue
					queue.offer(w);
				}
			}

		}		
	}

	/**
	 * Calculates whether the graph is strongly connected
	 * 
	 * @param g
	 *            directed edge weighted graph
	 * @return whether graph g is strongly connected.
	 */
	public static boolean isStronglyConnected(EdgeWeightedDigraph g) {
		// do breadth-first search from start and make sure all vertices
		// have been visited. If not, return false.

		//Do bfs
		breadthFirstSearch(g, 0);

		//Check if all vertices are visited
		for (int i=0; i< g.V(); i++){
			if (!g.isVisited(i)){
				return false;
			}
		}
		
		// now reverse the graph, do another breadth-first search,
		// and make sure all visited again. If not, return false

		//Create a reversed diagraph
		EdgeWeightedDigraph reverse = graphEdgeReversal(g);
		//Do bfs again
		breadthFirstSearch(reverse, 0);

		//Check if all vertices are visited
		for (int i=0; i< reverse.V(); i++){
			if (!reverse.isVisited(i)){
				return false;
			}
		}
		return true;
	}

	/**
	 * Runs Dijkstra's algorithm on path to calculate the shortest path from
	 * starting vertex to every other vertex of the graph.
	 * 
	 * @param g
	 *            directed edge weighted graph
	 * @param s
	 *            starting vertex
	 * @return a hashmap where a key-value pair <i, path_i> corresponds to the i-th
	 *         vertex and path_i is an arraylist that contains the edges along the
	 *         shortest path from s to i.
	 */
	public static HashMap<Integer, ArrayList<DirectedEdge>> dijkstra(EdgeWeightedDigraph g, int s) {
		//reset graph
		g.reset();

		//Do dijkstra alogrithm
		DijkstraSP sp = new DijkstraSP(g,s);

		// //print the algorithm
		// for (int i=0; i <sp.distTo.length; i++){
		// 	System.out.println(sp.distTo[i]);
		// }
		// for (int i=0; i <sp.edgeTo.length; i++){
		// 	System.out.println(sp.edgeTo[i]);
		// }

		//Create a hashmap
		HashMap<Integer, ArrayList<DirectedEdge>> hashmap = new HashMap<Integer, ArrayList<DirectedEdge>>();

		//Loop thorugh each vertex
		for (int v = 0; v<g.V(); v++){
			//Create an arraylist
			ArrayList<DirectedEdge> arr = new ArrayList<DirectedEdge>();
			//Intialize pointer
			int pointer = v;
			//loop
			while(true){
				//Add the edge to the array
				arr.add(sp.edgeTo[pointer]);
				//Update the pointer
				int to = sp.edgeTo[pointer].from(); 
				pointer = to;
				//If the pointer reaches the starting index, quit
				if (pointer==s){
					break;
				}
			}

			hashmap.put(v, arr);

		}
		// for (Entry<Integer, ArrayList<DirectedEdge>> enter : hashmap.entrySet()){
		// 	System.out.print(enter.getKey());
		// 	System.out.print(enter.getValue());
		// }

		return hashmap;
	}
	private static class DijkstraSP {
		private double[] distTo; // distTo[v] = distance of shortest s->v path
 		private DirectedEdge[] edgeTo; // edgeTo[v] = last edge on shortest s->v path
		private IndexMinPQ<Double> pq; // priority queue of vertices

		//Constructor
		public DijkstraSP(EdgeWeightedDigraph g, int s) {
			//Create distTo and edgeTo arrays
			distTo = new double [g.V()];
			edgeTo = new DirectedEdge[g.V()];

			//Create a edge for the starting index
			DirectedEdge startingEdge = new DirectedEdge(s, s, 0.0);

			//Populate distTo array with positive infinity
			for (int v = 0; v<g.V(); v++){
				distTo[v] = Double.POSITIVE_INFINITY;
			}
			//distTo for the starting index is 0, while edgeTo is 0->0
			distTo[s] = 0.0;
			edgeTo[s] = startingEdge;

			//relax vertices in order of distance from s
			//Use priority queue
			pq = new IndexMinPQ<Double>(g.V());

			//Insert the starting index at priority queue
			pq.insert(s, distTo[s]);

			//Loop through as long as priority queue is not empty
			while (!pq.isEmpty()){
				int v = pq.delMin(); // select the next vertex with the smallest distance from the source vertex.
				//Loop through all ajacent vertices to the vertex and relax them all
				for (DirectedEdge edge : g.adj(v)){
					relax(edge);
				}
			}
		}
		/** 
		 * Edge relaxation
		 * @param e DirectedEdge
		*/
		private void relax(DirectedEdge e){
			int v = e.from(); //get where we are coming from
			int w= e.to(); //get where we are going to

			//If the new path is smaller than the existing path, 
			if (distTo[w]> distTo[v] + e.weight()){
				distTo[w]= distTo[v] + e.weight(); //Update the distTo[w]
				edgeTo[w] = e; //Update the edgeTo[w]

				//If priority contains the vertex w
				if (pq.contains(w)){
					pq.decreaseKey(w, distTo[w]); //Update the weight
				}
				//If not, simply inssert one
				else{
					pq.insert(w,distTo[w]);
				}
			}
		}

	}
	

	/**
	 * Computes shortest path from start to end using Dijkstra's algorithm.
	 *
	 * @param g
	 *            directed graph
	 * @param start
	 *            starting node in search for shortest path
	 * @param end
	 *            ending node in search for shortest path
	 * @return a list of edges in that shortest path in correct order
	 */
	public static ArrayList<DirectedEdge> getShortestPath(EdgeWeightedDigraph g, int start, int end) {
		//Run dijkstra and create a new ArrayList with edges running from start to end.
		HashMap<Integer, ArrayList<DirectedEdge>> hashmap = dijkstra(g, start);
		return hashmap.get(end);
	
	}

	/**
	 * Using the output from getShortestPath, print the shortest path
	 * between two nodes
	 * 
	 * @param path shortest path from start to end
	 * @param isDistance prints it based on distance (true) or time (false)
	 */
	public static void printShortestPath(ArrayList<DirectedEdge> path, boolean isDistance, List<String> vertices) {
		//Get the arraylist
		ArrayList <DirectedEdge> finalList = path;

		//Total weight
		double tw = 0.0;
		for (int i = 0; i < finalList.size(); i++){
			tw = tw + finalList.get(i).weight();
		}

		//Construct line to print it out
		String line = "";
		line = line + "Begins at "; 
		//Get the starting index.
		line = line + vertices.get(finalList.get(finalList.size()-1).from()) + "\n";

		//Calculate by distance
		if (isDistance){
			//Provide all vertices to visit
			for (int i= finalList.size()-1; i>=0; i--){
				line = line + "Continues at ";
				line = line + vertices.get(finalList.get(i).to()) + " (" + finalList.get(i).weight()+")" + "\n";
			}
			//Give total distance
			line = line + "Total distance: " + tw + " miles";
		}
		//Calculate by time
		else{
			//Provide all vertices to visit
			for (int i= finalList.size()-1; i>=0; i--){
				line = line + "Continues at ";
				line = line + vertices.get(finalList.get(i).to()) + " (" + hoursToHMS(finalList.get(i).weight())+")" + "\n";
			}
			//Give total time
			line = line + "Total time: "  + hoursToHMS(tw);
		}

		System.out.println(line);

	}

	/**
	 * Converts hours (in decimal) to hours, minutes, and seconds
	 * 
	 * @param rawhours
	 *            time elapsed
	 * @return Equivalent of rawhours in hours, minutes, and seconds (to nearest
	 *         10th of a second)
	 */
	private static String hoursToHMS(double rawhours) {
		//Printing out hours
		String str = "";
		str = str + (int) rawhours + " hours ";

		//Printint out minutes
		double minutes = rawhours - (int) rawhours;
		minutes = minutes * 60;
		str = str + (int) minutes + " minutes ";

		//Printing out seconds
		double seconds = minutes - (int) minutes;
		seconds = seconds * 60;
		str = str + (int) seconds + " seconds";

		return str;
	}
}
