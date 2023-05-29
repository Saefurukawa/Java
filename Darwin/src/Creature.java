package darwin;

import java.util.*;
import java.awt.Color;
import java.io.*;

/**
 * This class represents one creature on the board. Each creature must remember
 * its species, position, direction, and the world in which it is living.
 * In addition, the Creature must remember the next instruction out of its
 * program to execute.
 * The creature is also responsible for making itself appear in the WorldMap. In
 * fact, you should only update the WorldMap from inside the Creature class.
 */
public class Creature {

	private Species species;
	private World world;
	private Position pos; 
	private int dir;
	private int programCounter = 1;
	private Random random = new Random();

	/**
	 * Create a creature of the given species, with the indicated position and
	 * direction. Note that we also pass in the world-- remember this world, so
	 * that you can check what is in front of the creature and update the board
	 * when the creature moves.
	 */
	public Creature(Species species, World world, Position pos, int dir) {

		//Set the world representation and worldmap visual representation for the current creature
		WorldMap.displaySquare(pos, species.getSpeciesChar(), dir, species.getColor());
		world.set(pos, this);

		//Set values for the constructors
		this.species = species;
		this.world = world;
		this.pos = pos;
		this.dir = dir;
	}

	
	/**
	 * Return the species of the creature.
	 */
	public Species species() {
		return species;
	}

	/**
	 * Return the current direction of the creature.
	 */
	public int direction() {
		return dir;
	}

	/**
	 * Sets the current direction of the creature to the given value 
	 */
	public void setDirection(int dir){
		this.dir = dir;
	}

	/**
	 * Return the position of the creature.
	 */
	public Position position() {
		return pos;
	}

	/**
	 * Execute steps from the Creature's program
	 *   starting at step #1
	 *   continue until a hop, left, right, or infect instruction is executed.
	 */
	public void takeOneTurn() {

		//Get the adjacent position.
		Position newPos = pos.getAdjacent(dir);

		//Iliterate a while loop until it hits hop, left, right or infect instruction.
		int loop = 0;
		while (loop == 0){
			switch (species.programStep(programCounter).getOpcode()){ //get opcode for the given instruction
				
				//If the square in front is occupied by enemy, jump to the given address, and if not, continue to the next instruction.
				case Instruction.IFENEMY:				
					if (world.inRange(newPos) && (world.get(newPos) != null)){ //Check if the square is in range, and is occupied
						Creature enemy = world.get(newPos);
						if (enemy.species != species){ //Get the address to jump for if the species is an enemy
							programCounter = species.programStep(programCounter).getAddress();
						}
						else {
							programCounter++;
						}
					}
					else {
						programCounter++;
					}
					break;
				
				//Extra credit: If the square in front is occupied by enemy, jump to the given address, and if the square two steps ahead 
				//is occupied by enemy, jump to the given address, and if not, continue to the next instruction.
				case Instruction.IF2ENEMY: 
					if (world.inRange(newPos)){ //Check if the square is in range
						if ((world.get(newPos) != null)){ //Check if the square is occupied
							Creature enemy = world.get(newPos);
							if (enemy.species != species){ //Get the address to jump for if the species is an enemy
								programCounter = species.programStep(programCounter).getAddress();
							}
							else{
								programCounter++;
							}
						}
						else {
							Position newPos2 = newPos.getAdjacent(dir); //Get the next adjacent square
							if ((world.get(newPos) != null)){ //Check if the square is occupied
								Creature enemy2 = world.get(newPos2); 
								if (enemy2.species != species){ //Get the address to jump for if the species is an enemy
									programCounter = species.programStep(programCounter).getAddress();
								}
								else{
									programCounter++;
								}


							}
							else{
								programCounter++;
							}
						}
					}
					else {
						programCounter++;
					}
					break;

				//If the square in front is unoccupied, jump to the given address, and if not, continue to the next instruction
				case Instruction.IFEMPTY: 
					if (world.inRange(newPos) && world.get(newPos) == null){ //Check if the square is in range, and is unoccupied
						programCounter = species.programStep(programCounter).getAddress();
					}
					else{
						programCounter++; 
					}
					break;


				//If the creature is facing the wall, jump to the given address, and if not, continue to the next instruction
				case Instruction.IFWALL:
					if (!world.inRange(newPos)){ //Check if the creature is facing the wall
						programCounter = species.programStep(programCounter).getAddress();
					}
					else {
						programCounter++; 
					}
					break;

		
				//If the creature is facing the creature of the same species, jump to the given address, and otherwise, continue to the next instruction
				case Instruction.IFSAME:
					if (world.inRange(newPos) && world.get(newPos) != null){ //Check if the square is within the range and is occupied
						Creature ally = world.get(newPos);
						if (ally.species == species){ //Jump to the given address if the species is an ally
							programCounter = species.programStep(programCounter).getAddress();
						}
						else{
							programCounter++; 
						}
					}
					else{
						programCounter++; 
					}
					break;

				
				//Either jump to the given address or continue to the next instruction at 50/50 probability.
				case Instruction.IFRANDOM:
					if(random.nextBoolean()){ //Half of the time, jump to the given address
						programCounter = species.programStep(programCounter).getAddress();
					}
					else{
						programCounter++; 
					}
					break;

				//Jump to the given address under any circumstances.
				case Instruction.GO:
					programCounter = species.programStep(programCounter).getAddress(); //Jump to the given address
					break;


				//The species moves forward as long as it stays within the boundary and there is no other species ahead.
				case Instruction.HOP: 
					if (world.inRange(newPos) && (world.get(newPos) == null)){ //Check if the square is within the range and is empty
						world.set(pos, null); //delete the species at the current position
						world.set(newPos, this); //move it ahead
						WorldMap.displaySquare(pos, ' ', 0, ""); //Delete the current creature in the visual representation
						pos = newPos; //update the position
						WorldMap.displaySquare(pos, species.getSpeciesChar(), dir, species.getColor()); //Put the creature at the new position inn the visual representation
						
					}
					programCounter ++;
					loop = 1; //exit the loop
					break;
					
				// The species turns left by 90 degrees
				case Instruction.LEFT:
					dir = leftFrom(dir); //Change the direction
					WorldMap.displaySquare(pos, species.getSpeciesChar(), dir, species.getColor()); //Change the display of the current creature
					programCounter++;
					loop = 1; //exit the loop
					break;

				//The species turns right by 90 degrees
				case Instruction.RIGHT:
					dir = rightFrom(dir); //Change the direction
					programCounter++;
					WorldMap.displaySquare(pos, species.getSpeciesChar(), dir, species.getColor()); //Change the display of the current creature
					loop = 1; //exit the loop
					break;

				//The species infects the enemy ahead and the infected species executes the infecting species' program at the given address.
				case Instruction.INFECT:

					if (world.inRange(newPos) && world.get(newPos) != null){ //Check if the square is within the range and is occupied
						Creature enemy = world.get(newPos); // get enemy
						if (enemy.species != species){ //Infect the creature if it's an enemy
							enemy.species = species;
							
							int address = enemy.species.programStep(programCounter).getAddress(); //Get address to jump to for the given species
							WorldMap.displaySquare(enemy.pos, enemy.species.getSpeciesChar(), enemy.dir, enemy.species.getColor()); //Update the display for the infected creature

							if (address != 0) {
								enemy.programCounter = address; //The infected creature operates the instruction of the infecting creature's given address
							}
							else{
								enemy.programCounter = 1; //If no address is given, the instruction starts from step 1
							}
						}
						else {
							programCounter++;
						}
					}
					else {
						programCounter++;

					}
					loop = 1; //exit the loop
					break;
				

			}
		}
	}
	

	/**
	 * Return the compass direction the is 90 degrees left of the one passed in.
	 */
	public static int leftFrom(int direction) {
		return (4 + direction - 1) % 4;
	}

	/**
	 * Return the compass direction the is 90 degrees right of the one passed
	 * in.
	 */
	public static int rightFrom(int direction) {
		return (direction + 1) % 4;
	}

}

