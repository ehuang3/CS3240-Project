package Generator.Lexer;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

import Generator.DFA.DFA;
import Generator.DFA.DFAFactory;
import Generator.NFA.NFA;
import Generator.NFA.NFAFactory;

public class Lexer {
	private Map<String, DFA> tokenDFA;  // Map of token-id's to DFA's
	private String spec;  // Token specification
	private String code;  // Input code to tokenize
	private String skip;  // Characters to skip, defaults to {space, tab, newline, carriage-return}
	private int pos;	  // Position of lexer in code
	
	public static boolean verbose = true;
	
	public Lexer() {
		this("");
	}
	
	public Lexer(String lexicalSpec) {
		spec = lexicalSpec;
		tokenDFA = new TreeMap<String, DFA>();
		skip = " \n\t\r";
		init();
	}
	
	public String spec() {
		return spec;
	}
	
	public void respec(String spec) {
		this.spec = spec;
		init();
	}
	
	public void adspec(String spec) {
		respec(this.spec + "\n" + spec);
	}
	
	private void init() {
		if(spec == null || spec.isEmpty()) {
			return;
		}
		// Build token NFAs
		NFAFactory nfaFactory = new NFAFactory();
		Scanner in = new Scanner(spec);
		while(in.hasNext()) {
			String line = in.nextLine().trim();
			if(!line.isEmpty()) {
				nfaFactory.build(line);
			}
		}
		in.close();
		// Ensure no duplicates from calling init() twice
		tokenDFA.clear();
		// Build token DFAs
		DFAFactory dfaFactory = new DFAFactory();
		for(NFA nfa : nfaFactory.cache().values()) {
			DFA dfa = dfaFactory.minimize(dfaFactory.build(nfa));
			tokenDFA.put(dfa.id(), dfa);
		}
	}
	
	public int pos() {
		return pos;
	}
	
	public void pos(int n) {
		pos = n;
	}
	
	public void reset() {
		pos = 0;
	}
	
	public void skip(String s) {
		skip = s;
	}
	
	public void tokenize(String input) {
		code = input;
	}
	
	public Token peek(int n) {
		int saved_pos = pos;
		for(int i = 1; i < n; i++) {
			next();
		}
		Token token = next();
		pos = saved_pos;
		
		return token;
	}
	
	public Token peek() {
		return peek(1);
	}
	
	public boolean match(String tokenID) {
		if(tokenID == peek().token_id) {
			Token m = next();
			if(verbose)
				System.out.println("Matched token: " + m);
			return true;
		} else {
			System.err.println("Failed to match token: ");
		}
		return false;
	}
	
	public Token next() {
		return next(false);
	}
	
	/**
	 * Finds and returns the nearest token of type token_id in 'code'.
	 * 
	 * @param token_id
	 * @return matched token or eoi
	 */
	public Token next(String token_id) {
		return next(new String[] {token_id}, true);
	}
	
	public Token next(boolean skipNonMatchable) {
		return next((String[]) tokenDFA.keySet().toArray(), skipNonMatchable);
	}
	
	/**
	 * Finds and returns next valid token from a list of Token ID's.
	 * 
	 * The function limits the types of tokens to return to those in the input
	 * String array of Token ID's.
	 * 
	 * If skipNonMatchable is true, the function will return either 
	 * 1) the longest and nearest valid token at or after the lexer's position 
	 * OR
	 * 2) an 'eoi' token.
	 * 
	 * If false, the function will return either
	 * 1) the longest valid token starting at the lexer's position
	 * OR
	 * 2) an 'epsilon' token.
	 * 
	 * If the lexer position is at the end of input code, an 'eoi' token
	 * always returned.
	 * 
	 * @param ids
	 * @param skipNonMatchable 
	 * @return Token
	 */
	public Token next(String[] ids, boolean skipNonMatchable) {
		if(pos >= code.length()) {
			return new Token("eoi", "", code.length());
		}
		
		Token token;
		// Initialize token to correct failure value
		if(skipNonMatchable) {
			// On no successful match, we expect to be at 'eoi' token
			token = new Token("eoi", "", code.length());
		} else {
			// On no successful match, we expect to return 'epsilon' token
			token = new Token("epsilon", "", code.length());  // Correct epsilon position later
		}
		
		skip();  // Pre-skip white space
		for(String tokenID : ids) {
			DFA dfa = tokenDFA.get(tokenID);
			
			int i = pos;
			do {
				String value = dfa.walk(code.substring(i));
				if(token.value.length() < value.length() && 
						i <= token.start_pos) {
					token = new Token(dfa.id(), value, i);
					break;  // Exit on first successful match
				}
				i++;
			// Loop only if we skip over invalid matches
			} while(i < code.length() && skipNonMatchable);
		}
		// Correct position of epsilon token
		if(token.token_id == "epsilon") {
			token = new Token("epsilon", "", pos);
		}
		pos = token.start_pos + token.value.length();
		skip();  // Post-skip white space
		
		return token;
	}
	
	/**
	 * Helper function that consumes white space in the 'code'.
	 */
	private void skip() {
		while(pos < code.length() &&
				skip.contains(code.substring(pos, pos+1))) {
			pos++;
		}
	}
}