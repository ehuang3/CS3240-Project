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
public class NFANode {
	boolean terminal;
	List<NFATransition> adj;
	
	static int number = 0;
	int color;
	String name;
	
	public NFANode() {
		this(false);
	}
	
	public NFANode(boolean isFinal) {
		terminal = isFinal;
		adj = new LinkedList<NFATransition>();
		
		name = String.valueOf(++number);
		color = 0;
	}
	
	public boolean isFinal() {
		return terminal;
	}
	
	public void terminal(boolean f) {
		terminal = f;
	}
	
	public List<NFATransition> adjacencyList() {
		return adj;
	}
	
	public void addTransition(NFANode end, CharacterClass match) {
		adj.add(new NFATransition(this, end, match));
	}
	
	public void addEpsilonTransition(NFANode end) {
		adj.add(new NFATransition(this, end));
	}
	
	public String toString() {
		return "Node " + name + (terminal ? " (final)" : "");
	}
}
