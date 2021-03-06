package Generator.Character;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import Generator.Character.Token.op_code;


/** 
 * Tokenizes Regex and CharacterClass specifications.
 * 
 * @author eric
 *
 */
public class Tokenizer {
	String code;
	int pos;
	boolean potentialEpsilon; 	// Alerts of incoming epsilon match
	boolean regexMode;			// Switches between regex tokens and cls tokens
	Set<String> ids;
	
	public boolean debug;
	
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
		ids = new HashSet<String>();
		debug = false;
	}
	
	public int pos() {
		return pos;
	}
	
	public Set<String> ids() {
		return ids;
	}
	
	public void tokenize(String in) {
		code = in.trim();
		reset();
	}
	
	public void reset() {
		pos = 0;
		potentialEpsilon = true;
		regexMode = true;
	}
	
	public void resetPast() {
		potentialEpsilon = true;
		regexMode = true;
	}
	
	public Token peek(int n) {
		// Save state
		int _pos = pos;
		boolean _potentialEpsilon = potentialEpsilon;
		boolean _regexMode = regexMode;
		
		// Peek ahead
		Token nextToken = null;
		while(n-- > 0) {
			nextToken = next();
		}
		
		// Restore state
		pos = _pos;
		potentialEpsilon = _potentialEpsilon;
		regexMode = _regexMode;
		
		return nextToken;
	}
	
	public Token peek() {
		return peek(1);
	}
	
	public boolean match(op_code op) {
		return match(new Token(op,""));
	}
	
	public boolean match(Token token) {
		if(token.equals(peek())) {
			Token m = next();
			if(debug)
				System.out.println("Matched token: " + m);
			return true;
		} else {
			if(debug)
				System.err.println("Failed to match token: Position " + pos + " in " + code + "\n" +
									"\tExpected: " + token  + "\n" +
									"\t  Actual: " + peek() + "\n" );
			return false;
		}
	}
	
	public boolean hasNext() {
		return peek().operand != op_code.eoi;
	}
	
	public Token next() {
		if(pos == code.length()) {
			if(potentialEpsilon) {
				potentialEpsilon = false;
				return new Token(op_code.epsilon, "");
			} else {
				return new Token(op_code.eoi, "");
			}
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
				token = new Token(op_code.left_paren, op);
				pos++;
			} else {
				potentialEpsilon = false;
				token = new Token(op_code.cls_char, op);
				pos++;
			}
			break;
		case ")" :
			if(regexMode) {
				if(potentialEpsilon) {
					potentialEpsilon = false;
					token = new Token(op_code.epsilon, "");
				} else {
					token = new Token(op_code.right_paren, op);
					pos++;
				}
			} else {
				potentialEpsilon = false;
				token = new Token(op_code.cls_char, op);
				pos++;
			}
			break;
		case "|" :
			if(regexMode) {
				if(potentialEpsilon) {
					potentialEpsilon = false;
					token = new Token(op_code.epsilon, "");
				} else {
					potentialEpsilon = true;
					token = new Token(op_code.or, op);
					pos++;
				}
			} else {
				potentialEpsilon = false;
				token = new Token(op_code.cls_char, op);
				pos++;
			}
			break;
		case "*" :
			if(regexMode) {
				token = new Token(op_code.star, op);
				pos++;
			} else {
				potentialEpsilon = false;
				token = new Token(op_code.cls_char, op);
				pos++;
			}
			break;
		case "+" :
			if(regexMode) {
				token = new Token(op_code.plus, op);
				pos++;
			} else {
				potentialEpsilon = false;
				token = new Token(op_code.cls_char, op);
				pos++;
			}
			break;
		case "$" :
			if(regexMode) {
				potentialEpsilon = false;
				String id = findId();
				token = new Token(op_code.id, id);
				pos += id.length();
			} else {
				potentialEpsilon = false;
				token = new Token(op_code.cls_char, op);
				pos++;
			}
			break;
		case "[" :
			if(regexMode) {
				regexMode = false;
				potentialEpsilon = true;
				token = new Token(op_code.left_brac, op);
				pos++;
			} else {
				potentialEpsilon = false;
				token = new Token(op_code.cls_char, op);
				pos++;
			}
			break;
		case "]" :
			if(!regexMode) {
				if(potentialEpsilon) {
					potentialEpsilon = false;
					token = new Token(op_code.epsilon, "");
				} else {
					regexMode = true;
					token = new Token(op_code.right_brac, op);
					pos++;
				}
			} else {
				potentialEpsilon = false;
				token = new Token(op_code.re_char, op);
				pos++;
			}
			break;
		case "." :
			if(regexMode) {
				potentialEpsilon = false;
				token = new Token(op_code.match_all, op);
				pos++;
			} else {
				potentialEpsilon = false;
				token = new Token(op_code.cls_char, op);
				pos++;
			}
			break;
		case "-" :
			if(!regexMode) {
				token = new Token(op_code.range, op);
				pos++;
			} else {
				potentialEpsilon = false;
				token = new Token(op_code.re_char, op);
				pos++;
			}
			break;
		case "^" :
			if(!regexMode) {
				potentialEpsilon = false;
				token = new Token(op_code.exclude, op);
				pos++;
			} else {
				potentialEpsilon = false;
				token = new Token(op_code.re_char, op);
				pos++;
			}
			break;
		case "IN" :
			if(regexMode) {
				token = new Token(op_code.in, op);
				pos += 2;
			} else {
				potentialEpsilon = false;
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
			potentialEpsilon = false;
			pos += 2;
			break;
		default :
			if(regexMode) {
				token = new Token(op_code.re_char, op);
			} else {
				token = new Token(op_code.cls_char, op);
			}
			potentialEpsilon = false;
			pos++;
		}
		// Consume trailing whitespace
		skip();
		return token;
	}
	
	private void skip() {
		if(pos == code.length() || !regexMode) {
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
		// Case 1: ID is at beginning of code
		if(pos == 0) {
			// Match ID until first whitespace
			Scanner S = new Scanner(code);
			String ID = S.next();
			ids.add(ID);  // Add new ID to set
			S.close();
			return ID;
		}
		// Case 2: ID exists and is in regex definition
		// Find longest existing match
		String ID = "";
		for(String i : ids) {
			int match = code.indexOf(i, pos);
			if(match == pos && ID.length() < i.length()) {
				ID = i;
			}
		}
		// Case 3: ID does not exist and is in regex definition
		if(ID.isEmpty()) {
			// Match ID until first whitespace
			Scanner S = new Scanner(code.substring(pos));
			ID = S.next();
			ids.add(ID);  // Add new ID to set
			S.close();
		}
		
		//FIXME: Handle ids with overlapping names.
		// 		 $PAPER and $PAPERWATER
		//FIXME: Ambiguous match
		// 		 $ID, $ID1, and $ID2
		// 		 regex = $ID1234$ID234
		
		return ID;
	}
	
	private String nextKeyword() {
		String keyword = code.substring(pos,pos+1);
		// For matching keywords with length > 1
		for(String k : keywords) {
			int dist = code.substring(pos).indexOf(k);
			if(dist == 0) {
				keyword = k;
			}
		}
		return keyword;
	}
}
