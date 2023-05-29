package darwin;

/**
 * A Position is an (x,y) coordinate in the World.
 */
public class Position {

	/** The North compass point. */
	public static final int NORTH = 0;

	/** The East compass point. */
	public static final int EAST = 1;

	/** The South compass point. */
	public static final int SOUTH = 2;

	/** The West compass point. */
	public static final int WEST = 3;

	private int x, y;

	/**
	 * Create a new Position object for the given x and y coordinates.
	 */
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Return the x coordinate for this Position object.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Return the y coordinate for this Position object.
	 */
	public int getY() {
		return y;
	}

	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	/**
	 * Return a new position that is in one of the four adjacent compass directions from
	 * this. 
	 * @pre direction must be NORTH, SOUTH, EAST, or WEST. 
	 * @post the Position adjecent to this in the given direction.
	 */
	public Position getAdjacent(int direction) {
		switch (direction) {
			case NORTH :
				return new Position(x, y - 1);
			case SOUTH :
				return new Position(x, y + 1);
			case EAST :
				return new Position(x + 1, y);
			case WEST :
				return new Position(x - 1, y);
		}
		return null;
	}
}
