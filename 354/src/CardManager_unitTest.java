import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.junit.Test;

public class CardManager_unitTest {

	@Test
	public void test() {
		
		CardManager cm_test = new CardManager("deck1.txt");
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
		
		// remove card from hands
		Card cardToRemove = cm_test.getHand().get(0);
		cm_test.removeCardFromHand(cardToRemove);
		assertFalse(this.checkIfCardInHand(cm_test, cardToRemove));
		
		// move pokemon to bench
		PokemonCard pokemonToMove = cm_test.getFirstPokemon();
		cm_test.movePokemonToBench(pokemonToMove);
		assertFalse(this.checkIfCardInHand(cm_test, pokemonToMove));
		assertTrue(this.checkIfCardInBench(cm_test, pokemonToMove));
		
		//shuffle hand into deck
		ArrayList<Card> CardsInHand = cm_test.getHand();
		if(CardsInHand.size() != 0) {
			cm_test.shuffleHandIntoDeck();
		}
		//check if all hand cards are now in deck and the hand is empty
		assertTrue(cm_test.getHand().size()==0);
		assertTrue(this.HandCardsInDeck(cm_test, CardsInHand));
		
		//hand has basic Pokemon. Should return true since we already moved on pokemon to 
		assertTrue(cm_test.benchHasBasicPokemon());
		
	}
	
	public boolean checkIfCardInHand(CardManager cm, Card card) {
		for (int i=0 ; i< cm.getHand().size(); i++) {
			if (card.equals(cm.getHand().get(i)))
				return true;
		}
		
		return false;
	}
	
	public boolean checkIfCardInBench(CardManager cm, Card card) {
		for (int i=0 ; i< cm.getBench().size(); i++) {
			if (card.equals(cm.getBench().get(i)))
				return true;
		}
		
		return false;
	}
	
	public boolean HandCardsInDeck(CardManager cm, ArrayList<Card> cards) {
		for(int i=0; i<cards.size(); i++) {
			for(int j=0; j<cm.getDeck().size(); j++) {
				if(cards.get(i)==cm.getDeck().get(j)) break;
				else if(j== cm.getDeck().size()-1) return false;
			}
		}
		return true;
	}
}
