import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Sander VanWilligen
 * 
 * This class puts together MinHash and LSH to 
 * detect near duplicates in a document collection.
 */
public class NearDuplicates {
	
	/**
	 * 
	 * @param folder the folder the files are stored in
	 * @param numPermutations number of permutations to use
	 * @param s similarity threshhold
	 * @param docName document name to test
	 * @return
	 * @throws IOException 
	 */
	public ArrayList<String> nearDuplicateDetector(String folder, int numPermutations, double s, String docName) throws IOException {
		
		MinHash minHash = new MinHash(folder, numPermutations);
		
		int[][] matrix = minHash.minHashMatrix();
		String[] files = minHash.allDocs();
		int bands = calculateBands(numPermutations, s);
		LSH lsh = new LSH(matrix, files, bands);
		return lsh.nearDuplicatesOf(docName);
		
	}
	
	/**
	 * 
	 * This helper function was suggested by Xiaochen Yang in the discussion board
	 */
	private int calculateBands(int numofpermutations, double threshold) {
		double reverseofpow = 1/(Math.pow(threshold, numofpermutations));
		for(int bands = 1; ; bands++) {
			if(Math.pow(bands, bands) < reverseofpow && Math.pow(bands+1, bands+1) > reverseofpow) {
				if((reverseofpow - Math.pow(bands, bands)) < (Math.pow(bands+1, bands+1) - reverseofpow)) return bands;
				else return bands+1;
			}
		}
	}

}
