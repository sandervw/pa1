import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 
 * @author Sander VanWilligen
 * 
 * This class creates a minhash for a group of documents (specified by a folder)
 * The matrix is K*N, where K is the number of random permutations, and N is the number of documents
 */
public class MinHash {
	
	File folder; //folder in which the files are stored
	int numPermutations; //number of permutations
	int p; //prime number used
	ArrayList<int[]> coeffs; //list of coefficients for the permutations (index 0 is A, index 1 is B)
	int termNum; //number of terms
	
	
	/**
	 * 
	 * @param folder the folder containing the documents for the minhash
	 * @param numPermutations the number of permutations (hash functions) to use
	 * @throws IOException 
	 */
	public MinHash(String folder, int numPermutations) throws IOException {
		
		this.folder = new File(folder);
		this.numPermutations = numPermutations;
		this.termNum = calcNumTerms();
		this.p = getPrime(this.termNum); //p should be greater than the number of terms
		
		coeffs = new ArrayList<int[]>();
		
		//create the permutation coefficients for the k permutations
		Random rand = new Random();
		for(int i = 0; i < numPermutations; i++) {
			int[] temp = {rand.nextInt(p), rand.nextInt(p)};
			while(coeffs.contains(temp)) {
				temp[0] = rand.nextInt(p);
				temp[1] = rand.nextInt(p);
			}
			coeffs.add(temp);
		}
		
		
	}
	
	/**
	 * 
	 * @return an array of strings consisting of all the names of files in the document collections
	 */
	public String[] allDocs() {
		
		return folder.list();
		
	}
	
	/**
	 * 
	 * @param file1 the name of the first file
	 * @param file2 the name of the second file
	 * @return the exact jaccard similarity of the two files
	 * @throws IOException 
	 */
	public double exactJaccard(String file1, String file2) throws IOException {
		
		Set<String> terms1 = getTermList(folder + File.separator + file1);
		Set<String> terms2 = getTermList(folder + File.separator + file2);
		int size1 = terms1.size();
		int size2 = terms2.size();
		
		//Jaccard similarity = (terms1 and terms2) / (terms1 or terms2)
		terms1.retainAll(terms2);
		int t1AndT2 = terms1.size();
		double result = t1AndT2 / (size1 + size2 - t1AndT2 * 1.0);
		
		return result;
		
	}
	
	/**
	 * 
	 * @param fileName the file to compute the minhash signature of
	 * @return the minhash signature of the given file
	 * @throws IOException 
	 */
	public int[] minHashSig(String fileName) throws IOException {
		
		FileReader fr = new FileReader(folder + File.separator + fileName);
		BufferedReader br = new BufferedReader(fr);
		
		int[] minHashSig = new int[numPermutations];
		//Need to set every value in the array to the maximum value, since were looking for a min
		Arrays.fill(minHashSig, Integer.MAX_VALUE);
		
		String line = "";
		String[] termList;
		int hash;
		//for each line in the file
		while((line = br.readLine()) != null) {
			
			line = line.replaceAll("[.,:;']", "");
			line = line.toLowerCase();
			termList = line.split("\\s+");
			
			//for each word in the list
			for(int j = 0; j < termList.length; j++) {

				//if the word is not a stop word
				if(termList[j] != "the" && termList[j].length() > 2) {
					
					//hash the word with every hash function, and if it is less than the current value, set the minHash
					for(int i = 0; i < numPermutations; i++) {
						hash = termList[j].hashCode();
						hash = ((coeffs.get(i)[0] * hash) + coeffs.get(i)[1]) % p;
						if(hash <= minHashSig[i]) minHashSig[i] = hash;
					}
					
				}//end if statement
				
			}//end for loop
			
		}//end while loop
		
		br.close();
		fr.close();
		
		return(minHashSig);
		
	}
	
	/**
	 * 
	 * @param file1 the name of the first file
	 * @param file2 the name of the second file
	 * @return the approximate jaccard similarity of the given file
	 * @throws IOException 
	 */
	public double approximateJaccard(String file1, String file2) throws IOException {
		
		int[] minHashSig1 = minHashSig(file1);
		int[] minHashSig2 = minHashSig(file2);
		
		int result = 0;
		for (int i=0; i < numPermutations; i++) {
			if (minHashSig1[i] == minHashSig2[i]) result++;
		}
		
		return (result * 1.0) / (numPermutations * 1.0);
		
	}
	
	/**
	 * 
	 * @return the minhash matrix of the collection
	 * @throws IOException 
	 */
	public int[][] minHashMatrix() throws IOException{
		
		File[] files = this.folder.listFiles();
		
		int[][] matrix = new int [files.length][numPermutations];
		
		int[] minHashSig;
		for(int i = 0; i < files.length; i++) {
			
			if(files[i].isFile() == true) {
				
				minHashSig = minHashSig(files[i].getName());
				
				for(int j = 0; j < numPermutations; j++) {
					matrix[i][j] = minHashSig[j];
				}
				
			}//end if statement
			
		}//end for loop
		
		return matrix;
		
	}
	
	/**
	 * 
	 * @return the number of terms in the document collection
	 */
	public int numTerms() {
		
		return termNum;
		
	}
	
	/**
	 * 
	 * @return the number of permutations used to construct the minhash matrix
	 */
	public int numPermutations() {
		
		return numPermutations;
		
	}
	
	/////////////////////////PRIVATE HELPER FUNCTIONS////////////////////////////////////
	
	/**
	 * method to calculate approximate jacc of two signatures rather than two strings
	 * @return
	 */
	public double calcApproxJacc(int[] sig1, int[] sig2) {
		
		int result = 0;
		
		for(int i = 0; i < numPermutations; i++) {
			if(sig1[i] == sig2[i]) result++;
		}
		
		return (result * 1.0) / (numPermutations * 1.0);
		
	}
	
	/**
	 * 
	 * @return the number of  terms in the document list
	 * @throws IOException 
	 */
	private int calcNumTerms() throws IOException {
		
		Set<String> terms = new HashSet<String>();
		File[] files = this.folder.listFiles();
		FileReader fr;
		BufferedReader br;
		
		for(int i = 0; i < files.length; i++) {
			
			if(files[i].isFile() == true) {
				
				fr = new FileReader(files[i]);
				br = new BufferedReader(fr);
				
				String line = "";
				String[] termList;
				while((line = br.readLine()) != null) {
					
					line = line.replaceAll("[.,:;']", "");
					line = line.toLowerCase();
					termList = line.split("\\s+");
					
					for(int j = 0; j < termList.length; j++) {

						if(termList[j] != "the" && termList[j].length() > 2) terms.add(termList[j]);
						
					}
					
				}//end while loop
				
				br.close();
				fr.close();
				
			}//end if statement
			
		}//end for loop
		
		return terms.size();
		
	}
	
	private Set<String> getTermList(String file) throws IOException{
		
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		
		Set<String> terms = new HashSet<String>();
		
		String line = "";
		String[] termList;
		while((line = br.readLine()) != null) {
			
			line = line.replaceAll("[.,:;']", "");
			line = line.toLowerCase();
			termList = line.split("\\s+");
			
			for(int j = 0; j < termList.length; j++) {

				if(termList[j] != "the" && termList[j].length() > 2) terms.add(termList[j]);
				
			}
		}
		
		br.close();
		fr.close();
		
		return(terms);
		
	}
	
	/**
	 *  Get the first prime number who's value is >= range.
	 *  @param range The range of the prime numbers.
	 *  @return The prime number found within the range.
	 */
	private int getPrime(int range){
		
		//first prime number
		int x = range;
		
		while(x<Integer.MAX_VALUE){
			int count = 0;
		    //increase count each time x is divisible by a whole number <= x
		    for(int j = 1; j <= x; j++){
		      	if(x%j == 0) count++;
		    }
		    //if x is prime return
		    if(count == 2) return x;
	        x++;
	    }
		return -1;
	}

}
