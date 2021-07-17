package ae2;

import java.util.Arrays;

/**
 * An implementation of the set ADT using a binary search tree
 * 
 * Student solution to ADS Assessed Exercise 2
 * 
 * @author Adam Fairlie <2461352f@student.gla.ac.uk>
 */
public class TreeSet<Item extends Comparable<Item>> implements AbstractSet<Item>{
	/*The root of the BST*/
	private Node root;
	/*The size of the BST*/
	private int size;
	
	/*A nested class representing each item in the tree*/
	private class Node{
		/*Value held by node*/
		private Item key; // value held by node
		/*Pointers to left subtree, right subtree and parent node*/
		private Node left, right, p;

		public Node(Item key) {
			this.key = key;
			this.left = null;
			this.right = null;
			this.p = null;
		}
	}
	
	/*Class constructor*/
	public TreeSet(){
		root = null;
		size = 0;
	}
	
	/**
	 * Converts this set to a ListSet
	 * 
	 * @return l The linked list implementation of this set
	 */
	public ListSet<Item> asList(){
		//start recursion at root
		ListSet<Item> l = new ListSet<Item>();
		this.asList(l, this.root);
		return l;
	}
	
	/**
	 * Converts the subtree with pointer x to a ListSet
	 * 
	 * @param x The node to begin conversion at
	 */
	private void asList(ListSet<Item> L, Node x){
		//traverse tree in order and add each element to list
		if(x != null) {
			asList(L, x.left);
			L.insert_tail(x.key);
			asList(L, x.right);	
		}
	}
	
	/**
	 * Returns the node with the minimum value in the subtree with root x
	 * 
	 * @param x The root of the subtree
	 * @return x The node with minimum element in the subtree
	 */
	public Node min(Node x) {
		//Go to end of leftmost branch
		while(x.left != null) {
			x = x.left;
		}
		return x;
	}
	
	/**
	 * Returns the minimum value in the tree
	 * 
	 * @param x The root of the tree
	 * @return k The minimum value of the tree
	 */
	public Item min_value(Node x) {
		return min(x).key;
	}
	
	/**
	 * Returns the node with the maximum value in the subtree with root x
	 * 
	 * @param x The root of the subtree
	 * @return x The node with maximum element in the subtree
	 */
	public Node max(Node x) {
		//Go to end of rightmost branch
		while(x.right != null) {
			x = x.right;
		}
		return x;
	}
	
	/**
	 * Returns the maximum value in the tree
	 * 
	 * @param x The root of the tree
	 * @return k The maximum value of the tree
	 */
	public Item max_value(Node x) {
		return max(x).key;
	}
	
	/**
	 * Adds an node to the set
	 * 
	 * @param z The node to add
	 */
	private void add (Node z) {
		Node y = null;
		Node x = this.root;
		
		//Traverse tree to find place for node value
		while(x != null) {
			y = x;
			//Do not add duplicates
			if(z.key.compareTo(x.key) == 0) {		
				return;
			}
			else if(z.key.compareTo(x.key) < 0) {
				x = x.left;
			}
			else {x = x.right;}
		}
		//Set nodes parent to node before
		z.p = y;
		//Set new node as left or right child of parent node (or as root if no parent)
		if(y == null) {
			this.root = z;
		}
		else if(z.key.compareTo(y.key) < 0) {
			y.left = z;
		}
		else {
			y.right = z;
		}
		this.size++;
	}
	
	/**
	 * Adds an item to the set
	 * 
	 * @param x The item to add
	 */
	@Override
	public void add(Item x) {
		//Create node to add
		Node n = new Node(x);
		this.add(n);
	}
	
	/**
	 * Searches for an item in the set and returns it if it finds it
	 * 
	 * @param x The root node to begin searching from
	 * @param k The key to search for
	 * @return Node The node with the corresponding key, or null if it doesn't exist
	 */
	private Node Search(Node x, Item k) {
		//Base case if current node is empty or equal to item
		if(x == null || x.key.compareTo(k) == 0) {
			return x;
		}
		//Traverse right or left subbranch
		if(x.key.compareTo(k) > 0) {
			return Search(x.left, k);
		}
		return Search(x.right, k);
	}
	
	/**
	 * Searches the tree for an element k
	 * 
	 * @param k The key to search for
	 * @return Node The node with the corresponding key, or null if it doesn't exist
	 */
	public Node Search(Item k) {
		//Start recursion at root
		return Search(this.root, k);
	}
	
	/**
	 * Removes a node from the set
	 * 
	 * @param z The node to remove
	 */
	private void remove(Node z) {
		//If no left branch, cut element from right branch
		if(z.left == null) {
			transplant(z, z.right);
			this.size--;
			return;
		}
		//If no right branch, cut element from left branch
		if(z.right == null) {
			transplant(z, z.left);
			this.size--;
			return;
		}
		
		//Find smallest element in right subtree
		Node y = this.min(z.right);
		
		if(y.p != z) {
			//Remove z and move its right subtree to y
			this.transplant(y, y.right);
			y.right = z.right;
			y.right.p = y;
		}
		//Remove z and move its left subtree to y
		this.transplant(z, y);
		y.left = z.left;
		y.left.p = y;
		
		this.size--;
		
	}
	
	/**
	 * Replaces a node u with new node v
	 * 
	 * @param u The node to replace
	 * @param v The replacement node
	 */
	private void transplant(Node u, Node v) {
		if(u.p == null) {
			this.root = v;
		}
		//If u is a left subtree, replace the parents left subtree with v
		else if(u == u.p.left) {
			u.p.left = v;
		}
		//Otherwise replace right subtree
		else {u.p.right = v;}
		//Give v its new parent (u's previous parent)
		if(v != null) {
			v.p = u.p;
		}
	}
	
	/**
	 * Removes an item from the set if it exists
	 * 
	 * @param x The item to remove
	 */
	@Override
	public void remove(Item x) {
		//Remove node if found
		Node n = this.Search(x);
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
		//return if element can be found
		return this.Search(x) != null;
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
	@Override
	public TreeSet<Item> union(AbstractSet<Item> T) {
		ListSet<Item> u;
		// Convert trees to lists, union lists, convert to array and create balanced tree
		if(T instanceof TreeSet<?>) {
			u = this.asList().union(((TreeSet<Item>) T).asList());
		} else {
			u = this.asList().union((ListSet<Item>) T);
		}
		
		return createBalancedTree(u.toArray());
		
		
	}

	/**
	 * Returns the intersection of this set and given set T
	 * 
	 * @param T The set to intersect this set with
	 * @return I The intersection of this set with T
	 */
	@Override
	public TreeSet<Item> intersection(AbstractSet<Item> T) {
		ListSet<Item> x;
		// Convert trees to lists, intersect lists, convert to array and create balanced tree
		if(T instanceof TreeSet<?>) {
			x = this.asList().intersection(((TreeSet<Item>) T).asList());
		} else {
			x = this.asList().intersection((ListSet<Item>) T);
		}
		
		return createBalancedTree(x.toArray());
	}

	/**
	 * Returns the difference of this set and a given set T
	 * 
	 * @param T The set to difference this set with
	 * @return D The difference of this set and T
	 */
	@Override
	public TreeSet<Item> difference(AbstractSet<Item> T) {
		ListSet<Item> d;
		// Convert trees to lists, difference lists, convert to array and create balanced tree
		if(T instanceof TreeSet<?>) {
			d = this.asList().difference(((TreeSet<Item>) T).asList());
		} else {
			d = this.asList().difference((ListSet<Item>) T);
		}
		
		return createBalancedTree(d.toArray());
	}

	/**
	 * Returns whether this set is a subset of given set T
	 * 
	 * @param T The set to check if this set is a subset of
	 * @return True if this set is a subset of T, False otherwise
	 */
	@Override
	public boolean subset(AbstractSet<Item> T) {
		//Convert to lists and use subset from the list implementation
		if(T instanceof TreeSet<?>) {
			return this.asList().subset(((TreeSet<Item>) T).asList());
		} else {
			return this.asList().subset((ListSet<Item>) T);
		}
	}
	
	/**
	 * Creates a balanced tree from a subarray of A
	 * 
	 * @param A the array to create a tree from
	 * @param l The left index of the subarray
	 * @param r The right index of the subarray
	 * @return n The node representing the head of the subtree
	 */
	private Node createBalancedTree(Item[] A, int l, int r){
		//If left and right indices overlap, return an empty node
		if(l > r) {
			return null;
			}
		//If pointers are the same, return single node
		if(l == r) {
			return new Node(A[r]);
		}
		//Create node from midpoint
		int mid = (l + r) / 2;
		Node n = new Node(A[mid]);
		//Create left and right subtrees from left and right subarrays
		n.left = createBalancedTree(A,l, mid-1);
		n.right = createBalancedTree(A,mid+1, r);
		return n;
	}
	
	/**
	 * Creates a balanced tree from an array A
	 * 
	 * @param A The array to create a tree from
	 * @return T The TreeSet created from A
	 */
	private TreeSet<Item> createBalancedTree(Item[] A) {
		System.out.println("Array: " + Arrays.toString(A));
		//If array is empty, return empty tree
		if(A == null) {
			return new TreeSet<Item>();
		}
		
		//Recursively create balanced tree
		Node n = createBalancedTree(A, 0, A.length - 1);
		//Create tree object to return
		TreeSet<Item> T = new TreeSet<Item>();
		T.root = n;
		T.size = A.length;
		return T;
	}
	
	/**
	 * Returns the height of the subtree with node x
	 * 
	 * @param x The root of the subtree
	 * @return h The height of the subtree
	 */
	private int height(Node x) {
		//base case for leaf element
		if(x == null) {
			return 0;
		}
		//Recursively find longest branch and add up each node along it
		return 1 + Math.max(height(x.left), height(x.right));
	}
	
	/**
	 * Returns the height of this tree
	 * 
	 * @return h The height of the tree
	 */
	public int height() {
		//start recursion at root
		return height(this.root);
	}
	
	/**
	 * Prints the subtree with root x using inorder traversal
	 * 
	 * @param x The root of the subtree
	 */
	private void inOrder(Node x) {
		//Print elements in order
		if(x != null) {
			inOrder(x.left);
			System.out.print(x.key + " ");
			inOrder(x.right);	
		}
	}

	/**
	 * Prints the tree in order using inorder traversal
	 * 
	 */
	@Override
	public void print() {
		//Print if empty
		if(this.set_empty()) {
			System.out.println("Empty set");
			return;
		}
		//Recursively print tree
		inOrder(this.root);
		System.out.println();
	}
	
}
