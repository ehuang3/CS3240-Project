package Generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Generator.DFA.DFA;
import Generator.DFA.DFAFactory;
import Generator.NFA.NFA;
import Generator.NFA.NFAFactory;
import Generator.NFA.NFASimulator;
import Generator.TableWalker.TableWalker;
import Generator.TableWalker.TableWalker.TokenResult;

public class Main {
	
	public static void main(String[] args) {
		// Handle command line arguments
		String spec_fname = null;
		String code_fname = null;
		boolean bonus = false;
		if(args.length < 2) {
			spec_fname = "test/Phase I/sample_spec.txt";
			code_fname = "test/Phase I/sample_input.txt";
			bonus = false;
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
			
			in = new Scanner(new File(code_fname));
			String code = in.useDelimiter("\\Z").next();
			in.close();
			
			//Tokenize input code
			TableWalker walker = new TableWalker(dfas);
			List<TokenResult> results = walker.walk(code);
			
			// Results and bonus
			NFASimulator sim = new NFASimulator();
			Map<String, NFA> nfas = RegexGenerator.cache();
			for(TokenResult r : results) {
				if(bonus) {
					System.out.println("DFA: " + r + " in " + r.token.length() + " moves.");
					System.out.println("NFA: " + r.id + " " + sim.parse(nfas.get(r.id), r.token));
				} else {
					System.out.println(r);
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
