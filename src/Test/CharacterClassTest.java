package Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import Generator.Tokenizer;
import Generator.Character.CharacterClass;
import Generator.Character.CharacterClassFactory;
import Generator.NFA.NFAFactory;
import Generator.NFA.NFASimulator;

public class CharacterClassTest 
{
	@Before
	public void setUp() throws Exception 
	{
		
	}

	@Test
	public void testCharacterClass()
	{
		String regex1 = "$NAME [abc]";
		String regex2 = "[abc]";
				
		String regex3 = "$NAME [kswl-q]";
		String regex4 = "[oolav-zldg]";
	
		String regex5 = "$EXCLUDE [^abc] IN $NAME";
		String regex6 = "$EXCLUDE [^abc] IN [a-z]";
		
		String regex7 = "[^skn] IN $NAME"; 
		String regex8 = "[^owq] IN [a-z]";
		
		String regex9 = "[a-dc-wA-Jj-u]";
		String regex10 = "[a-dXXc-wYYA-Jj-u]";
		
		ArrayList<String> all = new ArrayList<String>();
		all.add(regex1);
		all.add(regex2);
		all.add(regex3);
		all.add(regex4);
		all.add(regex5);
		all.add(regex6);
		all.add(regex7);
		all.add(regex8);
		all.add(regex9);
		all.add(regex10);
		
		Tokenizer t;
		CharacterClassFactory ccf;
		CharacterClass cc;
		
		for(String s: all)
		{
			t = new Tokenizer(s);
			ccf = new CharacterClassFactory(t);
			cc = new CharacterClass("$NAME");
			cc.acceptBoundary('a', 'z');
			ccf.map.put("$NAME", cc);
			ccf.build(s).print();
		}
	}
}
