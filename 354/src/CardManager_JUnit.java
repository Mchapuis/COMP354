import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class CardManager_JUnit {

	@Test
	public void test() {
		
		CardManager cm_test = new CardManager();
		PokemonCard card_test = new PokemonCard();
		
		// getFirstPokemon() 
		PokemonCard poke_output = cm_test.getFirstPokemon();
		assertNotSame(card_test, poke_output);
		
		// setActivePokemon() 
		cm_test.setActivePokemon((PokemonCard)poke_output);
		assertSame(cm_test.getActivePokemon() , (PokemonCard)poke_output);
		
		// getFirstEnergy()
		Card en_output = cm_test.getFirstEnergy();
		boolean isEnergy = false;
		if(en_output instanceof EnergyCard) isEnergy = true;
		assertTrue(isEnergy);
		assertNotNull(en_output);
		
		// attachEnergy()
		cm_test.attachEnergy((EnergyCard)en_output,(PokemonCard)poke_output);
		assertNotNull(cm_test.getFirstEnergy());
		
		// check if the energyCard is in the hand or not
		ArrayList<Card> hand_test =  cm_test.getHand();
		for(int i=0; i< hand_test.size(); i++)
		{
			// if still in hand...fail the test
			if(hand_test.equals((EnergyCard)en_output))
			{
				fail("Card removed from hand must be deleted");
			}
		}
		
		// TODO: find cards in the deck.
		// TODO: find cards in the discard.
		// TODO: loop through bench
		// TODO: loop through hand
		// TODO: loop through prize
		
		
	}
	

}
