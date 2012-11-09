package Generator.Character;

public class CharacterClass {
	
	
	public boolean isMatched(char c) {
		return false;
	}
	
	public boolean isMatched(String c) {
		return false;
	}
	
	public CharacterClass union(CharacterClass c) {
		return new CharacterClass();
	}
	
	public CharacterClass intersect(CharacterClass c) {
		return new CharacterClass();
	}
}
