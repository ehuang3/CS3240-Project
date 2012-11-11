package Generator.NFA;

import java.util.List;

import Generator.Character.CharacterClass;

public class NFAFactory {
	List<CharacterClass> charClassList;
	
	public NFAFactory(List<CharacterClass> list) {
		charClassList = list;
	}
	
	public NFA parse(String def) {
		return null;
	}
	
	public NFA parseRegex(String regex) {
		return null;
	}
}
