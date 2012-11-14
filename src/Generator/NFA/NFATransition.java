package Generator.NFA;

import Generator.Character.CharacterClass;

/**
 * A transition in a NFA. Supports epsilon transitions.
 * 
 * @author eric
 *
 */
public class NFATransition {
	CharacterClass match;
	NFANode start;
	NFANode end;
	boolean epsilon;
	
	public NFATransition() {
		this(null,null,null);
	}
	
	public NFATransition(NFANode s, NFANode e) {
		this(s, e, new CharacterClass());
		epsilon = true;
	}
	
	public NFATransition(NFANode s, NFANode e, CharacterClass c) {
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
	
	public NFANode start() {
		return start;
	}
	
	public NFANode end() {
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
