package Generator.NFA;

import Generator.Character.CharacterClass;


public class NFA {
	Node start;
	Node end;
	
	public NFA(CharacterClass match) {
		start = new Node();
		end = new Node();
		Node left = new Node();
		Node right = new Node();
		
		start.addEpsilonTransition(left);
		left.addTransition(right, match);
		right.addEpsilonTransition(end);
	}
}
