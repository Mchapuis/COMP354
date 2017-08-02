import static org.junit.Assert.*;

import org.junit.Test;

public class RandomNumberGen_unitTest {

	@Test
	public void test() {

		// test if the function return true or false
		// 0 is true and 1 is false
		boolean b_flip = RandomNumberGenerator.flipACoin();

		// making sure the method is random so it is expected that if the first flip is true 
		// a false will appear at a certain time and vice versa
		if(b_flip) {
			while(b_flip) {
				b_flip = RandomNumberGenerator.flipACoin();
			}
			assertFalse(b_flip);// after a number(doesn't matter how much) of true there is 1 false
		}else {
			while(!b_flip) {
				b_flip= RandomNumberGenerator.flipACoin();
			}
			assertTrue(b_flip);// after a number of false there is 1 true
		}
	}

}
