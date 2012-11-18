Ho Yin Pun
Eric Huang
Jung Gi Lee
Hyangjin Lee

Program Structure:

-CS3240-Project
	-Generator
		-Character
			-CharacterClass.java
			-CharacterClassFactory.java
		-DFA
			-DFA.java
			-DFAFactory.java
			-DFANode.java
			-DFATransition.java
			-EpsilonClosure.java
		-NFA
			-NFA.java
			-NFAFactory.java
			-NFANode.java
			-NFASimulator.java
			-NFATransition.java
		
		Token.java
		Tokenizer.java	
		main.java
		
	-TableWalker
		-TableWalker.java
		
	-Test
		-CharacterClassTest.java
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

