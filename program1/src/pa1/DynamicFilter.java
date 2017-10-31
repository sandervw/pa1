/**
 * @author Sander VanWilligen
 */

package pa1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

public class DynamicFilter {
	
	
	private ArrayList<BitSet> filterList;
	private ArrayList<Integer> filterSize, dataSize, prime, a, b;
	private int numHashes, sizeMultiplier, bitsPerElement;
	
	private ArrayList<BigInteger> bigFilterSize;
	
	public DynamicFilter(int l){
		
		filterSize = new ArrayList<Integer>();
		dataSize = new ArrayList<Integer>();
		prime = new ArrayList<Integer>();
		a = new ArrayList<Integer>();
		b = new ArrayList<Integer>();
		
		filterSize.add(1000*l);
		this.sizeMultiplier = 1;
		this.bitsPerElement = l;
		
		prime.add(this.getPrime(filterSize.get(0)));
		if (prime.get(0)<0) prime.set(0, prime.get(0)*-1);
		Random rn = new Random();
		a.add(rn.nextInt(prime.get(0)));
		b.add(rn.nextInt(prime.get(0)));
		
		filterList = new ArrayList<BitSet>();
		bigFilterSize = new ArrayList<BigInteger>();
		
		bigFilterSize.add(new BigInteger(filterSize.get(0)+""));
		dataSize.add(0);
		double temp = l*Math.log(2);
		this.numHashes = (int)Math.ceil(temp);
		filterList.add(new BitSet(filterSize.get(0)));
		
	}
	
	public void add(String s){
		
		s = s.toLowerCase();
		
		if(!this.appears(s)) {
			BigInteger hashDynamic1 = hashDynamic(s, filterList.size()-1);
			BigInteger hashDynamic2 = hashDynamic(hashDynamic1.toString(), filterList.size()-1);
			int result;
			for(int i=0; i<numHashes; i++){
				result = hash(i, hashDynamic1, hashDynamic2, filterList.size()-1).intValue();
				filterList.get(filterList.size()-1).set(result);
			}
			dataSize.set(filterList.size()-1, dataSize.get(filterList.size()-1)+1);
		}
		
		//if we have reached our max in the current filter, increase the size
		if(dataSize.get(filterList.size()-1)>=sizeMultiplier*1000) {
			
			sizeMultiplier*=2;
			
			filterSize.add(sizeMultiplier*1000*bitsPerElement);
			
			prime.add(this.getPrime(filterSize.get(filterList.size())));
			if (prime.get(filterList.size())<0) prime.set(filterList.size(), prime.get(filterList.size())*-1);
			Random rn = new Random();
			a.add(rn.nextInt(prime.get(filterList.size())));
			b.add(rn.nextInt(prime.get(filterList.size())));
			
			bigFilterSize.add(new BigInteger(filterSize.get(filterList.size())+""));
			dataSize.add(0);
			filterList.add(new BitSet(filterSize.get(filterSize.size()-1)));
			
			
		}
			
	}
	
	public boolean appears(String s){
		
		s = s.toLowerCase();
		for(int i=0; i<filterList.size(); i++) {
			BigInteger hashDynamic1 = hashDynamic(s, i);
			BigInteger hashDynamic2 = hashDynamic(hashDynamic1.toString(), i);
			int result;
			int numTrue = 0;
			for(int j=0; j<numHashes; j++){
				result = hash(j, hashDynamic1, hashDynamic2, i).intValue();
				if(filterList.get(i).get(result)) numTrue++;
			}
			if (numTrue == numHashes) return true;
		}
		return false;
		
	}
	
	public int filterSize(){
		int sizeSum = 0;
		for(int i=0; i< filterList.size(); i++) {
			sizeSum+=filterSize.get(i);
		}
		return sizeSum;
	}
	
	public int dataSize(){
		int sizeSum = 0;
		for(int i=0; i< filterList.size(); i++) {
			sizeSum+=dataSize.get(i);
		}
		return sizeSum;
	}
	
	public int numHashes(){
		return numHashes;
	}
	
	//hash function, where index is the index of the hash function
	private BigInteger hash(int index, BigInteger hash1, BigInteger hash2, int filterIndex){
		
		BigInteger result = hash1.add(hash2);
		BigInteger bigIndex = new BigInteger(index+"");
		result = result.multiply(bigIndex);
		result = result.mod(bigFilterSize.get(filterIndex));
		return result;
		
	}
	
	//returns the Dynamic hash of a string
	private BigInteger hashDynamic(String s, int filterIndex){
		
		int x = s.hashCode();
		int hash = (a.get(filterIndex) * x + b.get(filterIndex)) % prime.get(filterIndex);
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
