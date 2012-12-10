package MiniRE.VirtualMachine;


public class Variable {
	enum var_type {
		_int,
		_string_match_list;
	}
	
	var_type type;
	Integer number;
	StringMatchList list;
	
	String name;
	
	public Variable(String name, int n) {
		this(name, n, null);
	}
	
	public Variable(String name, StringMatchList l) {
		this(name, null, l);
	}
	
	public Variable(int n) {
		this("", n, null);
	}
	
	public Variable(StringMatchList l) {
		this("", null, l);
	}
	
	public Variable(String name, Integer n, StringMatchList l) {
		assert(name == null ^ l == null) : "FAILED ASSERT IN VARIABLE()";
		type = n != null ? var_type._int : var_type._string_match_list;
		this.name = name;
		number = n;
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
	
	public String toString() {
		return name + " = " + (type == var_type._int ? number : list);
	}
}
