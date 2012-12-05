package Generator.Lexer;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

import Generator.DFA.DFA;
import Generator.DFA.DFAFactory;
import Generator.NFA.NFA;
import Generator.NFA.NFAFactory;
import MiniRE.AST;

public class Lexer {
	private Map<String, DFA> tokenDFA;
	private String spec;
	private String code;
	private String skip;
	private int pos;
	
	public Lexer(String lexicalSpec) {
		spec = lexicalSpec;
		tokenDFA = new TreeMap<String, DFA>();
		skip = " \n\t\r";
	}
	
	public void init() {
		if(spec == null || spec.isEmpty()) {
			return;
		}
		NFAFactory nfaFactory = new NFAFactory();
		DFAFactory dfaFactory = new DFAFactory();
		Scanner in = new Scanner(spec);
		while(in.hasNext()) {
			String line = in.nextLine().trim();
			if(!line.isEmpty()) {
				nfaFactory.build(line);
			}
		}
		in.close();
		for(NFA nfa : nfaFactory.cache().values()) {
			DFA dfa = dfaFactory.minimize(dfaFactory.build(nfa));
			tokenDFA.put(dfa.id(), dfa);
		}
	}
	
	public void respec(String newSpec) {
		spec = spec + "\n" + newSpec;
		init();
	}
	
	public void resetSpec() {
		spec = "";
		tokenDFA.clear();
	}
	
	public void reset() {
		pos = 0;
	}
	
	public void tokenize(String input) {
		code = input;
	}
	
	public AST peek(int n) {
		int saved_pos = pos;
		AST ast = null;
		for(int i = 1; i <= n; i++) {
			ast = next();
		}
		pos = saved_pos;
		
		return ast;
	}
	
	public AST peek() {
		return peek(1);
	}
	
	public boolean match(String tokenID) {
		return false;
	}
	
	public AST next() {
		if(pos >= code.length()) {
			return new AST("eoi", "eoi");
		}
		AST ast = new AST("error","");
		
		for(Entry<String, DFA> e : tokenDFA.entrySet()) {
			DFA dfa = e.getValue();
			String value = dfa.walk(code.substring(pos));
			if(ast == null || ast.value.length() < value.length()) {
				ast = new AST(dfa.id(), value);
			}
		}
		pos += ast.value.length();
		
		return ast;
	}
	
	private String nextToken() {
		String token = "";
		return token;
	}
}