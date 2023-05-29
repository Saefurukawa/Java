package compression;

/**
 * TwoDTable.java Interface representing a 2-dimensional table
 *
 * @param <ValueType>
 */

interface TwoDTable<ValueType> {

	/**
	 * Updates contents of specified cell
	 * 
	 * @pre: (row,col) is legal cell in table
	 * 
	 * @post: (row,col) cell now holds newInfo
	 * 
	 * @param row
	 *            row of cell to be updated
	 * @param col
	 *            column of cell to be update
	 * @param newInfo
	 *            new value to place in cell (row, col)
	 */
	public void updateInfo(int row, int col, ValueType newInfo);

	/**
	 * Returns contents of specified cell
	 * 
	 * @pre: (row,col) is legal cell in table
	 * 
	 * @param row
	 *            row of cell to be queried
	 * @param col
	 *            column of cell to be queried
	 * @return value stored in (row, col) cell of table
	 */
	public ValueType getInfo(int row, int col);

	/**
	 * @return a string representation of the entire table
	 */
	public String entireTable();

}
