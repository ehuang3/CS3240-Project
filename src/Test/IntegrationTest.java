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
		Tokenizer T = new Tokenizer("");
		CharacterClassFactory ccf = new CharacterClassFactory(T);
		NFAFactory nf = new NFAFactory();
		nf.charClassFactory(ccf);
		nf.tokenizer(T);
		
		NFASimulator sim = new NFASimulator();
		
		
		String code = "$DIGIT   [0-9]";
		ccf.build(code);
		code = "$ALPHA   [a-zA-Z]";
		ccf.build(code);
		code = "$MULT   [*]";
		ccf.build(code);
		code = "$ADD    [+]";
		ccf.build(code);
		
		code = "$NUMBER    ($DIGIT)+";
		NFA digit = nf.build(code);
		
		System.out.println(digit);
		
		System.out.println(sim.parse(digit, "1239999"));
		
		code = "$IDEN    $ALPHA($ALPHA|$DIGIT)*";
		NFA iden = nf.build(code);
		
		System.out.println(sim.parse(iden, "ehuang3"));
		
		code = "$IDEN    $ALPHA($ALPHA|[^3] IN $DIGIT)*";
		NFA nfa = nf.build(code);
		
		System.out.println(sim.parse(nfa, "ehuang3"));
	}
	
	@Test
	public void testMore() {
		
	}

}
