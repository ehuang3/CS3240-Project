package Generator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class main {
	
	public static void main(String[] args) {
		// Handle command line arguments
		String spec_fname = null;
		String code_fname = null;
		boolean bonus = false;
		if(args.length < 2) {
			spec_fname = "input/sample_input_specification.txt";
			code_fname = "";
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
			
			//FIXME: Initialize Factory classes and Tokenizer
			
			// State switching variables
			int section = -1;
			final int CHAR_CLASS_DEF = 0;
			final int REGEX_DEF = 1;
			
			// Compile the input specification
			while(in.hasNext()) {
				String input = in.nextLine();
				if("%%" == input.substring(0, 2)) {
					section++;
				} else if(section == CHAR_CLASS_DEF) {
					//FIXME: Generate Character Classes
					
				} else if(section == REGEX_DEF) {
					// Generate Regex
					
				}
			}
			in.close();
			
			//FIXME: Tokenize input code
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
