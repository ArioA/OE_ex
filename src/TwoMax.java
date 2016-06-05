/* 
 * Ario Aliabadi
 * 
 * ario.aliabadi@gmail.com
 * 
 * 5/6/2016
 */

// This programme takes a plain text file of integers between MAX_INT and
// MIN_INT inclusive from the file 'numbers.txt' and outputs the ones which
// appear strictly less than three times into a new file 'output.txt'.
//
// 'numbers.txt' must be formated so that each line contains only an integer
// and a new line character. The outputed file 'output.txt' is formated in this
// way. No other characters are permitted.

package src;

import java.util.*;
import java.io.*;

public class TwoMax {

	// Largest integer value programme will accept.
	private static final int MAX_INT = 10000;

	// Smallest integer value programme will accept.
	private static final int MIN_INT = 0;

	// log_2(FACTOR) is the maximum number of times we may have to double the 
	// size of our hash map. We subtract 1 because of rounding down from 
	// integer division.
	private static final int FACTOR = 31;

//========================================================================
//================================ MAIN ==================================
//========================================================================

	public static void main(String[] args) {

		try {
			// Locate file.
			File numbers = new File("/Users/ariobsa/Code/OpenEnergi/numbers.txt");
		
			// Extract contents of file into a hash map which counts the 
			// frequency of occurency of each integer.
			HashMap <Integer, Integer> countHash = extractIntegers(numbers);


			// Create output.txt file.
			File output = new File("/Users/ariobsa/Code/OpenEnergi/output.txt");
			newEmptyFile(output);

		
			// Check if hash map is empty, if so then nothing to do.
			if(countHash.isEmpty())
				return;

			// Write all integers which occur once or twice to the file output.txt.
			createOutputFile(output, countHash);

			// Catch exception thrown by extractIntegers().
		} catch (IOException e){
				System.err.println(e.getMessage());
				e.printStackTrace();
		}
	}


//========================================================================
//========================== FUNCTIONS ===================================
//========================================================================


	/* Preconditions: 'file' is an initialised object of type File with a
	 *						valid pathname pointing to an existing file.
	 *						The file which it points to is a text file consisting of 
	 *						newline-seperated integers ranging from MIN_INT to 
	 *						MAX_INT (inclusive).
	 * Postconditions: Returns a hash map consisting of the distinct integers
	 * 					 appearing in 'file' as keys, and the number of times
	 * 					 they occur as values. Exceptions are generated if 'file'
	 * 					 contains an integer outside the specified range, contains
	 * 					 incorrect characters, doesn't exist, or other issues to 
	 * 					 do with the input file stream. 
	 */ 
	public static HashMap<Integer, Integer> extractIntegers(File file) 
		throws IOException  {

		// Create a hash map to store integers.
		// Given capacity is MAX_INT/FACTOR because priority of program is speed 
		// over memory efficiency, so don't want to have to double capacity
		// more than 5 times (2^5 = 32).
		HashMap<Integer, Integer> hm = 
			new HashMap<Integer, Integer>((int)MAX_INT/FACTOR);

	// ---------------------- Load file into hash map ------------------------	
		try {
			// Create an input file stream.
			Scanner reader = new Scanner(file);

			// Change reader delimiter to new line character (regular expression).
			reader.useDelimiter("\\n");

			// Create buffer for holding read-in values.
			int intBuffer;

			try {
				// While there are characters to be read...
				while(reader.hasNext()) {
	
					// If the next character is an integer...
					if(reader.hasNextInt()) {
	
						// Read the next integer into intBuffer.
						intBuffer = reader.nextInt();
						
						// If intBuffer is invalid for this exercise...
						if(intBuffer < MIN_INT || intBuffer > MAX_INT) {

							
							// Explain error.
							System.err.print("Error: '" + intBuffer + "' is not a");
							System.err.println("valid value for this file type.");
							System.err.print("Each integer must range between ");
							System.err.println(MIN_INT + " and " + MAX_INT + " only.");
							// Throw IOException.
							throw new IOException("Unexpected file formatting.");
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
					} else if(reader.hasNext("\\n")) {

						// Continue to next iteration of while loop.
						continue;

					// If line contains a non-integer or newline character...
					} else {

						// Explain error.
						System.err.print("Error: '" + reader.next() + "' is not a");
						System.err.println("valid character for this file type.");
						System.err.print("Each line may contain integers or ");
						System.err.println("new line characters (\\n) only.");
						// Throw IOException.
						throw new IOException("Unexpected file formatting.");
					}
				}	
				// Exception for when reading in an integer but data is not
				// of type integer.
			} catch (InputMismatchException e) {
				System.err.println("Error: Next token is not an integer.");
				System.err.println(e.getMessage());
				e.printStackTrace();
				// Exception for when there are no more characters to read but
				// Scanner atempts to do so nonetheless.
			} catch (NoSuchElementException e) {
				System.err.println("Error: Input is exhausted.");
				System.err.println(e.getMessage());
				e.printStackTrace();
				// Exception for when Scanner atempts to read while closed.
			} catch (IllegalStateException e) {
				System.err.println("Error: Scanner is closed");
				System.err.println(e.getMessage());
				e.printStackTrace();
			} finally {
				// Close the input file stream, exception or otherwise.
				reader.close();
			}		
			// Catch exception for when input file cannot be found.
		} catch (FileNotFoundException e) {
			System.err.println("File " + file.getName() + " not found.");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		// Return populated HashMap.
		return hm;
	}

//=========================================================================
	/* Preconditions: 'file' is an initialised File object with a correct
	 * 					pathname.
	 * Postconditions: If a file at the pathname of 'file' already exists then 
	 * 					 it is deleted. A new empty file is created at 'file'.
	 */

	public static void newEmptyFile(File file) {

		// Delete old output.txt file if one exists.
		if(file.exists())
			file.delete();

		try{			
			// Create a new file.
			file.createNewFile();
			// If creating a file produces an exception.
		} catch (IOException e) {
			System.err.println("An error occured when creating output.txt");
			System.err.println(e.getMessage());
			e.printStackTrace();	
		}
	}

//==========================================================================

	/* Preconditions: 'output' is a file which exists and is empty. Also, hm
	 * 					is an initialised, non-empty hash map. 
	 * Postconditions: Each key which has a value of 1 or 2 in hm is written to
	 * 					 'output' as many times as what its value is. All other
	 * 					  keys are ignored. Exceptions are raised if hm is empty
	 * 					  or if output is a non-existent file.
	 */

	public static void createOutputFile(File output, 
															HashMap<Integer, Integer> hm) {
		// Generate iterator to interate through the keys of hm.
		Iterator<Integer> it = hm.keySet().iterator();

		try {
			// Buffer to hold keys of hm.
			int hashKey;

			// PrintWriter object writes to file 'output'. 
			PrintWriter writer =  new PrintWriter(output);
	
	//----------------------- Iterate through hash map -----------------------
			try{

				// While there are visited keys...
				while(it.hasNext()) {

					// Save value of next hash key.
					hashKey = it.next();

					// If this integer appears strictly less than three times in 
					// numbers.txt...
					if(hm.get(hashKey) < 3) {

						// Write it to 'output' seperated by a new line character
						// however many times it appeared in numbers.txt.
						for(int k = 0; k < hm.get(hashKey); k++) {
							writer.print(hashKey);
							writer.print('\n');
						}
					}
				}

				// Catch exception for when trying to access an iterator when there
				// are none remaining.
			} catch (NoSuchElementException e) {
				System.err.println("Error: Iterator has no 'next()' element.");
				System.err.println(e.getMessage());
				e.printStackTrace();	
			} finally {
				// Close the output file stream, exception or otherwise.
				writer.close();
			}  
			// Catch exception for when trying to write to a non-existent file.
		} catch (FileNotFoundException e) {
			System.err.println( "Error: file " + output.getName() + " not found");
			System.err.println(e.getMessage());
			e.printStackTrace();
		} 

	}
}
