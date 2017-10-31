import java.io.IOException;

/**
 * @author Sander VanWilligen
 * 
 * This class tests whether it is faster to estimate Jaccard Similarities using MinHash matrix or
 * whether to compute the similarities exactly
 */
public class MinHashTime {

	/**
	 * function to test the accuracy for the space sample provided
	 * @throws IOException 
	 */
	public void testTimer() throws IOException {
		
		timer("space", 600);
		
	}
	
	/**
	 * 
	 * @param folder the folder the files are stored in
	 * @param numPermutations the number of permutations to use
	 * @throws IOException 
	 */
	public void timer(String folder, int numPermutations) throws IOException {
		
		MinHash minHash = new MinHash(folder, numPermutations);
		
		String[] files = minHash.allDocs();
		int num = files.length;
		
		System.out.println("Total Files: " + num + "\n");
		
		long startTime;
		long endTime;
		long totalTime = 0;
		
		startTime = System.currentTimeMillis();
		
		for(int i = 0; i < num; i++) {
			for(int j = i+1; j < num; j++) {
				minHash.exactJaccard(files[i], files[j]);
			}
		}
		
		endTime = System.currentTimeMillis() - startTime;
		
		System.out.println("===========================Exact Timer===============================");
		System.out.println("Exact jaccard time: " + endTime + " ms, or " + (double)endTime/1000.0 + " sec.\n");
		
		System.out.println("===========================Approx Timer==============================");
		startTime = System.currentTimeMillis();
		int[][] minHashMatrix = minHash.minHashMatrix();
		endTime = System.currentTimeMillis() - startTime;
		totalTime+=endTime;
		System.out.println("Matrix compute time: " + endTime + " ms, or " + (double)endTime/1000.0 + " sec.");
		
		startTime = System.currentTimeMillis();
		for(int i = 0; i < num; i++) {
			for(int j = i+1; j < num; j++) {
				minHash.calcApproxJacc(minHashMatrix[i], minHashMatrix[j]);
			}
		}
		endTime = System.currentTimeMillis() - startTime;
		totalTime+=endTime;
		System.out.println("Similarity compute time: " + endTime + " ms, or " + (double)endTime/1000.0 + " sec.");
		
		System.out.println("Total Approx jaccard time: " + totalTime + " ms, or " + (double)totalTime/1000.0 + " sec.");
		
	}
	
	

}
