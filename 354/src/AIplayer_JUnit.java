import static org.junit.Assert.*;

import java.util.ArrayList;

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
		
		// check that the attack function is not null
		HumanPlayer opp = new HumanPlayer();
		String str = aiPlayer_test.attack(0, opp);
		assertNotNull(str);
		
		// check if hand is not null
		ArrayList<Card> hand_Test = aiPlayer_test.getHand();
		assertNotNull(hand_Test);
		
		// Check if the turn is not returning null
		String str_ai = aiPlayer_test.playTurn(opp);
		assertNotNull(str_ai);
		
		// TODO: this test is not working
		// test that the energy is attached
		// setting up variables
//		EnergyCard ec_test = new EnergyCard("COLORLESS");
//		PokemonCard poke_card_test = new PokemonCard();
//		aiPlayer_test.attachEnergy(ec_test,poke_card_test);
//		aiPlayer_test.cardManager.setActivePokemon(poke_card_test);
//		boolean isSame = false;
//		
//		// testing energy card
//		while(!isSame)
//		{
//			EnergyCard test_en = aiPlayer_test.cardManager.getFirstEnergy();
//			if( ec_test.equals(test_en))
//			{
//				assertSame(ec_test, test_en);
//			}
//		}
			
		
		
		// TODO: this is not working
		// removing ActivePokemon() and check if we get null
		// aiPlayer_test.cardManager.discardActivePokemon();
		// assertNull(aiPlayer_test.cardManager.getActivePokemon());
		
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
