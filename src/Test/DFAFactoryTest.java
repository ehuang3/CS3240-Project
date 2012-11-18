<<<<<<< HEAD
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
	
	
	 
	

=======
/**
 * 
 */
package Test;

import org.junit.Before;
import org.junit.Test;

import Generator.DFA.DFA;
import Generator.DFA.DFAFactory;
import Generator.NFA.NFA;
import Generator.NFA.NFAFactory;
import Generator.NFA.NFASimulator;

/**
 * @author eric
 *
 */
public class DFAFactoryTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link Generator.DFA.DFAFactory#build(Generator.NFA.NFA)}.
	 */
	@Test
	public void testBuild() {
		NFAFactory nfafactory = new NFAFactory();
		NFASimulator nfasim = new NFASimulator();
		DFAFactory dfafactory = new DFAFactory();
		
		String code = "$DIGIT   [0-9]";
		nfafactory.build(code);
		code = "$ALPHA   [a-zA-Z]";
		nfafactory.build(code);
		code = "$MULT   [*]";
		nfafactory.build(code);
		code = "$ADD    [+]";
		nfafactory.build(code);
		code = "$NUMBER    ($DIGIT)+";
		code = "$VAR       abc(|$MULT|$ADD)($ALPHA|$DIGIT)*Z";
		NFA nfa = nfafactory.build(code);
		System.out.println(nfasim.parse(nfa, "abc999"));
		System.out.println(nfasim.parse(nfa, "bc999"));
		
		DFA dfa = dfafactory.build(nfa);
		
		DFA min_dfa = dfafactory.minimize(dfa);
		
		//System.out.println(dfa);
		System.out.println(min_dfa);
	}
	
	@Test
	public void testEpsilon() {
		NFAFactory nfafactory = new NFAFactory();
		NFASimulator nfasim = new NFASimulator();
		DFAFactory dfafactory = new DFAFactory();
		
		String code = "$VAR       |";
		NFA nfa = nfafactory.build(code);
		
		DFA dfa = dfafactory.build(nfa);
		
		DFA min_dfa = dfafactory.minimize(dfa);
		
		//System.out.println(dfa);
		System.out.println(min_dfa);
		System.out.println("start " + min_dfa.start);
	}
>>>>>>> 260c8cd1f38663b4ff6166ec125f90d7a65306a3

}
