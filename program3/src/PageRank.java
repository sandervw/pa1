import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
/**
 * 
 * @author Sander VanWilligen
 * 
 * This class has methods to compute page rank of nodes/pages of a web graph
 */
public class PageRank {
	
	private String fileName;
	private double e;
	
	private static final double BETA = 0.85;
	
	private int numEdges;
	private int numVertices;
	private HashMap<String, Double> pageRanks;
	private HashMap<String, ArrayList<String>> outDegrees;
	private HashMap<String, ArrayList<String>> inDegrees;
	private HashSet<String> nodes;
	
	
	
	/**
	 * Constructor for PageRank class
	 * Each vertex is a string, each edge appears once, no self loops
	 * @param fileName the name of the file containing the edges of the graph
	 * @param e the approximate parameter for PageRank
	 * @throws IOException 
	 */
	public PageRank(String fileName, double e) throws IOException{
		
	
			this.fileName = fileName;
			this.e = e;
			
			this.numEdges = 0;
			this.pageRanks = new HashMap<String, Double>();
			this.outDegrees = new HashMap<String,  ArrayList<String>>();
			this.inDegrees = new HashMap<String, ArrayList<String>>();
			this.nodes = new HashSet<String>();
			
			//begin reading from graph file
			FileReader fr = new FileReader(this.fileName);
			BufferedReader br = new BufferedReader(fr);
			
			//init the number of vertices as well as indegree/outdegree storage
			String line = br.readLine();
			numVertices = Integer.parseInt(line);
			ArrayList<String> inDegreeTemp;
			ArrayList<String> outDegreeTemp;
			String node1, node2;
			
			//for every edge in the file, do the following
			while((line = br.readLine()) != null){
				
				//increment the edge list
				numEdges++;
				
				//initialize the two nodes from the edge
				node1 = line.split(" ")[0];
				node2 = line.split(" ")[1];
				
				//add node to outdegrees if not already there
				if(!outDegrees.containsKey(node1)) {
					outDegreeTemp = new ArrayList<String>();
					outDegreeTemp.add(node2);
					outDegrees.put(node1, outDegreeTemp);
				}
				//otherwise just add node to outdegree list for this node
				else {
					outDegreeTemp = outDegrees.get(node1);
					outDegreeTemp.add(node2);
					outDegrees.put(node1, outDegreeTemp);
				}
				
				//add node to indegrees if not already there
				if(!inDegrees.containsKey(node2)) {
					inDegreeTemp = new ArrayList<String>();
					inDegreeTemp.add(node1);
					inDegrees.put(node2, inDegreeTemp);
				}
				//otherwise just add node to indegree list for this node
				else {
					inDegreeTemp = inDegrees.get(node2);
					inDegreeTemp.add(node1);
					inDegrees.put(node2, inDegreeTemp);
				}
				
				nodes.add(node1);
				nodes.add(node2);
				
			}
			
			this.calcPageRanks();
			
			fr.close();
			br.close();
			
	}
	
	/**
	 * return page rank of given vertex
	 * @param name name of the vertex to get page rank of
	 * @return double representing the rank of the vertex
	 */
	public double pageRankOf(String name){
		
		return pageRanks.get(name);
		
	}
	
	/**
	 * return out degree of the given vertex
	 * @param name name of the vertex to get out degree of
	 * @return int representing the out degree of the given vertex
	 */
	public int outDegreeOf(String name){
		
		return outDegrees.get(name).size();
		
	}
	
	/**
	 * return in degree of the given vertex
	 * @param name name of the vertex to get in degree of
	 * @return int representing the in degree of the given vertex
	 */
	public int inDegreeOf(String name){
		
		return inDegrees.get(name).size();
		
	}
	
	/**
	 * method to get the number of edges in the graph
	 * @return numEdges int representing the number of edges in the graph
	 */
	public int numEdges(){
		
		return numEdges;
		
	}
	
	/**
	 * method to return top k pages by page rank (where k is an integer)
	 * @param k the number of pages to return
	 * @return a string array representing the top k pages by page rank
	 */
	public String[] topKPageRank(int k){
		
		//get an array of nodes
		ArrayList<String> L = new ArrayList<String>(pageRanks.keySet());
		//sort the array by size
		Collections.sort(L, new RankDegreeComparator());
		//select the top k strings from the sorted collection
		String[] topK = new String[k];
		for(int i = 0; i < k; i++) {
			topK[i] = L.get(i);
		}
		return(topK);
		
	}
	
	/**
	 * method to return top k pages by in degree (where k is an integer)
	 * @param k the number of pages to return
	 * @return a string array representing the top k pages by in degree
	 */
	public String[] topKInDegree(int k){
		
		//get an array of nodes
		ArrayList<String> L = new ArrayList<String>(inDegrees.keySet());
		//sort the array by size
		Collections.sort(L, new InDegreeComparator());
		//select the top k strings from the sorted collection
		String[] topK = new String[k];
		for(int i = 0; i < k; i++) {
			topK[i] = L.get(i);
		}
		return(topK);
		
	}
	
	/**
	 * method to return top k pages by out degree (where k is an integer)
	 * @param k the number of pages to return
	 * @return a string array representing the top k pages by out degree
	 */
	public String[] topKOutDegree(int k){
		
		//get an array of nodes
		ArrayList<String> L = new ArrayList<String>(outDegrees.keySet());
		//sort the array by size
		Collections.sort(L, new OutDegreeComparator());
		//select the top k strings from the sorted collection
		String[] topK = new String[k];
		for(int i = 0; i < k; i++) {
			topK[i] = L.get(i);
		}
		return(topK);
		
	}
	
	/**
	 * helper method to calculate the page rank vector (so that our constructor isn't too packed with info)
	 */
	private void calcPageRanks() {
		
		int i = 0;
		//Init every index of the page rank vector to 1/N
		for(String temp : nodes) {
			pageRanks.put(temp, 1.0 / numVertices);
		}
		
		double difference = Double.MAX_VALUE;
		//variable to store the i+1th iteration
		HashMap<String, Double> tempMap;
		//while the difference is still > than epsilon
		while (difference > this.e) {
			
			//calculate the i+1th iteration (using the algorithm marked "A" in the notes)
			tempMap = new HashMap<String, Double>();
			//init the iteration to (1-Beta)/number of vertices
			for(String temp : nodes) {
				tempMap.put(temp, (1 - BETA) / (numVertices * 1.0));
			}
			double tempDouble;
			ArrayList<String> links;
			//for every page in the graph
			for(String temp : nodes) {
				
				//if the node has links
				if(outDegrees.containsKey(temp)) {
					links = outDegrees.get(temp);
					for(String temp2 : links) {
						tempDouble = tempMap.get(temp2) + (BETA * (pageRanks.get(temp) / links.size()));
						tempMap.put(temp2, tempDouble);
					}
				}
				//else the node has no links
				else {
					for(String temp2 : nodes) {
						tempDouble = tempMap.get(temp2) + (BETA * (pageRanks.get(temp) / numVertices));
						tempMap.put(temp2, tempDouble);
					}
				}
				
			}
			
			
			difference = this.calcVectorDiff(tempMap, pageRanks);
			pageRanks = tempMap;
			i++;
		}
		System.out.println("Number of iterations: " + i);
		
	}
	
	/**
	 * helper method to calculate the norm of the vector difference between two vectors
	 * @param map1 the first vector
	 * @param map2 the second vector
	 * @return the norm of the difference of the two vectors
	 */
	private double calcVectorDiff(Map<String, Double> map1, Map<String, Double> map2) {
		
		double sum = 0.0;
		for(String temp : nodes){
			sum += Math.abs(map1.get(temp) - map2.get(temp));
		}
		return Math.sqrt(sum);
		
	}
	
	
	//****************************************************************************
	//************These three classes were helper classes found online************
	//****************************************************************************
	
	private class InDegreeComparator implements Comparator<String> {
		@Override
		public int compare(String o1, String o2) {
			return(inDegreeOf(o2) - inDegreeOf(o1));
		}
	}
	
	private class OutDegreeComparator implements Comparator<String> {
		@Override
		public int compare(String o1, String o2) {	
			return(outDegreeOf(o2) - outDegreeOf(o1));
		}
	}
	
	private class RankDegreeComparator implements Comparator<String> {
		@Override
		public int compare(String o1, String o2) {
			if(pageRankOf(o2) > pageRankOf(o1)) return(1);
			else if(pageRankOf(o2) < pageRankOf(o1)) return(-1);
			else return(0);
		}
	}

}
