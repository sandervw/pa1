/**
 * @author Sander VanWilligen
 */

package pa1;

import java.math.BigInteger;
import java.util.BitSet;

public class BloomFilterMurmur {
	
	private BitSet filter;
	private int filterSize, dataSize, numHashes;
	
	private BigInteger bigFilterSize;
	
	public BloomFilterMurmur(int setSize, int bitsPerElement){
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
			BigInteger hashMurmur1 = hashMurmur(s);
			BigInteger hashMurmur2 = hashMurmur(hashMurmur1.toString());
			int result;
			for(int i=0; i<numHashes; i++){
				result = hash(i, hashMurmur1, hashMurmur2).intValue();
				filter.set(result);
			}
			dataSize++;
		}
			
	}
	
	public boolean appears(String s){
		
		s = s.toLowerCase();
		
		BigInteger hashMurmur1 = hashMurmur(s);
		BigInteger hashMurmur2 = hashMurmur(hashMurmur1.toString());
		int result;
		for(int i=0; i<numHashes; i++){
			result = hash(i, hashMurmur1, hashMurmur2).intValue();
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
	
	//returns the Murmur hash of a string
	private BigInteger hashMurmur(String s){
		
		byte[] data = s.getBytes();
		int length = s.length();
		int seed = 0xe17a1465;
		
		final long m = 0xc6a4a7935bd1e995L;
		final int r = 47;

		long h = (seed&0xffffffffl)^(length*m);

		int length8 = length/8;

		for (int i=0; i<length8; i++) {
			final int i8 = i*8;
			long k =  ((long)data[i8+0]&0xff)      +(((long)data[i8+1]&0xff)<<8)
					+(((long)data[i8+2]&0xff)<<16) +(((long)data[i8+3]&0xff)<<24)
					+(((long)data[i8+4]&0xff)<<32) +(((long)data[i8+5]&0xff)<<40)
					+(((long)data[i8+6]&0xff)<<48) +(((long)data[i8+7]&0xff)<<56);
			
			k *= m;
			k ^= k >>> r;
			k *= m;
			
			h ^= k;
			h *= m; 
		}
		
		switch (length%8) {
		case 7: h ^= (long)(data[(length&~7)+6]&0xff) << 48;
		case 6: h ^= (long)(data[(length&~7)+5]&0xff) << 40;
		case 5: h ^= (long)(data[(length&~7)+4]&0xff) << 32;
		case 4: h ^= (long)(data[(length&~7)+3]&0xff) << 24;
		case 3: h ^= (long)(data[(length&~7)+2]&0xff) << 16;
		case 2: h ^= (long)(data[(length&~7)+1]&0xff) << 8;
		case 1: h ^= (long)(data[length&~7]&0xff);
		        h *= m;
		};
	 
		h ^= h >>> r;
		h *= m;
		h ^= h >>> r;

		return new BigInteger(h+"");
		
		
	}


}
