package Test;

import org.junit.Before;
import org.junit.Test;

import Generator.Tokenizer;
import Generator.Character.CharacterClassFactory;
import Generator.NFA.NFA;
import Generator.NFA.NFAFactory;
import Generator.NFA.NFASimulator;

public class IntegrationTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSpec() {
		NFAFactory factory = new NFAFactory();
		NFASimulator sim = new NFASimulator();
		
		String code = "$DIGIT   [0-9]";
		factory.build(code);
		code = "$ALPHA   [a-zA-Z]";
		factory.build(code);
		code = "$MULT   [*]";
		factory.build(code);
		code = "$ADD    [+]";
		factory.build(code);
		
		code = "$NUMBER    ($DIGIT)+";
		NFA digit = factory.build(code);
		
		System.out.println(digit);
		
		System.out.println(sim.parse(digit, "1239999"));
		
		code = "$IDEN    $ALPHA($ALPHA|$DIGIT)*";
		NFA iden = factory.build(code);
		
		System.out.println(sim.parse(iden, "ehuang3"));
		
		code = "$IDEN    $ALPHA($ALPHA|[^6] IN $DIGIT)*";
		NFA nfa = factory.build(code);
		
		System.out.println(sim.parse(nfa, "ehuang3"));
	}
	
	@Test
	public void testMore() {
		
	}

}
