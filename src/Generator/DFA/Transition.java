package Generator.DFA;

import Generator.Character.CharacterClass;
import Generator.DFA.Node;

public class Transition {
	CharacterClass match;
	Node start;
	Node end;
	
	public Transition(Node s, Node e, CharacterClass m) {
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
	
	public Node start() {
		return start;
	}
	
	public Node end() {
		return end;
	}
	
	public String toString() {
		String out = "";
		out += match;
		out += " --> " + end;
		return out;
	}
}
