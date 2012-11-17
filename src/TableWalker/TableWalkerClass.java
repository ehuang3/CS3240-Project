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
	String[] dfas = new String[size];
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
		for(int b=0; b<dfas.length; b++)
			dfas[b] = "";
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
	
	public void walkTable(){
		
		for(int i=0; i<input.length(); i++){
			
			
			
			
		}
		
		ArrayList validList = getValid();
		
		
		
	}
}

