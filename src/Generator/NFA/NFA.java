package Generator.NFA;

import java.util.Stack;

import Generator.Character.CharacterClass;

/**
 * Class representing a Nondeterministic Finite Automaton (NFA). 
 * 
 * The internals of different NFA may not be independent!
 * 
 * @author eric
 *
 */
public class NFA {
	String name;
	Node start;
	Node end;
	private boolean epsilonNFA;
	
	/**
	 * Default NFA constructor returns a no-match NFA.
	 */
	private NFA() {
		start = new Node();
		end = new Node();
		
		end.terminal(true);
		
		epsilonNFA = false;
	}
	
	/**
	 * Primitive NFA matching a single character.
	 * 
	 * @param match
	 */
	public NFA(CharacterClass match) {
		this();
		start.addTransition(end, match);
	}
	
	/**
	 * Constructs a epsilon-matching NFA.
	 * 
	 * @return NFA
	 */
	public static NFA EpsilonNFA() {
		NFA N = new NFA();
		N.epsilonNFA = true;
		N.start.addEpsilonTransition(N.end);
		return N;
	}
	
	/**
	 * Constructs a no-match NFA.
	 * 
	 * @return
	 */
	public static NFA NoTransitionNFA() {
		return new NFA();
	}
	
	/**
	 * True if NFA matches epsilon.
	 * 
	 * @return
	 */
	public boolean isEpsilonNFA() {
		return epsilonNFA;
	}
	
	/**
	 * 
	 * @return start node of NFA
	 */
	public Node start() {
		return start;
	}
	
	/**
	 * 
	 * @return end node of NFA
	 */
	public Node end() {
		return end;
	}
	
	/**
	 * Performs the union of NFA B with itself.
	 * 
	 * @param B
	 * @return self
	 */
	public NFA or(NFA B) {
		Node S = new Node();
		Node E = new Node();
		
		S.addEpsilonTransition(start);
		end.addEpsilonTransition(E);
		
		S.addEpsilonTransition(B.start);
		B.end.addEpsilonTransition(E);
		
		end.terminal(false);
		B.end.terminal(false);
		
		E.terminal(true);
		
		start = S;
		end = E;

		if(!B.isEpsilonNFA()) {
			epsilonNFA = false;
		}
		return this;
	}
	
	/**
	 * Concatenates itself with B.
	 * 
	 * @param B
	 * @return
	 */
	public NFA and(NFA B) {
		Node S = new Node();
		Node E = new Node();
		
		S.addEpsilonTransition(start);
		end.addEpsilonTransition(B.end);
		B.end.addEpsilonTransition(E);
		
		end.terminal(false);
		B.end.terminal(false);
		
		E.terminal(true);
		
		start = S;
		end = E;

		if(!B.isEpsilonNFA()) {
			epsilonNFA = false;
		}
		return this;
	}
	
	/**
	 * Kleene star of itself.
	 * 
	 * @return
	 */
	public NFA star() {
		if(isEpsilonNFA()) {
			return this;
		} else {
			Node S = new Node();
			Node E = new Node();
			
			S.addEpsilonTransition(start);
			end.addEpsilonTransition(start);
			
			end.addEpsilonTransition(E);
			S.addEpsilonTransition(E);
			
			end.terminal(false);
			
			E.terminal(true);
			
			start = S;
			end = E;

			return this;
		}
	}
	
	/**
	 * Concatenates itself with its Kleene star.
	 * 
	 * @return
	 */
	public NFA plus() {
		if(isEpsilonNFA()) {
			return this;
		} else {
			Node S = new Node();
			Node E = new Node();
			
			S.addEpsilonTransition(start);
			end.addEpsilonTransition(start);
			
			end.addEpsilonTransition(E);
			
			end.terminal(false);
			
			E.terminal(true);
			
			start = S;
			end = E;

			return this;
		}
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
