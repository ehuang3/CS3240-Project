package Test;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import MiniRE.AST;
import MiniRE.RecursiveDescent.MiniParser;

public class MiniParserTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testMiniParser() {
		MiniParser mini = new MiniParser();
		
		System.out.println(mini.lexer);
		
		AST ast;
		ast = mini.parseFile("test/sample/script.txt");
		System.out.println("===============================");
		mini.parseFile("test/phase-ii/debug/ascii_test.txt");
		System.out.println("===============================");
		mini.parseFile("test/phase-ii/debug/regex_test.txt");
		System.out.println("===============================");
		mini.parseFile("test/phase-ii/debug/fail/empty_program.txt");
		
		System.out.println(ast);
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
