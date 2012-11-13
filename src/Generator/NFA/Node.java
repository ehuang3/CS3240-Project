package Generator.NFA;

import java.util.LinkedList;
import java.util.List;

import Generator.Character.CharacterClass;

/**
 * A node in a NFA.
 * 
 * @author eric
 *
 */
public class Node {
	boolean terminal;
	List<Transition> adj;
	
	static int number = 0;
	int color;
	String name;
	
	public Node() {
		this(false);
	}
	
	public Node(boolean isFinal) {
		terminal = isFinal;
		adj = new LinkedList<Transition>();
		
		name = String.valueOf(++number);
		color = 0;
	}
	
	public boolean isFinal() {
		return terminal;
	}
	
	public void terminal(boolean f) {
		terminal = f;
	}
	
	public List<Transition> adjacencyList() {
		return adj;
	}
	
	public void addTransition(Node end, CharacterClass match) {
		adj.add(new Transition(this, end, match));
	}
	
	public void addEpsilonTransition(Node end) {
		adj.add(new Transition(this, end));
	}
	
	public String toString() {
		return "Node " + name + (terminal ? " (final)" : "");
	}
}
