package MiniRE.VirtualMachine;


public class Variable {
	enum var_type {
		_int,
		_string_match_list;
	}
	
	var_type type;
	Integer number;
	StringMatchList list;
	
	public Variable(int n) {
		type = var_type._int;
		number = n;
		list = null;
	}
	
	public Variable(StringMatchList l) {
		type = var_type._string_match_list;
		number = 0;
		list = l;
	}
	
	public boolean isInt() {
		return type == var_type._int;
	}
	
	public boolean isStringMatchList() {
		return type == var_type._string_match_list;
	}
	
	public var_type getType() {
		return type;
	}
	
	public Integer getInteger() {
		return type == var_type._int ? number : null;
	}
	
	public StringMatchList getStringMatchList() {
		return type == var_type._string_match_list ? list : null;
	}
}
