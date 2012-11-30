package TableWalker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Generator.DFA.DFA;
import Generator.DFA.DFANode;
import Generator.DFA.DFATransition;


public class TableWalker {
	List<DFA> dfas;

	public TableWalker(List<DFA> dlist) {
		dfas = dlist;
	}
	
	public List<TokenResult> walk(String input) {
		int pos = 0;
		List<TokenResult> output_tokens = new ArrayList<>();
		while(pos < input.length()) {
			LinkedList<TokenResult> results = new LinkedList<>();
			for(DFA dfa : dfas) {
				results.add(walk(dfa, pos, input));
			}
			TokenResult longestMatched = new TokenResult();
			for(TokenResult r : results) {
				if(r.token.length() > longestMatched.token.length()) {
					longestMatched = r;
				}
			}
			output_tokens.add(longestMatched);
			pos += longestMatched.token.length();
			pos += skip(pos, input);
		}
		return output_tokens;
	}
	
	private int skip(int pos, String input) {
		int i=0;
		while(pos+i < input.length() && input.substring(pos+i,pos+i+1).trim().isEmpty()) {
			i ++;
		}
		return i;
	}
	
	private TokenResult walk(DFA dfa, int pos, String input) {
		DFANode now = dfa.start();
		boolean transitionMatched = true;
		int i=0;
		String lastTerminalMatch = "";
		String currentMatch = "";
		while( transitionMatched && (pos+i < input.length()) ) {
			transitionMatched = false;
			char c = input.charAt(pos+i);
			for(DFATransition t : now.adjacencyList()) {
				if(t.isTriggered(c)) {
					now = t.end();
					currentMatch += c;
					if(t.end().isTerminal()) {
						lastTerminalMatch = currentMatch;
					}
					i++;
					transitionMatched = true;
					break;
				}
			}
		}
		TokenResult r = new TokenResult();
		r.id = dfa.id();
		r.token = lastTerminalMatch;
		return r;
	}
	
	public class TokenResult {
		public String token = "";
		public String id = "";
		
		public TokenResult() {
			token = "";
			id = "";
		}
		
		public boolean isEmpty() {
			return token.isEmpty() || id.isEmpty();
		}
		
		public String toString() {
			return id + " " + token;
		}
	}
}

