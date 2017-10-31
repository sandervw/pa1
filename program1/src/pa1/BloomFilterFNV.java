/**
 * @author Sander VanWilligen
 */

package pa1;

import java.math.BigInteger;
import java.util.BitSet;

public class BloomFilterFNV {
	
	private static final BigInteger FNV64PRIME = new BigInteger("109951168211");
	private static final BigInteger FNV64INIT = new BigInteger("4695981039346656037");
	private static final BigInteger MOD64 = new BigInteger("2").pow(64);
	private BigInteger bigFilterSize;
	
	private BitSet filter;
	private int filterSize, dataSize, numHashes;
	
	public BloomFilterFNV(int setSize, int bitsPerElement){
		this.filterSize = setSize*bitsPerElement;
		bigFilterSize = new BigInteger(filterSize+"");
		this.dataSize = 0;
		double temp = (filterSize/setSize)*Math.log(2);
		this.numHashes = (int)Math.ceil(temp);
		filter = new BitSet(filterSize);
	}
	
	public void add(String s){
		
		s = s.toLowerCase();
		
		if(!this.appears(s)) {
			BigInteger hashFNV1 = hashFNV(s);
			BigInteger hashFNV2 = hashFNV(hashFNV1.toString());
			int result;
			for(int i=0; i<numHashes; i++){
				result = hash(i, hashFNV1, hashFNV2).intValue();
				filter.set(result);
			}
			dataSize++;
		}
			
	}
	
	public boolean appears(String s){
		
		s = s.toLowerCase();
		
		BigInteger hashFNV1 = hashFNV(s);
		BigInteger hashFNV2 = hashFNV(hashFNV1.toString());
		int result;
		for(int i=0; i<numHashes; i++){
			result = hash(i, hashFNV1, hashFNV2).intValue();
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
	
	//returns the FNV hash of a string
	private BigInteger hashFNV(String s){
		
		BigInteger h = FNV64INIT;
		byte[] data = s.getBytes();
		for (byte b : data) {
			h = h.multiply(FNV64PRIME).mod(MOD64);
			h = h.xor(BigInteger.valueOf((int) b & 0xff));
		}
		return h;
		
	}

}
