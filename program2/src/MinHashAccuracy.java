import java.io.IOException;

/**
 * @author Sander VanWilligen
 */
public class MinHashAccuracy {
	
	/**
	 * function to test the accuracy for the space sample provided
	 * @throws IOException 
	 */
	public void testAccuracy() throws IOException {
		
		accuracy("space", 600, 0.04);
		
	}
	
	/**
	 * 
	 * @param folder the folder which the files are stored in
	 * @param numPermutations the number of permutations to use
	 * @param error the error (less than 1, greater than 0)
	 * @return the number of files with an approx and exact jacc difference > error
	 * @throws IOException
	 */
	public int accuracy(String folder, int numPermutations, double error) throws IOException {
		
		MinHash minHash = new MinHash(folder, numPermutations);
		
		int result = 0;
		String[] files = minHash.allDocs();
		int num = files.length;
		int comparisons = 0;
		double temp = 0.0;
		double exactJacc = 0.0;
		double approxJacc = 0.0;
		
		int[][] minHashMatrix = minHash.minHashMatrix();
		
		for(int i = 0; i < num; i++) {
			for(int j = i+1; j < num; j++) {
				exactJacc = minHash.exactJaccard(files[i], files[j]);
				approxJacc = minHash.calcApproxJacc(minHashMatrix[i], minHashMatrix[j]);
				temp = Math.abs(exactJacc - approxJacc);
				if(temp > error) result++;
				comparisons++;
			}
		}
		
		System.out.println("Total Files: " + num);
		System.out.println("Total comparisons: " + comparisons);
		System.out.println("Total num differences: " + result);
		
		return result;
		
	}

}
