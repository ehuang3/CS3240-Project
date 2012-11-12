package Generator.Character;

import java.util.ArrayList;

public class CharacterClass 
{	
	private boolean[] valid;
	private ArrayList<Character> real;
	private String name;
	
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
	
	public ArrayList<Character> getAllCharacter()
	{
		if(real.size() == 0)
		{
			getAllCharacterHelper();
		}
		return real;
	}
	
	/*
	//to do
	
	public CharacterClass union(CharacterClass c) {
		return new CharacterClass();
	}
	
	public CharacterClass intersect(CharacterClass c) {
		return new CharacterClass();
	}
	*/
}
