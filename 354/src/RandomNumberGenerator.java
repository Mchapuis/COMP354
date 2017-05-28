import java.util.Random;

public class RandomNumberGenerator {

	public static boolean flipACoin(){
		
		Random flip = new Random();
		
		int lands = flip.nextInt(2);
		
		if (lands == 1){
			/*System.out.println("Tails");*/
			return false;
		}
				
		/*else if (lands == 0){
			System.out.println("Heads");
			return true;
		}*/
		/*System.out.println("Heads");*/
		return true;
		
	}
	
	/*public static void main(String[]args){
		
		int heads =0;
		int tails = 0;
		
		for (int i=0;i<200;i++){
			boolean result = flipACoin();
			if (result == true){
				heads++;
			}
			if (result == false){
				tails++;
			}
		}
	
		System.out.println("Number of heads: "+heads);
		System.out.println("Number of tails: " + tails);
		
	}*/
	
}