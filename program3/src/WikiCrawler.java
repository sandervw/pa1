import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Sander VanWilligen
 * 
 * This class will have methods that can be used to crawl Wiki.
 * It will do a focused crawling: Only crawl pages that are about a particular topic
 * It will perform a weighted BFS on the web graph and use the described 
 *   mechanism to compute weight of a web page.
 * The crawler will write the discovered graph to a file.
 * It will be crawling only wiki pages.
 */
public class WikiCrawler {
	
	private static final String BASE_URL = "https://en.wikipedia.org";
	private String seedUrl;
	private String[] keyWords;
	private int max;
	private String fileName;
	private boolean isWeighted;
	
	private ArrayList<String> edges;
	private TreeSet<Tuple> Q;
	private HashSet<String> related;
	private HashSet<String> unrelated;
	
	private int connections = 0;
	
	
	/**
	 * The constructor for the crawler
	 * @param seedUrl relative address of the seed url
	 * @param keywords array that contains key words that describe a topic
	 * @param max representing Maximum number sites to be crawled
	 * @param fileName representing name of a le{The graph will be written to this file
	 * @param isWeighted
	 */
	public WikiCrawler(String seedUrl, String[] keywords, int max, String fileName, boolean isWeighted){
		
		//Init basic variables
		this.seedUrl = seedUrl;
		this.keyWords = keyWords;
		this.max = max;
		this.fileName = fileName;
		this.isWeighted = isWeighted;
		
		//Init variables to store nodes and edges
		this.edges = new ArrayList<String>();
		this.Q = new TreeSet<Tuple>();
		this.related = new HashSet<String>();
		this.unrelated = new HashSet<String>();
		
	}
	
	/**
	 * Method to crawl max many pages
	 * If isWeighted is false, then set weight of every link/page to 0
	 * If isWeighted is true, determine the weigh of a page/link via the described heuristic
	 * Crawling done using weighted BFS (or normal BFS if isWeighted = false)
	 * Constructs the web graph over all pages visited by the BFS and write the graph to fileName
	 * Number of vertices = max
	 */
	public void crawl(){
		
		//Create a regex pattern to match everything after the first <p> tag only
		String  pattern = "<p>(.*)";
		Pattern r       = Pattern.compile(pattern);
		String  url     = BASE_URL + seedUrl;
		Matcher m;
		
		m = r.matcher(getHTML(url));
		m.find();
		//Extract all the links for the new page
		ArrayList<String> temp = extractTuples(m.group(0));
		for(int i = 0; i < temp.size(); i++){
			System.out.println(temp.get(i));
		}
		System.out.println("test");
		
	}
	
	//**************************************************************************
	//********Some of the methods below are from my 311 wikicrawler code********
	//**************************************************************************
	
	/*
	 * This method gets a string (that represents contents of a .html
	 * page as parameter. This method should return an array list (of Tuples) consisting of 
	 *   links and weights
	 */
	private ArrayList<String> extractTuples(String doc) {
		
		ArrayList<String> results = new ArrayList<String>();
		String pattern = "<a href=\"\\/wiki\\/(.[^#:]*?)\"";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(doc);
		while (m.find()) {
			//Cut out the directory path and re-add the /wiki/ part. (this should always be a /wiki/ link)
			results.add(m.group());
		}

		return results;
	}
	
	/**
	 * get the html content of a given link
	 * @param urlPath name of the link
	 * @return string representing the html content
	 */
	private String getHTML(String urlPath) {
		try {
			//create the base node url, based on the constructor
			URL url = new URL(urlPath);

			//create an input stream, and read every line of the page into a string
			InputStream is = url.openStream();
			
			//Timeout for 3 seconds after every 100 requests.
			connections++;
			if(connections >= 9){
				TimeUnit.SECONDS.sleep(1);
				System.out.println("Waited 1 seconds after " + (connections+1) + " connections.");
				connections = 0;
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = "";
			String page = "";
			
			while (line != null) {
				line = br.readLine();
				page = page + line;
			}
			
			return page;

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return "failed";
		}
	}
	
	/**
	 * 
	 * @author Sander VanWilligen
	 * 
	 * This is a container class to store a page and its weight
	 */
	public class Tuple{
		
		private String name;
		private double weight;
		
		public Tuple(String name, double weight){
			
			this.name = name;
			this.weight = weight;
			
		}
		
		public String getName(){
			return this.name;
		}
		
		public double getWeight(){
			return this.weight;
		}
		
		public int compareTo(Tuple t){
			if (this.weight > t.weight) return 1;
			else if (t.weight > this.weight) return -1;
			else return 0;
		}
		
	}

}
