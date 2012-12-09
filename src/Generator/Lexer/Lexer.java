package Generator.Lexer;

import java.io.File;
import java.io.FileNotFoundException;
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
	
	public static boolean verbose = true;  // Prints out matches
	
	public Lexer() {
		this("");
	}
	
	/**
	 * Initialize a Lexer with the input token definition.
	 * 
	 * See respec()
	 * 
	 * @param lexicalSpec
	 */
	public Lexer(String lexicalSpec) {
		spec = lexicalSpec;
		tokenDFA = new TreeMap<String, DFA>();
		skip = " \n\t\r";
		code = "";
		init();
	}
	
	public String spec() {
		return spec;
	}
	
	/**
	 * Load the Lexer with a new list of token definitions.
	 * 
	 * All token definitions must follow the format of Phase I e.g.,
	 * 
	 * $TOKEN1       (abc)|(a)*
	 * $CATFISH		 (FISHCAT)+
	 * 
	 * where in this example the token-ids are TOKEN1 and CATFISH, and
	 * these will be mapped to their corresponding DFAs.
	 * 
	 * Hence, one would use next("CATFISH") to retrieve the next instance
	 * of a CATFISH token in the input code.
	 * 
	 * @param spec
	 */
	public void respec(String spec) {
		this.spec = spec;
		init();
	}
	
	/**
	 * Add a token definition to the Lexer.
	 * 
	 * @param spec
	 */
	public void adspec(String spec) {
		respec(this.spec + "\n" + spec);
	}
	
	/**
	 * Load Lexer with tokens defined in file.
	 * 
	 * @param fname
	 */
	public void fspec(String fname) {
		Scanner in;
		try {
			in = new Scanner(new File(fname));
			spec = in.useDelimiter("\\Z").next();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		init();
	}
	
	/**
	 * Helper function for converting the token definition to usable DFAs.
	 */
	private void init() {
		if(spec == null || spec.isEmpty()) {
			tokenDFA.clear();
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
	
	/**
	 * Sets the skip characters to those in the input String.
	 * 
	 * @param s
	 */
	public void skip(String s) {
		skip = s;
	}
	
	/**
	 * Tells the Lexer to tokenize on the input string.
	 * 
	 * @param input
	 */
	public void tokenize(String input) {
		reset();
		code = input;
	}
	
	public String code() {
		return code;
	}
	
	/**
	 * Replaces the token with ascii-string with keeping correctness of
	 * Lexer position.
	 * 
	 * No error checking in this function!
	 * 
	 * @param token
	 * @param ascii_string
	 */
	public void replace(Token token, String ascii_string) {
		code = code.substring(0,token.start_pos) + ascii_string
				+ code.substring(token.end_pos);
		pos += ascii_string.length() - token.value.length();
	}
	
	/**
	 * Returns the nth token without advancing the Lexer position.
	 * 
	 * @param n
	 * @return
	 */
	public Token peek(int n) {
		int saved_pos = pos;
		for(int i = 1; i < n; i++) {
			next();
		}
		Token token = next();
		pos = saved_pos;
		
		return token;
	}
	
	/**
	 * Returns the next token without advancing the Lexer position.
	 * 
	 * @return peeked token
	 */
	public Token peek() {
		return peek(1);
	}
	
	/**
	 * Attempts to consume a token of type tokenID.
	 * 
	 * Reports an error if the next token is not of type tokenID.
	 * 
	 * @param tokenID
	 * @return true if successful match
	 */
	public boolean match(String tokenID) {
		if(tokenID.equals(peek().token_id)) {
			Token m = next();
			if(verbose)
				System.out.println("Matched token: " + m);
			return true;
		} else {
			Scanner s = new Scanner(code);
			String fail_line = "";
			int line_num = 1;
			int col = 0;
			int i = 0;
			// Determine line and column number of failed match
			while(s.hasNext()) {
				String line = s.nextLine();
				int line_length = line.length() + 1;  // Count \n character
				if(i + line_length > pos) {
					fail_line = line;
					col = 1 + (pos - i) % line_length;
					break;
				}
				line_num ++;
				i += line_length;
			}
			s.close();
			// Draw ^ pointing at location of pos
			String arrow = "^";
			for(i=1;i<col;i++) {
				arrow = " " + arrow;
			}
			// Output error text
			System.err.println(
			"Failed to match token: \n" +
			"\tLocation: " + "line " + line_num + " col " + col + "\n" +
			"\t          " + fail_line + "\n" + 
			"\t          " + arrow + "\n" +
			"\tExpected: " + tokenID + "\n" +
			"\tActual:   " + peek() + "\n"
			);
		}
		return false;
	}
	
	/**
	 * Finds and returns the next token starting at the Lexer's position.
	 * 
	 * On success, Lexer position is incremented to the character immediately
	 * after the matched token.
	 * 
	 * @return matched token, epsilon or eoi
	 */
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
	
	/**
	 * Finds and returns either the next or nearest token.
	 * 
	 * @param skipNonMatchable
	 * @return matched token, epsilon or eoi
	 */
	public Token next(boolean skipNonMatchable) {
		return next(tokenDFA.keySet().toArray(new String[] {}), skipNonMatchable);
	}
	
	/**
	 * Finds and returns next valid token from a list of Token ID's.
	 * 
	 * On success, Lexer position is incremented to the character immediately
	 * after the matched token.
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
	 * If the Lexer position is at the end of input code, an 'eoi' token
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
			// Find the next instance of tokenID
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
		if(token.token_id.equals("epsilon")) {
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
	
	public String toString() {
		String out = "Lexer:";
		for(Entry<String, DFA> e : tokenDFA.entrySet()) {
			out += "\n" + e.getValue();
		}
		return out;
	}
}