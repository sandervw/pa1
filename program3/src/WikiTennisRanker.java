import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class WikiTennisRanker {

	public static void main(String[] args) throws IOException {
		
		
		PageRank rank1 = new PageRank("wikiTennis.txt", .01);
		System.out.println("Top 10 pages by in degree, out degree, and page rank for wikiTennis and eps = 0.01:\n ");
		String[] topPageRanks = rank1.topKPageRank(10);
		String[] topInDegrees = rank1.topKInDegree(10);
		String[] topOutDegrees = rank1.topKOutDegree(10);
		
		HashSet<String> terms1 = new HashSet<String>();
		HashSet<String> terms2 = new HashSet<String>();
		terms1.addAll(Arrays.asList(topPageRanks));
		terms2.addAll(Arrays.asList(topInDegrees));
		terms1.retainAll(terms2);
		int t1AndT2 = terms1.size();
		double PageRank_InDegreeSim = (t1AndT2 * 1.0) / (20 - t1AndT2 * 1.0);
		
		terms1 = new HashSet<String>();
		terms2 = new HashSet<String>();
		terms1.addAll(Arrays.asList(topPageRanks));
		terms2.addAll(Arrays.asList(topOutDegrees));
		terms1.retainAll(terms2);
		t1AndT2 = terms1.size();
		double PageRank_OutDegreeSim = (t1AndT2 * 1.0) / (20 - t1AndT2 * 1.0);
		
		terms1 = new HashSet<String>();
		terms2 = new HashSet<String>();
		terms1.addAll(Arrays.asList(topOutDegrees));
		terms2.addAll(Arrays.asList(topInDegrees));
		terms1.retainAll(terms2);
		t1AndT2 = terms1.size();
		double OutDegree_InDegreeSim = (t1AndT2 * 1.0) / (20 - t1AndT2 * 1.0);
		
		System.out.println("Page Rank:");
		for(int i = 0; i < 10; i++) {
			System.out.println("\t" + topPageRanks[i]);
		}
		System.out.println("In Degree:");
		for(int i = 0; i < 10; i++) {
			System.out.println("\t" + topInDegrees[i]);
		}
		System.out.println("Out Degree:");
		for(int i = 0; i < 10; i++) {
			System.out.println("\t" + topOutDegrees[i]);
		}
		System.out.println("\nPage Rank and In Degree Similarity:\t" + PageRank_InDegreeSim);
		System.out.println("Page Rank and Out Degree Similarity:\t" + PageRank_OutDegreeSim);
		System.out.println("Out Degree and In Degree Similarity:\t" + OutDegree_InDegreeSim);
		
		
		PageRank rank2 = new PageRank("wikiTennis.txt", .005);
		System.out.println("\n\nTop 10 pages by in degree, out degree, and page rank for wikiTennis and eps = 0.005:\n ");
		topPageRanks = rank2.topKPageRank(10);
		topInDegrees = rank2.topKInDegree(10);
		topOutDegrees = rank2.topKOutDegree(10);
		
		terms1 = new HashSet<String>();
		terms2 = new HashSet<String>();
		terms1.addAll(Arrays.asList(topPageRanks));
		terms2.addAll(Arrays.asList(topInDegrees));
		terms1.retainAll(terms2);
		t1AndT2 = terms1.size();
		PageRank_InDegreeSim = (t1AndT2 * 1.0) / (20 - t1AndT2 * 1.0);
		
		terms1 = new HashSet<String>();
		terms2 = new HashSet<String>();
		terms1.addAll(Arrays.asList(topPageRanks));
		terms2.addAll(Arrays.asList(topOutDegrees));
		terms1.retainAll(terms2);
		t1AndT2 = terms1.size();
		PageRank_OutDegreeSim = (t1AndT2 * 1.0) / (20 - t1AndT2 * 1.0);
		
		terms1 = new HashSet<String>();
		terms2 = new HashSet<String>();
		terms1.addAll(Arrays.asList(topOutDegrees));
		terms2.addAll(Arrays.asList(topInDegrees));
		terms1.retainAll(terms2);
		t1AndT2 = terms1.size();
		OutDegree_InDegreeSim = (t1AndT2 * 1.0) / (20 - t1AndT2 * 1.0);
		
		System.out.println("Page Rank:");
		for(int i = 0; i < 10; i++) {
			System.out.println("\t" + topPageRanks[i]);
		}
		System.out.println("In Degree:");
		for(int i = 0; i < 10; i++) {
			System.out.println("\t" + topInDegrees[i]);
		}
		System.out.println("Out Degree:");
		for(int i = 0; i < 10; i++) {
			System.out.println("\t" + topOutDegrees[i]);
		}
		
		System.out.println("\nPage Rank and In Degree Similarity:\t" + PageRank_InDegreeSim);
		System.out.println("Page Rank and Out Degree Similarity:\t" + PageRank_OutDegreeSim);
		System.out.println("Out Degree and In Degree Similarity:\t" + OutDegree_InDegreeSim);

	}

}
