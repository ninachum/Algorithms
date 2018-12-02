import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
	
	private Node first;
	private Node last;
	private int num_items;
	
	private class Node {
		Item item;
		Node prev;
		Node next;
	}
	
	public Deque() {
		// first and last are initialized to NULL, automatically.
		// also, num_items to 0.
	}
	
	public boolean isEmpty() { return num_items == 0; }
	
	public int size() { return num_items; }
	
	public void addFirst(Item item) {
		if (item == null)
			throw new java.lang.IllegalArgumentException("addFirst failed : argument is null.");
		
		Node new_node = new Node();
		new_node.item = item;
		new_node.prev = null;	 // not necessary, but for clarity
		new_node.next = first;	 // NULL or a Node.
		first = new_node;
		if (isEmpty()) { last = first; }
		else { first.next.prev = first; } // same as first.prev = new_node;
		num_items++;
	}
	
	public void addLast(Item item) {
		if (item == null)
			throw new java.lang.IllegalArgumentException("addLirst failed : argument is null.");
		
		Node new_node = new Node();
		new_node.item = item;
		new_node.next = null;	// not necessary, but for clarity
		new_node.prev = last;	// NULL or a Node.
		last = new_node;
		if (isEmpty()) { first = last; }
		else { last.prev.next = last; } // same as last.next = new_node;
		num_items++;
	}
	
	public Item removeFirst() {
		if (isEmpty())
			throw new java.util.NoSuchElementException("removeFirst failed : empty queue.");
		Item result = first.item;
		first = first.next;
		num_items--;
		if (isEmpty()) { last = first; }
		else { first.prev = null; }
		return result;
	}
	
	public Item removeLast() {
		if (isEmpty())
			throw new java.util.NoSuchElementException("removeFirst failed : empty queue.");
		
		Item result = last.item;
		last = last.prev;
		num_items--;
		if (isEmpty()) { first = last; }
		else { last.next = null; }
		return result;
	}
	
	public Iterator<Item> iterator() { return new DequeIterator(); }
	
	private class DequeIterator implements Iterator<Item> {
		
		private Node current = first;
		
		public boolean hasNext() { return current != null; }
		public void remove() { throw new java.lang.UnsupportedOperationException("remove() is not supported."); } // not supported 
		public Item next() { 
			if (!hasNext())
				throw new java.util.NoSuchElementException("removeFirst failed : empty queue.");
			
			Item result = current.item;
			current = current.next;
			return result;
		}
	}
	
	public static void main(String[] args) {
		Deque<Integer> test_1 = new Deque<Integer>();
		
		test_1.addFirst(34);
		System.out.println(test_1.removeLast());
		
	}
	
}