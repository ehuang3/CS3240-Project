package Generator.Character;

import java.util.HashMap;

import Generator.Token;
import Generator.Token.op_code;
import Generator.Tokenizer;

public class CharacterClassFactory 
{
	Tokenizer tokenizer;
	HashMap<String, CharacterClass> map;
	Token token, peekToken;
	op_code top, ptop;
	char start, end;
	
	public CharacterClassFactory(Tokenizer tokenizer)
	{
		this.tokenizer = tokenizer;  
		map = new HashMap<String, CharacterClass>();
	}
	
	public CharacterClass build(String regex)  //char name
	{
		token = tokenizer.next();  //consume 1st token (may be a name or [) 
		CharacterClass temp = new CharacterClass();
		
		if(token.operand == op_code.id)  //name of char class (1st token)
		{
			temp.name = token.value;  //set char class name
			map.put(temp.name, temp);  //add char class with a name to map

			token = tokenizer.next();  //consume [
			if(token.operand == op_code.left_brac) 
			{
				temp = basic();
			}
		}
		else  //[ is 1st token
		{
			temp = charClass();
		}
		return temp;
	}
	
	public CharacterClass charClass()  //no char name
	{
		return basic();  //return [^...] or [...]
	}
	
	public CharacterClass basic()  //build [...], already consumed [
	{
		CharacterClass temp = null;
		peekToken = tokenizer.peek();  //check if next token is ^
		
		if(peekToken.operand == op_code.exclude)  //[^ exclude
		{
			token = tokenizer.next();  //consume ^
			CharacterClass e = charList();
			
			if(token.operand == op_code.in)  //IN
			{
				temp = charList();  //original class to exclude from
				temp.exclude(e);  //exclude everything of e inside o
			}
		}
		else  //not exclude 
		{
			temp = charList();
		}
		return temp;
	}
	
	public CharacterClass charList()
	{
		CharacterClass temp = new CharacterClass();
		peekToken = tokenizer.peek();  //check if next token is cls_char
		
		while(peekToken.operand != op_code.right_brac)  //keep matching until ]
		{
			if(token.operand == op_code.cls_char)
			{
				token = tokenizer.next();  //consume current cls_char
				temp.accept(token.value.charAt(0));  //add current cls_char to char class
			}
			if(peekToken.operand == op_code.range)
			{ 
				start = token.value.charAt(0);  //set current token as start bound
				token = tokenizer.next();  //consume range
				token = tokenizer.next();  //consume end
				end = token.value.charAt(0);  //set current token as end bound
				
				temp.acceptBoundary(start, end);  //add range to char class
			}	
		}
		
		token = tokenizer.next();  //consume ]
		return temp;  //return finished char class
	}
}