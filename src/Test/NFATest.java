package Test;

import org.junit.Before;
import org.junit.Test;

import Generator.Character.CharacterClass;
import Generator.NFA.NFA;
import Generator.NFA.NFAFactory;

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
		
		NFA n1 = new NFA(a);
		@SuppressWarnings("unused")
		NFA n2 = new NFA(b);
		
		System.out.println(n1.plus());
	}
	
	@Test
	public void testNFACopy() {
		NFAFactory Factory = new NFAFactory();
		NFA word = Factory.build("$WORD [a-zA-Z0-9_]+");
		NFA copy = new NFA(word);
		System.out.println(word);
		System.out.println("COPY");
		System.out.println(copy);
	}
}
