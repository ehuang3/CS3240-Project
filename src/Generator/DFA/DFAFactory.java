package Generator.DFA;
import Generator.Character.CharacterClass;
import Generator.NFA.*;

import java.util.*;

public class DFAFactory {
	NFA nfa;
	boolean finalDFANode;
	
	public DFAFactory(NFA generatedNFA)
	{
		nfa = generatedNFA;
		finalDFANode = false;
	}
	public DFA build()
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
}
