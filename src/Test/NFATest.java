package Test;

import org.junit.Before;
import org.junit.Test;

import Generator.Character.CharacterClass;
import Generator.NFA.NFA;

public class NFATest {

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testPrimitive() {
		//NFA nfa = new NFA(new CharacterClass());
		//System.out.println(nfa);
	}
	
	@Test
	public void testNFA() {
		CharacterClass a = new CharacterClass();
		//a.add('a');
		
		CharacterClass b = new CharacterClass();
		//b.add('b');
		//b.add('c');
		//b.add('0');
		//b.add('1');
		
		//CharacterClass c = a.union(b);
		
		NFA n1 = new NFA(null);
		NFA n2 = new NFA(b);
		
		System.out.println(n1.plus());
	}

}
