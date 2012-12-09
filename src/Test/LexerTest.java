package Test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Generator.Lexer.Lexer;
import Generator.Lexer.Token;

public class LexerTest {
	Lexer lexer;
	String spec;
	String code;

	@Before
	public void setUp() throws Exception {
		lexer = new Lexer();
		spec = "$SPACE [ ]\n" +
			   "$ALPHA [a-zA-Z]\n" +
			   "$DIGIT [0-9]\n" +
			   "$LOWER [a-z]\n" +
			   "$UPPER [A-Z]\n" +
			   "$PERIOD [.]\n" +
			   "\n" +
			   "$INT   ($DIGIT)+\n" +
			   "$FLOAT ($DIGIT)+($PERIOD)($DIGIT)*\n" +
			   "$ID    $ALPHA ($ALPHA | $DIGIT | [_\\-])*\n" +
			   "$PLUS  \\+\n" +
			   "$MULT  \\*\n" +
			   "$DIV   \\\\\n" +
			   "$EQUAL =\n" +
			   "$SEMI  ;\n";
		code = "a = 10 + 8;\n" +
			   "b = a * 10;\n" +
			   "c = b + a;\n" +
			   "d = b \\ a;\n" +
			   "e = 1.91283;";
		//lexer.respec(spec);
		//lexer.tokenize(code);
	}

	@Test
	public void testLexer() {
		lexer = new Lexer();
		lexer.tokenize(code);
		assertFalse(lexer.match("ID"));  // Test empty specification
		
		lexer.respec(spec);
		// "a = 10 + 8;\n"
		assertEquals("ID", lexer.next().token_id);
		assertEquals("EQUAL", lexer.next().token_id);
		assertEquals("INT", lexer.next().token_id);
		assertEquals("PLUS", lexer.next().token_id);
		assertEquals("INT", lexer.next().token_id);
		assertEquals("SEMI", lexer.next().token_id);
		// "b = a * 10;\n"
		assertEquals("ID", lexer.next().token_id);
		assertEquals("EQUAL", lexer.next().token_id);
		assertEquals("ID", lexer.next().token_id);
		assertEquals("MULT", lexer.next().token_id);
		assertEquals("INT", lexer.next().token_id);
		assertEquals("SEMI", lexer.next().token_id);
		// "c = b + a;\n"
		assertEquals("ID", lexer.next().token_id);
		assertEquals("EQUAL", lexer.next().token_id);
		assertEquals("ID", lexer.next().token_id);
		assertEquals("PLUS", lexer.next().token_id);
		assertEquals("ID", lexer.next().token_id);
		assertEquals("SEMI", lexer.next().token_id);
		// "d = b \\ a;\n"
		assertEquals("ID", lexer.next().token_id);
		assertEquals("EQUAL", lexer.next().token_id);
		assertEquals("ID", lexer.next().token_id);
		assertEquals("DIV", lexer.next().token_id);
		assertEquals("ID", lexer.next().token_id);
		assertEquals("SEMI", lexer.next().token_id);
		// "e = 1.91283;"
		assertEquals("ID", lexer.next().token_id);
		assertEquals("EQUAL", lexer.next().token_id);
		assertEquals("FLOAT", lexer.next().token_id);
		assertEquals("SEMI", lexer.next().token_id);
		// eoi
		assertEquals("eoi", lexer.next().token_id);
	}

	@Test
	public void testLexerString() {
		lexer = new Lexer(spec);
		lexer.tokenize(code);
		// "a = 10 + 8;\n"
		assertEquals("ID", lexer.next().token_id);
		assertEquals("EQUAL", lexer.next().token_id);
		assertEquals("INT", lexer.next().token_id);
		assertEquals("PLUS", lexer.next().token_id);
		assertEquals("INT", lexer.next().token_id);
		assertEquals("SEMI", lexer.next().token_id);
		// "b = a * 10;\n"
		assertEquals("ID", lexer.next().token_id);
		assertEquals("EQUAL", lexer.next().token_id);
		assertEquals("ID", lexer.next().token_id);
		assertEquals("MULT", lexer.next().token_id);
		assertEquals("INT", lexer.next().token_id);
		assertEquals("SEMI", lexer.next().token_id);
		// "c = b + a;\n"
		assertEquals("ID", lexer.next().token_id);
		assertEquals("EQUAL", lexer.next().token_id);
		assertEquals("ID", lexer.next().token_id);
		assertEquals("PLUS", lexer.next().token_id);
		assertEquals("ID", lexer.next().token_id);
		assertEquals("SEMI", lexer.next().token_id);
		// "d = b \\ a;\n"
		assertEquals("ID", lexer.next().token_id);
		assertEquals("EQUAL", lexer.next().token_id);
		assertEquals("ID", lexer.next().token_id);
		assertEquals("DIV", lexer.next().token_id);
		assertEquals("ID", lexer.next().token_id);
		assertEquals("SEMI", lexer.next().token_id);
		// "e = 1.91283;"
		assertEquals("ID", lexer.next().token_id);
		assertEquals("EQUAL", lexer.next().token_id);
		assertEquals("FLOAT", lexer.next().token_id);
		assertEquals("SEMI", lexer.next().token_id);
		// eoi
		assertEquals("eoi", lexer.next().token_id);
	}

	@Test
	public void testSpec() {
		lexer.respec(spec);
		//System.out.println(lexer.spec());
	}

	@Test
	public void testRespec() {
		// Already tested
	}

	@Test
	public void testAdspec() {
		// TODO
	}

	@Test
	public void testPos() {
		// TODO
	}

	@Test
	public void testPosInt() {
		// TODO
	}

	@Test
	public void testReset() {
		// TODO
	}

	@Test
	public void testSkip() {
		lexer.respec(spec);
		lexer.skip("a \n");
		lexer.tokenize("a = b + c;\n b = a + c;\n");
		
		assertTrue(lexer.match("EQUAL"));
		assertTrue(lexer.match("ID"));
		assertTrue(lexer.match("PLUS"));
		assertTrue(lexer.match("ID"));
		assertTrue(lexer.match("SEMI"));
		
		assertTrue(lexer.match("ID"));
		assertTrue(lexer.match("EQUAL"));
		assertTrue(lexer.match("PLUS"));
		assertTrue(lexer.match("ID"));
		assertTrue(lexer.match("SEMI"));
		
		assertTrue(lexer.match("eoi"));
	}

	@Test
	public void testTokenize() {
		// TODO
	}

	@Test
	public void testCode() {
		// TODO
	}

	@Test
	public void testReplace() {
		lexer.respec(spec);
		lexer.tokenize(code);
		
		Token token = lexer.next("SEMI");
		lexer.replace(token, "REPLACED");
		
		System.out.println(lexer.code());
	}

	@Test
	public void testPeekInt() {
		// TODO
	}

	@Test
	public void testPeek() {
		// TODO
	}

	@Test
	public void testMatch() {
		// TODO
	}

	@Test
	public void testNext() {
		// TODO
	}

	@Test
	public void testNextString() {
		// TODO
	}

	@Test
	public void testNextBoolean() {
		lexer.respec(spec);
		
		lexer.next("SEMI");
		lexer.next("SEMI");
		lexer.next("SEMI");
		lexer.next("SEMI");
		lexer.next("SEMI");
		
		assertTrue(lexer.match("eoi"));
	}

	@Test
	public void testNextStringArrayBoolean() {
		// TODO
	}

}
