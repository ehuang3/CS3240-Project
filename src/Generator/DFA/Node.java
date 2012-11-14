package Generator.DFA;

import java.util.LinkedList;
import java.util.List;

import Generator.Character.CharacterClass;

public class Node {
	List<Transition> adj;
	boolean terminal;
	String token;
	
	static int number = 0;
	int color;
	String name;
	
	public Node() {
		adj = new LinkedList<Transition>();
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
	
	public List<Transition> adjacencyList() {
		return adj;
	}
	
	public void addTransition(Node end, CharacterClass match) {
		adj.add(new Transition(this, end, match));
	}
	
	public String toString() {
		return "Node " + name + (terminal ? (" (final) " + token) : "");
	}
}
