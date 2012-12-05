package Test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Generator.Character.Token;
import Generator.Character.Tokenizer;
import Generator.Character.Token.op_code;

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
		assertEquals(T.next().operand, op_code.epsilon);
		assertEquals(T.next().operand, op_code.eoi);
		
		in = "a|b";
		T = new Tokenizer(in);
		assertEquals(T.next().operand, op_code.re_char);
		assertEquals(T.next().operand, op_code.or);
		assertEquals(T.next().operand, op_code.re_char);
		
		in = "a||b";
		T.tokenize(in);
		T.next();
		T.next();
		Token token = T.next();
		assertEquals(op_code.epsilon, token.operand);
		token = T.next();
		assertEquals(op_code.or, token.operand);
		token = T.next();
		assertEquals(op_code.re_char, token.operand);
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
	public void testID_2() {
		String in = "$DIGIT   [0-9] \n" + 
					"$NON-ZERO    [^0]  IN  $DIGIT";
		Tokenizer T = new Tokenizer(in);
		
		Token token = T.next();
		assertEquals(op_code.id, token.operand);
		assertEquals("$DIGIT", token.value);
		
		token = T.next();
		assertEquals(op_code.left_brac, token.operand);
		assertEquals("[", token.value);
		
		token = T.next();
		assertEquals(op_code.cls_char, token.operand);
		assertEquals("0", token.value);
		
		token = T.next();
		assertEquals(op_code.range, token.operand);
		assertEquals("-", token.value);

		token = T.next();
		assertEquals(op_code.cls_char, token.operand);
		assertEquals("9", token.value);
		
		token = T.next();
		assertEquals(op_code.right_brac, token.operand);
		assertEquals("]", token.value);
		
		token = T.next();
		assertEquals(op_code.id, token.operand);
		assertEquals("$NON-ZERO", token.value);
		
		token = T.next();
		assertEquals(op_code.left_brac, token.operand);
		assertEquals("[", token.value);
		
		token = T.next();
		assertEquals(op_code.exclude, token.operand);
		assertEquals("^", token.value);
		
		token = T.next();
		assertEquals(op_code.cls_char, token.operand);
		assertEquals("0", token.value);
		
		token = T.next();
		assertEquals(op_code.right_brac, token.operand);
		assertEquals("]", token.value);
		
		token = T.next();
		assertEquals(op_code.in, token.operand);
		assertEquals("IN", token.value);
		
		token = T.next();
		assertEquals(op_code.id, token.operand);
		assertEquals("$DIGIT", token.value);
		
		token = T.next();
		assertEquals(op_code.eoi, token.operand);
		assertEquals("", token.value);
		
		List<String> ids = T.ids();
		assertEquals("$DIGIT", ids.get(0));
		assertEquals("$NON-ZERO", ids.get(1));
	}
	
	@Test
	public void testEscape() {
		String re_esc = "\\*\\ \\\\\\+\\?\\|\\[\\]\\(\\)\\.\\'\\\"\\$";
		String cls_esc = "[\\\\\\^\\-\\[\\]]";
		String re_no_esc = "^-";
		String cls_no_esc = "[ *+?|().'\"$IN]";
		
		System.out.println(re_esc);
		System.out.println(cls_esc);
		System.out.println(re_no_esc);
		System.out.println(cls_no_esc);
		
		Tokenizer T = new Tokenizer(re_esc);
		assertEquals(op_code.re_char, T.peek().operand);
		assertEquals("*", T.next().value);
		assertEquals(op_code.re_char, T.peek().operand);
		assertEquals(" ", T.next().value);
		assertEquals(op_code.re_char, T.peek().operand);
		assertEquals("\\", T.next().value);
		assertEquals(op_code.re_char, T.peek().operand);
		assertEquals("+", T.next().value);
		assertEquals(op_code.re_char, T.peek().operand);
		assertEquals("?", T.next().value);
		assertEquals(op_code.re_char, T.peek().operand);
		assertEquals("|", T.next().value);
		assertEquals(op_code.re_char, T.peek().operand);
		assertEquals("[", T.next().value);
		assertEquals(op_code.re_char, T.peek().operand);
		assertEquals("]", T.next().value);
		assertEquals(op_code.re_char, T.peek().operand);
		assertEquals("(", T.next().value);
		assertEquals(op_code.re_char, T.peek().operand);
		assertEquals(")", T.next().value);
		assertEquals(op_code.re_char, T.peek().operand);
		assertEquals(".", T.next().value);
		assertEquals(op_code.re_char, T.peek().operand);
		assertEquals("'", T.next().value);
		assertEquals(op_code.re_char, T.peek().operand);
		assertEquals("\"", T.next().value);
		assertEquals(op_code.re_char, T.peek().operand);
		assertEquals("$", T.next().value);
		
		T.tokenize(cls_esc);
		T.next();
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals("\\", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals("^", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals("-", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals("[", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals("]", T.next().value);
		assertEquals(op_code.right_brac, T.peek().operand);
		assertEquals("]", T.next().value);
		
		T.tokenize(re_no_esc);
		assertEquals(op_code.re_char, T.peek().operand);
		assertEquals("^", T.next().value);
		assertEquals(op_code.re_char, T.peek().operand);
		assertEquals("-", T.next().value);
		
		T.tokenize(cls_no_esc);
		T.next();
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals(" ", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals("*", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals("+", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals("?", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals("|", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals("(", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals(")", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals(".", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals("'", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals("\"", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals("$", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals("I", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals("N", T.next().value);
		assertEquals(op_code.right_brac, T.peek().operand);
		assertEquals("]", T.next().value);
	}
	
	@Test
	public void testEpsilon() {
		String in = "()";
		Tokenizer T = new Tokenizer(in);
		
		assertEquals(op_code.left_paren, T.peek().operand);
		assertEquals("(", T.next().value);
		assertEquals(op_code.epsilon, T.peek().operand);
		assertEquals("", T.next().value);
		assertEquals(op_code.right_paren, T.peek().operand);
		assertEquals(")", T.next().value);
		
		T.tokenize("[]");
		assertEquals(op_code.left_brac, T.peek().operand);
		assertEquals("[", T.next().value);
		assertEquals(op_code.epsilon, T.peek().operand);
		assertEquals("", T.next().value);
		assertEquals(op_code.right_brac, T.peek().operand);
		assertEquals("]", T.next().value);
		
		T.tokenize("[^]");
		assertEquals(op_code.left_brac, T.peek().operand);
		assertEquals("[", T.next().value);
		assertEquals(op_code.exclude, T.peek().operand);
		assertEquals("^", T.next().value);
		assertEquals(op_code.right_brac, T.peek().operand);
		assertEquals("]", T.next().value);
		
		// [ *+?|().'"]
		T.tokenize("[ ]");
		assertEquals(op_code.left_brac, T.peek().operand);
		assertEquals("[", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals(" ", T.next().value);
		assertEquals(op_code.right_brac, T.peek().operand);
		assertEquals("]", T.next().value);
		
		T.tokenize("[*]");
		assertEquals(op_code.left_brac, T.peek().operand);
		assertEquals("[", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals("*", T.next().value);
		assertEquals(op_code.right_brac, T.peek().operand);
		assertEquals("]", T.next().value);
		
		T.tokenize("[+]");
		assertEquals(op_code.left_brac, T.peek().operand);
		assertEquals("[", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals("+", T.next().value);
		assertEquals(op_code.right_brac, T.peek().operand);
		assertEquals("]", T.next().value);
		
		T.tokenize("[?]");
		assertEquals(op_code.left_brac, T.peek().operand);
		assertEquals("[", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals("?", T.next().value);
		assertEquals(op_code.right_brac, T.peek().operand);
		assertEquals("]", T.next().value);
		
		T.tokenize("[|]");
		assertEquals(op_code.left_brac, T.peek().operand);
		assertEquals("[", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals("|", T.next().value);
		assertEquals(op_code.right_brac, T.peek().operand);
		assertEquals("]", T.next().value);
		
		T.tokenize("[(]");
		assertEquals(op_code.left_brac, T.peek().operand);
		assertEquals("[", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals("(", T.next().value);
		assertEquals(op_code.right_brac, T.peek().operand);
		assertEquals("]", T.next().value);
		
		T.tokenize("[)]");
		assertEquals(op_code.left_brac, T.peek().operand);
		assertEquals("[", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals(")", T.next().value);
		assertEquals(op_code.right_brac, T.peek().operand);
		assertEquals("]", T.next().value);
		
		T.tokenize("[.]");
		assertEquals(op_code.left_brac, T.peek().operand);
		assertEquals("[", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals(".", T.next().value);
		assertEquals(op_code.right_brac, T.peek().operand);
		assertEquals("]", T.next().value);
		
		T.tokenize("[']");
		assertEquals(op_code.left_brac, T.peek().operand);
		assertEquals("[", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals("'", T.next().value);
		assertEquals(op_code.right_brac, T.peek().operand);
		assertEquals("]", T.next().value);
		
		T.tokenize("[\"]");
		assertEquals(op_code.left_brac, T.peek().operand);
		assertEquals("[", T.next().value);
		assertEquals(op_code.cls_char, T.peek().operand);
		assertEquals("\"", T.next().value);
		assertEquals(op_code.right_brac, T.peek().operand);
		assertEquals("]", T.next().value);
	}
	
	@Test
	public void testRegex() {
		String cls = "$ZOOK [1-22-90-4&$#@1a-zZ-Z]\n" +
					 "$GURG [^0123zwoqeidj] IN $ZOOK\n";
		String regex = "$BABEL   (a|A)*.($ZOOK)+[^554] IN [[11-9\\]20](])(\\)\\$)$GURG|(|(|))|";
		
		Tokenizer T = new Tokenizer(cls);
		while(T.hasNext()) {
			System.out.println(T.next());
		}
		T.tokenize(regex);
		while(T.hasNext()) {
			System.out.println(T.next());
		}
	}

	@Test
	public void testTokenizer() {
		String regex = "$PRINT PRINT";
		Tokenizer T = new Tokenizer(regex);
		T.debug = true;
		while(T.hasNext()) {
			T.match(T.peek());
		}
	}
}
