package ae2;

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
	
	default void addAll(Iterable<Item> a) {
		for(Item i : a) {
			this.add(i);
		}
	}
	
	default void addAll(Item[] a) {
		for(Item i : a) {
			this.add(i);
		}
	}
	
}
