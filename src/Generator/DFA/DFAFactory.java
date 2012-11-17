package Generator.DFA;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

import Generator.NFA.NFA;
import Generator.NFA.NFANode;
import Generator.NFA.NFATransition;

public class DFAFactory {
	static final char CLS_MIN = ' ';	// 32
	static final char CLS_MAX = '~';	// 126
	
	public DFA minimize(DFA dfa) {
		ArrayList<DFANode> states = new ArrayList<>();
		Queue<DFANode> Q = new LinkedList<>();
		Q.add(dfa.start);
		states.add(dfa.start);
		while(!Q.isEmpty()) {
			DFANode u = Q.poll();
			for(DFATransition t : u.adjacencyList()) {
				if(!states.contains(t.end())) {
					states.add(t.end());
					Q.add(t.end());
				}
			}
		}
		int numStates = states.size();
		boolean[][] table = new boolean[numStates][numStates];
		for(int i=0; i<numStates-1; i++) {
			for(int j=i+1; j<numStates; j++) {
				table[i][j] = states.get(i).terminal == states.get(j).terminal;
			}
		}
		
//		System.out.print("        ");
//		for(int i=numStates-1; i>=1; i--) {
//			System.out.format("%6s ", states.get(i).name);
//		}
//		System.out.println();
//		for(int i=0; i<numStates-1; i++) {
//			System.out.print(states.get(i) + ": ");
//			for(int j=numStates-1; j>=i+1; j--) {
//				System.out.format("%6b ", table[i][j]);
//			}
//			System.out.println();
//		}
		
		boolean min = false;
		while(!min) {
			min = true;
			for(int i=0; i<numStates-1; i++) {
				for(int j=i+1; j<numStates; j++) {
					DFANode p = states.get(i);
					DFANode q = states.get(j);
					for(char input=CLS_MIN; input<=CLS_MAX; input++) {
						DFANode r = null;
						for(DFATransition t : p.adjacencyList()) {
							if(t.isTriggered(input)) {
								r = t.end();
								break;
							}
						}
						DFANode s = null;
						for(DFATransition t : q.adjacencyList()) {
							if(t.isTriggered(input)) {
								s = t.end();
								break;
							}
						}
						if(r == null && s == null) {
							// Ignore
						} else if(r == null && s != null) {
							table[i][j] = false;
						} else if(r != null && s == null) {
							table[i][j] = false;
						} else if(s == r){
							// Ignore
						} else {
							int k = states.indexOf(s);
							int l = states.indexOf(r);
							min &= !(table[i][j] ^ table[k][l]);
							table[i][j] &= table[k][l];
						}
					}
				}
			}
		}
		
//		System.out.print("        ");
//		for(int i=numStates-1; i>=1; i--) {
//			System.out.format("%6s ", states.get(i).name);
//		}
//		System.out.println();
//		for(int i=0; i<numStates-1; i++) {
//			System.out.print(states.get(i) + ": ");
//			for(int j=numStates-1; j>=i+1; j--) {
//				System.out.format("%6b ", table[i][j]);
//			}
//			System.out.println();
//		}
		
		Hashtable<DFAClosure<DFANode>, DFANode> map = new Hashtable<>();
		ArrayList<DFAClosure<DFANode>> min_states = new ArrayList<>();
		boolean[] visited = new boolean[numStates];
		for(int i=0; i<numStates; i++) {
			if(!visited[i]) {
				DFAClosure<DFANode> node = new DFAClosure<>();
				node.add(states.get(i));
				node.terminal = states.get(i).terminal;
				for(int j=i+1; j<numStates; j++) {
					if(table[i][j]) {
						node.add(states.get(j));
						visited[j] = true;
					}
				}
				min_states.add(node);
			}
		}
		for(DFAClosure<DFANode> u : min_states) {
			map.put(u, new DFANode(u.terminal));
		}
		for(DFAClosure<DFANode> U : min_states) {
			for(char input = CLS_MIN; input <= CLS_MAX; input++) {
				DFAClosure<DFANode> V = new DFAClosure<>();
				for(DFANode u : U) {
					for(DFATransition t : u.adjacencyList()) {
						if(t.isTriggered(input)) {
							for(DFAClosure<DFANode> C : min_states) {
								if(C.contains(t.end)) {
									V = C;
									break;
								}
							}
							break;
						}
					}
				}
				if(!V.isEmpty()) {
					//System.out.println(U + " " + input + " --> " + V);
					map.get(U).appendTransition(map.get(V), input);
				}
			}
		}
		DFA min_dfa = new DFA();
		//System.out.println(min_states);
		//System.out.println(dfa.start);
		for(DFAClosure<DFANode> U : min_states) {
			if(U.contains(dfa.start)) {
				min_dfa.start = map.get(U);
				break;
			}
		}
		min_dfa.id = dfa.id;
		return min_dfa;
	}
	
	public DFA build(NFA nfa) {
		Hashtable<EpsilonClosure<NFANode>, DFANode> table = new Hashtable<>();
		Queue<EpsilonClosure<NFANode>> Q = new LinkedList<>();
		EpsilonClosure<NFANode> start = epsilonClosure(nfa.start());
		table.put(start, new DFANode(start.terminal));
		Q.add(start);
		while(!Q.isEmpty()) {
			EpsilonClosure<NFANode> U = Q.poll();
			for(char input = CLS_MIN; input <= CLS_MAX; input++) {
				EpsilonClosure<NFANode> V = new EpsilonClosure<>();
				for(NFANode n : U) {
					for(NFATransition t : n.adjacencyList()) {
						if(!t.isEpsilonTransition() && t.isTriggered(input)) {
							EpsilonClosure<NFANode> v = epsilonClosure(t.end());
							V.addAll(v);
							V.terminal |= v.terminal;
						}
					}
				}
				if(!V.isEmpty()) {
					if(!table.containsKey(V)) {
						table.put(V, new DFANode(V.terminal));
						Q.add(V);
					}
					table.get(U).appendTransition(table.get(V), input);
				}
			}
		}
		DFA dfa = new DFA();
		dfa.start = table.get(start);
		dfa.id = nfa.id();
		return dfa;
	}
	
	private EpsilonClosure<NFANode> epsilonClosure(NFANode start) {
		EpsilonClosure<NFANode> eclosure = new EpsilonClosure<NFANode>();
		Queue<NFANode> Q = new LinkedList<NFANode>();
		Q.add(start);
		eclosure.add(start);
		while(!Q.isEmpty()) {
			NFANode u = Q.poll();
			for(NFATransition t : u.adjacencyList()) {
				NFANode v = t.end();
				if(t.isEpsilonTransition() && !eclosure.contains(v)) {
					eclosure.terminal |= v.isFinal();
					eclosure.add(v);
					Q.add(v);
				}
			}
		}
		return eclosure;
	}
	
	@SuppressWarnings("serial")
	class EpsilonClosure<T> extends HashSet<T> {
		boolean terminal;
	}
	
	@SuppressWarnings("serial")
	class DFAClosure<T> extends EpsilonClosure<T> {
	}
}