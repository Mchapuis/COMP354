import static org.junit.Assert.*;

import org.junit.Test;

public class RandomNumberGen_JUnit {

	@Test
	public void test() {
		RandomNumberGenerator rand = new RandomNumberGenerator();
		
		// test if the function return true or false
		// 0 is true and 1 is false
		boolean b_flip = rand.flipACoin();
		if(b_flip) assertTrue(b_flip);
		else if(!b_flip) assertFalse(b_flip);
		
	}

}
