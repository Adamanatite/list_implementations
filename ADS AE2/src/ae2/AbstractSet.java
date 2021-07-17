package ae2;

/**
 * Interface for the set ADT and its required methods
 * 
 * Student solution to ADS Assessed Exercise 2
 * 
 * @author Adam Fairlie <2461352f@student.gla.ac.uk>
 */
public interface AbstractSet<Item extends Comparable<Item>> {
	
	//Standard operations
	void add(Item x);
	void remove(Item x);	
	boolean is_element(Item x);
	int set_size();
	default boolean set_empty() {
		return this.set_size() == 0;
	};
	
	//Set theory methods
	AbstractSet<Item> union(AbstractSet<Item> S);
	AbstractSet<Item> intersection(AbstractSet<Item> S);
	AbstractSet<Item> difference(AbstractSet<Item> S);
	boolean subset(AbstractSet<Item> S);
	
	//Useful shared methods
	void print();
	
	/**
	 * Adds all of a list into the set at once
	 * 
	 * @param a The list or other iterable
	 */
	default void addAll(Iterable<Item> a) {
		for(Item i : a) {
			this.add(i);
		}
	}
	
	/**
	 * Adds all of an array into the set at once
	 * 
	 * @param a The array
	 */
	default void addAll(Item[] a) {
		for(Item i : a) {
			this.add(i);
		}
	}
	
}
