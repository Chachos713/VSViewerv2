package Util;

/**
 * A simple node class used in the linked list for when reading in the vsv file.
 * 
 * @author Kyle Diller
 *
 * @param <E>
 *            a generic tage to denote thst this node can be used with more than
 *            just molecules
 */
public class Node<E> {
	private Node<E> next;
	private E data;

	/**
	 * Creates a node that does not have a node after it
	 * 
	 * @param data
	 *            the data to store in the node
	 */
	public Node(E data) {
		this(data, null);
	}

	/**
	 * Creates a node with some data, and a node that follows it
	 * 
	 * @param data
	 *            the data to store within the node
	 * @param next
	 *            the node that follows this one in the stack
	 */
	public Node(E data, Node<E> next) {
		this.data = data;
		this.next = next;
	}

	/**
	 * @return the data stored within the node
	 */
	public E getData() {
		return data;
	}

	/**
	 * Changes the data that is stored within this node
	 * 
	 * @param data
	 *            the new data to store within the node
	 */
	public void setData(E data) {
		this.data = data;
	}

	/**
	 * @return the node that follows this one in the linked list
	 */
	public Node<E> getNext() {
		return next;
	}

	/**
	 * @return whether the next node exists or is null
	 */
	public boolean hasNext() {
		return next != null;
	}

	/**
	 * Changes which node is next in the linked list
	 * 
	 * @param next
	 *            the node that is to follow this one
	 */
	public void setNext(Node<E> next) {
		this.next = next;
	}
}
