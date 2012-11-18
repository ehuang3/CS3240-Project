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
	
	public DFANode(boolean term) {
		this();
		terminal = term;
	}
	
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
	
	public void appendTransition(DFANode end, char c) {
		for(DFATransition t : adj) {
			if(t.end == end) {
				t.addMatch(c);
				return;
			}
		}
		CharacterClass cls = new CharacterClass();
		cls.accept(c);
		addTransition(end, cls);
	}
	
	public String toString() {
		return "Node " + name + (terminal ? (" (final) " + token) : "");
	}
}
