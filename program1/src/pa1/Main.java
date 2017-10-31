package pa1;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		
		/*int numStrings = 200;
		int bitsPerElement = 8;
		
		String[] s1 = {"hello", "goodbye", "testing", "bigtest", "littletest", "ugh", "ihatethis", "sfsfsf", "dogtoy", "dogtoy"};
		
		BloomFilterFNV filterFNV = new BloomFilterFNV(numStrings, bitsPerElement);
		BloomFilterMurmur filterMurmur = new BloomFilterMurmur(numStrings, bitsPerElement);
		BloomFilterRan filterRan = new BloomFilterRan(numStrings, bitsPerElement);
		DynamicFilter filterDynamic = new DynamicFilter(bitsPerElement);
		
		for(int i = 0; i < s1.length; i++) {
			filterDynamic.add(s1[i]);
			filterMurmur.add(s1[i]);
			filterRan.add(s1[i]);
			filterDynamic.add(s1[i]);
		}
		
		System.out.println("Results:");
		System.out.println("FNV item count = " + filterDynamic.dataSize() + ", Contain 'ugh' = " + filterDynamic.appears("ugh")
			+ ", contains 'wheeee' = " + filterDynamic.appears("wheeee"));
		System.out.println("FNV item count = " + filterMurmur.dataSize() + ", Contain 'ugh' = " + filterMurmur.appears("ugh")
		+ ", contains 'wheeee' = " + filterMurmur.appears("wheeee"));
		System.out.println("FNV item count = " + filterRan.dataSize() + ", Contain 'ugh' = " + filterRan.appears("ugh")
		+ ", contains 'wheeee' = " + filterRan.appears("wheeee"));
		System.out.println("FNV item count = " + filterDynamic.dataSize() + ", Contain 'ugh' = " + filterDynamic.appears("ugh")
		+ ", contains 'wheeee' = " + filterDynamic.appears("wheeee"));*/
		
		FalsePositives fp = new FalsePositives(20, 500000, 8);
		
		//BloomJoin b = new BloomJoin("Relation1.txt", "Relation2.txt");
		
		//b.join("relationJoin.txt");
		
		System.out.println("done");

	}

}
