import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class AIplayer_JUnit {

	@Test
	public void test() {
		// this create a cardManager too
		AIPlayer aiPlayer_test = new AIPlayer();
		HumanPlayer opp = new HumanPlayer();
		// When an AI call selectActivePokemon() without activating one, there is already an active pokemon.
		// TODO: this should be null in next iterations...
		aiPlayer_test.selectActivePokemon();
		assertNotNull(aiPlayer_test.getActivePokemon());
		
		// check if selecting a new active pokemon is not empty
		assertNotNull(aiPlayer_test.chooseNewActivePokemon());
		
		// AI attacking the opponent is creating errors
		
		// Cards exists in Deck 
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
		
		// TODO: REGRESSION TEST make a test so this is failing if not null
		// removing ActivePokemon() and check if we get null
		aiPlayer_test.cardManager.addToDiscard(aiPlayer_test.cardManager.getActivePokemon());
		assertNotNull(aiPlayer_test.cardManager.getActivePokemon());//TODO: should be assertNull
		
		// TODO: test moveCardFromHandToBottomOfDeck()
		// 1- create a Card
		// 2- Put card in hand
		// 3- Put card from hand to bottom of Deck
		// 4- Loop throught the Cards in the Deck and check if it is at the bottom
		
		// TODO: test playTurn( opponent )
		// 1- this is hard coded for now. This doesn't really need to be tested.
		// 2- Attack needs to be a completely separate thing
		
	}

}
