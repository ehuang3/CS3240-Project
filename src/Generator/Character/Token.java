package Generator.Character;

public class Token {
	public enum op_code {
		left_paren,
		right_paren,
		or,
		star,
		plus,
		id,
		re_char,
		epsilon,
		left_brac,
		right_brac,
		match_all,		// "."
		range,			// "-"
		exclude,		// "^"
		cls_char,		// 
		in,				// "IN"
		eoi
	}
	
	public op_code operand;
	public String value;
	
	public Token(op_code o, String v) {
		operand = o;
		value = v;
	}
	
	public String toString() {
		return operand + ": " + value;
	}
	
	public boolean equals(Token other) {
		return operand == other.operand;
	}
}
