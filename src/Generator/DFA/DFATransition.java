package Generator.DFA;

import Generator.Character.CharacterClass;
import Generator.DFA.DFANode;

public class DFATransition {
	CharacterClass match;
	DFANode start;
	DFANode end;
	
	public DFATransition(DFANode s, DFANode e, CharacterClass m) {
		start = s;
		end = e;
		match = m;
	}
	
	public boolean isTriggered(String c) {
		return match.isMatched(c);
	}
	
	public boolean isTriggered(char c) {
		return match.isMatched(c);
	}
	
	public void addMatch(char c) {
		match.accept(c);
	}
	
	public DFANode start() {
		return start;
	}
	
	public DFANode end() {
		return end;
	}
	
	public String toString() {
		String out = "";
		out += match;
		out += " --> " + end;
		return out;
	}
}
