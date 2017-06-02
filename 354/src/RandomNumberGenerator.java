import java.util.Random;

public class RandomNumberGenerator {

	public static boolean flipACoin(){
		
		Random flip = new Random();
		
		int lands = flip.nextInt(2);
		
		if (lands == 0){
			return false;
		}
				
		return true;
		
	}
	
}