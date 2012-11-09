package Test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Generator.NFA.NFA;

public class NFATest {
	NFA nfa;

	@Before
	public void setUp() throws Exception {
		nfa = new NFA();
		
		NFA letter = new NFA("abc");
		NFA digit = new NFA("012");
		
		NFA or = letter.or(digit);
		NFA star = or.star();
		NFA done = letter.and(star);
		
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
