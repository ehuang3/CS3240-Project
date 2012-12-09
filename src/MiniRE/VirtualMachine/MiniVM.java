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
			match("REPLACE", ast.get(0));
			break;
		case "RECREP" :
			match("RECREP", ast.get(0));
			break;
		case "ID" :
			statement_righthand(ast.get(0));
			break;
		case "PRINT" :
			// TODO
			break;
		
		}
	}
	
	public Variable statement_righthand(AST ast) throws Exception {
		match("statement-righthand", ast);
		
		switch (ast.get(0).rule_id) {
		case "exp" :
			exp(ast.get(0));
			break;
		case "HASH" :
			match("HASH", ast.get(0));
			break;
		case "MAXFREQ" :
			match("MAXFREQ", ast.get(0));
			break;
		}
		
		return null;
	}
	
	public String[] file_names(AST ast) throws Exception{
		match("file-names", ast);
		
		//TODO
		
		return null;
	}
	
	public String source_file(AST ast) throws Exception{
		match("source-file", ast);
		
		match("ASCII-STR", ast.get(0));		
		return null;
	}
	
	public void destination_file(AST ast) throws Exception{
		match("destination-file", ast);
		
		match("ASCII-STR", ast.get(0));

	}
	
	public void exp_list(AST ast) throws Exception{
		match("exp-list", ast);
		
		exp(ast.get(0));
		exp_list_tail(ast.get(0));

	}
	
	public void exp_list_tail(AST ast) throws Exception{
		match("exp-list-tail", ast);
		
		// comma?????
	}
	
	public void exp(AST ast) throws Exception{
		match("exp", ast);
		
		switch(ast.get(0).rule_id) {
		case "ID" :
			match("ID", ast.get(0));
			break;
		case "exp" :
			exp(ast.get(0));
			break;
		case "term" :
			term(ast.get(0));
			exp_tail(ast.get(1));
			break;
		}
	}
	
	public void exp_tail(AST ast) throws Exception{
		match("exp-tail", ast);
		
		switch(ast.get(0).rule_id) {
		case "bin-op" :
			bin_op(ast.get(0));
			term(ast.get(1));
			exp_tail(ast.get(2));
			break;
		case "epsilon" :
			match("epsilon", ast.get(0));
			break;
		}

	}
	
	public Variable term(AST ast) throws Exception{
		match("term", ast);
		
		
	}
	
	public String file_name(AST ast) throws Exception{
		match("file-name", ast);
		
		//TODO
		
		return null;
	}
	
	public void bin_op(AST ast) throws Exception{
		match("bin-op", ast);
		
		switch(ast.get(0).rule_id){
		case "DIFF" :
			match("DIFF", ast.get(0));
			break;
		case "UNION" :
			match("UNION", ast.get(0));
			break;
		case "INTERS" :
			match("INTERS", ast.get(0));
			break;
		}
	}
}
