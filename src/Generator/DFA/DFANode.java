package Generator.DFA;

import java.util.LinkedList;
import java.util.List;

import Generator.Character.CharacterClass;

public class DFANode {
	List<DFATransition> adj;
	boolean terminal;
	String token;
	
	static int number = 0;
	int color;
	String name;
	
	public DFANode() {
		adj = new LinkedList<DFATransition>();
		terminal = false;
		token = "";
		
		name = String.valueOf(++number);
		color = 0;
	}
	
	public boolean isTerminal() {
		return terminal;
	}
	
	public void terminal(boolean f) {
		terminal = f;
	}
	
	public void token(String t) {
		token = t;
	}
	
	public List<DFATransition> adjacencyList() {
		return adj;
	}
	
	public void addTransition(DFANode end, CharacterClass match) {
		adj.add(new DFATransition(this, end, match));
	}
	
	public String toString() {
		return "Node " + name + (terminal ? (" (final) " + token) : "");
	}
}
