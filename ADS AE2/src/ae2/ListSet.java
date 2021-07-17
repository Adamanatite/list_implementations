package ae2;

/**
 * An implementation of the set ADT using a linked list
 * 
 * Student solution to ADS Assessed Exercise 2
 * 
 * @author Adam Fairlie <2461352f@student.gla.ac.uk>
 */
public class ListSet<Item extends Comparable<Item>> implements AbstractSet<Item>{
	/*Head and tail of list*/
	private Node head, tail;
	/*Size of set*/
	private int size;
	
	/*A nested class representing each item in the list*/
	private class Node{
		/*Value stored in node*/
		private Item key;
		/*Pointers to previous and next elements in list*/
		private Node prev, next;

		public Node(Item key) {
			this.key = key;
			this.next = null;
			this.prev = null;
		}
	}
	
	/*Class constructor*/
	public ListSet(){
		head = null;
		tail = null;
		size = 0;
	}
	
	
	//METHODS
	
	/**
	 * Adds an node to the set
	 * 
	 * @param x The node to add
	 */
	private void add(Node x) {
		
		//If x is the first element
		if(this.head == null) {
			this.head = x;
			this.tail = x;
			this.size++;
			return;
		}
		Node y = this.head;
		
		//If x is the same as the head
		if(y.key.compareTo(x.key) == 0) {
			return;
		}
		
		//If x is the new smallest element
		if(y.key.compareTo(x.key) > 0) {
			this.head = x;
			y.prev = x;
			x.next = y;
			this.size++;
			return;
		}
		
		//Find first element larger than key (or until end of list
		while(y.next != null && y.next.key.compareTo(x.key) < 0) {
			y = y.next;
		}
		
		if(y.next != null) {
			//skip duplicates
			if(y.next.key.compareTo(x.key) == 0) return;
			y.next.prev = x;
		}
		else {
			//set to tail if end of list
			this.tail = x;
		}
		//insert x after y
		x.next = y.next;
		y.next = x;
		x.prev = y;
		//increase list size
		this.size++;
	}	
	
	/**
	 * Adds an item to the set
	 * 
	 * @param x The item to add
	 */
	@Override
	public void add(Item x) {
		//create node and add
		Node n = new Node(x);
		this.add(n);
	}
	
	/**
	 * Searches for a key in the set and returns it if it finds it
	 * 
	 * @param k The key to search for
	 * @return Node The node with the corresponding key, or null if it doesn't exist
	 */
	private Node search(Item k) {
		Node x = this.head;
		//loop until end of list or until an equal or larger element is found
		while(x != null && x.key.compareTo(k) < 0) {
			x = x.next;
		}
		//if element is equal, we have found it, otherwise it is not in list
		if(x != null && x.key.compareTo(k) == 0) {
			return x;
		}
		return null;
	}
	
	/**
	 * Removes a node from the set
	 * 
	 * @param x The node to remove
	 */
	private void remove(Node x) {
		//connect node before x to node after x, or make x.next the new head
		if(x.prev != null) {
			x.prev.next = x.next;
		} else {
			this.head = x.next;
		}
		
		//connect node after x to node before x, or make x.prev the new tail
		if(x.next != null) {
			x.next.prev = x.prev;
		} else {
			this.tail = x.prev;
		}
		//decrease set size
		this.size--;
	}
	
	/**
	 * Removes an item from the set if it exists
	 * 
	 * @param x The item to remove
	 */
	@Override
	public void remove(Item x) {
		//remove node if found
		Node n = this.search(x);
		if(n != null) {
			this.remove(n);
		}		
	}

	/**
	 * Returns whether a given key is present in the set
	 * 
	 * @param x The key to check
	 * @return True if the element is present, otherwise false
	 */
	@Override
	public boolean is_element(Item x) {
		//return if node can be found
		return this.search(x) != null;
	}

	/**
	 * Returns the current size of the set
	 * 
	 * @return size The size of this set
	 */
	@Override
	public int set_size() {
		return this.size;
	}

	/**
	 * Returns a union of this set and the given set T
	 * 
	 * @param T The set to union this set with
	 * @return U The union of this set and T
	 */
	public ListSet<Item> union(AbstractSet<Item> T) {
		
		//If tree, convert to list
		ListSet<Item> L;
		if(T instanceof TreeSet<?>) {
			L = ((TreeSet<Item>) T).asList();
		} else {
			L = (ListSet<Item>) T;
		}
		
		//Create new list for union
		ListSet<Item> u = new ListSet<Item>();
		
		//Initialise 2 pointers to heads of lists
		Node n1 = this.head; Node n2 = L.head;
		
		while(n1 != null && n2 != null) {
			int comparison = n1.key.compareTo(n2.key);
			if(comparison <= 0) {
				//Insert smaller value and move its lists pointer on. Move both pointers on if values are equal (to avoid duplicates)
				u.insert_tail(n1.key);
				n1 = n1.next;
				if(comparison == 0) {
					n2 = n2.next;
				}
			}		
			else {
				u.insert_tail(n2.key);
				n2 = n2.next;
			}
		}
		
		//If one of the lists have not been exhausted, add the rest of its elements
		if(n1 == null) {
			while(n2 != null) {
				u.insert_tail(n2.key);
				n2 = n2.next;
			}
		}
		else{
			while(n1 != null) {
				u.insert_tail(n1.key);
				n1 = n1.next;
			}
		}
		return u;
	}

	/**
	 * Returns the intersection of this set and given set T
	 * 
	 * @param T The set to intersect this set with
	 * @return I The intersection of this set with T
	 */
	public ListSet<Item> intersection(AbstractSet<Item> T) {
		
		//If tree, convert to list
		ListSet<Item> L;
		if(T instanceof TreeSet<?>) {
			L = ((TreeSet<Item>) T).asList();
		} else {
			L = (ListSet<Item>) T;
		}
		
		//Create new list to store intersection
		ListSet<Item> x = new ListSet<Item>();
		
		
		Node n1 = this.head; Node n2 = L.head;
		while(n1 != null && n2 != null) {
			int comparison = n1.key.compareTo(n2.key);
			//Move smaller values pointer on, if equal then add the element and move both pointers on
			if(comparison <= 0) {
				n1 = n1.next;			
				if(comparison == 0) {
					x.insert_tail(n2.key);
					n2 = n2.next;
				}
			}
			
			else {
				n2 = n2.next;
			}
		}
		return x;
	}

	/**
	 * Returns the difference of this set and a given set T
	 * 
	 * @param T The set to difference this set with
	 * @return D The difference of this set and T
	 */
	public ListSet<Item> difference(AbstractSet<Item> T) {
		
		//If tree, convert to list
		ListSet<Item> L;
		if(T instanceof TreeSet<?>) {
			L = ((TreeSet<Item>) T).asList();
		} else {
			L = (ListSet<Item>) T;
		}
		
		//Create new list to store difference
		ListSet<Item> d = new ListSet<Item>();
		
		Node n1 = this.head; Node n2 = L.head;
		while(n1 != null && n2 != null) {
			int comparison = n1.key.compareTo(n2.key);
			//If first list value is smaller, it is not in second list so add to difference
			if(comparison < 0) {
				d.insert_tail(n1.key);
				n1 = n1.next;
			}
			else {
				//Move both pointers on or second pointer on if greater
				if(comparison == 0) n1 = n1.next;
				n2 = n2.next;
			}
		}
		
		//If we have exhausted second list, add rest of first list (as none of its last elements are in list 2)
		if(n2 == null) {
			while(n1 != null) {
				d.insert_tail(n1.key);
				n1 = n1.next;
			}
		}
		
		return d;	
	}

	/**
	 * Returns whether this set is a subset of given set T
	 * 
	 * @param T The set to check if this set is a subset of
	 * @return True if this set is a subset of T, False otherwise
	 */
	@Override
	public boolean subset(AbstractSet<Item> T) {
		//check if difference is empty
		return this.difference(T).set_empty();
	}

	/**
	 * Inserts a node at the tail of this list
	 * 
	 * @param x The node to insert
	 */
	private void insert_tail(Node x) {
		//If no elements, make x the head
		if(this.tail == null) {
			this.head = x;
		}
		else {
			//Add x after previous tail
			this.tail.next = x;
		}
		//Connect x to previous tail
		x.prev = this.tail;

		//Make x the tail and increase size
		this.tail = x;
		x.next = null;
		this.size++;
	}
	
	/**
	 * Inserts a key at the tail of this list
	 * 
	 * @param k The key to insert
	 */
	public void insert_tail (Item k) {
		//Create node and insert to tail
		Node n = new Node(k);
		this.insert_tail(n);
	}
	
	/**
	 * Converts this set to an array of items
	 * 
	 * @return a The array equivalent of this set
	 */
	public Item[] toArray() {
		
		if(this.set_empty()) {
			return null;
		}
		
		Node n = this.head;
		Item[] a = (Item[]) new Comparable[this.size];
		int i = 0;
		//Loop through list and add to corresponding index of array
		while(i < this.size && n != null) {
			a[i] = n.key;
			i++;
			n = n.next;
		}
		return a;
		
	}
	
	/**
	 * Prints the contents of this set
	 */
	@Override
	public void print() {
		
		//Print if empty
		if(this.set_empty()) {
			System.out.println("Empty set");
		}
		
		//Loop through list and add every element to print line
		Node n = this.head;
		while(n != null) {
			System.out.print(n.key + " ");
			n = n.next;
		}
		System.out.println();
	}
	
}
