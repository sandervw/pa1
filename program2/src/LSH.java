import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 
 * @author Sander VanWilligen
 * 
 * This class implements locality sensitive hashing to detect near duplicates of a document.
 * given a K * N MinHash matrix M, we perform locality sensitive hashing as follows:
 *   - For a given b, divide the rows of M into b bands each consisting of r = k/b rows
 *   - create b hash tables
 *   - for each document, let sig be its minHash signature which is an array of k integers
 *   - divide sig into b bands (each band has r entries), compute hash value of each band
 *   - of jth band of sig is hashed to t, store the document (name) Di at Tj[t]
 */
public class LSH {
	
	private int[][] minHashMatrix;
	private String[] docNames;
	private int bands;
	
	private int rowsPerBand; //number of rows per band
	
	private ArrayList<HashMap<Integer, String>> hashMaps; //hash maps
	
	//vars for hash function ax + b % p
	int a, b, p;
	
	/**
	 * 
	 * @param minHashMatrix the MinHash matrix of the document collection
	 * @param docNames an array of Strings consisting of names of documents in the document collection
	 * @param bands the number of bands to be used to perform locality sensitive hashing
	 */
	public LSH(int [][] minHashMatrix, String[] docNames, int bands) {
		
		this.minHashMatrix = minHashMatrix;
		this.docNames = docNames;
		this.bands = bands;
		
		//get number of rows per band
		this.rowsPerBand = (int)Math.ceil(minHashMatrix[0].length / bands * 1.0);
		
		//initialize the hashtables
		this.hashMaps = new ArrayList<HashMap<Integer, String>>();
		for(int i = 0; i < bands; i++) {
			HashMap<Integer, String> tempMap = new HashMap<Integer, String>();
			hashMaps.add(tempMap);
		}
		
		//initialize the hash function
		Random rand = new Random();
		p = getPrime(8*minHashMatrix.length);
		a = rand.nextInt(p);
		b = rand.nextInt(p);
		
		int result = 1;
		//for all documents
		for(int i = 0; i < minHashMatrix.length; i++) {
			
			//for each band
			for(int j = 0; j < bands; j++) {
				
				result = 1;
				//for each row in a band, hash the value
				for(int k = j*rowsPerBand; (k < (j*rowsPerBand + rowsPerBand)) && k < minHashMatrix[0].length; k++) {
					result = (result + (a * minHashMatrix[i][k] + b)) % p;
				}
				//if the hashMap doesn't already have the key, just add it
				if(!hashMaps.get(j).containsKey(result)) {
					hashMaps.get(j).put(result, docNames[i]);
				}
				//otherwise, append the name to a list of names
				else {
					String temp = hashMaps.get(j).get(result);
					hashMaps.get(j).put(result, temp + "," + docNames[i]);
				}
				
			}
			
		}
		
		
		
	}
	
	/**
	 * 
	 * @param docName the document name to get near duplicates of
	 * @return an arraylist containing the names of near duplicate documents
	 */
	public ArrayList<String> nearDuplicatesOf (String docName) {
		
		Set<String> tempSet = new HashSet<String>(); //need a set to avoid storing duplicates
		ArrayList<String> results = new ArrayList<String>();
		
		int index = 0;
		for(int i = 0; i < minHashMatrix.length; i++) {
			if(docNames[i].equals(docName)) {
				index = i;
			}
		}
		
		int result;
		String[] tempList;
		//for each band
		for(int j = 0; j < bands; j++) {
			
			result = 1;
			//for each row in a band
			for(int k = j*rowsPerBand; k < j*rowsPerBand + rowsPerBand && k < minHashMatrix[0].length; k++) {
				result = (result + (a * minHashMatrix[index][k] + b)) % p;
			}
			if(hashMaps.get(j).containsKey(result)) {
				tempList =  hashMaps.get(j).get(result).split(",");
				tempSet.addAll(Arrays.asList(tempList));
			}
			
		}
		
		results.addAll(tempSet);
		return results;
		
	}
	
	//////////////////////////HELPER FUNCTIONS/////////////////////////////////////
	
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
