package Generator.NFA;

import Generator.Character.CharacterClass;

/**
 * A transition in a NFA. Supports epsilon transitions.
 * 
 * @author eric
 *
 */
public class Transition {
	CharacterClass match;
	Node start;
	Node end;
	boolean epsilon;
	
	public Transition() {
		this(null,null,null);
	}
	
	public Transition(Node s, Node e) {
		this(s, e, new CharacterClass());
		epsilon = true;
	}
	
	public Transition(Node s, Node e, CharacterClass c) {
		start = s;
		end = e;
		match = c;
	}
	
	public boolean isTriggered(String c) {
		return epsilon || match.isMatched(c);
	}
	
	public boolean isTriggered(char c) {
		return epsilon || match.isMatched(c);
	}
	
	public boolean isTriggered() {
		return epsilon;
	}
	
	public boolean isEpsilonTransition() {
		return epsilon;
	}
	
	public Node start() {
		return start;
	}
	
	public Node end() {
		return end;
	}
	
	public String toString() {
		String out = "";
		if(epsilon) {
			out += "ε";
		} else {
			out += match;
		}
		out += " --> " + end;
		return out;
	}
}
