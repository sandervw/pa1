/**
 * @author Sander VanWilligen
 */

package pa1;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.Random;

public class BloomFilterRan {
	
	private BitSet filter;
	private int filterSize, dataSize, numHashes, prime, a, b;
	
	private BigInteger bigFilterSize;
	
	public BloomFilterRan(int setSize, int bitsPerElement){
		
		this.filterSize = setSize*bitsPerElement;
		
		prime = this.getPrime(filterSize);
		if (prime<0) prime = prime*-1;
		Random rn = new Random();
		this.b = rn.nextInt(prime);
		this.a = rn.nextInt(prime);
		
		bigFilterSize = new BigInteger(filterSize+"");
		this.dataSize = 0;
		double temp = (filterSize/setSize)*Math.log(2);
		this.numHashes = (int)Math.ceil(temp);
		filter = new BitSet(filterSize);
		
	}
	
	public void add(String s){
		
		s = s.toLowerCase();
		
		if(!this.appears(s)) {
			BigInteger hashRan1 = hashRan(s);
			BigInteger hashRan2 = hashRan(hashRan1.toString());
			int result;
			for(int i=0; i<numHashes; i++){
				result = hash(i, hashRan1, hashRan2).intValue();
				filter.set(result);
			}
			dataSize++;
		}
			
	}
	
	public boolean appears(String s){
		
		s = s.toLowerCase();
		
		BigInteger hashRan1 = hashRan(s);
		BigInteger hashRan2 = hashRan(hashRan1.toString());
		int result;
		for(int i=0; i<numHashes; i++){
			result = hash(i, hashRan1, hashRan2).intValue();
			if(!filter.get(result)) return false;
		}
		return true;
	}
	
	public int filterSize(){
		return filterSize;
	}
	
	public int dataSize(){
		return dataSize;
	}
	
	public int numHashes(){
		return numHashes;
	}
	
	//hash function, where index is the index of the hash function
	private BigInteger hash(int index, BigInteger hash1, BigInteger hash2){
		
		BigInteger result = hash1.add(hash2);
		BigInteger bigIndex = new BigInteger(index+"");
		result = result.multiply(bigIndex);
		result = result.mod(bigFilterSize);
		return result;
		
	}
	
	//returns the Ran hash of a string
	private BigInteger hashRan(String s){
		
		int x = s.hashCode();
		int hash = (this.a * x + this.b) % this.prime;
		BigInteger result = new BigInteger(hash+"");
		return result;
		
	}
	
	/**
	 *  Get the first prime number who's value is >= range.
	 *  @param range The range of the prime numbers.
	 *  @return The prime number found within the range.
	 */
	private int getPrime(int range){
		
		//first prime number
		int x = range;
		
		while(x<Integer.MAX_VALUE){
			int count = 0;
		    //increase count each time x is divisible by a whole number <= x
		    for(int j = 1; j <= x; j++){
		      	if(x%j == 0) count++;
		    }
		    //if x is prime return
		    if(count == 2) return x;
	        x++;
	    }
		return -1;
	}

}
