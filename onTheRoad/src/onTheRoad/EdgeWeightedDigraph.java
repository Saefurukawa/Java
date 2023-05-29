/******************************************************************************
 *  Dependencies: Bag.java DirectedEdge.java *
 *  An edge-weighted digraph, implemented using adjacency lists.
 *
 ******************************************************************************/
package onTheRoad;

/**
 *  The {@code EdgeWeightedDigraph} class represents a edge-weighted
 *  digraph of vertices named 0 through <em>V</em> - 1, where each
 *  directed edge is of type {@link DirectedEdge} and has a real-valued weight.
 *  It supports the following two primary operations: add a directed edge
 *  to the digraph and iterate over all of edges incident from a given vertex.
 *  It also provides methods for returning the indegree or outdegree of a
 *  vertex, the number of vertices <em>V</em> in the digraph, and
 *  the number of edges <em>E</em> in the digraph.
 *  Parallel edges and self-loops are permitted.
 *  <p>
 *  This implementation uses an <em>adjacency-lists representation</em>, which
 *  is a vertex-indexed array of {@link Bag} objects.
 *  It uses &Theta;(<em>E</em> + <em>V</em>) space, where <em>E</em> is
 *  the number of edges and <em>V</em> is the number of vertices.
 *  All instance methods take &Theta;(1) time. (Though, iterating over
 *  the edges returned by {@link #adj(int)} takes time proportional
 *  to the outdegree of the vertex.)
 *  Constructing an empty edge-weighted digraph with <em>V</em> vertices
 *  takes &Theta;(<em>V</em>) time; constructing an edge-weighted digraph
 *  with <em>E</em> edges and <em>V</em> vertices takes
 *  &Theta;(<em>E</em> + <em>V</em>) time. 
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class EdgeWeightedDigraph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;                // number of vertices in this digraph
    private int E;                      // number of edges in this digraph
    private Bag<DirectedEdge>[] adj;    // adj[v] = adjacency list for vertex v
    private int[] indegree;             // indegree[v] = indegree of vertex v
    private boolean[] marked;  // marked[v] = is there an s->v path?
    private DirectedEdge[] edgeTo;      // edgeTo[v] = last edge on shortest s->v path
    private double[] distTo;      // distTo[v] = length of shortest s->v path

    
    /**
     * Initializes an empty edge-weighted digraph with {@code V} vertices and 0 edges.
     *
     * @param  V the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public EdgeWeightedDigraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        this.V = V;
        this.E = 0;
        this.indegree = new int[V];
        adj = (Bag<DirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<DirectedEdge>();
        this.marked = new boolean[V];
        this.distTo = new double[V];
        this.edgeTo = new DirectedEdge[V];
    }

    /**
     * Returns the number of vertices in this edge-weighted digraph.
     *
     * @return the number of vertices in this edge-weighted digraph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in this edge-weighted digraph.
     *
     * @return the number of edges in this edge-weighted digraph
     */
    public int E() {
        return E;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Adds the directed edge {@code e} to this edge-weighted digraph.
     *
     * @param  e the edge
     * @throws IllegalArgumentException unless endpoints of edge are between {@code 0}
     *         and {@code V-1}
     */
    public void addEdge(DirectedEdge e) {
        int v = e.from();
        int w = e.to();
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        indegree[w]++;
        E++;
    }

    /**
     * Returns the directed edges incident from vertex {@code v}.
     *
     * @param  v the vertex
     * @return the directed edges incident from vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<DirectedEdge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    /**
     * Returns the number of directed edges incident from vertex {@code v}.
     * This is known as the <em>outdegree</em> of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the outdegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int outdegree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    /**
     * Returns the number of directed edges incident to vertex {@code v}.
     * This is known as the <em>indegree</em> of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the indegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int indegree(int v) {
        validateVertex(v);
        return indegree[v];
    }

    /**
     * Returns all directed edges in this edge-weighted digraph.
     * To iterate over the edges in this edge-weighted digraph, use foreach notation:
     * {@code for (DirectedEdge e : G.edges())}.
     *
     * @return all edges in this edge-weighted digraph, as an iterable
     */
    public Iterable<DirectedEdge> edges() {
        Bag<DirectedEdge> list = new Bag<DirectedEdge>();
        for (int v = 0; v < V; v++) {
            for (DirectedEdge e : adj(v)) {
                list.add(e);
            }
        }
        return list;
    } 
    
    /**
     * Resets the auxiliary arrays in this edge-weighted graph.
     * 
     */
    public void reset() {
        this.marked = new boolean[V];
        this.distTo = new double[V];
        this.edgeTo = new DirectedEdge[V];
    }
    
/**
     * Marks destination vertex as visited and updates its distance
     * @param e
     * @param distance
     */
    public void visit(DirectedEdge e, double distance) {
    	marked[e.to()] = true;
    	distTo[e.to()] = distance;
    	edgeTo[e.to()] = e;
    }
    
    /**
     * Confirms whether vertex v has been marked as visited
     * @param v
     * @return true if v has been marked, false otherwise
     */
    public boolean isVisited(int v) {
    	return marked[v];
    }

    /**
     * Returns the value of distTo at index v
     * @param v
     * @return the value of distTo at index v
     */
    public double getDist(int v) {
    	return distTo[v];
    }
    
    /**
     * Sets the value weight of distTo at index v
     */
    public void setDist(int v, double weight) {
    	distTo[v] = weight;
    }
    
    /**
     * Sets the edge from v to w
     */
    public void setEdgeTo(DirectedEdge e) {
    	edgeTo[e.to()] = e;
    }
    
    /**
     * Gets the edge from v
     */
    public DirectedEdge getEdgeTo(DirectedEdge e) {
    	return edgeTo[e.from()];
    }
    
    /**
     * Gets the edge from v
     */
    public DirectedEdge getEdgeTo(int v) {
    	return edgeTo[v];
    }
    
    /**
     * Returns a string representation of this edge-weighted digraph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *         followed by the <em>V</em> adjacency lists of edges
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (DirectedEdge e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    /**
     * Unit tests the {@code EdgeWeightedDigraph} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(10);
        G.addEdge(new DirectedEdge(0, 1, 1.5));
        G.addEdge(new DirectedEdge(3, 4, 2.5));
        G.addEdge(new DirectedEdge(1, 5, 5));
        G.addEdge(new DirectedEdge(5, 1, 10));
        G.addEdge(new DirectedEdge(0, 9, 1.5));
        System.out.println(G);
    }

}
