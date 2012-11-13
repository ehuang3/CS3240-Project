package Generator.Character;

import Generator.Token;
import Generator.Token.op_code;
import Generator.Tokenizer;

public class CharacterClassFactory 
{
	Tokenizer tokenizer;
	CharacterClass cc;
	Token token, peekToken;
	op_code top, ptop;
	boolean exclude;
	char start, end;
	
	public CharacterClassFactory(Tokenizer tokenizer)
	{
		this.tokenizer = tokenizer;
	}
	
	public CharacterClass generateCharacterClass(String regex)
	{
		token = tokenizer.next();
		top = token.operand;
		
		//parse the regex string for tokens
		while(top != op_code.eoi)
		{
			if(top == op_code.id)  //get the character class's name
			{
				cc = new CharacterClass(token.value);
			}
			else if(top == op_code.left_brac)  //detected [
			{
				matchBracket();  
			}
			else if(top == op_code.left_paren)  //detected (
			{
				
			}
			else if(top == op_code.match_all)  //detected .
			{
				cc.acceptAll();
			}
			else if(top == op_code.or)  //detected |
			{
				
			}
			
			top = token.operand;  //sync the token operand for next iteration
		}
		
		return cc;
	}
	
	public void matchBracket()
	{
		advancePeek();  //look ahead one token  
		
		while(ptop != op_code.right_brac)  //keep checking for exclude, range or char matching
		{
			matchExclude();
			matchRange();
			matchCharacter();
		}
	}
	
	public void matchParenthesis()
	{
		
	}
	
	public void matchOr()
	{
		if(ptop == op_code.or)
		{
			
		}
	}
	
	public void matchExclude()
	{
		if(ptop == op_code.exclude)
		{
			rangeBoundary();  //[abc] or [a-z] ????
			advancePeek();  
			
			if(ptop == op_code.in)  //detect IN
			{
				token = tokenizer.next();
				top = token.operand;
				
				if(top == op_code.id)  //next token is a char class
				{
					if(token.value.equals("$CHAR"))
					{
						for(int a = 65; a <= 122; a++)
						{
							if(!(a >= 91 && a <= 96))
							{
								if(!(a >= start && a <= end))
								{
									cc.accept((char)a);
								}
							}
						}
					}
					else if(token.value.equals("$UPPER"))
					{
						for(int a = 65; a <= 90; a++)
						{
							if(!(a >= start && a <= end))
							{
								cc.accept((char)a);
							}
						}
					}
					else if(token.value.equals("$DIGIT"))
					{
						for(int a = 48; a <= 57; a++)
						{
							if(!(a >= start && a <= end))
							{
								cc.accept((char)a);
							}
						}
					}
					else if(token.value.equals("$NON-ZERO"))
					{
						for(int a = 49; a <= 57; a++)
						{
							if(!(a >= start && a <= end))
							{
								cc.accept((char)a);
							}
						}
					}
				}
			}
		}
	}
	
	public void matchRange()
	{
		if(ptop == op_code.range)  //range token
		{
			rangeBoundary();  //set start and end bounds
			cc.acceptBoundary(start, end);  //accept all characters in the bounds
			advancePeek();  //peek before leaving the match
		}
	}
	
	public void rangeBoundary()
	{
		start = token.value.charAt(0);  //set current token as start bound
		token = tokenizer.next();  //consume range token
		token = tokenizer.next();  //consume end token
		end = token.value.charAt(0);  //set end bound
	}
	
	public void matchCharacter()
	{
		while(ptop == op_code.cls_char)  //individual cls_char tokens
		{
			token = tokenizer.next();  //consume current token for token == peekToken
			cc.accept(token.value.charAt(0));  //add current token to character class
			advancePeek();  //move peek by one
		}
	}
	
	public void advancePeek()
	{
		peekToken = tokenizer.peek();  
		ptop = peekToken.operand;
	}
}