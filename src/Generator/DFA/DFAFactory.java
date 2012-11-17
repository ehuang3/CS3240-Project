package Generator.DFA;

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
}