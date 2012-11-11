package Generator;

import Generator.Token.op_code;

/** 
 * Tokenizes Regex and CharacterClass specifications.
 * 
 * @author eric
 *
 */
public class Tokenizer {
	String code;
	int pos;
	boolean potentialEpsilon; 	// Alerts the tokenizer of incoming epsilon match
	boolean regexMode;
	
	static String[] keywords = {
								 "(", ")", "|", "*", "+", "$", "\\",	// Regex Keywords
								 ".", "[", "]", "^", "-"				// CharacterClass Keywords
							   };
	
	static String[] skipper = { " ", "\t", "\n", "\r" };
	
	public Tokenizer(String in) {
		this(in, true);
	}
	
	public Tokenizer(String in, boolean rMode) {
		code = in;
		pos = 0;
		potentialEpsilon = true;
		regexMode = rMode;
	}
	
	public int pos() {
		return pos;
	}
	
	public Token peek() {
		
		return null;
	}
	
	public Token next() {
		if(pos == code.length()) {
			return null;
		}
		// Consume whitespace
		skip();
		// Next keyword
		String op = code.substring(pos,pos+1);
		// Parse token
		Token token;
		switch (op) {
			case "(" :
				potentialEpsilon = true;
				token = new Token(op_code.left_paren, "");
				pos++;
				break;
			case ")" :
				if(potentialEpsilon) {
					potentialEpsilon = false;
					token = new Token(op_code.epsilon, "");
				} else {
					token = new Token(op_code.right_paren, "");
					pos++;
				}
				break;
			case "|" :
				if(potentialEpsilon) {
					potentialEpsilon = false;
					token = new Token(op_code.epsilon, "");
				} else {
					potentialEpsilon = true;
					token = new Token(op_code.or, "");
				}
				break;
			case "*" :
				token = new Token(op_code.star, "");
				pos++;
				break;
			case "+" :
				token = new Token(op_code.plus, "");
				pos++;
				break;
			case "$" :
				String id = findId();
				token = new Token(op_code.id, id);
				pos += id.length();
				break;
			case "\\" :
				token = new Token(op_code.re_char, code.substring(pos+1,pos+2));
				pos += 2;
				break;
			default :
				token = new Token(op_code.re_char, op);
				pos++;
				
		}		
		// Consume trailing whitespace
		skip();
		return token;
	}
	
	private void skip() {
		if(pos == code.length()) {
			return;
		} else if(isSpace(code.substring(pos,pos+1))) {
			pos += 1;
			skip();
		}
	}
	
	private boolean isSpace(String in) {
		for(String s : skipper) {
			if(in.equals(s)) {
				return true;
			}
		}
		return false;
	}
	
	private String findId() {
		return null;
	}
	
	private String nextKeyword() {
		return null;
	}
	
	public void reset() {
		pos = 0;
	}
}
