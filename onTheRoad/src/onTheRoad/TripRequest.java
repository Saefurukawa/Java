package onTheRoad;
/**
 * Class representing a trip request with start, end, and whether it
 * is optimizing distance or time.
 */
import java.util.List;

public class TripRequest {
	
	// Starting point of trip
	private int start;
	
	// Ending point of trip
	private int end;
	
	// Whether trip should optimize distance (instead of time)
	private boolean isDistance;
	
	/**
	 * Create trip request
	 * @param req 
	 *    String representing a textual request formatted with
	 *    the number of the start and end nodes, and a "D" if
	 *    we are to optimize the trip by distance, "T" if by time
	 * @param vertices
	 * 	A list of vertices of the graph so can look up label of nodes.
	 */
	public TripRequest(String req, List<String> vertices) {
		// TO DO: Check input to make sure it has requisite number of
		// pieces, that the indices are legal, and tag letter is "D" or "T"
		String[] reqPieces = req.split(" ");
		
		// index of start node
		int s = Integer.parseInt(reqPieces[0]);
		
		// index of end node
		int e = Integer.parseInt(reqPieces[1]);
		
		//validity check: vertex needs to be valid
		if ((e > 0)&&(e < vertices.size())&&(s >0)&&(s<vertices.size())){ 
			start = s;
			end = e;
		}
		
		//the input must be D or F
		if ((reqPieces[2].equals("D"))||(reqPieces[2].equals("T"))){
			// true iff optimize by distance
			isDistance = reqPieces[2].equals("D");
		}
	}
	
	/**
	 * @return index of starting node for trip
	 */
	public int getStart() {
		return start;
	}
	
	/** 
	 * @return index of ending node for trip
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * @return whether to optimize by distance (instead of time)
	 */
	public boolean isDistance() {
		return isDistance;
	}
	
	/**
	 * @return representation of trip as a string
	 */
	public String toString() {
		return "Request going from "+start+" to "+end+" by "
				+(isDistance ?"distance":"time");
	}
}
