package Generator.NFA;

import java.util.Stack;

import Generator.Character.CharacterClass;

/**
 * NFA implementation. The internals of the NFA are not copy-safe!
 * So do not concatenate NFAs with unrelated NFAs.
 * 
 * @author eric
 *
 */
public class NFA {
	String name;
	Node start;
	Node end;
	private boolean epsilonNFA;
	
	private NFA() {
		start = new Node();
		end = new Node();
		end.setFinal(true);
	}
	
	public NFA(CharacterClass match) {
		this();
		start.addTransition(end, match);
	}
	
	public static NFA EpsilonNFA() {
		NFA N = new NFA();
		N.start.addEpsilonTransition(N.end);
		N.epsilonNFA = true;
		return N;
	}
	
	public boolean isEpsilonNFA() {
		return epsilonNFA;
	}
	
	public static NFA NoTransitionNFA() {
		return new NFA();
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
		
		A.end().setFinal(false);
		B.end().setFinal(false);
		
		return N;
	}
	
	public NFA and(NFA B) {
		NFA A = this;
		NFA N = new NFA();
		
		N.start.addEpsilonTransition(A.start);
		A.end.addEpsilonTransition(B.start);
		B.end.addEpsilonTransition(N.end);
		
		A.end().setFinal(false);
		B.end().setFinal(false);
		
		return N;
	}
	
	public NFA star() {
		NFA A = this;
		NFA N = new NFA();
		
		N.start.addEpsilonTransition(A.start);
		A.end.addEpsilonTransition(A.start);
		
		A.end.addEpsilonTransition(N.end);
		N.start.addEpsilonTransition(N.end);
		
		A.end().setFinal(false);
		
		return N;
	}
	
	public NFA plus() {
		NFA A = this;
		NFA N = new NFA();
		
		N.start.addEpsilonTransition(A.start);
		
		A.end.addEpsilonTransition(A.start);
		A.end.addEpsilonTransition(N.end);
		
		A.end().setFinal(false);
		
		return N;
	}
	
	public String toString() {
		String out = "";
		
		Stack<Node> Q = new Stack<Node>();
		Q.add(start);
		while(!Q.isEmpty()) {
			Node u = Q.pop();
			u.color = 1;
			out += u + "\n";
			for(Transition e : u.adjacencyList()) {
				if(e.end.color == 0) {
					Q.add(e.end);
				}
				out += "\t" + e + "\n";
			}
		}
		
		out = out.substring(0, out.length()-1);
		
		// Uncolor
		Q.add(start);
		while(!Q.isEmpty()) {
			Node u = Q.pop();
			u.color = 0;
			for(Transition e : u.adjacencyList()) {
				if(e.end.color == 1) {
					Q.add(e.end);
				}
			}
		}
		return out;
	}
}
