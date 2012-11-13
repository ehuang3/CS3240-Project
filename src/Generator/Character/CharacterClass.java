package Generator.Character;

import java.util.ArrayList;

public class CharacterClass 
{	
	boolean[] valid;
	ArrayList<Character> real;
	String name;
	
	public CharacterClass(String name)
	{
		valid = new boolean[256];
		real = new ArrayList<Character>();
		this.name = name;
	}

	public boolean isMatched(char c) 
	{
		return valid[(int)c];
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
	
	public void acceptBoundary(int start, int end)
	{
		for(int a = (int)start; a <= (int)end; a++)
		{
			valid[(char)a] = true;
		}
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
	
	public CharacterClass union(CharacterClass c) 
	{
		for(int a = 0; a < valid.length; a++)
		{
			if(c.valid[a])
			{
				valid[a] = true;
			}
		}
		return this;
	}
}
