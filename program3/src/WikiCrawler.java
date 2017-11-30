import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.SortedSet;
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
	private String[] keywords;
	private int max;
	private String fileName;
	private boolean isWeighted;
	
	private ArrayList<String> edges;
	private SortedSet<Tuple> Q;
	private HashSet<String> nodes;
	private HashSet<String> forbidden;
	
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
		this.keywords = keywords;
		this.max = max;
		this.fileName = fileName;
		this.isWeighted = isWeighted;
		
		//Init variables to store nodes and edges
		this.edges = new ArrayList<String>();
		this.Q = new TreeSet<Tuple>();
		this.nodes = new HashSet<String>();
		this.forbidden = new HashSet<String>();
		
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
		
		this.getForbidden();
		
		nodes.add(seedUrl);
		
		//Create a regex pattern to match everything after the first <p> tag only
		String  pattern = "<p>(.*)";
		Pattern r       = Pattern.compile(pattern);
		String  url     = BASE_URL + seedUrl;
		Matcher m;
		//get first node
		m = r.matcher(getHTML(url));
		m.find();
		String tempUrl = seedUrl;
		ArrayList<Tuple> resultList = extractTuples(m.group(0));
		
		String link;
		for(int i = 0; i < resultList.size(); i++) {
			link = resultList.get(i).getName();
			if (!tempUrl.equals(link)) {
				//Add all the new links to Nodes (If we haven't hit the max)
				if (!nodes.contains(link) && nodes.size() < max && !forbidden.contains(link)) {
					nodes.add(link);
					Q.add(resultList.get(i));
				}
				//Add all the new directions to Edges (if it exists in Nodes)
				if (nodes.contains(link) && !edges.contains(tempUrl + " " + link) && !forbidden.contains(link)) {
					edges.add(tempUrl + " " + link);
				}
			}
		}
		
		Tuple first;
		while(!Q.isEmpty()) {
			//get the highest weighted node first
			first = Q.last();
			Q.remove(first);
			tempUrl = first.getName();
			
			url = BASE_URL + tempUrl;
			m = r.matcher(getHTML(url));
			if(m.find()) {
				resultList = extractTuples(m.group(0));
				
				for(int i = 0; i < resultList.size(); i++) {
					link = resultList.get(i).getName();
					if (!tempUrl.equals(link)) {
						//Add all the new links to Nodes (If we haven't hit the max)
						if (!nodes.contains(link) && nodes.size() < max  && !forbidden.contains(link)) {
							nodes.add(link);
							Q.add(resultList.get(i));
						}
						//Add all the new directions to Edges (if it exists in Nodes)
						if (nodes.contains(link) && !edges.contains(tempUrl + " " + link)  && !forbidden.contains(link)) {
							edges.add(tempUrl + " " + link);
						}
					}
				}
			}
			
		}
		
		//Print the edges to a result file, specified in the constructor.
		PrintWriter out = null;
				
		try {
			out = new PrintWriter(fileName);
			out.println(max);
			for(int i = 0; i < edges.size(); i++){
				out.println(edges.get(i));
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	private void getForbidden() {
		ArrayList<String> temp = extractLinks(getHTML(BASE_URL + "/robots.txt"));
		for(int i = 0; i < temp.size(); i++) {
			this.forbidden.add(temp.get(i));
		}
	}
	
	//**************************************************************************
	//********Some of the methods below are from my 311 wikicrawler code********
	//**************************************************************************
	
	/*
	 * This method gets a string (that represents contents of a .html
	 * page as parameter. This method should return an array list (of Tuples) consisting of 
	 *   links and weights
	 */
	private ArrayList<Tuple> extractTuples(String doc) {
		
		ArrayList<Tuple> results = new ArrayList<Tuple>();
		String pattern = "href=\"(\\/wiki\\/.[^#:]*?)\".*?</a>";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(doc);
		while (m.find()) {
			double weight = 0;
			for(int i = 0; i < keywords.length; i++) {
				if(m.group().contains(keywords[i]) && this.isWeighted) weight = 1;
			}
			Tuple t = new Tuple (m.group(1), weight);
			results.add(t);
		}

		return results;
	}
	
	private ArrayList<String> extractLinks(String doc) {
		
		ArrayList<String> results = new ArrayList<String>();
		String pattern = "(\\/wiki\\/.*?)[D# \\s\\r\\n]";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(doc);
		while (m.find()) {
			results.add(m.group(1));
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
			
			//Timeout for 1 seconds after every 10 requests.
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
	public class Tuple implements Comparable<Tuple>{
		
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

		@Override
		public int compareTo(Tuple t) {
			if (this.weight > t.weight) return 1;
			else if (t.weight > this.weight) return -1;
			else if (this.name.compareTo(t.name) != 0) return this.name.compareTo(t.name);
			else return 0;
		}
		
		@Override
		public boolean equals(Object obj) {
			Tuple t = (Tuple)obj;
			if (this.name.equals(t.name) && this.weight == t.weight) return true;
			return false;
		}
		
	}

}
