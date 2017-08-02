import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class AIplayer_unitTest {

	@Test
	public void test() {
		// this create a cardManager too
		AIPlayer aiPlayer_test = new AIPlayer("deck1.txt");
		HumanPlayer opp = new HumanPlayer("deck1.txt");
		// When an AI call selectActivePokemon() without activating one, there is already an active pokemon.
		
		aiPlayer_test.selectActivePokemon();
		assertNotNull(aiPlayer_test.getActivePokemon());
		
		// check if selecting a new active pokemon is not empty
		assertNotNull(aiPlayer_test.chooseNewActivePokemon());
		
		
		
		// Cards exists in bench 
		ArrayList<PokemonCard> lst = aiPlayer_test.getBench();
		assertNotNull(lst);
		
		// Cards exists in Deck
		ArrayList<Card> lst_deck = aiPlayer_test.getDeck();
		assertNotNull(lst_deck);
		
		// Cards exists in the Prize Card array
		ArrayList<Card> lst_prize = aiPlayer_test.getPrizeCards();
		assertNotNull(lst_prize);
		
		// Hand is not null
		ArrayList<Card> hand_Test = aiPlayer_test.getHand();
		assertNotNull(hand_Test);
		
		// save the current active Pokemon
		PokemonCard oldActivePokemon = aiPlayer_test.cardManager.getActivePokemon();
		
		// removing ActivePokemon() and check if a new active pokemon is set
		aiPlayer_test.cardManager.addToDiscard(aiPlayer_test.cardManager.getActivePokemon());
		assertNotNull(aiPlayer_test.cardManager.getActivePokemon());
		assertEquals(oldActivePokemon, aiPlayer_test.cardManager.getActivePokemon());
		
		// check if the old active pokemon is in the discard pile
		ArrayList<Card> discardPile = aiPlayer_test.getDiscard();
		for(int i=0; i< discardPile.size(); i++) {
			if(oldActivePokemon.equals(discardPile.get(i)))
				assertTrue(oldActivePokemon.equals(discardPile.get(i)));
		}
	}

}
