package Test;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import MiniRE.RecursiveDescent.MiniParser;

public class MiniParserTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testMiniParser() {
		MiniParser mini = new MiniParser();
		
		System.out.println(mini.lexer);
		
		mini.parseFile("test/sample/myScript.txt");
	}

	@Test
	public void testParseFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testParse() {
		fail("Not yet implemented");
	}

}
