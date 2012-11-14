package TableWalker;

import java.util.ArrayList;
import java.util.List;

import Generator.DFA.DFANode;
import Generator.DFA.DFA;
import Generator.DFA.DFATransition;

/**
 * This class takes in a String input and compares each character in the input to the DFA transition table to walk in the DFA.
 * @author Hyangjin Lee
 *
 */
public class tablewalker {
	
	DFA dfa;
	DFATransition transition;
	String input;
	int i, j;
	List<DFATransition> list;
	ArrayList<String> tokenlist;
	

	public tablewalker(DFA dfa, String input){
		this.dfa = dfa;
		this.input = input;
	}	
	
	/**
	 * This method takes in the input String (could be a long text), takes each character at a time and walks the DFA table.
	 * If a valid token is determined, then it saves the token in the token list (String[] tokenlist), and repeat the process 
	 * from the next character in the input String file.
	 * 
	 */
	public void walkDFA(){
		
		char current;
		String token = "";
		DFANode now = dfa.start();
		List<DFATransition> list;
		
		for(i=0; i < input.length(); i++){
			j = 0;
			list = now.adjacencyList();
			DFATransition transition = list.get(j);
			int size = list.size();
			boolean found = false;	
			boolean valid = true;
			
			current = input.charAt(i);
			token += current;
			
			if(!valid)
				now = dfa.start();
			
			while(!found && j < size){
				
				if(transition.isTriggered(current)){
					now = transition.end();
					found = true;
				}

				transition = list.get(++j);
						
			}
			
			if(!found && j == size-1){
				tokenlist.add(token);
				token = "";
				valid = false;
			}
			
					
		}
		
		
	}

	/**
	 * This method returns the list of token.
	 * @return
	 */
	public ArrayList<String> getTokenlist(){
		
		return tokenlist;
	}
	
	/**
	 * This method prints out each token gotten from walking the DFA with the input String file.
	 */
	public void printTokens(){
		
		for(i=0; i < tokenlist.size(); i++){
			
			System.out.println(tokenlist.get(i));
			
		}
			
	}
}


/**
You will not need our files for the DFA table. The DFA table is going to be a text file formatted like so:

<DFA-name> <#states>
<state1> start <final | >
<state2> <final | >
     .
     .
     .
<staten> <final | >
<char-class1-1> <char-class1-2> ... <char-class1-n>
<char-class2-1> <char-class2-2> ... <char-class2-n>
     .
     .
     .
<char-classn-1> <char-classn-2> ... <char-classn-n>

^ For each individual DFA. That's 
- 1 line for name of DFA and number of states, 
- n lines for name of states and whether or not they are final (with the first always being the start). If it's not final, there is no word after the state name
- n lines with n entries in each line representing transitions from state to state. interpret row and from and column as to. So, row i col j represents from state i to state j
  each entry will be a representation of the valid character class that can be used to transition. It will be a string of all valid character matches.

- Eric
**/