import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
	
	private Item[] queue;
	private int last;
	private int num_items;
	
	public RandomizedQueue() {
		// last, and num_items are initialized to 0, automatically.
		queue = (Item[]) new Object[1]; 	// creation of generic array is not supported in Java.
		
	}
	
	public boolean isEmpty() { return num_items == 0; }
	
	public int size() { return num_items; };
	
	public void enqueue(Item item) {
		if (item == null)
			throw new java.lang.IllegalArgumentException("enqueue failed : item is null.");
		
		if (isFull())
			resize(2 * queue.length);
		
		queue[last++] = item;
		num_items++;
		
	}
	
	/*
	 * the idea of swapping random element with last one (to return & remove queue[last]) is from the web page below :
	 * https://stackoverflow.com/questions/10048069/what-is-the-most-pythonic-way-to-pop-a-random-element-from-a-list/10048313#10048313
	 */
	
	public Item dequeue() {
		if (isEmpty())
			throw new java.util.NoSuchElementException("dequeue failed : empty queue.");
		
		exch(queue, StdRandom.uniform(num_items), --last);
		Item result = queue[last];
		queue[last] = null;
		num_items--;

		if (isQuarter())
			resize(queue.length / 2);
		
		return result;
		
	}
	
	public Item sample() {
		if (isEmpty())
			throw new java.util.NoSuchElementException("dequeue failed : empty queue.");
		
		return queue[StdRandom.uniform(num_items)];
	}
	
	public Iterator<Item> iterator() {
		return new RandomizedQueueIterator();
	}
	
	private class RandomizedQueueIterator implements Iterator<Item> {
		
		private int[] randomNumbers;
		private int counter;
		
		public RandomizedQueueIterator() {
			
			randomNumbers = new int[num_items];
			
			for (int i = 0; i < num_items; i++) {
				randomNumbers[i] = i;
			}
			
			// do Knuth shuffle to the initialized randomNumbers.
			for (int i = 1; i < num_items; i++) {
				exchInt(randomNumbers, StdRandom.uniform(i + 1), i);
			}
			
			// counter is initialized to 0.
		}
		
		public boolean hasNext() { return counter != num_items; }
		
		public void remove() { throw new java.lang.UnsupportedOperationException("remove() is not supported."); } // not supported 
		
		public Item next() {
			if (counter >= num_items)
				throw new java.util.NoSuchElementException("next() failed : tried to access out of array bound.");
			
			return queue[randomNumbers[counter++]];
				
		}
		
		// helper function for shuffling randomNumbers.
		private void exchInt(int[] a, int temp1, int temp2) {
			if (temp1 == temp2)
				return;
			
			int temp = a[temp1];
			a[temp1] = a[temp2];
			a[temp2] = temp;
		}
	}

	
	private boolean isFull() { return num_items == queue.length; } // ?
	
	private boolean isQuarter() { return num_items > 0 && num_items == queue.length / 4; } // queue.length is always power of 2. 
	
	// exchange function for Item type.
	private void exch(Item[] a, int temp1, int temp2) {
		if (temp1 == temp2)
			return;
		
		Item tempItem = a[temp1];
		a[temp1] = a[temp2];
		a[temp2] = tempItem;
	}
	
	private void resize(int arrSize) {
		Item[] newQueue = (Item[]) new Object[arrSize];
		
		for (int i = 0; i < num_items; i++) {
			newQueue[i] = queue[i];
		}
		
		queue = newQueue;
	}
	
}