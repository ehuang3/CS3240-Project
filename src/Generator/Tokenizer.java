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
								 ".", "[", "]", "^", "-", "IN"			// CharacterClass Keywords
							   };
	
	static String[] skipper = { " ", "\t", "\n", "\r" };
	
	public Tokenizer(String in) {
		code = in;
		pos = 0;
		potentialEpsilon = true;
		regexMode = true;
	}
	
	public int pos() {
		return pos;
	}
	
	public Token peek() {
		// Save state
		int _pos = pos;
		boolean _potentialEpsilon = potentialEpsilon;
		boolean _regexMode = regexMode;
		// Peek ahead
		Token nextToken = next();
		// Restore state
		pos = _pos;
		potentialEpsilon = _potentialEpsilon;
		regexMode = _regexMode;
		
		return nextToken;
	}
	
	public Token next() {
		if(pos == code.length()) {
			return null;
		}
		// Consume whitespace
		skip();
		// Next keyword
		String op = nextKeyword();
		// Parse token
		Token token;
		switch (op) {
			case "(" :
				if(regexMode) {
					potentialEpsilon = true;
					token = new Token(op_code.left_paren, "");
					pos++;
					break;
				}
			case ")" :
				if(regexMode) {
					if(potentialEpsilon) {
						potentialEpsilon = false;
						token = new Token(op_code.epsilon, "");
					} else {
						token = new Token(op_code.right_paren, "");
						pos++;
					}
					break;
				}
			case "|" :
				if(regexMode) {
					if(potentialEpsilon) {
						potentialEpsilon = false;
						token = new Token(op_code.epsilon, "");
					} else {
						potentialEpsilon = true;
						token = new Token(op_code.or, "");
					}
					break;
				}
			case "*" :
				if(regexMode) {
					token = new Token(op_code.star, "");
					pos++;
					break;
				}
			case "+" :
				if(regexMode) {
					token = new Token(op_code.plus, "");
					pos++;
					break;
				}
			case "$" :
				if(regexMode) {
					String id = findId();
					token = new Token(op_code.id, id);
					pos += id.length();
					break;
				}
			case "[" :
				if(regexMode) {
					regexMode = false;
					token = new Token(op_code.left_brac, "");
					pos++;
					break;
				}
			case "]" :
				if(!regexMode) {
					regexMode = true;
					token = new Token(op_code.right_brac, "");
					pos++;
					break;
				}
			case "." :
				if(regexMode) {
					token = new Token(op_code.match_all, "");
					pos++;
					break;
				}
			case "-" :
				token = new Token(op_code.range, "");
				pos++;
				break;
			case "^" :
				token = new Token(op_code.exclude, "");
				pos++;
				break;
			case "IN" :
				if(regexMode) {
					token = new Token(op_code.in, "");
					pos += 2;
				} else {
					token = new Token(op_code.cls_char, "I");
					pos++;
				}
				break;
			case "\\" :
				if(regexMode) {
					token = new Token(op_code.re_char, code.substring(pos+1,pos+2));
				} else {
					token = new Token(op_code.cls_char, code.substring(pos+1,pos+2));
				}
				pos += 2;
				break;
			default :
				if(regexMode) {
					token = new Token(op_code.re_char, op);
				} else {
					token = new Token(op_code.cls_char, op);
				}
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
		String keyword = "";
		int min_dist = code.length();
		for(String k : keywords) {
			int dist = code.substring(pos).indexOf(k);
			if(dist >= 0 && dist < min_dist) {
				min_dist = dist;
				keyword = k;
			}
		}
		return keyword;
	}
	
	public void reset() {
		pos = 0;
	}
}
