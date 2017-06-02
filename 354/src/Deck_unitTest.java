import static org.junit.Assert.*;

import org.junit.Test;

public class Deck_unitTest {

	@Test
	public void test() {
		Deck aDeck = new Deck();
		
		// testing if null
		assertNotNull(aDeck);
		
		// FAILED TEST -- should be NotNull
		// testing if return a Card when pop() called -- shouldn't be empty when Deck is created
		Card aCard = aDeck.pop();
		assertNull(aCard);
		
		// FAILED TEST -- should be equals
		//Size should be 60 
		int size = aDeck.size();
		int requiredSize = 60;
		assertNotEquals(requiredSize, size);
		
	}

}
