package Generator.DFA;

import java.util.Stack;

public class DFA {
	String id;
	public DFANode start;
	
	public DFA() {
		start = new DFANode();
	}
	
	public void id(String n) {
		id = n;
	}
	
	public String id() {
		return id;
	}

	public DFANode start() {
		return start;
	}
	
	public String walk(String input) {
		DFANode now = start();
		boolean transitionMatched = true;
		int i=0;
		String lastTerminalMatch = "";
		String currentMatch = "";
		while( transitionMatched && (i < input.length()) ) {
			transitionMatched = false;
			char c = input.charAt(i);
			for(DFATransition t : now.adjacencyList()) {
				if(t.isTriggered(c)) {
					now = t.end();
					currentMatch += c;
					if(t.end().isTerminal()) {
						lastTerminalMatch = currentMatch;
					}
					i++;
					transitionMatched = true;
					break;
				}
			}
		}
		return lastTerminalMatch;
	}

	public String toString() {
		String out = id + "\n";
		
		Stack<DFANode> Q = new Stack<DFANode>();
		Q.add(start);
		while(!Q.isEmpty()) {
			DFANode u = Q.pop();
			out += u + "\n";
			for(DFATransition e : u.adjacencyList()) {
				if(e.end.color == 0) {
					Q.add(e.end);
					e.end.color = 1;
				}
				out += "\t" + e + "\n";
			}
		}
		
		out = out.substring(0, out.length()-1);
		
		// Uncolor
		Q.add(start);
		while(!Q.isEmpty()) {
			DFANode u = Q.pop();
			u.color = 0;
			for(DFATransition e : u.adjacencyList()) {
				if(e.end.color == 1) {
					Q.add(e.end);
				}
			}
		}
		return out;
	}
}
