package MiniRE.VirtualMachine;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import MiniRE.VirtualMachine.StringMatch.StringMatchInfo;

public class StringMatchList {
	Map<String, StringMatch> matches;
	
	public StringMatchList() {
		matches = new TreeMap<String, StringMatch>();
	}
	
	public StringMatchList(StringMatch m) {
		this();
		matches.put(m.value, new StringMatch(m));
	}
	
	public void add(StringMatch m) {
		if(!matches.containsKey(m.value)) {
			matches.put(m.value, new StringMatch(m));
		} else {
			StringMatch match = matches.get(m.value);
			for(StringMatchInfo smi : m.info) {
				match.add(smi);
			}
		}
	}
	
	public boolean contains(StringMatch m) {
		return matches.containsKey(m.value);
	}
	
	public int size() {
		int size = 0;
		List<StringMatch> list = list();
		for(StringMatch match : list) {
			size += match.size();
		}
		return size;
	}
	
	public StringMatchList maxFreq() {
		StringMatch max = null;
		List<StringMatch> list = list();
		for(StringMatch match : list) {
			if(max == null || max.size() < match.size()) {
				max = match;
			}
		}
		return new StringMatchList(max);
	}
	
	public List<StringMatch> list() {
		List<StringMatch> list = new LinkedList<StringMatch>();
		for(Entry<String, StringMatch> e : matches.entrySet()) {
			list.add(e.getValue());
		}
		return list;
	}
	
	public StringMatchList union(StringMatchList other) {
		StringMatchList U = new StringMatchList();
		
		List<StringMatch> list = list();
		for(StringMatch match : list) {
			U.add(match);
		}
		list = other.list();
		for(StringMatch match : list) {
			U.add(match);
		}
		
		return U;
	}
	
	public StringMatchList inters(StringMatchList other) {
		StringMatchList I = new StringMatchList();
		
		for(Entry<String, StringMatch> e : matches.entrySet()) {
			if(other.contains(e.getValue())) {
				I.add(other.matches.get(e.getKey()));
				I.add(e.getValue());
			}
		}
		
		return I;
	}
	
	/**
	 * this - other
	 * 
	 * @param other
	 * @return
	 */
	public StringMatchList diff(StringMatchList other) {
		StringMatchList D = new StringMatchList();
		
		List<StringMatch> list = list();
		for(StringMatch match : list) {
			if(!other.contains(match)) {
				D.add(match);
			}
		}
		
		return D;
	}
	
	public String toString() {
		String out = "{";
		if(size() != 0) {
			List<StringMatch> list = list();
			int i = 0;
			for(StringMatch match : list) {
				if(i == 0) {
					out += match.value + " ";
					for(StringMatchInfo info : match.info) {
						out += info;
					}
				} else {
					out += ", " + match.value + " ";
					for(StringMatchInfo info : match.info) {
						out += info;
					}
				}
				i++;
			}
		}
		out += "}";
		return out;
	}
}
