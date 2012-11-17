package Generator.DFA;

import java.util.Stack;

import Generator.DFA.DFANode;
import Generator.DFA.DFATransition;

public class DFA {
	String name;
	public DFANode start;
	
	public DFA() {
		start = new DFANode();
	}
	
	public void name(String n) {
		name = n;
	}

	public DFANode start() {
		return start;
	}

	public String toString() {
		String out = "";
		
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
