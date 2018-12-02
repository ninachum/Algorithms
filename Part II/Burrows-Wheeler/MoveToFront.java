import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

	private static final int MAX = 256; // ASCII
	
	public static void encode() {
		MyLinkedList list = new MyLinkedList();				  // linked STACK will be maintained during encoding.
		for (char i = MAX - 1; i != 0; i--) {				  // insert ASCII chars (0x00 - 0xFF) into the list, in reverse order.  
			list.insert(i);
		}
		list.insert((char) 0);
		
		String a = BinaryStdIn.readString();				  // read input into a String
		for (int i = 0; i < a.length(); i++) {
			char read = a.charAt(i);						  // read a byte (which is ASCII char), and...
			BinaryStdOut.write(list.occurringPosition(read)); // find the byte's first occurring position in the linked list. And write the position to StdOut. 
			list.delete(read);								  // move the byte to the front of list.
			list.insert(read);								  
		}
		
		BinaryStdIn.close();
		BinaryStdOut.close();
		
	}
	
	public static void decode() {
		MyLinkedList list = new MyLinkedList();
		for (char i = MAX - 1; i != 0; i--) {
			list.insert(i);
		}
		list.insert((char) 0);
		
		String a = BinaryStdIn.readString();
		for (int i = 0; i < a.length(); i++) {
			char read = a.charAt(i);						  // read a byte (this time, it indicates a position in the list), and...
			char temp = list.charInPosition(read);			  // find a byte of that position, in the linked list.
			BinaryStdOut.write(temp);						  
			list.delete(temp);								  // imitate the maintained list in the encode()
			list.insert(temp);
		}

		BinaryStdIn.close();
		BinaryStdOut.close();
	}
	
	public static void main(String[] args) {
		if (args[0].contentEquals("-"))
			encode();
		else if (args[0].contentEquals("+"))
			decode();
		
	}
}
