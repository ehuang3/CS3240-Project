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
	
	public NFAFactory() {
		T = new Tokenizer("");
	}
	
	public void classes(Map<String, CharacterClass> map) {
		ccMap = map;
	}
	
	public void tokenizer(Tokenizer t) {
		T = t;
	}
	
	public NFA build(String in) {
		T.tokenize(in);
		NFA nfa = NFA.EpsilonNFA();
		if(op_code.id == T.peek().operand) {
			nfa.name = T.peek().value;
			T.match(op_code.id);
		}
		return nfa.and(regex());
	}
	
	public void input(String in) {
		T.tokenize(in);
	}
	
	public NFA regex() {
		NFA nfa = unary_list();
		while(op_code.or == T.peek().operand) {
			T.match(op_code.or);
			nfa.or(unary_list());
		}
		return nfa;
	}
	
	private NFA unary_list() {
		NFA nfa = unary_expr();
		NFA unary = unary_expr();
		while( op_code.or  != T.peek().operand &&
			         unary != null				  ) {
			nfa.and(unary);
			unary = unary_expr();
		}
		return nfa;
	}
	
	private NFA unary_expr() {
		NFA nfa = term_expr();
		switch (T.peek().operand) {
		case star :
			T.match(op_code.star);
			nfa.star();
			break;
		case plus :
			T.match(op_code.plus);
			nfa.plus();
			break;
		default:
			break;
		}
		return nfa;
	}
	
	private NFA term_expr() {
		NFA nfa = null;
		switch (T.peek().operand) {
		case re_char :
			CharacterClass cls = new CharacterClass("");
			cls.accept(T.peek().value.charAt(0));
			nfa = new NFA(cls);
			T.match(op_code.re_char);
			break;
		case left_paren :
			T.match(op_code.left_paren);
			nfa = regex();
			T.match(op_code.right_paren);
			break;
		default :
			nfa = char_class_expr();
		}
		return nfa;
	}
	
	private NFA char_class_expr() {
		NFA nfa = null;
		switch (T.peek().operand) {
		case left_brac :
			//FIXME: Call clsFactory subroutine
			break;
		case match_all :
			//FIXME: Replace with match-all cls
			nfa = new NFA( new CharacterClass(".") );
			T.match(op_code.match_all);
		case id :
			//FIXME: Implement ccMap
			nfa = new NFA( ccMap.get(T.peek().value) );
			T.match(op_code.id);
		case epsilon :
			T.match(op_code.epsilon);
			nfa = NFA.EpsilonNFA();
		default :
			// Return null to signal no more terminals to match
		}
		return nfa;
	}
	
	public CharacterClass buildCharClass() {
		return null;
	}
}
