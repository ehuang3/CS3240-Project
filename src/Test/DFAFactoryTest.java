package Test;

import java.util.HashSet;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;

import Generator.DFA.DFAFactory;
import Generator.DFA.DFANode;
import Generator.NFA.NFA;
import Generator.NFA.NFAFactory;
import Generator.NFA.NFANode;
import Generator.NFA.NFASimulator;



public class DFAFactoryTest {
	DFAFactory dfaFactory;
	NFASimulator NFASim;
	NFAFactory nfaFactory;
	NFA nfa;

	@Before
	public void setup() {
		NFASim = new NFASimulator();
		nfaFactory = new NFAFactory();
		nfa = nfaFactory.build("$TEST abc(a|b)*abc");			
		dfaFactory = new DFAFactory(nfa);
		
		System.out.println(dfaFactory.build());
		
		/**
		HashSet<NFANode> DFAstates = new HashSet<NFANode>();
		dfaFactory = new DFAFactory(nfa);
		DFAstates = dfaFactory.epsilonClosure(nfa.start());
		DFANode dfaNode = new DFANode();
		Hashtable<HashSet<NFANode>, DFANode> mappedDFA = new Hashtable<HashSet<NFANode>, DFANode>();
		mappedDFA.put(DFAstates, dfaNode);
		
		DFAstates = dfaFactory.epsilonClosure(nfa.end());
		dfaNode = new DFANode();
		mappedDFA.put(DFAstates, dfaNode);
		
		System.out.println(mappedDFA);
		**/
	}
	@Test
	public void test(){
		
	}
	
	
	 
	


}
