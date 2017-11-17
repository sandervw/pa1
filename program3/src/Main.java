
public class Main {

	public static void main(String[] args) {
		String[] s1 = {};
		WikiCrawler thing = new WikiCrawler("/wiki/Computer_Science", s1, 100, "WikiTennisGraph.txt", true);
		thing.crawl();
		
	}

}
