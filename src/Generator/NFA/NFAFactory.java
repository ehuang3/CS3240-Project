package Generator.NFA;

import java.util.Map;

import Generator.Token;
import Generator.Tokenizer;
import Generator.Character.CharacterClass;
import Generator.Character.CharacterClassFactory;

public class NFAFactory {
	CharacterClassFactory ccFactory;
	Map<String, CharacterClass> ccMap;
	Tokenizer T;
	
	public NFAFactory() {
	}
	
	public void classes(Map<String, CharacterClass> map) {
		ccMap = map;
	}
	
	public void tokenizer(Tokenizer t) {
		T = t;
	}
	
	public NFA build(String in) {
		T.tokenize(in);
		Token nameTok = T.next();
		
		NFA N = reg_expr();
		N.name = nameTok.value;
		
		return N;
	}
	
	// rexp
	public NFA reg_expr() {
		return and_expr().or(or_expr());
	}
	
	// rexp'
	private NFA or_expr() {
		Token token = T.peek();
		
		NFA N = null;
		switch (token.operand) {
		case or :
			T.next();
			N = reg_expr();
			break;
		default :
			N = NFA.NoTransitionNFA();
		}
		return N;
	}
	
	// rexp1
	private NFA and_expr() {
		return term_expr().and(and_tail_expr());
	}
	
	// rexp1'
	private NFA and_tail_expr() {
		NFA N = term_expr();
		if(N.isEpsilonNFA()) {
			return N;
		} else {
			return N.and(and_tail_expr());
		}
	}
	
	// rexp2, rexp2-tail
	private NFA term_expr() {
		Token token = T.peek();
		
		NFA N = null;
		switch (token.operand) {
			case left_paren :
				T.next();
				N = reg_expr();
				break;
			case re_char :
				T.next();
				N = new NFA(new CharacterClass(token.value));
				break;
			default:
				N = char_class_expr();
		}
		return N;
	}
	
	// rexp3
	private NFA char_class_expr() {
		Token token = T.peek();
		
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
		return N;
	}
	
	public CharacterClass buildCharClass() {
		return null;
	}
}
