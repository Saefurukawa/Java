package darwin;

import java.io.*;
import java.util.ArrayList;

/**
 * The individual creatures in the world are all representatives of some
 * species class and share certain common characteristics, such as the species
 * name and the program they execute. Rather than copy this information into
 * each creature, this data can be recorded once as part of the description for
 * a species and then each creature can simply include the appropriate species
 * reference as part of its internal data structure.
 * 
 * Note: The instruction addresses start at one, not zero.
 */
public class Species {
	private String name;
	private String color;
	private char speciesChar; // the first character of Species name
	private ArrayList<Instruction> program;

	/**
	 * Create a species for the given fileReader. 
	 */
	public Species(BufferedReader fileReader) {
		
		//Create the arraylist for instructions
		program = new ArrayList <Instruction>();

		try {
				int i = 0;
				while (true){

					//Read line by line
					String br = fileReader.readLine();
					if (br.equals("")){ //If it hits an empty line, stop reading
						break;
					}
					
					else{

						if (i==0){
							name = br; //First line reads name of the species
							speciesChar = name.charAt(0); //Get the first character of the name
						}
						else if (i == 1){ //Second line reads color of the species
							color = br;
						}
						else {

							//intialize variables
							int optcode = 0;
							int address = 0;
							Instruction instr;
							
							//Separate the instruction from the address
							if (br.contains(" ")){
								String[] list = br.split(" ");
								br = list[0];
								address = Integer.parseInt(list[1]);
							}

							//Converting the instruction to optcode
							if (br.toUpperCase().equals("HOP")){ 
								optcode = 1;
							}
							else if (br.toUpperCase().equals("LEFT")){
								optcode = 2;
							}
							else if (br.toUpperCase().equals("RIGHT")){
								optcode = 3;
							}
							else if (br.toUpperCase().equals("INFECT")){
								optcode = 4;
								
							}
							else if (br.toUpperCase().equals("IFEMPTY")){
								optcode = 5;
							}
							else if (br.toUpperCase().equals("IFWALL")){
								optcode =6;

							}
							else if (br.toUpperCase().equals("IFSAME")){
								optcode = 7;
							}
							else if (br.toUpperCase().equals("IFENEMY")){
								optcode = 8;

							}
							else if (br.toUpperCase().equals("IFRANDOM")){
								optcode = 9;

							}
							else if (br.toUpperCase().equals("GO")){
								optcode = 10;
							}
							else if (br.toUpperCase().equals("IF2ENEMY")){
								optcode = 11;
							}

							//Create the instruction object without the address
							if (address == 0){
								instr = new Instruction(optcode);
							}

							//Create the instruction object with the address
							else{
								instr = new Instruction(optcode, address);
							}

							program.add(instr); //Add the instruction to the arraylist

							}
					
						i++;
					}
				
					
			
				} 
		
		}catch (IOException e) {
			System.out.println(
					"Could not read file '"
						+ fileReader
						+ "'");
				System.exit(1);
		}
	}


	/**
	* Return the char for the species
	*/
	public char getSpeciesChar() {
		return speciesChar;		
	}

	/**
	 * Return the name of the species.
	 */
	public String getName() {
		return name;    
	}

	/**
	 * Return the color of the species.
	 */
	public String getColor() {
		return color;    
	}

	/**
	 * Return the number of instructions in the program.
	 */
	public int programSize() {
		return program.size();    
	}

	/**
	 * Return an instruction from the program.
	 * @pre 1 <= i <= programSize().
	 * @post returns instruction i of the program.
	 */
	public Instruction programStep(int i) {
		return program.get(i-1);    
	}

	/**
	 * Return a String representation of the program.
	 * 
	 * do not change
	 */
	public String programToString() {
		String s = "";
		for (int i = 1; i <= programSize(); i++) {
			s = s + (i) + ": " + programStep(i) + "\n";
		}
		return s;
	}

}
