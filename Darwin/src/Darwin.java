package darwin;

import java.util.ArrayList;
import java.util.Random;
import java.io.*;

/**
 * This class controls the simulation. The design is entirely up to you. You
 * should include a main method that takes the array of species file names
 * passed in and populates a world with species of each type. You class should
 * be able to support anywhere between 1 to 4 species.
 * 
 * Be sure to call the WorldMap.pause() method every time through the main
 * simulation loop or else the simulation will be too fast. For example:
 * 
 * 
 * public void simulate() { 
 * 	for (int rounds = 0; rounds < numRounds; rounds++) {
 * 		giveEachCreatureOneTurn(); 
 * 		WorldMap.pause(500); 
 * 	} 
 * }
 * 
 */
class Darwin {
	private String [] speciesFilenames;
	private Random random = new Random();
	private ArrayList <Creature> creatures = new ArrayList <Creature>();

	public Darwin(String[] speciesFilenames) {
		this.speciesFilenames = speciesFilenames;
	}

	/**
	 * The array passed into main will include the arguments that appeared on the
	 * command line. For example, running "java Darwin Hop.txt Rover.txt" will call
	 * the main method with s being an array of two strings: "Hop.txt" and
	 * "Rover.txt".
	 * 
	 * The autograder will always call the full path to the creature files, for
	 * example "java Darwin /home/user/Desktop/Assignment02/Creatures/Hop.txt" So
	 * please keep all your creates in the Creatures in the supplied
	 * Darwin/Creatures folder.
	 *
	 * To run your code you can either: supply command line arguments through
	 * Eclipse ("Run Configurations -> Arguments") or by creating a temporary array
	 * with the filenames and passing it to the Darwin constructor. If you choose
	 * the latter options, make sure to change the code back to: Darwin d = new
	 * Darwin(s); before submitting. If you want to use relative filenames for the
	 * creatures they should be of the form "./Creatures/Hop.txt".
	 */
	

	//Main function
	public static void main(String s[]) {
		Darwin d = new Darwin(s);

	 	d.simulate();
	}
	
	/**
	 * Simulate Darwin for the given species.
	 */
	public void simulate() {
		try {

			//Set up the matrix s
			World world = new World(10, 10);
			WorldMap.createWorldMap(10, 10);

			//Read the files and create species
			for (int i = 0; i<speciesFilenames.length; i++){
				BufferedReader in = new BufferedReader(new FileReader("./Darwin/Creatures/" + speciesFilenames[i]));
				Species s = new Species(in);  
				
				//Populate the matrix
				for (int j = 0; j < 10; j++){
					while (true){
						Position pos = new Position(random.nextInt(10), random.nextInt(10)); //Randomize the position
						if (world.get(pos)==null){
							Creature c = new Creature(s, world, pos, random.nextInt(4)); //Randomize the direction and put the creature
							creatures.add(c); //Add the creature to the arraylist
							break;
						}
					}
				}

			}


			//Move each creature
			for (int rounds = 0; rounds <100; rounds++){ //Try 100 rounds
				for (Creature creature : creatures){
					creature.takeOneTurn();
				}
				WorldMap.pause(500);
			}
	

		} catch (Exception | Error e) {
			System.out.println(e);
		}
	}

}		
	
