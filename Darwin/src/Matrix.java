package darwin;

import java.util.ArrayList;

/**
 * Class representing a two dimensional square matrix
 * When you instantiate objects of type Matrix, keep in mind that Matrix works with generics. 
 * In our case, a Matrix will eventually hold Creatures, that is Matrix<Creature>.
 * The syntax for instantiating a Matrix of Creatures will be similar to instantiating ArrayLists of Booleans, as we saw them in lab1.
 */
public class Matrix<E> {
	
	// representation of matrix as a list of lists
	private ArrayList<ArrayList<E>> rep;

	// number of rows and columns in the matrix
	private int numRows, numCols;
	
	//Create an empty matrix
	public Matrix() {
		this(0,0);
	}

	// Create a new matrix with numRows rows and numCols columns
	public Matrix(int numRows, int numCols) {
		this.numRows = numRows;
		this.numCols = numCols;
		rep = new ArrayList<ArrayList<E>>(numRows);
		for (int row = 0; row < numRows; row++) {
			rep.add(new ArrayList<E>(numCols));
			for (int col = 0; col < numCols; col++) {
				rep.get(row).add(null);
			}
		}
	}

	// Set the entry at (row,col) to newValue.
	public void set(int row, int col, E newValue) {
		checkRowCol(row, col);
		rep.get(row).set(col, newValue);
	}

	// Return the entry at (row,col)
	public E get(int row, int col) {
		checkRowCol(row, col);
		return rep.get(row).get(col);
	}

	// Return the number of rows in the matrix
	public int numRows() {
		return numRows;
	}

	// Return the number of columns in the matrix
	public int numCols() {
		return numCols;
	}
	
	private void checkRowCol(int row, int col) {
		if (row < 0 || row >= numRows) {
			throw new IllegalArgumentException("Row out of bounds");
		}
		
		if (col < 0 || col >= numCols) {
			throw new IllegalArgumentException("Col out of bounds");
		}
	}

}
