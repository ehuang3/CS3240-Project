Ho Yin Pun
Eric Huang
Jung Gi Lee
Hyangjin Lee

Program Structure:

-CS3240-Project
	-Generator
		-Main.java
	-Generator.Character
		-CharacterClass.java
		-CharacterClassFactory.java
		-Token.java
		-Tokenizer.java	
	-Generator.DFA
		-DFA.java
		-DFAFactory.java
		-DFANode.java
		-DFATransition.java
		-EpsilonClosure.java
	-Generator.NFA
		-NFA.java
		-NFAFactory.java
		-NFANode.java
		-NFASimulator.java
		-NFATransition.java
	-Generator.Lexer
		-Lexer.java
		-Token.java
	-Generator.TableWalker
		-TableWalker.java
	-MiniRE
		-AST.java
	-MiniRE.RecursiveDescent
		-MiniParser.java
	-MiniRE.VirtualMachine
		-MiniVM.java
		-StringMatch.java
		-StringMatchList.java
		-Variable.java
	-Test
		-CharacterClassTest.java
		-DFAFactoryTest.java
		-LexerTest.java
		-MiniParserTest.java
		-IntegrationTest.java
		-NFAFactoryTest.java
		-NFATest.java
		-TableWalkerTest.java
		-TokenizerTest.java
		
The main.java file is the driver for this entire program. It contains the main() that triggers the scanner generator. 

Executing scanner generator instructions:
1.) Open Eclipse IDE
2.) Import the extracted project (from tarball) into the IDE as an existing project
3.) Run main.java by following this path /CS3240-Project/src/Generator/main.java

Executing JUnit tests:
1.) Right click on the test file
2.) Run as JUnit test

