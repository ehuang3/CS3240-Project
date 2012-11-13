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
	
	public CharacterClass build(String regex)  
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
				temp = basic();  //get first [^...] or [...] char class object  
			}
		}
		else  //[ is 1st token, char class without a name (not added to map)
		{
			//temp = charClass();  
			temp = basic();  //get first [^...] or [...] char class object 
		}
		
		//temp is a char class of [^...] or [...], need to check for IN 
		//current token could be IN if not finished
		if(token.operand == op_code.in)  //IN
		{
			CharacterClass temp2;
			token = tokenizer.next();  //consume IN
			temp2 = charList();  //original class to exclude from
			temp2.exclude(temp);  //exclude everything of temp inside temp2
			temp = temp2; 
		}
		return temp;
	}
	
	/*
	public CharacterClass charClass()  
	{
		return basic();  //return [^...] or [...]
	}
	*/
	
	public CharacterClass basic()  //build [...], already consumed [
	{
		peekToken = tokenizer.peek();  //check if next token is ^
		
		if(peekToken.operand == op_code.exclude)  //[^ exclude
		{
			token = tokenizer.next();  //consume ^
		}
		return charList();
	}
	
	public CharacterClass charList()  //start by consuming 1st cls_char token
	{
		CharacterClass temp = new CharacterClass();
		peekToken = tokenizer.peek();  //check if next token is cls_char
		
		while(peekToken.operand != op_code.right_brac)  //keep matching until ]
		{	
			//check if next token is range to determine if current token is cls_char or start of range
			if(peekToken.operand == op_code.range)
			{ 
				start = token.value.charAt(0);  //set current token as start bound
				token = tokenizer.next();  //consume range
				token = tokenizer.next();  //consume end
				end = token.value.charAt(0);  //set current token as end bound
				
				temp.acceptBoundary(start, end);  //add range to char class
			}	
			
			if(token.operand == op_code.cls_char)
			{
				token = tokenizer.next();  //consume current cls_char
				temp.accept(token.value.charAt(0));  //add current cls_char to char class
			}
		}
		
		token = tokenizer.next();  //consume ]
		return temp;  //return finished char class
	}
}