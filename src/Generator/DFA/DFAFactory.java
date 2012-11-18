package Generator.DFA;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;

import Generator.Character.CharacterClass;
import Generator.NFA.NFA;
import Generator.NFA.NFANode;
import Generator.NFA.NFATransition;

public class DFAFactory {
	boolean finalDFANode;
	
	public DFAFactory()
	{
		finalDFANode = false;
	}
	
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
	
	public DFA build(NFA nfa)
	{
		
		DFA dfa = new DFA();		
		LinkedList<HashSet<NFANode>> queue = new LinkedList<HashSet<NFANode>>();  
		HashSet<NFANode> DFAstates = epsilonClosure(nfa.start()); //start node
		
		DFANode dfaNode = new DFANode(); //map NFANodes to a dfaNode (start node of DFA)
		dfa.start = dfaNode; //set start node of DFA
		
		//???????????need to make dfa.end by LinkedList (multiple end nodes)
		
		Hashtable<HashSet<NFANode>, DFANode> mappedDFA = new Hashtable<HashSet<NFANode>, DFANode>(); 
		mappedDFA.put(DFAstates, dfaNode);
		
		if(finalDFANode == true) ////////////////////////////////////////////////
		{
			dfaNode.terminal(true);
			finalDFANode = false;
		}
				
		queue.addLast(DFAstates);
		
		
		while(queue.size() > 0)
		{
			HashSet<NFANode> current = queue.removeFirst();
		
			for(char input=32; input<=126; input++){
				HashSet<NFANode> destination = new HashSet<NFANode>();
				Iterator<NFANode> itr = current.iterator();
				NFANode temp = new NFANode();
				List<NFATransition> adj;
				while(itr.hasNext())
				{
					temp = itr.next();
					adj = temp.adjacencyList();
					ListIterator<NFATransition> listItr = adj.listIterator();
					NFATransition trans = new NFATransition();
					while(listItr.hasNext())
					{
						trans = listItr.next();
						if(trans.isEpsilonTransition()==false && trans.isTriggered(input))
						{
							if(trans.end().isFinal() == true)
							{
								finalDFANode = true;
							}
							destination.add(trans.end());
							destination.addAll(epsilonClosure(trans.end()));
						}
					}
				}
		
				if(destination.size() == 0)
				{
					//do nothing
				}
				else
				{
					
					if(mappedDFA.containsKey(destination) == false) //new DFAnode
					{
						DFANode newNode = new DFANode();
						mappedDFA.put(destination, newNode);
						queue.addLast(destination);
						if(finalDFANode == true)
						{
							newNode.terminal(true);
							finalDFANode = false;
						}
						
						CharacterClass cls = new CharacterClass();
						cls.accept(input);
					    
						mappedDFA.get(current).addTransition(newNode, cls); 
					}
					else //there is transition with already existed DFA nodes
					{
						DFANode start = mappedDFA.get(current);
						DFANode end = mappedDFA.get(destination);
						if(finalDFANode == true)
						{
							end.terminal(true);
							finalDFANode = false;
						}
						CharacterClass cls = new CharacterClass();
						cls.accept(input);
						
						start.addTransition(end, cls);
					}
				}				
			}					
		}
		return dfa;
	}
	
	//make e-closure(node) which is state of DFA
	public HashSet<NFANode> epsilonClosure(NFANode node) {		
		return epsilonClosure(node, new HashSet<NFANode>());		
	}
	private HashSet<NFANode> epsilonClosure(NFANode node, HashSet<NFANode> set)
	{
		List<NFATransition> adj = node.adjacencyList();
		
		if(node.isFinal() == true)
		{
			finalDFANode = true; 
		}
		set.add(node);
		Iterator<NFATransition> itr = adj.iterator();
		NFATransition temp;
		while(itr.hasNext())
		{
			temp = itr.next();
			if(temp.isEpsilonTransition())
			{
				if(set.add(temp.end())==true) //in case of adding new node, find e-closure 
				{					
					epsilonClosure(temp.end(), set); //call recursively
				}
			}
		}
		return set;		
	}

	@SuppressWarnings("serial")
	class EpsilonClosure<T> extends HashSet<T> {
		boolean terminal;
	}
	
	@SuppressWarnings("serial")
	class DFAClosure<T> extends EpsilonClosure<T> {
	}
}
