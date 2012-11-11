package Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import Generator.Token;
import Generator.Token.op_code;
import Generator.Tokenizer;

public class TokenizerTest {

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testUnion() {
		String in = "|";
		
		Tokenizer T = new Tokenizer(in);
		assertEquals(T.next().operand, op_code.epsilon);
		assertEquals(T.next().operand, op_code.or);
		assertEquals(T.next().operand, op_code.eoi);
		
		in = "a|b";
		T = new Tokenizer(in);
		assertEquals(T.next().operand, op_code.re_char);
		assertEquals(T.next().operand, op_code.or);
		assertEquals(T.next().operand, op_code.re_char);
	}
	
	@Test
	public void testID() {
		String in = "$PAPER";
		Tokenizer T = new Tokenizer(in);
		
		Token token = T.next();
		assertEquals(op_code.id, token.operand);
		assertEquals("$PAPER", token.value);
		
		in = "$IDENTIFIER \t $IDENTIFIERabc";
		T = new Tokenizer(in);
		
		token = T.next();
		assertEquals(op_code.id, token.operand);
		assertEquals("$IDENTIFIER", token.value);
		
		token = T.next();
		assertEquals(op_code.id, token.operand);
		assertEquals("$IDENTIFIER", token.value);
		
		token = T.next();
		assertEquals(op_code.re_char, token.operand);
		assertEquals("a", token.value);
		
		token = T.next();
		assertEquals(op_code.re_char, token.operand);
		assertEquals("b", token.value);
		
		token = T.next();
		assertEquals(op_code.re_char, token.operand);
		assertEquals("c", token.value);
		
		token = T.next();
		assertEquals(op_code.eoi, token.operand);
		assertEquals("", token.value);
	}

	@Test
	public void testTokenizer() {
		fail("Not yet implemented");
	}

	@Test
	public void testPos() {
		fail("Not yet implemented");
	}

	@Test
	public void testPeek() {
		fail("Not yet implemented");
	}

	@Test
	public void testNext() {
		fail("Not yet implemented");
	}

	@Test
	public void testReset() {
		fail("Not yet implemented");
	}

}
