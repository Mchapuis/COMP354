import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Test;

public class HumanPlayer_unitTest {

	@Test
	public void test() {
		
		// Creating variables
		HumanPlayer hp_test = new HumanPlayer("deck1.txt");
		
		// Check if Human Player is not null is not null at the start of the game
		assertNotNull(hp_test);
		
		// Check if hand is not null at the start of the game
		ArrayList<Card> testList = hp_test.getHand();
		assertNotNull(testList);
		
		// Check if there is no active pokemon at the start of the game
		PokemonCard poke_test = hp_test.getActivePokemon();
		assertNull(poke_test);
		
		// set active Pokemon card and check if active pokemon of player is the same
		poke_test = new PokemonCard();
		hp_test.cardManager.setActivePokemon(poke_test);
		PokemonCard check_poke = hp_test.getActivePokemon();
		assertEquals(check_poke, poke_test);
		
		
		
	}

}
