package Generator.DFA;
import Generator.NFA.*;

import java.util.*;

public class DFAFactory {
	NFA nfa;
		
	public DFAFactory(NFA generatedNFA)
	{
		nfa = generatedNFA;		
	}
	public DFA build()
	{
		DFA dfa = new DFA();
		
		LinkedList<HashSet<Node>> queue = new LinkedList<HashSet<Node>>(); 
		HashSet<Node> DFAstates = epsilonClosure(nfa.start()); //start node
		
		Node dfaNode = new Node(); //map DFAstates to dfaNode
		Hashtable<HashSet<Node>, Node> mappedDFA = new Hashtable<HashSet<Node>, Node>(); 
		mappedDFA.put(DFAstates, dfaNode);
		queue.addLast(DFAstates);
		
		while(queue.size() > 0)
		{
			HashSet<Node> current = queue.removeFirst();
			
			for(char input=32; input<=126; input++){
				HashSet<Node> destination = new HashSet<Node>();
				Iterator<Node> itr = current.iterator();
				Node temp = new Node();
				List<Transition> adj;
				while(itr.hasNext())
				{
					temp = itr.next();
					adj = temp.adjacencyList();
					ListIterator<Transition> listItr = adj.listIterator();
					Transition trans = new Transition();
					while(listItr.hasNext())
					{
						trans = listItr.next();
						if(trans.isEpsilonTransition()==false && trans.isTriggered(input))
						{
							destination.add(trans.end());				
						}
							
					}					
				}
				if(destination.size() == 0)
				{
					//do nothing
				}
				else
				{
					itr = destination.iterator();
					while(itr.hasNext())
					{
						temp = itr.next();
						destination.addAll(epsilonClosure(temp));
					}
					destination = ep
				}
				

				
			}
			
			
		}
				
		
		return dfa;
	}
	
	
	//make e-closure(node) which is state of DFA
	public HashSet<Node> epsilonClosure(Node node) {		
		return epsilonClosure(node, new HashSet<Node>());		
	}	
	private HashSet<Node> epsilonClosure(Node node, HashSet<Node> set)
	{
		List<Transition> adj = node.adjacencyList();
				
		set.add(node);
		Iterator<Transition> itr = adj.iterator();
		Transition temp;
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
		return;		
	}
	
	public void getDFAstate(HashSet<Node> setOfNode)
	{
		if(hashtable.containsKey(setOfNode) == false)
		{
			hashtable.put(setOfNode, setOfNode.hashCode());
		}
	}
	
	
	//find available inputs from the e-closure(node)
	public void findAvailInput(HashSet<Node> state)
	{
		List<Transition> adj = node.adjacencyList();
		Iterator<Transition> itr = adj.iterator();
		Transition temp;
		while(itr.hasNext())
		{
			temp = itr.next();
			if(temp.isEpsilonTransition())
			{
				state.add(temp.end());
			}
		}
	}
	
}
