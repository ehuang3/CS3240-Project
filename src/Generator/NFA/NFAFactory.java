package Generator.NFA;

import java.util.Map;

import Generator.Token;
import Generator.Tokenizer;
import Generator.Character.CharacterClass;
import Generator.Character.CharacterClassFactory;
import Generator.Token.op_code;

public class NFAFactory {
	CharacterClassFactory ccFactory;
	Map<String, CharacterClass> ccMap;
	Tokenizer T;
	
	String indent = "";
	String tab = "| ";
	
	public NFAFactory() {
		T = new Tokenizer("");
	}
	
	public void classes(Map<String, CharacterClass> map) {
		ccMap = map;
	}
	
	public void tokenizer(Tokenizer t) {
		T = t;
	}
	
	private void addTab() {
		indent += tab;
	}
	
	private void removeTab() {
		indent = indent.substring(tab.length());
	}
	

	public NFA build(String in) {
		System.out.println(indent + "BUILD: " + in);
		addTab();
		
		T.tokenize(in);
		Token nameTok = T.next();
		
		NFA N = reg_expr();
		N.name = nameTok.value;		
		
		removeTab();
		System.out.println(indent + "BUILD: " + in);
		
		return N;
	}
	
	// rexp
	public NFA reg_expr() {
		System.out.println(indent + "REG_EXPR:");
		addTab();		
		
		NFA N = and_expr().or(or_expr());
		
		removeTab();
		System.out.println(indent + "REG_EXPR:");
		
		return N;
	}
	
	// rexp'
	private NFA or_expr() {		
		Token token = T.peek();
		
		System.out.println(indent + "OR_EXPR: " + token);
		addTab();
		
		NFA N = null;
		switch (token.operand) {
		case or :
			T.next();
			N = reg_expr();
			break;
		default :
			N = NFA.NoTransitionNFA();
		}
		
		removeTab();
		System.out.println(indent + "OR_EXPR: " + token);
		return N;
	}
	
	// rexp1
	private NFA and_expr() {
		System.out.println(indent + "AND_EXPR:");
		addTab();
		
		NFA N =  term_expr().and(and_tail_expr());
		
		removeTab();
		System.out.println(indent + "AND_EXPR:");
		
		return N;
	}
	
	// rexp1'
	private NFA and_tail_expr() {
		System.out.println(indent + "AND_TAIL_EXPR:");
		addTab();
		
		NFA N = term_expr();
		if(N.isEpsilonNFA()) {
			// Do nothing
		} else {
			N =  N.and(and_tail_expr());
		}
		
		removeTab();
		System.out.println(indent + "AND_TAIL_EXPR:");
		
		return N;
	}
	
	// rexp2, rexp2-tail
	private NFA term_expr() {
		Token token = T.peek();
		
		System.out.println(indent + "TERM_EXPR: " + token);
		addTab();
		
		NFA N = null;
		switch (token.operand) {
			case left_paren :
				T.next();
				N = reg_expr();
				T.next();
				break;
			case re_char :
				T.next();
				CharacterClass match = new CharacterClass("");
				match.accept(token.value);
				N = new NFA(match);
				break;
			default:
				N = char_class_expr();
		}
		
		token = T.peek();
		switch (token.operand) {
			case star :
				T.next();
				N = N.star();
				break;
			case plus :
				T.next();
				N = N.plus();
				break;
			default:
				// Do nothing
		}
		
		removeTab();
		System.out.println(indent + "TERM_EXPR: " + token);
		return N;
	}
	
	// rexp3
	private NFA char_class_expr() {
		Token token = T.peek();
		
		System.out.println(indent + "CHAR_CLASS_EXPR: " + token);
		addTab();
		
		NFA N = null;
		switch (token.operand) {
			case match_all :
				 N = new NFA( CharacterClass.MatchAll() );
				 T.next();
			break;
			case left_brac :
				N = new NFA( ccFactory.build() );
			break;
			case id : 
				N = new NFA( ccMap.get(token.value) );
				T.next();
			break;
			default :
				N = NFA.EpsilonNFA();
		}
		
		removeTab();
		System.out.println(indent + "CHAR_CLASS_EXPR: " + token);
		return N;
	}
	
	public CharacterClass buildCharClass() {
		return null;
	}
}
