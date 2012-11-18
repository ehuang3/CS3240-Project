package Generator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import Generator.DFA.DFA;
import Generator.DFA.DFAFactory;
import Generator.NFA.NFA;
import Generator.NFA.NFAFactory;

public class main {
	
	public static void main(String[] args) {
		// Handle command line arguments
		String spec_fname = null;
		String code_fname = null;
		boolean bonus = false;
		if(args.length < 2) {
			spec_fname = "input/sample_spec.txt";
			code_fname = "input/sample_input.txt";
			bonus = true;
		} else if(args.length == 2) {
			spec_fname = args[0];
			code_fname = args[1];
		} else {
			assert(args[0].equals("--bonus"));
			bonus = true;
			spec_fname = args[1];
			code_fname = args[2];
		}
		
		// Main execution
		try {
			Scanner in = new Scanner(new BufferedReader(new FileReader(spec_fname)));
			
			//Initialize Factory classes.
			NFAFactory RegexGenerator = new NFAFactory();
			DFAFactory DFAGenerator = new DFAFactory();
			
			// Compile the input specification
			while(in.hasNext()) {
				String input = in.nextLine().trim();
				if(!input.isEmpty()) {
					RegexGenerator.build(input);
				}
			}
			in.close();
			
			//NFA to DFA
			List<DFA> dfas = new LinkedList<DFA>();
			for(NFA nfa : RegexGenerator.cache().values()) {
				dfas.add(DFAGenerator.minimize(DFAGenerator.build(nfa)));
			}
			
			for(DFA dfa : dfas) {
				System.out.println(dfa.id());
				System.out.println(dfa);
			}
			
			//FIXME: Tokenize input code
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
