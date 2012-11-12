package Generator.NFA;

import java.util.Map;

import Generator.Tokenizer;
import Generator.Character.CharacterClass;

public class NFAFactory {
	Map<String, CharacterClass> charClass;
	Tokenizer T;
	
	public NFAFactory() {
	}
	
	public void charClass(Map<String, CharacterClass> map) {
		charClass = map;
	}
	
	public void tokenizer(Tokenizer t) {
		T = t;
	}
	
	public NFA build(String in) {
		return null;
	}
}
