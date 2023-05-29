package onTheRoad;

/**
 * Class representing a segment of a journey.  To be used in finding
 * shortest paths.
 */
public class Segment {
	// index of start and end nodes
	private int start;
	private int end;
	
	// distance and travel speed on segment
	private double distance;
	private double speed;
	
	/**
	 * String representing encoding of segment in graph
	 * Pre: Speed must be greater than 0
	 * @param segmentString
	 * 	String representing segment.  Has start and end indices
	 * as well as length (distance) and speed (for optimizing time)
	 */
	public Segment(String segmentString) {
		String[] segString = segmentString.split(" ");
		start = Integer.parseInt(segString[0]);
		end = Integer.parseInt(segString[1]);
		distance = Double.parseDouble(segString[2]);
		if (distance <= 0){
			throw new ArithmeticException("distance must be positive");
		}
		speed = Double.parseDouble(segString[3]);
		if (speed <= 0) {
			throw new ArithmeticException("speed must be positive");
		}
	}

	/**
	 * @return index of start node for segment
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @return index of end node for segment
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * @return length of segment
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * @return speed of segment
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * @return string representation of segment
	 */
	public String toString() {
		return "from "+start+" to "+end+" is "+getDistance()+" units"
				+" & goes at speed "+getSpeed();
	}

}
