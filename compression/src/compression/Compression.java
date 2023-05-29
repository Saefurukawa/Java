package compression;

import java.util.Scanner;

/**
 * Main class
 * @author cs62
 *
 */
public class Compression {

	// Table holding the strings displayed in the grid
	protected TwoDTable<String> table;

	public void processInput() {
		Scanner in = new Scanner(System.in);

		System.out.print("Provide #rows #cols defaultValue:\n");
		String[] splitContents = in.nextLine().split(" ");
		int rows = Integer.parseInt(splitContents[0]);
		int cols = Integer.parseInt(splitContents[1]);
		String defaultValue = splitContents[2];
		
		table = new CompressedTable<String>(rows, cols, defaultValue);
		System.out.println(table.entireTable());

		System.out.print("What's next?\n");
		String line = in.nextLine();

		while (!line.equals("exit")) {
			if (line.equals("display")) {
				System.out.println(table);
			} else if (line.startsWith("update")){
				splitContents = line.split(" ");
				int r = Integer.parseInt(splitContents[1]);
				int c = Integer.parseInt(splitContents[2]);
				String val = splitContents[3];

				table.updateInfo(r, c, val);
			}
			else {
				System.out.println("I didn't get that. Try again!");
			}
			
			System.out.println(table.entireTable());
			System.out.print("What's next?\n");
			line = in.nextLine();

		}
		in.close();
	}

	public static void main(String args[]) {
		Compression compression = new Compression();
		compression.processInput();
	}

}
