package Generator;

import java.util.ArrayList;

public class CharacterClass 
{
	private boolean[] valid;
	private ArrayList<Character> real;
	
	public CharacterClass()
	{
		valid = new boolean[256];
		real = new ArrayList<Character>();
	}
	
	public void accept(char c)
	{
		valid[(int)c] = true;
	}
	
	public boolean match(char c)
	{
		return valid[(int)c];
	}
	
	public void getAllCharacterHelper()
	{
		for(int a = 0; a < 129; a++)
		{
			if(valid[a])
			{
				real.add((char)a);
			}
		}
	}
	
	public ArrayList<Character> getAllCharacter()
	{
		if(real.size() == 0)
		{
			getAllCharacterHelper();
		}
		return real;
	}
}
