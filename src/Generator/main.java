package Generator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class main {
	
	public static void main(String[] args) {
		try {
			String fname = "input/sample_input_specification.txt";
			Scanner s = new Scanner(new BufferedReader(new FileReader(fname)));
			
			while(s.hasNext()) {
				System.out.println(s.nextLine());
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
