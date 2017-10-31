/**
 * @author Sander VanWilligen
 */

package pa1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class BloomJoin {
	
	private Hashtable<String, String> relation1;
	private BloomFilterMurmur relationFilter;
	
	private ArrayList<String> relation2Keys, relation2Values;
	
	
	public BloomJoin(String r1, String r2) throws IOException{
		
		String line = null;
		String key = null;
		String value = null;
		StringTokenizer st = null;
		relation2Keys = new ArrayList<String>();
		relation2Values = new ArrayList<String>();
		relation1 = new Hashtable<String, String>();
		relationFilter = new BloomFilterMurmur(4000000, 16);
		
		FileReader fileReader =  new FileReader(r2);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		
		while((line = bufferedReader.readLine()) != null) {
			st = new StringTokenizer(line);
			relation2Keys.add(st.nextToken());
			relation2Values.add(st.nextToken());
        }
		
		fileReader =  new FileReader(r1);
		bufferedReader = new BufferedReader(fileReader);
		while((line = bufferedReader.readLine()) != null) {
			st = new StringTokenizer(line);
			key = st.nextToken();
			value = st.nextToken();
			relation1.put(key, value);
			relationFilter.add(key);
        }
		
        bufferedReader.close();
        fileReader.close();
		
	}
	
	public void join(String r3) throws IOException {
		
		FileWriter fw = new FileWriter(r3);
		BufferedWriter bufferedWriter = new BufferedWriter(fw);
		String writeLine = "";
		
		for(int i=0; i<relation2Keys.size(); i++) {
			
			if(relationFilter.appears(relation2Keys.get(i))) {
				writeLine = relation1.get(relation2Keys.get(i)) + "\t" + relation2Keys.get(i) + "\t" + relation2Values.get(i);
				bufferedWriter.write(writeLine);
				bufferedWriter.newLine();
				
			}
			
		}
		
		bufferedWriter.close();
		fw.close();
		
	}
	
}
