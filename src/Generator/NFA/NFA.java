package Generator.NFA;

import Generator.Character.CharacterClass;

/**
 * NFA implementation. The internals of the NFA are not copy-safe!
 * So do not concatenate NFAs with unrelated NFAs.
 * 
 * @author eric
 *
 */
public class NFA {
	Node start;
	Node end;
	
	private NFA() {
		start = new Node();
		end = new Node();
	}
	
	public NFA(CharacterClass match) {
		start = new Node();
		end = new Node();
		Node left = new Node();
		Node right = new Node();
		
		start.addEpsilonTransition(left);
		left.addTransition(right, match);
		right.addEpsilonTransition(end);
	}
	
	public Node start() {
		return start;
	}
	
	public Node end() {
		return end;
	}
	
	public NFA or(NFA B) {
		NFA A = this;
		NFA N = new NFA();
		
		N.start.addEpsilonTransition(A.start);
		A.end.addEpsilonTransition(N.end);
		
		N.start.addEpsilonTransition(B.start);
		B.end.addEpsilonTransition(N.end);
		
		return N;
	}
	
	public NFA and(NFA B) {
		NFA A = this;
		NFA N = new NFA();
		
		N.start.addEpsilonTransition(A.start);
		A.end.addEpsilonTransition(B.start);
		B.end.addEpsilonTransition(N.end);
		
		return N;
	}
	
	public NFA star() {
		NFA A = this;
		NFA N = new NFA();
		
		N.start.addEpsilonTransition(A.start);
		A.start.addEpsilonTransition(N.end);
		
		A.end.addEpsilonTransition(A.start);
		N.start.addEpsilonTransition(N.end);
		
		return N;
	}
}
