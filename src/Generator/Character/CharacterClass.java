package Generator.Character;

public class CharacterClass {
	static char CLS_START = ' ';	// Hex: 20
	static char CLS_END   = '~';	// Hex: 7E
	static int  CLS_RANGE = CLS_END-CLS_START+1; 
	
	boolean[] accepted;
	String name;
	
	public CharacterClass() {
		accepted = new boolean[CLS_RANGE];
	}
	
	public CharacterClass(String c) {
		this();
		add(c);
	}
	
	public static CharacterClass MatchAll() {
		CharacterClass M = new CharacterClass();
		for(int i=0; i<CLS_RANGE; i++) {
			M.accepted[i] = true;
		}
		return M;
	}
	
	public void setName(String id) {
		name = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void add(char c) {
		accepted[c-CLS_START] = true;
	}
	
	public void add(String c) {
		assert (c.length() == 1) : "Non-unit length String: " + c;
		
		add(c.charAt(0));
	}
	
	public boolean isMatched(char c) {
		return accepted[c-CLS_START];
	}
	
	public boolean isMatched(String c) {
		assert (c.length() == 1) : "Non-unit length String: " + c;
		
		return isMatched(c.charAt(0));
	}
	
	public CharacterClass union(CharacterClass C) {
		CharacterClass U = new CharacterClass();
		for(char c = CLS_START; c <= CLS_END; c++) {
			if(isMatched(c) || C.isMatched(c)) {
				U.add(c);
			}
		}
		return U;
	}
	
	public CharacterClass intersect(CharacterClass C) {
		CharacterClass I = new CharacterClass();
		for(char c = CLS_START; c <= CLS_END; c++) {
			if(isMatched(c) && C.isMatched(c)) {
				I.add(c);
			}
		}
		return I;
	}
	
	public CharacterClass subtract(CharacterClass C) {
		CharacterClass I = new CharacterClass();
		for(char c = CLS_START; c <= CLS_END; c++) {
			if(isMatched(c) && !C.isMatched(c)) {
				I.add(c);
			}
		}
		return I;
	}
	
	public String toString() {
		String out = "";
		boolean rangeNotation = false;
		for(int i=0; i<CLS_RANGE; i++) {
			if(accepted[i] && !rangeNotation) {
				out += (char)(i + CLS_START);
				rangeNotation = true;
			}
			if(!accepted[i] && rangeNotation) {
				if(out.charAt(out.length()-1) != (char)(i + CLS_START - 1)) {
					out += "-" + (char)(i + CLS_START - 1);
				}
				rangeNotation = false;
			}
		}
		if(rangeNotation) {
			out += "-" + CLS_END;
		}
		return "[" + out + "]";
	}
}
