package MiniRE;

import java.util.ArrayList;

/** 
 * Abstract-Syntax-Tree (AST) holds the tree representation of a parse.
 * 
 * AST recursively defines the tree, the class is both the tree and the nodes
 * in the tree.
 * 
 * @author eric
 */
public class AST {
	public final String rule_id;   // Name of grammar rule
	public final String value;     // Value associated with AST (optional)
	private ArrayList<AST> child;  // Order list of descendants
	
	public AST(String id) {
		this(id, "");
	}
	
	public AST(String id, String val) {
		rule_id = id;
		value = val;
		child = new ArrayList<AST>();
	}
	
	public void add(AST ast) {
		child.add(ast);
	}
	
	public AST get(int i) {
		return child.get(i);
	}
	
	public int size() {
		return child.size();
	}
	
	public boolean terminal() {
		return child.size() == 0;
	}
}
