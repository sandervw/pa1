/**
 * @author Sander VanWilligen
 */

package pa1;

import java.util.ArrayList;
import java.util.Random;

/**
 * 
 * This class tests the false positive rates of the four bloom filters I designed
 * It has only a constructor
 *
 */
public class FalsePositives {
	
	private int stringSize, numStrings, bitsPerElement;
	
	private ArrayList<String> stringList;
	
	Random random;
	StringBuilder sb;
	char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
	
	/**
	 * The constructor creates a list of random strings, none of witch are equal as specified below
	 * @param stringSize the size of the strings to test
	 * @param numStrings the number of strings to make
	 * @param bitsPerElement the number of bits per element passed to the bloom filters
	 */
	public FalsePositives(int stringSize, int numStrings, int bitsPerElement) {
		
		this.stringSize = stringSize;
		this.numStrings = numStrings;
		this.bitsPerElement = bitsPerElement;
		
		random = new Random();
		
		stringList = new ArrayList<String>();
		//generate list of distinct random strings
		int i = 0;
		while(i < this.numStrings) {
			String temp = getRandomString();
			if(!stringList.contains(temp)) {
				stringList.add(temp);
				i++;
			}
		}
		
		//print expected FP rate
		System.out.println("Expected False Positive rate is 0.618^"+this.bitsPerElement+"\t=\t"+Math.pow(0.618, bitsPerElement));
		
		//Initialize the filters
		BloomFilterFNV filterFNV = new BloomFilterFNV(numStrings, bitsPerElement);
		BloomFilterMurmur filterMurmur = new BloomFilterMurmur(numStrings, bitsPerElement);
		BloomFilterRan filterRan = new BloomFilterRan(numStrings, bitsPerElement);
		DynamicFilter filterDynamic = new DynamicFilter(bitsPerElement);
		
		//add the elements to the filters (since they are distinct elements, the size should be = number of strings)
		//the false positive rate = (number of strings - size)/number of strings
		
		for(i=0; i<this.numStrings; i++) {
			filterFNV.add(stringList.get(i));
			filterMurmur.add(stringList.get(i));
			filterRan.add(stringList.get(i));
			filterDynamic.add(stringList.get(i));
		}
		
		System.out.println("FNV False Positive rate\t\t\t=\t"+((numStrings-filterFNV.dataSize())/(numStrings*1.000000)));
		System.out.println("Murmur False Positive rate\t\t=\t"+((numStrings-filterMurmur.dataSize())/(numStrings*1.000000)));
		System.out.println("Ran False Positive rate\t\t\t=\t"+((numStrings-filterRan.dataSize())/(numStrings*1.000000)));
		System.out.println("Dynamic False Positive rate\t\t=\t"+((numStrings-filterDynamic.dataSize())/(numStrings*1.000000)));
		
	}
	
	private String getRandomString() {
		
		sb = new StringBuilder();
		for (int i = 0; i < stringSize; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		return sb.toString();
	}

}
