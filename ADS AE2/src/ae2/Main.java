package ae2;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		//test data structures
		testStructures();	
	}

	public static void testStructures() {
		//get test data file
		Path file = Paths.get("src/int20k.txt");
		
		//parse lines into list of integers
		List<Integer> alist = new ArrayList<Integer>();
		try (Scanner s = new Scanner(new FileReader(file.toString()))) {
		    while (s.hasNext()) {
		        alist.add(s.nextInt());
		    }
		    
		    //initialise and populate sets
		    ListSet<Integer> dll = new ListSet<Integer>();
		    TreeSet<Integer> bst = new TreeSet<Integer>();
		    
		    dll.addAll(alist);
		    bst.addAll(alist);
		    
		    
		    //Print output
		    System.out.println("Results for int20k.txt: ");
		    System.out.println("------------------------------------");
		    //Doubly linked list
		    System.out.println("Doubly Linked List:");
		    testSet(dll);
		    //Binary search tree
		    System.out.println("Binary Search Tree:");
		    testSet(bst);
		    
		}
		catch(IOException e) {
			//Print error if file can't be opened
			System.out.println("Couldn't open file int20k.txt");
		}
	}
	
	public static void testSet(AbstractSet<Integer> s) {
		Random r = new Random();
	    Long ctime;    
	    int totalTime = 0;
	    //Generate 100 random numbers
	    for(int i = 0; i < 100; i++) {
	    	int rand = r.nextInt(50000);
	    	//Test if number is element and time result
	    	ctime = System.currentTimeMillis();
	    	s.is_element(rand);
	    	totalTime += (System.currentTimeMillis() - ctime);
	    	
	    }
	    
	    //Print results
	    System.out.println("Size: " + s.set_size());
	    //If set is BST, print its height
	    if(s instanceof TreeSet<?>) {
	    	System.out.println("Height: " + ((TreeSet<Integer>) s).height());
	    }
	    System.out.println("Total search time: " + totalTime + "ms");
	    System.out.println("Average search time: " + (totalTime / 100) + "ms\n");
	    
	    
	}
	
	
}
