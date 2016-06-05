/* 
 * Ario Aliabadi
 * 
 * ario.aliabadi@gmail.com
 * 
 * 5/6/2016
 */



package src;

import java.util.*;
import java.io.*;

public class TwoMax {

	private static final int MAX_INT = 10000;
	private static final int MIN_INT = 0;

	public static void main(String[] args) throws IOException {

		// Locate file.
		File numbers = new File("/Users/ariobsa/Code/OpenEnergi/numbers.txt");

		// Create a hash map to store integers.
		// Capacity of 500 because maximum possible size is 100001,
		// and priority is program speed over memory efficiency,
		// so want to avoid having to increase capacity often.
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>(500);

// ------------------------ Load file into hash map ------------------------	
		try {
			Scanner readr = new Scanner(numbers);

			int intBuffer;

			readr.useDelimiter("\\n");
			try {
				// While there are characters to be read...
				while(readr.hasNext()) {
	
					// If the next character is an integer...
					if(readr.hasNextInt()) {
	
						// Read the next integer into intBuffer.
						intBuffer = readr.nextInt();
						
						// If intBuffer is invalid for this exercise...
						if(intBuffer < MIN_INT || intBuffer > MAX_INT) {

							// Go to next iteration of the while loop.
							continue;
						}

						// If intBuffer is present in our hash table...
						if(hm.containsKey(intBuffer)) {

							// Increment the value that intBuffer maps to.
							hm.put(intBuffer, hm.get(intBuffer) + 1);
						} else {

							// Otherwise, set it to 1.
							hm.put(intBuffer, 1);
						}

					// Else, if line is just an emtpy new line...
					} else if(readr.hasNext("\\n")) {

						// Continue to next iteration of while loop.
						continue;

					// If line contains a non-integer or newline character...
					} else {

						// Explain error.
						System.err.print("Error: '" + readr.next() + "' is not a");
						System.err.println("valid character for this file type.");
						System.err.print("Each line may contain integers or ");
						System.err.println("new line characters (\\n) only.");
						// Throw IOException.
						throw new IOException("Unexpected file formatting.");
					}
				}	
			} catch (InputMismatchException e) {
				System.err.println("Error: Next token is not an integer.");
				System.err.println(e.getMessage());
				e.printStackTrace();
			} catch (NoSuchElementException e) {
				System.err.println("Error: Input is exhausted.");
				System.err.println(e.getMessage());
				e.printStackTrace();
			} catch (IllegalStateException e) {
				System.err.println("Error: Scanner is closed");
				System.err.println(e.getMessage());
				e.printStackTrace();
			} finally {
				readr.close();
			}		
		} catch (FileNotFoundException e) {
			System.err.println("File " + numbers.getName() + " not found.");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
// ------------------------ Create output.txt File ----------------------------

		File output = new File("/Users/ariobsa/Code/OpenEnergi/output.txt");

		// Delete old output.txt file if one exists.
		if(output.exists())
			output.delete();

		try{			
			// Create a new file.
			output.createNewFile();
		} catch (IOException e) {
			System.err.println("An error occured when creating output.txt");
			System.err.println(e.getMessage());
			e.printStackTrace();	
		}
//------------------------- Iterate through hash map -----------------------
		

		// Check if hash map is empty, if so then nothing to do.
		if(hm.isEmpty())
			return;

		Iterator<Integer> it = hm.keySet().iterator();

		try {
			int hashKey;

			PrintWriter writer =  new PrintWriter(output);
			
			try{

				while(it.hasNext()) {
					hashKey = it.next();

					if(hm.get(hashKey) < 3) {

						for(int k = 0; k < hm.get(hashKey); k++) {
							writer.print(hashKey);
							writer.print('\n');
						}
					}
				}

			} catch (NoSuchElementException e) {
				System.err.println("Error: Iterator has no 'next()' element.");
				System.err.println(e.getMessage());
				e.printStackTrace();	
			} finally {
				writer.close();
			}  
		} catch (FileNotFoundException e) {
			System.err.println( "Error: file " + output.getName() + " not found");
			System.err.println(e.getMessage());
			e.printStackTrace();
		} 
	}
}
