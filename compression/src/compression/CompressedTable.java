/** 
 * Class representing an efficient implementation of a 2-dimensional table 
 * when lots of repeated entries as a doubly linked list. Idea is to record entry only when a 
 * value changes in the table as scan from left to right through 
 * successive rows.
 * 
 * @author cs62
 * @param <E> type of value stored in the table
 */
package compression;

class CompressedTable<E> implements TwoDTable<E> {
	// List holding table entries - do not change
	// We've made the variables protected to facilitate testing (grading)
	protected CurDoublyLinkedList<Association<RowOrderedPosn, E>> tableInfo;
	protected int numRows, numCols; // Number of rows and cols in table

	/**
	 * Constructor for table of size rows x cols, all of whose values are initially
	 * set to defaultValue
	 * 
	 * @param rows
	 *            # of rows in table
	 * @param cols
	 *            # of columns in table
	 * @param defaultValue
	 *            initial value of all entries in table
	 */
	public CompressedTable(int rows, int cols, E defaultValue) {
		numRows = rows;
		numCols = cols;
		tableInfo = new CurDoublyLinkedList<>();
		RowOrderedPosn pos = new RowOrderedPosn(0, 0, numRows, numCols);
		Association <RowOrderedPosn,E> as = new Association<RowOrderedPosn,E>(pos, defaultValue); 
		tableInfo.add(as);
		}

	/**
	 * Given a (x, y, rows, cols) RowOrderedPosn object, it searches for it in the
	 * table which is represented as a doubly linked list with a current pointer. If
	 * the table contains the (x,y) cell, it sets the current pointer to it.
	 * Otherwise it sets it to the closest cell in the table which comes before that
	 * entry.
	 * 
	 * e.g., if the table only contains a cell at (0,0) and you pass the cell (3,3)
	 * it will set the current to (0,0).
	 */
	private void find(RowOrderedPosn findPos) {
		tableInfo.first();
		Association<RowOrderedPosn, E> entry = tableInfo.currentValue(); // value set to null 
		RowOrderedPosn pos = tableInfo.currentValue().getKey();
		while (!findPos.less(pos)) {
			// search through list until pass elt looking for
			tableInfo.next();
			if (tableInfo.isOff()) {
				break;
		
			}
			entry = tableInfo.currentValue();
			pos = entry.getKey();
		}

		tableInfo.back(); // Since passed desired entry, go back to it.
	}

	/**
	 * Given a legal (row, col) cell in the table, update its value to newInfo. 
	 * 
	 * @param row
	 *            row of cell to be updated
	 * @param col
	 *            column of cell to be update
	 * @param newInfo
	 *            new value to place in cell (row, col)
	 */
	public void updateInfo(int row, int col, E newInfo) {

		// Check if the user input is valid
		if((row >= numRows) || (col >= numCols)|| (row < 0) || (col < 0)){
			return;
		}

		// Make a new posititon object
		RowOrderedPosn pos = new RowOrderedPosn(row, col, numRows, numCols);
		find(pos);

		// Our position object at the current pointer
		RowOrderedPosn tablePos = tableInfo.currentValue().getKey();
		E oldInfo = tableInfo.currentValue().getValue();
 
		// If the information to update equals the existing info, dont do anything
		if (oldInfo.equals(newInfo)){
				return;
			}

		//If the user input matches the current pointer item
		else if (tablePos.equals(pos)){
				// getting the next node
				tableInfo.next();

				//If there is no next node
				if (tableInfo.isOff()){

					//Bring back the pointer
					tableInfo.back();

					//Access the previous value 
					tableInfo.back();

					//If there is no previous node
					if (tableInfo.isOff()){

						//Bring back the pointer
						tableInfo.next();

						//Just update the value
						tableInfo.currentValue().setValue(newInfo);

						//If it is not at the last position on the table, meaning there is a gap
						if (pos.next()!=null){
							//Fill the gap with old info
							fillGapAfterCurrent (pos, oldInfo);
						}

						return;
					}

					//If there is a previous node, get the previous node
					Association<RowOrderedPosn, E> prevNode = tableInfo.currentValue();

					//Bring back the pointer
					tableInfo.next();

					// if the user input equals the previous node value, 
					if (newInfo.equals(prevNode.getValue())){

						//remove the current node
						tableInfo.removeCurrent();

						///If it is not at the last position on the table
						if (pos.next()!=null){
							fillGapAfterCurrent (pos, oldInfo);
						}

					}
					//if not the same
					else{
						//Justupdate the info at the current pointer
						tableInfo.currentValue().setValue(newInfo);

						//If it is not at the last position on the table
						if (pos.next()!=null){
							fillGapAfterCurrent (pos, oldInfo);
						}
					}
				}

				//If there is a next node
				else{
					//Get next node
					Association<RowOrderedPosn, E> nextNode = tableInfo.currentValue();
					tableInfo.back();

					//Still update the info at the current pointer
					tableInfo.currentValue().setValue(newInfo);

					//If there is no gap between the current node and the next node
					if (noGap(tableInfo.currentValue(), nextNode)){

						//If the user's input equals the value of the next node
						if(newInfo.equals(nextNode.getValue())){
							//compress, meaning deleting the next node
							tableInfo.next();
							tableInfo.removeCurrent();
							tableInfo.back();
						}

					}

					//If there is a gap between the current node and the next node
					else{
						fillGapAfterCurrent (pos, oldInfo);
					}

					//Access the previous value 
					tableInfo.back();

					//If there is no previous node
					if (tableInfo.isOff()){
						//Bring back the pointer
						tableInfo.next();

						return;
					}

					//If there is a previos node, get the value
					Association<RowOrderedPosn, E> prevNode = tableInfo.currentValue();
					tableInfo.next();

					// if the user input equals the previous node value, remove the current node
					if (newInfo.equals(prevNode.getValue())){
						tableInfo.removeCurrent();
						return;
					}

				}
		}

		//If the current pointer is before where the user wants to update
		else if (tablePos.less(pos)){

			//Add a new node for the user's input
			Association<RowOrderedPosn, E> newNode = new Association<RowOrderedPosn, E>(pos, newInfo);
			tableInfo.addAfterCurrent(newNode);
			
			// getting the next node
			tableInfo.next();

			//If there is no next node
			if (tableInfo.isOff()){ //this is not running
				tableInfo.back();

				//If it is not at the last position on the table
				if (pos.next()!=null){
					fillGapAfterCurrent (pos, oldInfo);
				}

			}

			//If there is the next node
			else{
				//Get next node
				Association<RowOrderedPosn, E> nextNode = tableInfo.currentValue();
				tableInfo.back();

				//If there is no gap between the current node and the next node
				if (noGap(newNode, nextNode)){
					//If the next node has the same value, 
					if(newInfo.equals(nextNode.getValue())){
						//compress to the right
						tableInfo.next();
						tableInfo.removeCurrent();
						tableInfo.back();
					}

				}
				//If there is a gap,
				else{
					fillGapAfterCurrent (pos, oldInfo);
				}
			}


		}
	}

	/**
	 * returns true if two nodes are right next to each other on the table
	 * @param current
	 * @param currentNext
	 * @return boolean
	 */
	public boolean noGap (Association<RowOrderedPosn, E> current, Association<RowOrderedPosn, E> currentNext){
		return current.getKey().next().equals(currentNext.getKey());
	}

	/**
	 * If there is a gap between the user's position and the next node, it fills the gap with the old information. It also moves back the
	 * current pointer to the user's position
	 * @param pos
	 * @param oldInfo
	 */
	public void fillGapAfterCurrent (RowOrderedPosn pos, E oldInfo){
		Association<RowOrderedPosn, E> oldNode = new Association<RowOrderedPosn, E>(pos.next(), oldInfo);
		tableInfo.addAfterCurrent(oldNode);
		tableInfo.back();
	}


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
	public E getInfo(int row, int col) {
		RowOrderedPosn pos = new RowOrderedPosn(row, col, numRows, numCols);
		find(pos);
		return tableInfo.currentValue().getValue();
		
	}

	/**
	 *  @return
	 *  		 succinct description of contents of table
	 */
	public String toString() { // do not change
	    return tableInfo.otherString();
	}

	public String entireTable() { //do not change
		StringBuilder ans = new StringBuilder("");
		for (int r = 0; r<numRows; r++) {
			for (int c = 0; c< numCols; c++) {
				ans.append(this.getInfo(r, c));
			}
			ans.append("\n");
		}
		return ans.toString();

	}

	/**
	 * program to test implementation of CompressedTable
	 * @param args
	 * 			ignored, as not used in main
	 */
	public static void main(String[] args) {
		
		// add your own tests to make sure your implementation is correct!!
		CompressedTable<String> table = new CompressedTable<String>(3, 3, "r");
		System.out.println("table is" + table);
		System.out.println("entire table is: \n" + table.entireTable());
		table.updateInfo(2, 1, "b");
		System.out.println("table is " + table);
		System.out.println("entire table is: \n" + table.entireTable());
		table.updateInfo(2, 2, "a");
		System.out.println("table is " + table);
		System.out.println("entire table is: \n" + table.entireTable());
		
		table.updateInfo(2, 0, "c");
		System.out.println("entire table is: \n" + table.entireTable());
		System.out.println("table is " + table);
		table.updateInfo(1, 0, "b");
		System.out.println("entire table is: \n" + table.entireTable());
		System.out.println("table is " + table);
		table.updateInfo(1, 1, "b");
		System.out.println("entire table is: \n" + table.entireTable());
		System.out.println("table is " + table);
		table.updateInfo(2, 0, "x");
		System.out.println("entire table is: \n" + table.entireTable());
		System.out.println("table is " + table);
		table.updateInfo(2, 1, "x");
		System.out.println("entire table is: \n" + table.entireTable());
		System.out.println("table is " + table);
		table.updateInfo(2, 2, "x");
		System.out.println("entire table is: \n" + table.entireTable());
		System.out.println("table is " + table);
		table.updateInfo(2, 1, "a");
		System.out.println("entire table is: \n" + table.entireTable());
		System.out.println("table is " + table);
		table.updateInfo(2, 0, "b");
		System.out.println("entire table is: \n" + table.entireTable());
		System.out.println("table is " + table);
		table.updateInfo(2, 1, "b");
		System.out.println("entire table is: \n" + table.entireTable());
		System.out.println("table is " + table);
		table.updateInfo(0, 1, "b");
		System.out.println("entire table is: \n" + table.entireTable());
		System.out.println("table is " + table);
		table.updateInfo(0, 2, "c");
		System.out.println("entire table is: \n" + table.entireTable());
		System.out.println("table is " + table);
		table.updateInfo(1, 0, "c");
		System.out.println("entire table is: \n" + table.entireTable());
		System.out.println("table is" + table);
		table.updateInfo(1, 1, "b");
		System.out.println("entire table is: \n" + table.entireTable());
		System.out.println("table is " + table);
		table.updateInfo(0, 2, "b");
		System.out.println("entire table is: \n" + table.entireTable());
		System.out.println("table is " + table);
		table.updateInfo(1, 0, "b");
		System.out.println("entire table is: \n" + table.entireTable());
		System.out.println("table is " + table);	
	}

}
