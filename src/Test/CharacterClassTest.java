package Test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Generator.Character.CharacterClass;
import Generator.Character.CharacterClassFactory;
import Generator.Character.Tokenizer;

public class CharacterClassTest 
{
	Tokenizer t;
	CharacterClassFactory ccf;
	CharacterClass cc;	
	
	@Before
	public void setUp() throws Exception 
	{

	}
	
	@Test
	public void testName()
	{
		String regex1 = "$NAME [abc]";
		assertEquals("wrong result", "abc", testProcedure(regex1));
		String regex2 = "[abc]";
		assertEquals("wrong result", "abc", testProcedure(regex2));
	}
	
	@Test
	public void testRange()
	{
		String regex3 = "$NAME [kswl-q]";
		assertEquals("wrong result", "klmnopqsw", testProcedure(regex3));
		String regex4 = "[oolav-zldg]";
		assertEquals("wrong result", "adglovwxyz", testProcedure(regex4));
	}
	
	@Test
	public void testNameExclude()
	{
		String regex5 = "$EXCLUDE [^abc] IN $NAME";
		assertEquals("wrong result", "defghijklmnopqrstuvwxyz", testProcedure(regex5));
		String regex6 = "$EXCLUDE [^abc] IN [a-z]";
		assertEquals("wrong result", "defghijklmnopqrstuvwxyz", testProcedure(regex6));
	}
	
	@Test
	public void testNoNameExclude()
	{
		String regex7 = "[^skn] IN $NAME"; 
		assertEquals("wrong result", "abcdefghijlmopqrtuvwxyz", testProcedure(regex7));
		String regex8 = "[^owq] IN [a-z]";
		assertEquals("wrong result", "abcdefghijklmnprstuvxyz", testProcedure(regex8));
	}
	
	@Test
	public void testComplexNoName()
	{
		String regex9 = "[a-dc-wA-Jj-u]";
		assertEquals("wrong result", "ABCDEFGHIJabcdefghijklmnopqrstuvw", testProcedure(regex9));
		String regex10 = "[a-dXXc-wYYA-Jj-u]";		
		assertEquals("wrong result", "ABCDEFGHIJXYabcdefghijklmnopqrstuvw", testProcedure(regex10));
	}
	
	public String testProcedure(String s)
	{
		t = new Tokenizer(s);
		ccf = new CharacterClassFactory(t);
		cc = new CharacterClass("$NAME");
		cc.acceptBoundary('a', 'z');
		ccf.map.put("$NAME", cc);
		return ccf.build(s).print();
	}
}
