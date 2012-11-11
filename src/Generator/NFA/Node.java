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
	
	public Node() {
		this(false);
	}
	
	public Node(boolean isFinal) {
		terminal = isFinal;
		adj = new LinkedList<Transition>();
	}
	
	public boolean isFinal() {
		return terminal;
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
}
