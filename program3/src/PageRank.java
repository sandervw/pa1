/**
 * 
 * @author Sander VanWilligen
 * 
 * This class has methods to compute page rank of nodes/pages of a web graph
 */
public class PageRank {
	
	/**
	 * Constructor for PageRank class
	 * Each vertex is a string, each edge appears once, no self loops
	 * @param fileName the name of the file containing the edges of the graph
	 * @param e the approximate parameter for PageRank
	 */
	public PageRank(String fileName, double e){
		
		//TODO
		
	}
	
	/**
	 * return page rank of given vertex
	 * @param name name of the vertex to get page rank of
	 * @return double representing the rank of the vertex
	 */
	public double pageRankOf(String name){
		
		//TODO
		
	}
	
	/**
	 * return out degree of the given vertex
	 * @param name name of the vertex to get out degree of
	 * @return int representing the out degree of the given vertex
	 */
	public int outDegreeOf(String name){
		
		//TODO
		
	}
	
	/**
	 * return in degree of the given vertex
	 * @param name name of the vertex to get in degree of
	 * @return int representing the in degree of the given vertex
	 */
	public int inDegreeOf(String name){
		
		//TODO
		
	}
	
	/**
	 * method to get the number of edges in the graph
	 * @return numEdges int representing the number of edges in the graph
	 */
	public int numEdges(){
		
		//TODO
		
	}
	
	/**
	 * method to return top k pages by page rank (where k is an integer)
	 * @param k the number of pages to return
	 * @return a string array representing the top k pages by page rank
	 */
	public String[] topKPageRank(int k){
		
		//TODO
		
	}
	
	/**
	 * method to return top k pages by in degree (where k is an integer)
	 * @param k the number of pages to return
	 * @return a string array representing the top k pages by in degree
	 */
	public String[] topKInDegree(int k){
		
		//TODO
		
	}
	
	/**
	 * method to return top k pages by out degree (where k is an integer)
	 * @param k the number of pages to return
	 * @return a string array representing the top k pages by out degree
	 */
	public String[] topKOutDegree(int k){
		
		//TODO
		
	}

}
