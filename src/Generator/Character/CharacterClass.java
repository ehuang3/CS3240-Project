package Generator.Character;

public class CharacterClass 
{	
	boolean[] valid;
	String name;
	
	public CharacterClass()
	{
		this("");
	}
	
	public CharacterClass(String name)
	{
		valid = new boolean[256];
		this.name = name;
	}
	
	public boolean isMatched(String c) {
		return isMatched(c.charAt(0));
	}

	public boolean isMatched(char c) 
	{
		if((int) c >= 0 && (int) c < 256)
			return valid[(int)c];
		return false;
	}
	
	public void accept(char c)
	{
		valid[(int)c] = true;
	}
	
	public void acceptAll()
	{
		for(int a = 0; a < valid.length; a++)
		{
			valid[a] = true;
		}
	}
	
	public void acceptBoundary(char start, char end)
	{
		for(int a = (int)start; a <= (int)end; a++)
		{
			valid[(char)a] = true;
		}
	}

	public CharacterClass union(CharacterClass c) 
	{
		CharacterClass temp = new CharacterClass(name);
		for(int a = 0; a < valid.length; a++)
		{
			if(c.valid[a] || valid[a])
			{
				temp.valid[a] = true;
			}
		}
		return temp;
	}
	
	public CharacterClass intersect(CharacterClass c)
	{
		CharacterClass temp = new CharacterClass(name);
		for(int a = 0; a < valid.length; a++)
		{
			if(c.valid[a] && valid[a])
			{
				temp.valid[a] = true;
			}
		}
		return temp;
	}
	
	public CharacterClass exclude(CharacterClass c)
	{
		CharacterClass temp = new CharacterClass(name);
		for(int a = 0; a < valid.length; a++)
		{
			if(valid[a] && !c.valid[a])
			{
				//System.out.println("setting: " + (char)a);
				temp.valid[a] = true;
			}
		}
		return temp;
	}
	
	public String print()
	{
		System.out.println("\n\nprinting character class\n");
		StringBuilder s = new StringBuilder();
		
		for(int a = 0; a < valid.length; a++)
		{
			if(valid[a])
			{
				System.out.print((char)a);
				s.append((char)a);
			}
		}		
		System.out.println("\n\n========================================================\n\n");
		return s.toString();
	}
	
	public String toString() {
 		String out = "";
 		boolean rangeNotation = false;
 		for(int i=0; i<valid.length; i++) {
 			if(valid[i] && !rangeNotation) {
 				out += (char)(i);
 				rangeNotation = true;
 			}
 			if(!valid[i] && rangeNotation) {
 				if(out.charAt(out.length()-1) != (char)(i - 1)) {
 					out += "-" + (char)(i - 1);
 				}
 				rangeNotation = false;
 			}
 		}
 		if(rangeNotation) {
 			out += "-" + "HAHA";
 		}
 		return "[" + out + "]";
	}
}
