package MiniRE.VirtualMachine;

import java.util.LinkedList;
import java.util.List;

public class StringMatch {
	public String value;
	public List<StringMatchInfo> info;
	
	public StringMatch(String val, String fn, int line, int start) {
		this(val, fn, line, start, start + val.length());
	}
	
	public StringMatch(String val, String fn, int line, int start, int end) {
		value = val;
		info = new LinkedList<StringMatchInfo>();
		info.add(new StringMatchInfo(fn, line, start, end));
	}
	
	public void add(StringMatchInfo smi) {
		info.add(smi);
	}
	
	public int size() {
		return info.size();
	}
	
	public class StringMatchInfo {
		public String fname;
		public int line_num;
		public int start_pos;
		public int end_pos;
		
		public StringMatchInfo(String fn, int line, int start, int end) {
			fname = fn;
			line_num = line;
			start_pos = start;
			end_pos = end;
		}
		
		public String toString() {
			return "<'" + fname + "'," + line_num + ", " + start_pos + ", " + end_pos + ">";
		}
	}
}
