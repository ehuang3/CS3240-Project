package Generator.DFA;

import java.util.Stack;

import Generator.DFA.Node;
import Generator.DFA.Transition;

public class DFA {
	String name;
	Node start;
	Node end;
	
	public DFA() {
		start = new Node();
		end = new Node();
	}
	
	public void name(String n) {
		name = n;
	}

	public Node start() {
		return start;
	}

	public Node end() {
		return end;
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
