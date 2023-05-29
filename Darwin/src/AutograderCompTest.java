package darwin;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * This is a very basic test suite for the early stages of a Darwin
 * implementation.
 */
public class AutograderCompTest {

	public AutograderCompTest() {
		System.out.println("Please note that this only tests compatibility, not correctness.");
	}

	/**
	 * We create a world, place a string in a known place, and see if it reads back
	 * as we set it.
	 */
	public void testWorld() {
		try {
			World world = new World(5, 3);
			if (world.height() != 3) {
				System.out.println("World compatability test failed - incorrect height");
				return;
			}
			if (world.width() != 5) {
				System.out.println("World compatability test failed - incorrect width");
				return;
			}
			Position pos = new Position(4, 2);
			world.get(pos);
			System.out.println("World compatibility test passed.");
		} catch (Exception | Error e) {
			System.out.println("World compatibility test failed." + e);
		}
	}

	// these must agree with the data in Hop.txt
	private static final String CREATURE_NAME = "Hop";
	private static final String CREATURE_COLOR = "blue";
	private static final int CREATURE_PGM_SIZE = 2;
	private static final int CREATURE_FIRST_INST = Instruction.HOP;
	private static final int CREATURE_DIR = Position.EAST;

	/**
	 * We test the Species class by reading in a known description and see if it is
	 * correctly instantiated.
	 */
	public void testSpecies() {
		try {
			BufferedReader in = new BufferedReader(new FileReader("./Darwin/Creatures/" + CREATURE_NAME + ".txt"));

			Species s = new Species(in);
			char c = s.getSpeciesChar();
			if (c != CREATURE_NAME.charAt(0)) {
				System.out.println("Species compatibility test failed: incorrect species character");
				return;
			}
			String name = s.getName();
			if (!name.equals(CREATURE_NAME)) {
				System.out.println("Species compatibility test failed: incorrect name readback");
				return;
			}
			String color = s.getColor();
			if (!color.equals(CREATURE_COLOR)) {
				System.out.println("Species compatibility test failed: incorrect color readback");
				return;
			}
			int size = s.programSize();
			if (size != CREATURE_PGM_SIZE) {
				System.out.println("Species compatibility test failed: incorrect program size");
				return;
			}
			Instruction step = s.programStep(1);
			if (step.getOpcode() != CREATURE_FIRST_INST) {
				System.out.println("Species compatibility test failed: incorrect first instruction");
				return;
			}

			System.out.println("Species compatibility test passed.");
		} catch (Exception | Error e) {
			System.out.println("Species compatibility test failed." + e);
		}
	}

	public void testCreature() {
		try {
			// create a world<Creature>
			World world = new World(10, 10);
			WorldMap.createWorldMap(10, 10);

			// read in a Species description
			BufferedReader in = new BufferedReader(new FileReader("./Darwin/Creatures/" + CREATURE_NAME + ".txt"));
			Species s = new Species(in);

			// create a creature of that species
			Position pos = new Position(1, 1);
			Creature c = new Creature(s, world, pos, CREATURE_DIR);

			// does the Creature.species return the expected value
			Species s1 = c.species();
			if (s1 != s) {
				System.out.println("Creature compatibility test failed: wrong species returned");
				return;
			}

			// does Creature.position return the expected value
			Position pos1 = c.position();
			if (pos1.getX() != pos.getX() || pos1.getY() != pos.getY()) {
				System.out.println("Creature compatibility test failed: wrong pos returned");
				return;
			}

			// does Creature.direction return the expected value
			int dir = c.direction();
			if (dir != CREATURE_DIR) {
				System.out.println("Creature compatibility test failed: wrong direction returned");
				return;
			}

			System.out.println("Creature compatibility test passed.");
		} catch (Exception | Error e) {
			System.out.println("Creature compatibility test failed." + e);
			e.printStackTrace();
		}
	}

	/**
	 * to test the full program, we invoke the main module with three files, and
	 * expect to see an appropriately populated world.
	 */
	public void testDarwin() {
		try {
			String[] s = new String[3];
			s[0] = "Hop.txt";
			s[1] = "Flytrap.txt";
			s[2] = "Food.txt";

			System.out.println("Darwin compatibility test passed if a well populated board appears w/no errors.");

			Darwin.main(s);
		} catch (Exception | Error e) {
			System.out.println("Darwin compatibility test failed." + e);
		}
	}

	/**
	 * if this module is invoked as an application it runs a set of (very) basic
	 * unit tests.
	 * 
	 * As you add additional tests, it us up to you whether you want to add them
	 * here, or create real JUnit test modules.
	 */
	public static void main(String[] args) {
		AutograderCompTest a = new AutograderCompTest();
		a.testWorld();
		a.testSpecies();
		a.testCreature();
		a.testDarwin();
	}
}

