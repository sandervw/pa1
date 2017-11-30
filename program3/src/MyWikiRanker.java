import java.io.IOException;

public class MyWikiRanker {

	public static void main(String[] args) throws IOException {
		
		//String[] topics = {"String", "Tuning", "Bow"};
		//WikiCrawler w = new WikiCrawler("/wiki/Violin", topics, 500, "MyWikiViolinGraph.txt", true);
		//w.crawl();
		PageRank rank1 = new PageRank("MyWikiViolinGraph.txt", .01);
		System.out.println("Top 20 pages by page rank for graph formed by starting at /wiki/Violin, and crawling 500 pages, ");
		System.out.println("with ['String', 'Bow', 'Tuning'] as the topics, and epsilon of 0.01:");
		String[] topPageRanks = rank1.topKPageRank(20);
		for(int i = 0; i < 20; i++) {
			System.out.println("\t" + topPageRanks[i]);
		}
	}

}
