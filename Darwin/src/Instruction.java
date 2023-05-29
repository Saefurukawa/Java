package darwin;

/**
 * This class represents one Darwin instruction. Instructions contain two
 * parts: an opcode and an address. For instructions that do not perform jumps
 * (hop, left, right, infect), the address part is not used.
 * 
 * The fill instruction set is the following:
 * <p>
 * 
 * <pre>
 *  hop left right infect ifempty ifwall ifsame ifenemy ifrandom go
 * </pre>
 * 
 * The following instructions require a target address to jump to:
 * 
 * <pre>
 *  ifempty, ifwall, ifsame, ifenemy, ifrandom, go</pre>
 * 
 *  
 */
public class Instruction {

	/** opcode for the hop instruction */
	public static final int HOP = 1;
	/** opcode for the left instruction */
	public static final int LEFT = 2;
	/** opcode for the right instruction */
	public static final int RIGHT = 3;
	/** opcode for the infect instruction */
	public static final int INFECT = 4;
	/** opcode for the ifempty instruction */
	public static final int IFEMPTY = 5;
	/** opcode for the ifwall instruction */
	public static final int IFWALL = 6;
	/** opcode for the ifsame instruction */
	public static final int IFSAME = 7;
	/** opcode for the ifenemy instruction */
	public static final int IFENEMY = 8;
	/** opcode for the ifrandom instruction */
	public static final int IFRANDOM = 9;
	/** opcode for the go instruction */
	public static final int GO = 10;
	/** opcode for the if2enemy instruction */
	public static final int IF2ENEMY = 11;

	private int opcode; /** the opcode */
	private int address; /** the address */

	/**
	 * Creates a new instruction for operations that do
	 * NOT require an address.
	 * @pre 0 < opcode <= GO.
	 */
	public Instruction(int opcode) {
		this(opcode, 0);	
	}
	
	/**
	 * Creates a new instruction for operations that require an address.
	 * @pre 0 < opcode <= GO.
	 */
	public Instruction(int opcode, int address) {
		this.opcode = opcode;
		this.address = address;
	}

	/**
	 * Returns the opcode @post returns the opcode
	 */
	public int getOpcode() {
		return opcode;
	}

	/**
	 * @returns the address 
	 */
	public int getAddress() {
		return address;
	}

	/**
	 * Returns a printable representation of an Instruction
	 */
	public String toString() {
		switch (opcode) {
			case HOP :
				return "hop";
			case LEFT :
				return "left";
			case RIGHT :
				return "right";
			case INFECT :
				return "infect";
			case IFEMPTY :
				return "ifempty " + address;
			case IFWALL :
				return "ifwall " + address;
			case IFSAME :
				return "ifsame " + address;
			case IFENEMY :
				return "ifenemy " + address;
			case IF2ENEMY :
				return "if2enemy " + address;
			case IFRANDOM :
				return "ifrandom " + address;
			case GO :
				return "go " + address;
			default :
				return "BAD INSTRUCTION: " + opcode + " " + address;
		}
	}
}
