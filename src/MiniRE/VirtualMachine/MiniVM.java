package MiniRE.VirtualMachine;

import java.util.Map;
import java.util.TreeMap;

import MiniRE.AST;

public class MiniVM {
	Map<String, Variable> symbol_table;
	
	public MiniVM() {
		symbol_table = new TreeMap<>();
	}
	
	public void match(String rule_id, AST ast) throws Exception {
		if(ast == null || !rule_id.equals(ast.rule_id)) {
			throw new Exception();
		}
	}
	
	public void run(AST ast) throws Exception {
		MiniRE_program(ast);
	}
	
	public void MiniRE_program(AST ast) throws Exception {
		match("MiniRE-program", ast);
		
		match("BEGIN", ast.get(0));
		statement_list(ast.get(1));
		match("END", ast.get(2));
	}
	
	public void statement_list(AST ast) throws Exception {
		match("statement-list", ast);
		
		statement(ast.get(0));
		statement_list_tail(ast.get(1));
	}
	
	public void statement_list_tail(AST ast) throws Exception {
		match("statement-list-tail", ast);
		
		switch (ast.get(0).rule_id) {
		case "statement" :
			statement(ast.get(0));
			statement_list_tail(ast.get(1));
			break;
		case "epsilon" :
			match("epsilon", ast.get(0));
			break;
		}
	}
	
	public void statement(AST ast) throws Exception {
		match("statement", ast);
		
		switch (ast.get(0).rule_id) {
		case "REPLACE" :
			// TODO
			break;
		case "RECREP" :
			// TODO
			break;
		case "ID" :
			// TODO
			break;
		}
	}
	
	public Variable statement_righthand(AST ast) {
		match("statement-righthand", ast);
		
		switch (ast.get(0).rule_id) {
		case "exp" :
			// TODO
			break;
		case "HASH" :
			// TODO
			break;
		case "MAXFREQ" :
			// TODO
			break;
		}
		
		return null;
	}
}
