package Generator.Lexer;

public class Token {
	public final String token_id;
	public final String value;
	public final int start_pos;
	public final int end_pos;
	
	public Token(String id, String val, int start) {
		this(id, val, start, start + val.length());
	}
	
	public Token(String id, String val, int start, int end) {
		token_id = id;
		value = val;
		start_pos = start;
		end_pos = end;
	}
	
	public boolean equals(Token other) {
		return token_id == other.token_id && value == other.value;
	}
	
	public String toString() {
		String val = token_id.equals("epsilon") ? "ε" : value;
		return token_id + ": " + val + 
			   " (start: " + start_pos + ", end: " + end_pos + ")";
	}
}
