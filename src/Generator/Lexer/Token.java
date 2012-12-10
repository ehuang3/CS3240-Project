package Generator.Lexer;

public class Token {
	public final String token_id;
	public final String value;
	public final int start_pos;
	public final int end_pos;
	public final int line_num;
	
	public Token(String id, String val, int line, int start) {
		this(id, val, line, start, start + val.length());
	}
	
	public Token(String id, String val, int line, int start, int end) {
		token_id = id;
		value = val;
		start_pos = start;
		end_pos = end;
		line_num = line;
	}
	
	public boolean equals(Token other) {
		return token_id == other.token_id && value == other.value;
	}
	
	public String toString() {
		String val = token_id.equals("epsilon") ? "ε" : value;
		return token_id + ": " + val + 
			   " (line: " + line_num + ", start: " + start_pos + ", end: " + end_pos + ")";
	}
}
