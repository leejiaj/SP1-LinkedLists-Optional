/** @author rbk
 *  @author Leejia James
 *  Singly linked list: for use in Terrence Park's and Leejia James's SP1 team task
 *  					for use in Leejia James's SP1 Optional tasks
 *  Ver 1.0: 2018/08/21
 *  Ver 2.0: 2018/08/28: modified to be able to extend to DoublyLinkedList
 *  Entry class has generic type associated with it, to allow inheritance.
 *  We can now have a doubly linked list class DLL that has

public class DoublyLinkedList<T> extends SinglyLinkedList<T> {
static class Entry<E> extends SinglyLinkedList.Entry<E> {
Entry<E> prev;
Entry(E x, Entry<E> next, Entry<E> prev) {
super(x, nxt);
this.prev = prev;
}
}

 *  Ver 3.0: 2018/09/02: Added the methods addFirst(x), removeFirst(), remove(x).
 *  Added indexing operations: get(index), set(index, x), add(index, x), remove(index).
 *  Added separate options to test operations add(x) and unzip().
 */

package lxj171130;
import java.util.Iterator;
import java.util.Scanner;
import java.util.NoSuchElementException;

public class SinglyLinkedList<T> implements Iterable<T> {

	/** Class Entry holds a single node of the list */
	static class Entry<E> {
		E element;
		Entry<E> next;

		Entry(E x, Entry<E> nxt) {
			element = x;
			next = nxt;
		}
	}

	// Dummy header is used.  tail stores reference of tail element of list
	Entry<T> head, tail;
	int size;

	public SinglyLinkedList() {
		head = new Entry<>(null, null);
		tail = head;
		size = 0;
	}

	public Iterator<T> iterator() { return new SLLIterator(); }

	protected class SLLIterator implements Iterator<T> {
		Entry<T> cursor, prev;
		boolean ready;  // is item ready to be removed?

		SLLIterator() {
			cursor = head;
			prev = null;
			ready = false;
		}

		public boolean hasNext() {
			return cursor.next != null;
		}

		public T next() {
			prev = cursor;
			cursor = cursor.next;
			ready = true;
			return cursor.element;
		}

		// Removes the current element (retrieved by the most recent next())
		// Remove can be called only if next has been called and the element has not been removed
		public void remove() {
			if(!ready) {
				throw new NoSuchElementException();
			}
			prev.next = cursor.next;
			// Handle case when tail of a list is deleted
			if(cursor == tail) {
				tail = prev;
			}
			cursor = prev;
			ready = false;  // Calling remove again without calling next will result in exception thrown
			size--;
		}
		
		
	}  // end of class SLLIterator

	// Add new elements to the end of the list
	public void add(T x) {
		add(new Entry<>(x, null));
	}

	protected void add(Entry<T> ent) {
		tail.next = ent;
		tail = tail.next;
		size++;
	}
	
	// Insert a new item x to the beginning of the list
	public void addFirst( T x) {
		Entry<T> temp = head.next;
		head.next = new Entry<>(x,temp);
		size++;
	}
	
	// Remove first element of the list
	public void removeFirst() {
		if(head.next == null) {
			throw new NoSuchElementException();
		}
		if(head.next == tail){
			tail = head;
		}
		Entry<T> temp = head.next.next;
		head.next = temp;
		size--;
	}
	
	// Delete and return the first occurrence of x from the list
	public T remove(T x) {
		if(head.next == null) {
			throw new NoSuchElementException();
		}
		Entry<T> temp = head.next;
		Entry<T> previous = head;
		while(temp!=null) {
			if(temp.element == x) {
				if(temp == tail) {
					tail = previous;
				}
				previous.next = temp.next;
				size--;
				return temp.element;
			}
			previous = temp;
			temp = temp.next;
		}
		throw new NoSuchElementException();
	}
	
	// Return the previous element of the element at index
	public Entry<T> getPrevious(int index){
		if(size == 0)
			throw new NoSuchElementException();
		if(index >= size)
			throw new IndexOutOfBoundsException();
		Entry<T> temp = head.next;
		Entry<T> previous = head;
		int currentIndex = 0;
		while(temp!=null) {
			if(currentIndex == index) {
				return previous;
			}
			previous = temp;
			temp = temp.next;
			currentIndex++;
		}
		return null;
	}
	
	// Return the element at index
	public T get(int index) {
		return getPrevious(index).next.element;		
	}
	
	// Replace the element at given index to be x
	public void set(int index, T x) {
		getPrevious(index).next.element = x;
	}
	
	// Add x as a new element at given index
	public void add(int index, T x) {
		Entry<T> previous = getPrevious(index);
		previous.next = new Entry<>(x, previous.next);
		size++;
	}
	
	// Delete and return element at index
	public T removeElemAtIndex(int index) {
		Entry<T> previous = getPrevious(index);
		Entry<T> temp = previous.next;
		if(temp.next == null)
			tail = previous;
		previous.next = temp.next;
		size--;
		return temp.element;
	}

	public void printList() {
		System.out.print(this.size + ": ");
		for(T item: this) {
			System.out.print(item + " ");
		}

		System.out.println();
	}

	// Rearrange the elements of the list by linking the elements at even index
	// followed by the elements at odd index. Implemented by rearranging pointers
	// of existing elements without allocating any new elements.
	public void unzip() {
		if(size < 3) {  // Too few elements.  No change.
			return;
		}

		Entry<T> tail0 = head.next;
		Entry<T> head1 = tail0.next;
		Entry<T> tail1 = head1;
		Entry<T> c = tail1.next;
		int state = 0;

		// Invariant: tail0 is the tail of the chain of elements with even index.
		// tail1 is the tail of odd index chain.
		// c is current element to be processed.
		// state indicates the state of the finite state machine
		// state = i indicates that the current element is added after taili (i=0,1).
		while(c != null) {
			if(state == 0) {
				tail0.next = c;
				tail0 = c;
				c = c.next;
			} else {
				tail1.next = c;
				tail1 = c;
				c = c.next;
			}
			state = 1 - state;
		}
		tail0.next = head1;
		tail1.next = null;
		// Update the tail of the list
		tail = tail1;
	}

	public static void main(String[] args) throws NoSuchElementException {
		int n = 10;
		int index, x;
		if(args.length > 0) {
			n = Integer.parseInt(args[0]);
		}

		SinglyLinkedList<Integer> lst = new SinglyLinkedList<>();
		for(int i=1; i<=n; i++) {
			lst.add(Integer.valueOf(i));
		}
		lst.printList();

		Iterator<Integer> it = lst.iterator();
		Scanner in = new Scanner(System.in);
		whileloop:
		while(in.hasNext()) {
			int com = in.nextInt();
			switch(com) {
			case 1:  // Move to next element and print it
				if (it.hasNext()) {
					System.out.println(it.next());
				} else {
					break whileloop;
				}
				break;
			case 2:  // Remove element
				it.remove();
				lst.printList();
				break;
			case 3:  // Add new element to the end of list
				int elemAddEnd = in.nextInt();
				lst.add(elemAddEnd);
				lst.printList();
				break;
			case 4:	// Unzip elements of the list
				lst.unzip();
				lst.printList();
				break;
			case 5:	// Insert a new item x to the beginning of the list
				int elemAdd = in.nextInt();
				lst.addFirst(elemAdd);
				lst.printList();
				break;
			case 6:  // Remove first element of the list
				lst.removeFirst();
				lst.printList();
				break;
			case 7:	// Delete and return the first occurrence of x from the list
				int elemDel = in.nextInt();
				System.out.println(lst.remove(elemDel));
				lst.printList();
				break;
			case 8:	// Return the element at index
				index = in.nextInt();
				System.out.println(lst.get(index));
				lst.printList();
				break;
			case 9:	// Replace the element at given index to be x
				index = in.nextInt();
				x = in.nextInt();
				lst.set(index, x);
				lst.printList();
				break;
			case 10:	// Add x as a new element at given index
				index = in.nextInt();
				x = in.nextInt();
				lst.add(index, x);
				lst.printList();
				break;
			case 11:	// Delete and return element at index
				index = in.nextInt();
				System.out.println(lst.removeElemAtIndex(index));
				lst.printList();
				break;
			default:  // Exit loop
				break whileloop;
			}
		}
		lst.printList();
		lst.unzip();
		lst.printList();
	}
}

/* Sample input:
   1 2 1 2 1 1 1 2 1 1 2 0
   Sample output:
10: 1 2 3 4 5 6 7 8 9 10
1
9: 2 3 4 5 6 7 8 9 10
2
8: 3 4 5 6 7 8 9 10
3
4
5
7: 3 4 6 7 8 9 10
6
7
6: 3 4 6 8 9 10
6: 3 4 6 8 9 10
6: 3 6 9 4 8 10
*/