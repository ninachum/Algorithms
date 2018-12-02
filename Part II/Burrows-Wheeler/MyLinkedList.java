// just a linked list.
public class MyLinkedList {
	
	private Node first;
	private int size;
	
	private class Node {
		char content;
		Node next;
		
		public Node(char x) {
			content = x;
		}
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public void insert(char x) {
		Node newNode = new Node(x);
		newNode.next = first;
		first = newNode;
		size++;
	}
	
	public void delete(char x) {
		for (Node front = first, back = null; front != null; back = front, front = front.next) {
			if (front.content != x) 
				continue;
			
			if (back != null)
				back.next = front.next;
			else
				first = front.next;
			size--;
			return;
		}
	}
	
	public char occurringPosition(char x) {
		char temp = 0;
		for (Node i = first; i != null; i = i.next, temp++) {
			if (i.content == x)
				return temp;
		}
		return x; // dead code
	}
	
	public char charInPosition(int x) {
		Node a = first;
		for (char i = 0; i != x; i++) {
			a = a.next;
		}
		return (Character) a.content;

	}
	
	public String toString() {
		StringBuilder x = new StringBuilder();
		for (Node i = first; i != null; i = i.next) {
			x.append(i.content);
		}
		return x.toString();
	}
	
	/*
	public static void main(String[] args) {
		MyLinkedList a = new MyLinkedList();
	}
	*/
}
