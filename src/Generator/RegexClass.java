package Generator;

public class RegexClass 
{
	public RegexClass()
	{
		
	}
	
	public CharacterClass generateCharacterClass(String regex)
	{
		CharacterClass cc = new CharacterClass();
		Character temp;
		for(int a = 0; a < regex.length(); a++)
		{
			temp = regex.charAt(a);
			if(temp.equals('\\'))
			{
				a++;
			}
			else if(!temp.equals(' '))
			{
				cc.accept(temp);
			}
		}
		return cc;
	}
}
