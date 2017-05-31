import static org.junit.Assert.*;

import org.junit.Test;

public class AIplayer_JUnit {

	@Test
	public void test() {
		// this create a cardManager too
		AIPlayer aiPlayer_test = new AIPlayer();
		
		// When an AI call selectActivePokemon() without activating one, there is already an active pokemon.
		// TODO: this should be null in next iterations...
		aiPlayer_test.selectActivePokemon();
		assertNotNull(aiPlayer_test.getActivePokemon());
		
		// TODO: this is not working
		// removing ActivePokemon() and check if we get null
		// aiPlayer_test.cardManager.discardActivePokemon();
		// assertNull(aiPlayer_test.cardManager.getActivePokemon());
		
		
		
		
	}

}
