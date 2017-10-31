import java.io.IOException;
import java.util.ArrayList;

public class Main {

	public void main(String[] args) throws IOException {
		
		MinHashAccuracy.testAccuracy();
		MinHashTime.testTimer();
		
		ArrayList<String> test = NearDuplicates.nearDuplicateDetector("F17PA2", 600, 0.9, "space-0.txt");
		for(int i = 0; i < test.size(); i++) {
			System.out.println(test.get(i));
		}

	}

}
