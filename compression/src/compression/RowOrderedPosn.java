package compression;

// Class of 2-Dimensional table subscripts which are ordered first by row and then column

class RowOrderedPosn{
	private int row, col;			// row and column of entry
	private int numRows, numCols; 	// total number of rows and columns in table

	/**
	 *  Constructor for position at (row,col) in table with numRows rows and numCols columns.
	 * @param row	row number
	 * @param col	column number
	 * @param numRows total number of rows in table
	 * @param numCols total number of columns in table
	 */
	public RowOrderedPosn(int row,int col,int numRows,int numCols){
		this.row = row;
		this.col = col;
		this.numRows = numRows;
		this.numCols = numCols;
	}	
	
	/**
	 * @return row corresponding to this position
	 */
	public int getRow(){
		return row;
	}
	
	/**
	 * @return column corresponding to this position
	 */
	public int getCol(){
		return col;
	}
	

	
	/**
	 * @return next position in scanning from left to right across successive rows
	 * if already at last position then return null
	 */
	public RowOrderedPosn next(){
		int newRow, newCol;
		if (col == numCols -1){
			if (row == numRows-1) return null;  // already at last position
			newCol = 0;
			newRow = row + 1;
		} else {
			newCol = col + 1;
			newRow = row;
		}
		return new RowOrderedPosn(newRow,newCol,numRows,numCols);
	}
	
	/**
	 *  @pre: other != null
	 * @param other  position to be compared with this one
	 * @return true iff this position comes before other.
	 */
	public boolean less(RowOrderedPosn other){
		return (row < other.getRow()) || (row == other.getRow() && col < other.getCol());
	}
	
	/**
	 *  @pre: other != null
	 * @param other  position to be compared with this one
	 * @return true iff this position comes after other.
	 */
	public boolean greater(RowOrderedPosn other){
		return !(equals(other) || less(other));
	}
	/**
	 * @pre: other != null
	 * @param other  position to be compared with this one
	 * @return true iff this position has same row and column as other.
	 */
	public boolean equals(Object other){
		if (other instanceof RowOrderedPosn) {
			RowOrderedPosn otherPos = (RowOrderedPosn) other;
			return (row == otherPos.getRow()) && (col == otherPos.getCol());			
		} else {
			return false;
		}
	}
		
	/**
	 *  @return description of position
	 */
	public String toString(){
		return "Position: ("+row+","+col+")";
	}
}	

