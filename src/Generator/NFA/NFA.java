package Generator.NFA;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
	String id;
	NFANode start;
	NFANode end;
	private boolean epsilonNFA;
	
	/**
	 * Default NFA constructor returns a no-match NFA.
	 */
	private NFA() {
		start = new NFANode();
		end = new NFANode();
		end.terminal(true);
		epsilonNFA = false;
	}
	
	/**
	 * Copy constructor
	 */
	public NFA(NFA nfa) {
		HashMap<NFANode, NFANode> map = new HashMap<NFANode, NFANode>();
		LinkedList<NFANode> Q = new LinkedList<NFANode>();
		
		Q.add(nfa.start);
		map.put(nfa.start, new NFANode(nfa.start));
		while(!Q.isEmpty()) {
			NFANode u = Q.poll();
			List<NFATransition> adj = u.adjacencyList();
			for(NFATransition t : adj) {
				NFANode v = t.end;
				if(!map.containsKey(v)) {
					map.put(v, new NFANode(v));
					Q.add(v);
				}
				NFANode u_copy = map.get(u);
				NFANode v_copy = map.get(v);
				if(t.isEpsilonTransition()) {
					u_copy.addEpsilonTransition(v_copy);
				} else {
					u_copy.addTransition(v_copy, t.match);
				}
			}
		}
		start = map.get(nfa.start);
		end = map.get(nfa.end);
		id = nfa.id;
		epsilonNFA = nfa.epsilonNFA;
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
	public NFANode start() {
		return start;
	}
	
	/**
	 * 
	 * @return end node of NFA
	 */
	public NFANode end() {
		return end;
	}
	
	public String id() {
		return id;
	}
	
	/**
	 * Performs the union of NFA B with itself.
	 * 
	 * @param B
	 * @return self
	 */
	public NFA or(NFA B) {
		NFANode S = new NFANode();
		NFANode E = new NFANode();
		
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
		NFANode S = new NFANode();
		NFANode E = new NFANode();
		
		S.addEpsilonTransition(start);
		end.addEpsilonTransition(B.start);
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
			NFANode S = new NFANode();
			NFANode E = new NFANode();
			
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
			NFANode S = new NFANode();
			NFANode E = new NFANode();
			
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
		
		Stack<NFANode> Q = new Stack<NFANode>();
		Q.add(start);
		while(!Q.isEmpty()) {
			NFANode u = Q.pop();
			u.color = 1;
			out += u + "\n";
			for(NFATransition e : u.adjacencyList()) {
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
			NFANode u = Q.pop();
			u.color = 0;
			for(NFATransition e : u.adjacencyList()) {
				if(e.end.color == 1) {
					Q.add(e.end);
				}
			}
		}
		return out;
	}
}
