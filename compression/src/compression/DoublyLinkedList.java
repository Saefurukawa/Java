package compression;

import java.util.Iterator;

/**
 * The {@code DoublyLinkedList} class represents a doubly linked list. It has
 * been implemented based on Sedgewick and Wayne's Algorithms textbook (4th
 * edition).
 * 
 * @author Aden Siebel
 * @author Alexandra Papoutsaki
 *
 */
public class DoublyLinkedList<Item> implements Iterable<Item> {
	protected Node first; // head of the doubly linked list
	protected Node last; // tail of the doubly linked list
	protected int n; // number of nodes in the doubly linked list

	/**
	 * This nested class defines the nodes in the doubly linked list with a value
	 * and pointers to the previous and next node they are connected.
	 */
	protected class Node {
		Item item;
		Node next;
		Node prev;

		/**
		 * a Node displays as its item
		 */
		public String toString() {
			return item.toString();
		}
	}

	/**
	 * Returns true if the doubly linked list does not contain any item.
	 * 
	 * @return true if the doubly linked list does not contain any item
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Returns the number of items in the doubly linked list.
	 * 
	 * @return the number of items in the doubly linked list
	 */
	public int size() {
		return n;
	}

	/**
	 * Returns item at the specified index.
	 * 
	 * @param index
	 *            the index of the item to be returned
	 * @return the item at specified index
	 */
	public Item get(int index) {
		rangeCheck(index);

		if (index == 0)
			return first.item;

		else if (index == size() - 1)
			return last.item;

		Node finger = first;
		// search for index-th element or end of list
		while (index > 0) {
			finger = finger.next;
			index--;
		}
		return finger.item;
	}
	
	/**
	 * Returns index of the specified item
	 * 
	 * @param item ... the item to be located
	 * @return index of that item (or -1)
	 */
	public int getIndex(Item desired) {
		int index = 0;
		for(Node finger = first; finger != null; finger = finger.next) {
			if (finger.item == desired)
				return index;
			index++;
		}
		return(-1);
	}

	 /**
     * Add a value to head of list.
     *
     * @post adds value to beginning of list
     * 
     * @param value value to be added.
     */
    public void add(Item item)
    {
        this.addFirst(item);
    }

	/**
	 * Inserts the specified item at the head of the doubly linked list.
	 * 
	 * @param item
	 *            the item to be inserted
	 */
	public void addFirst(Item item) {
		// Save the old node
		Node oldfirst = first;

		// Make a new node and assign it to head. Fix pointers.
		first = new Node();
		first.item = item;
		first.next = oldfirst;
		first.prev = null;

		// if first node to be added, adjust tail to it.
		if (last == null)
			last = first;
		else
			oldfirst.prev = first;

		n++; // increase number of nodes in doubly linked list.
	}

	/**
	 * Inserts the specified item at the tail of the doubly linked list.
	 * 
	 * @param item
	 *            the item to be inserted
	 */
	public void addLast(Item item) {
		// Save the old node
		Node oldlast = last;

		// Make a new node and assign it to tail. Fix pointers.
		last = new Node();
		last.item = item;
		last.next = null;
		last.prev = oldlast;

		// if first node to be added, adjust head to it.
		if (first == null)
			first = last;
		else
			oldlast.next = last;

		n++;
	}

	/**
	 * Inserts the specified item at the specified index.
	 * 
	 * @param index
	 *            the index to insert the item
	 * @param item
	 *            the item to insert
	 */
	public void add(int index, Item item) {
		rangeCheck(index);

		if (index == 0) {
			this.addFirst(item);
		} else if (index == size()) {
			this.addLast(item);
		} else {
			Node previous = null;
			Node finger = first;
			// search for index-th position
			while (index > 0) {
				previous = finger;
				finger = finger.next;
				index--;
			}
			// create new value to insert in correct position
			Node current = new Node();
			current.item = item;
			current.next = finger;
			current.prev = previous;
			previous.next = current;
			finger.prev = current;

			n++;
		}
	}

	/**
	 * Retrieves and removes the head of the doubly linked list.
	 * 
	 * @return the head of the doubly linked list.
	 */
	public Item removeFirst() {
		Node oldFirst = first;
		// Fix pointers.
		first = first.next;
		// at least 1 nodes left.
		if (first != null) {
			first.prev = null;
		} else {
			last = null; // remove final node.
		}
		oldFirst.next = null;

		n--;

		return oldFirst.item;
	}

	/**
	 * Retrieves and removes the tail of the doubly linked list.
	 * 
	 * @return the tail of the doubly linked list.
	 */
	public Item removeLast() {

		Node temp = last;
		last = last.prev;

		// if there was only one node in the doubly linked list.
		if (last == null) {
			first = null;
		} else {
			last.next = null;
		}
		n--;
		return temp.item;
	}

	/**
	 * Retrieves and removes the item at the specified index.
	 * 
	 * @param index
	 *            the index of the item to be removed
	 * @return the item previously at the specified index
	 */
	public Item remove(int index) {
		rangeCheck(index);

		if (index == 0) {
			return this.removeFirst();
		} else if (index == size() - 1) {
			return this.removeLast();
		} else {
			Node previous = null;
			Node finger = first;
			// search for value indexed, keep track of previous
			while (index > 0) {
				previous = finger;
				finger = finger.next;
				index--;
			}
			previous.next = finger.next;
			finger.next.prev = previous;

			n--;
			// finger's value is old value, return it
			return finger.item;
		}

	}

	/**
	 * Removes the first node with the specified item and returns it.
	 * If node has the specified item then returns null.
	 * 
	 * @param item
	 *            the item value of the first node to be removed
	 * @return the item that was removed
	 */
	public Item remove(Item item) {

		Node finger = first;
		// search for index-th element or end of list
		while (finger !=null && !finger.item.equals(item)) {

			finger = finger.next;
		}
		if (finger!=null){
			if(finger.prev !=null){
				finger.prev.next = finger.next;
			}
			else{
				first = finger.next;
			}
			if(finger.next !=null){
				finger.next.prev = finger.prev;
			}
			else{
				last = finger.prev;
			}
			n--;
			return finger.item;
		}
		return null;
	}

	/**
	 * remove all items from list
	 */
	public void clear() {
		first = null;
		last = null;
		n = 0;
	}

	/**
	 * A helper method to check if the specified index is in range.
	 * 
	 * @param index
	 *            the index to check
	 */
	private void rangeCheck(int index) {
		if (index >= n || index < 0)
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds");
	}
	
	/**
	 * Converts the doubly linked list to a String.
	 */
	public String toString() {
		if (isEmpty()) {
			return "Doubly Linked List: []";
		}

		String ret = "Doubly Linked List: [<- ";
		Iterator<Item> i = this.iterator();
		while (i.hasNext()) {
			ret += i.next();
			ret += " <-> ";
		}
		ret = ret.substring(0, ret.length() - 5);

		ret += " ->] First: ";
		ret += first.item;
		ret += ", Last: " + last.item;
		return ret;
	}

	/**
	 * Constructs an iterator for the doubly linked list.
	 */
	public Iterator<Item> iterator() {
		return new ListIterator();
	}

	/**
	 * A subclass that defines the iterator for the doubly linked list.
	 */
	private class ListIterator implements Iterator<Item> {
		private Node current = first;

		public boolean hasNext() {
			return current != null;
		}

		public void remove() {
		}

		public Item next() {
			Item item = current.item;
			current = current.next;
			return item;
		}
	}

	public static void main(String args[]) {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		dll.addFirst(1);
		System.out.println(dll);
		dll.add(0, 2);
		System.out.println(dll);
		dll.addLast(20);
		System.out.println(dll);
		dll.addFirst(30);
		System.out.println(dll);
		dll.removeFirst();
		System.out.println(dll);
		dll.removeLast();
		System.out.println(dll);
		dll.removeLast();
		System.out.println(dll);
		dll.addLast(1);
		System.out.println(dll);
		dll.addFirst(3);
		System.out.println(dll);
		dll.removeFirst();
		System.out.println(dll);
		dll.removeFirst();
		System.out.println(dll);
		dll.removeFirst();
		System.out.println(dll);
	}
}
