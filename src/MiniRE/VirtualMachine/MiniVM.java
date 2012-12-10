package MiniRE.VirtualMachine;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Generator.Lexer.Lexer;
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
			replace(ast);
			break;
		case "RECREP" :
			recursivereplace(ast);
			break;
		case "ID" :
			id(ast);
			break;
		case "PRINT" :
			print(ast);
			break;
		
		}
	}
	
	/*
	 * Input AST is <statement>, use the children of statement
	 */
	public void replace(AST ast) {
		match("REPLACE", ast.get(0));
		Lexer lexer = regex(ast.get(1));
		match("WITH", ast.get(2));
		String ascii_str = ascii_str(ast.get(3));
		match("IN", ast.get(4));
		String fname = file_name(ast.get(5));
		
		// TODO: Implement replace
	}
	
	/*
	 * Input AST is <statement>, use the children of statement
	 */
	public void recursivereplace(AST ast) {
		match("RECREP", ast.get(0));
		Lexer lexer = regex(ast.get(1));
		match("WITH", ast.get(2));
		String ascii_str = ascii_str(ast.get(3));
		match("IN", ast.get(4));
		String fname = file_name(ast.get(5));
		match("SEMICOLON", ast.get(6));
		
		// TODO: Implement recursivereplace
	}
	
	/*
	 * Input AST is <statement>, use the children of statement
	 * 
	 * <statement> --
	 * ID = <statement-righthand>;
	 */
	public void assign_expr(AST ast) {
		String id = id(ast.get(0));
		match("EQ", ast.get(1));
		Variable var = statement_righthand(ast.get(2));
		match("SEMICOLON", ast.get(3));
		
		// TODO: Implement assign statement
	}
	
	public String id(AST ast) {
		match("ID", ast);
		return ast.value;
	}
	
	/*
	 * Input AST is <statement>, use the children of statement
	 */
	public void print(AST ast) {
		match("PRINT", ast.get(0));
		match("OPENPARENS", ast.get(1));
		List<Variable> exp_list = exp_list(ast.get(2));
		match("CLOSEPARENS", ast.get(3));
		match("SEMICOLON", ast.get(4));
		
		// TODO: Implement print
	}
	
	public Variable statement_righthand(AST ast) throws Exception {
		match("statement-righthand", ast);
		
		Variable var = null;
		switch (ast.get(0).rule_id) {
		case "exp" :
			var = exp(ast.get(0));
			break;
		case "HASH" :
			var = hash(ast);
			break;
		case "MAXFREQ" :
			var = maxfreq(ast);
			break;
		}
		
		return var;
	}
	
	public String[] file_names(AST ast) throws Exception{
		match("file-names", ast);
		
		String[] files = new String[2];
		files[0] = source_file(ast.get(0));
		match("GRTNOT", ast.get(1));
		files[1] = destination_file(ast.get(2));
		
		return files;
	}
	
	public String ascii_str(AST ast)  {
		match("ASCII-STR", ast);
		
		String ascii = ast.value;
		ascii = ascii.substring(1, ascii.length()-2);
		return ascii;
	}
	
	public String source_file(AST ast) throws Exception{
		match("source-file", ast);
		
		return ascii_str(ast.get(0));
	}
	
	public String destination_file(AST ast) throws Exception{
		match("destination-file", ast);
		
		return ascii_str(ast.get(0));

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
		
		return ascii_str(ast.get(0));
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
