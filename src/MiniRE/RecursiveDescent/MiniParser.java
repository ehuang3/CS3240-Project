package MiniRE.RecursiveDescent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import Generator.Lexer.Lexer;
import Generator.Lexer.Token;
import MiniRE.AST;

public class MiniParser {
	public Lexer lexer;
	
	public MiniParser() {
		lexer = new Lexer();
		lexer.fspec("test/sample/token_spec.txt");
	}
	
	private AST match(String token_id) {
		AST ast = new AST(lexer.peek(token_id));
		if(!lexer.match(token_id)) {
			ast = new AST("error-token");
		}
		return ast;
	}
	
	public AST parseFile(String fname) {
		Scanner in;
		String code = "";
		try {
			in = new Scanner(new File(fname));
			code = in.useDelimiter("\\Z").next();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return parse(code);
	}
	
	public AST parse(String input) {
		lexer.tokenize(input);
		return MiniRE_program();
	}
	
	public AST MiniRE_program() {
		AST ast = new AST("MiniRE-program");
		
		ast.add(match("BEGIN"));
		ast.add(statement_list());
		ast.add(match("END"));
		
		return ast;
	}
	
	public AST statement_list() {
		AST ast = new AST("statement-list");
		
		ast.add(statement());
		ast.add(statement_list_tail());
		
		return ast;
	}
	
	public AST statement_list_tail() {
		AST ast = new AST("statement-list-tail");
		
		AST statement = statement();
		if(statement == null) {
			ast.add(new AST("epsilon"));
		} else {
			ast.add(statement);
			ast.add(statement_list_tail());
		}
		
		return ast;
	}
	
	public AST statement() {
		AST ast = new AST("statement");
		Token token = lexer.peek();
		switch (token.token_id) {
		case "REPLACE" :
			ast.add(match("REPLACE"));
			ast.add(match("REGEX"));
			ast.add(match("WITH"));
			ast.add(match("ASCII-STR"));
			ast.add(match("IN"));
			ast.add(file_names());
			ast.add(match("SEMICOLON"));
			break;
		case "RECREP" :
			ast.add(match("RECREP"));
			ast.add(match("REGEX"));
			ast.add(match("WITH"));
			ast.add(match("ASCII-STR"));
			ast.add(match("IN"));
			ast.add(file_names());
			ast.add(match("SEMICOLON"));
			break;
		case "ID" :
			ast.add(match("ID"));
			ast.add(match("EQ"));
			ast.add(statement_righthand());
			ast.add(match("SEMICOLON"));
			break;
		case "PRINT" :
			ast.add(match("PRINT"));
			ast.add(match("OPENPARENS"));
			ast.add(exp_list());
			ast.add(match("CLOSEPARENS"));
			ast.add(match("SEMICOLON"));
			break;
		default :
			ast = null;
		}
		
		return ast;
	}
	
	public AST statement_righthand() {
		AST ast = new AST("statement-righthand");
		
		Token token = lexer.peek();
		switch (token.token_id) {
		case "HASH" :
			ast.add(match("HASH"));
			ast.add(exp());
			break;
		case "MAXFREQ" :
			ast.add(match("MAXFREQ"));
			ast.add(match("OPENPARENS"));
			ast.add(match("ID"));
			ast.add(match("CLOSEPARENS"));
			break;
		default :
			ast.add(exp());
		}
		
		return ast;
	}
	
	public AST file_names() {
		AST ast = new AST("file-names");
		
		ast.add(source_file());
		ast.add(match("GRTNOT"));
		ast.add(destination_file());
		
		return ast;
	}
	
	public AST source_file() {
		AST ast = new AST("source-file");
		
		ast.add(match("ASCII-STR"));
		
		return ast;
	}
	
	public AST destination_file() {
		AST ast = new AST("destination-file");
		
		ast.add(match("ASCII-STR"));
		
		return ast;
	}
	
	public AST exp_list() {
		AST ast = new AST("exp-list");
		
		ast.add(exp());
		ast.add(exp_list_tail());
		
		return ast;
	}
	
	public AST exp_list_tail() {
		AST ast = new AST("exp-list-tail");
		
		Token token = lexer.peek();
		switch (token.token_id) {
		case "COMMA" :
			ast.add(match("COMMA"));
			ast.add(exp());
			ast.add(exp_list_tail());
			break;
		default :
			ast.add(new AST("epsilon"));
		}
		
		return ast;
	}
	
	public AST exp() {
		AST ast = new AST("exp");
		
		Token token = lexer.peek();
		switch (token.token_id) {
		case "ID" :
			ast.add(match("ID"));
			break;
		case "OPENPARENS" :
			ast.add(match("OPENPARENS"));
			ast.add(exp());
			ast.add(match("CLOSEPARENS"));
			break;
		default :
			ast.add(term());
			ast.add(exp_tail());
		}
		
		return ast;
	}
	
	public AST exp_tail() {
		AST ast = new AST("exp-tail");
		
		AST bin_op = bin_op();
		if(bin_op == null) {
			ast.add(new AST("epsilon"));
		} else {
			ast.add(bin_op);
			ast.add(term());
			ast.add(exp_tail());
		}
		
		return ast;
	}
	
	public AST term() {
		AST ast = new AST("term");
		
		ast.add(match("FIND"));
		ast.add(match("REGEX"));
		ast.add(match("IN"));
		ast.add(file_name());
		
		return ast;
	}
	
	public AST file_name() {
		AST ast = new AST("file-name");
		
		ast.add(match("ASCII-STR"));
		
		return ast;
	}
	
	public AST bin_op() {
		AST ast = new AST("bin-op");
		
		Token token = lexer.peek();
		switch (token.token_id) {
		case "DIFF" :
			ast.add(match("DIFF"));
			break;
		case "UNION" :
			ast.add(match("UNION"));
			break;
		case "INTERS" :
			ast.add(match("INTERS"));
			break;
		default :
			ast = null;
		}
		
		return ast;
	}
}
