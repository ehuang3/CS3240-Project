package Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import Generator.NFA.NFA;
import Generator.NFA.NFAFactory;
import Generator.NFA.NFASimulator;
import Generator.Token.op_code;

public class NFAFactoryTest {
	NFASimulator NFASim;
	NFAFactory Factory;

	@Before
	public void setUp() throws Exception {
		NFASim = new NFASimulator();
		Factory = new NFAFactory();
	}
	
	@Test
	public void testEpsilon() {
		NFA epsNFA = NFA.EpsilonNFA();
		
		epsNFA = Factory.build("$NULL ()()()()()*||||a");
		
		System.out.println(epsNFA);
		
		assertEquals("a", NFASim.parse(epsNFA, "a").token);
		assertEquals("Failed", NFASim.parse(epsNFA, " ").token);
	}

	@Test
	public void testReg_expr() {
		fail("Not yet implemented");
	}

}
