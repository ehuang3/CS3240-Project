package TableWalker;

import java.util.ArrayList;
import java.util.List;

import Generator.DFA.DFANode;
import Generator.DFA.DFA;
import Generator.DFA.DFATransition;

public class TableWalkerClass {

	String input;
	List<DFA> DFAList;
	ArrayList<String> tokenlist;
	int size = 0;

	/* We are gonna save each token and the next index of the input String gotten from running each DFAs in these arrays */
	String[] DFATokenList = new String[size];
	int[] index = new int[size];
	int[] block = new int[size];
	ArrayList<DFANode> currentNode = new ArrayList<DFANode>(size);
	
	
	public TableWalkerClass(List<DFA> DFAList, String input){
		this.DFAList = DFAList;
		this.input = input;
		size = DFAList.size();
	}	
	
	/**
	 * This method checks the block list. If the block list is not empty,
	 * it means there is still a DFA that is building a longer token.
	 * If the block list is empty, it means that there is no DFA that can walk.
	 * So in that case, we don't DFAs any more.
	 * @return
	 */
	private boolean emptyBlockList(){

		int isZero = 0;
		
		for(int b=0; b<block.length; b++){
			if(block[b]==0)
				isZero++;
		}
		
		if(isZero==size)//means block array is all zero.
			return true;
		else
			return false;
	}
	
	/**
	 * This method checks if blocklist is all one, which means we are about to build
	 * a whole new token and need to check all the DFAs.
	 * @return
	 */
	private boolean fullBlockList(){
		int isOne = 0;
		
		for(int b=0; b<block.length; b++){
			if(block[b]==1)
				isOne++;
		}
		
		if(isOne==size)
			return true;
		else
			return false;
	}
	
	/**
	 * This method empties the tokenList so that we can start rebuilding a new token.
	 */
	private void cleanTokenList(){
		for(int b=0; b<DFATokenList.length; b++)
			DFATokenList[b] = "";
	}
	
	/**
	 * This method adds a new token to the tokenlist. Tokenlist contains all the valid tokens
	 * from the original input String.
	 * @param newToken A newly found valid token.
	 */
	private void addToken(String newToken){
		tokenlist.add(newToken);
	}
	
	/**
	 * This method returns an array that contains the list of the DFAs that are subject to checks
	 * for a further valid transitions.
	 * @return
	 */
	private ArrayList getValid(){
		ArrayList validList = new ArrayList();
		
		for(int b=0; b<block.length; b++){
			if(block[b] == 1)
				validList.add(b);
		}
		return validList;
	}
	
	/**
	 * This method sets the block array to all ones.
	 */
	private void cleanBlockList(){
		for(int b=0; b<block.length; b++)
			block[b]=1;
	}
	
	/**
	 * This method filters out the unnecessary part of the input String
	 * @param character the current character to be checked if it equls one of the three: \n, \r, and \t.
	 * @return
	 */
	private boolean needFilter(char character){
		if(character == '\n')
			return true;
		else if(character == '\r')
			return true;
		else if(character == '\t')
			return true;
		else
			return false;
	}
	
	/**
	 * This method walks the DFAs.
	 */
	public void walkTable(){
		
		int i=0;
		while(i<input.length()){

			
			
			//to be implemented: filter that skips all the white spaces
			//except for the whitespace.
			char current = input.charAt(i);
			boolean needFiltered = false;
			
			do{
				needFiltered = needFilter(current);
				if(needFiltered){
					i++;
					current = input.charAt(i);
				}
			}while(!needFilter(current));
		
			
			//upon each char in the input String, we check block list,			
			if(emptyBlockList()){
				//if it is empty, then we check the DFATokenList and
				//get the longest one and add it to the tokenlist
				//and clean the blocklist and DFATokenList.
				tokenlist.add(getTheLongest());
				cleanBlockList();
				cleanDFATokenList();
			}
			else if(fullBlockList()){
				//if it is all 1s, then we simply walks through all the 
				//DFA tables and update the CurrentDFANode
				//and update the DFATokenList
				//and record the invalid DFAs and update the blocklist.
				
				DFA dfa;
				DFANode now;
				List<DFATransition> list;
				
				for(int c=0; c<DFAList.size(); c++){
					dfa = DFAList.get(c);
					now = dfa.start();
					list = now.adjacencyList();
					String token = "";
					
					int j=0;
					boolean found = false;
					//run while loop for all transitions at the node.
					while(!found && j<list.size()){
						
						DFATransition transition = list.get(j);
						if(transition.isTriggered(current)){
							found = true;
							currentNode.add(c, transition.end());
							DFATokenList[c] += current;
							
						}
						
						
						j++; //increment the index of transitions
					}
					
					if(!found){
						block[c] = 0;
						currentNode.remove(c); 
						//remove the current node info at this particular DFA that doesn't have a match. 
					}
					
				}
				
			}
			else{
				//if it is not entirely open, but it is still open,
				//then we get the valid list, and walk only those DFAs
				//in the valid list, and update the CurrentDFANode
				//and the DFATokenList
				//and record the invalid DFAs and update the blocklist.
				ArrayList validList = getValid();
				DFA dfa;
				DFANode now;
				List<DFATransition> list;
				
				for(int c=0; c<validList.size(); c++){
					
					int d= (int) validList.get(c); // d is the DFA id.
					
					dfa = DFAList.get(d);
					now = dfa.start();
					list = now.adjacencyList();
					String token = "";
					
					int j=0;
					boolean found = false;
					while(!found && j<list.size()){
						
						DFATransition transition = list.get(j);
						if(transition.isTriggered(current)){
							found = true;
							currentNode.add(d, transition.end());
							DFATokenList[d] += current;
						}		
						
						j++; //increment the index of transitions
					}
					
					if(!found){
						block[d] = 0;
						currentNode.remove(d); 
						//remove the current node info at this particular DFA that doesn't have a match. 
					}

				}
	
			}
	
			i++; //increment the index of the String input.
		}
		
	}
	
	/**
	 * This method cleans out the DFATokenList.
	 */
	private void cleanDFATokenList(){
		for(int b=0; b<DFATokenList.length; b++){
			DFATokenList[b]="";
		}
	}
	
	/**
	 * This method goes through the DFATokenList and returns the longest token
	 * among those gotten from each DFAs.
	 * @return
	 */
	private String getTheLongest(){
		
		String longest = DFATokenList[0];
		
		for(int b=1; b<DFATokenList.length; b++){
			if(longest.length() < DFATokenList[b].length())
				longest = DFATokenList[b];
		}
		
		return longest;
		
	}
}

