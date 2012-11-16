package Generator.Character;

import java.util.HashMap;

import Generator.Token;
import Generator.Token.op_code;
import Generator.Tokenizer;

public class CharacterClassFactory 
{
	public Tokenizer tokenizer;
	public HashMap<String, CharacterClass> map;
	Token token, peekToken;
	op_code top, ptop;
	char start, end;
	
	public CharacterClassFactory(Tokenizer tokenizer)
	{
		this.tokenizer = tokenizer;  
		map = new HashMap<String, CharacterClass>();
	}
	
	public CharacterClass getCharClass(String name) {
		return map.get(name);
	}
	
	public CharacterClass build(String regex)  
	{
		tokenizer.tokenize(regex);
		
		System.out.println("\tinside build()");
		peekToken = tokenizer.peek();  //consume 1st token (may be a name or [) 
		CharacterClass temp = new CharacterClass();
		String s = "";
		
		if(peekToken.operand == op_code.id)  //name of char class (1st token)
		{
			consume(1);  //consume name of class 1
			s = token.value;  //set char class name
			//System.out.println("\t\t\t\t\t\t\t\t\tname: " + token.value + "\tsize: " + map.size());
		}
		
		temp = charClass();  //get first [^...] or [...] char class object 
		temp.name = s;
		map.put(s, temp);
		return temp;
	}
	
	public CharacterClass charClass()  //start at [ not consumed (may be part of named or no named line
	{	
		CharacterClass temp = null;
		
		peekToken = tokenizer.peek();  //peek at next token to determine if it is [
		if(peekToken.operand == op_code.left_brac) 
		{
			//token = tokenizer.next();  //consume [
			consume(1);
			temp = basic();  //get first [^...] or [...] char class object  
		}
		
		/*
		System.out.println("back from first char class");
		System.out.println("class 1: ");
		temp.print();
		System.out.println("\n\n");
		*/
		
		//just consumed ]
		peekToken = tokenizer.peek();
		
		//temp is a char class of [^...] or [...], need to check for IN 
		//current token could be IN if not finished
		if(peekToken.operand == op_code.in)  //IN
		{
			CharacterClass temp2 = null;
			
			//token = tokenizer.next();  //consume IN
			consume(1);
			
			peekToken = tokenizer.peek();  //check if next token is $___ or [
			if(peekToken.operand == op_code.left_brac)
			{
				//token = tokenizer.next();  //consume [ 
				consume(1);
				
				temp2 = charList();  //generate another [...] to exclude from
			}
			else if(peekToken.operand == op_code.id) 
			{
				consume(1);  //consume name of class 2
				if(map.containsKey(token.value))
				{
					temp2 = map.get(token.value);  //get existing [...] from map
				}
			}

			temp = temp2.exclude(temp);  //exclude everything of temp inside temp2
		}	
		return temp;
	}
	
	public CharacterClass basic()  //build [...], already consumed [
	{
		System.out.println("\tinside basic()");
		peekToken = tokenizer.peek();  //check if next token is ^
		
		if(peekToken.operand == op_code.exclude)  //[^ exclude
		{
			//token = tokenizer.next();  //consume ^
			consume(1);
		}
		return charList();
	}
	
	public CharacterClass charList()  //start by consuming 1st cls_char token
	{
		System.out.println("\tinside charList()");
		CharacterClass temp = new CharacterClass();
		peekToken = tokenizer.peek();  //check if next token is cls_char
		
		while(peekToken.operand != op_code.right_brac)  //keep matching until ]
		{	
			//check if next token is range to determine if current token is cls_char or start of range
			if(peekToken.operand == op_code.range)
			{ 
				start = token.value.charAt(0);  //set current token as start bound
				System.out.println("\t\t\t\t\t\t\t\t\tstart: " + start);
				
				//token = tokenizer.next();  //consume range
				//token = tokenizer.next();  //consume end
				consume(2);
				
				end = token.value.charAt(0);  //set current token as end bound
				System.out.println("\t\t\t\t\t\t\t\t\tend: " + end);
				
				temp.acceptBoundary(start, end);  //add range to char class
			}	
			
			if(peekToken.operand == op_code.cls_char)
			{
				//token = tokenizer.next();  //consume current cls_char
				consume(1);
				
				temp.accept(token.value.charAt(0));  //add current cls_char to char class
			}
			
			peekToken = tokenizer.peek();  //check if next token is cls_char
		}
		
		//token = tokenizer.next();  //consume ]
		consume(1);
		return temp;  //return finished char class
	}
	
	public void consume(int i)
	{
		for(int a = 0; a < i; a++)
		{
			token = tokenizer.next();
			System.out.println("\t\t\tconsumed: " + token.value + "\ttype: " + token.operand);
		}
	}
}