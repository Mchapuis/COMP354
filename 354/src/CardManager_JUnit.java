import static org.junit.Assert.*;

import org.junit.Test;

public class CardManager_JUnit {

	@Test
	public void test() {
		
		CardManager cm_test = new CardManager();
		PokemonCard card_test = new PokemonCard();
		
		// getFirstPokemon() 
		PokemonCard poke_output = cm_test.getFirstPokemon();
		
		//assertSame((PokemonCard)card_test, (PokemonCard)poke_output);
		
		// setActivePokemon() 
		cm_test.setActivePokemon((PokemonCard)poke_output);
		assertSame(cm_test.activePokemon, (PokemonCard)poke_output);
		
		// removeActivePokemon()
		// discardActivePokemon()

		//---> TODO: loop through discard pile to find a card. 
		//			the function doesn't exist yet.
		
		// getFirstEnergy()
		EnergyCard en_test = new EnergyCard("testing");
		EnergyCard en_output = cm_test.getFirstEnergy();
		 
		//if(en_output != null)
		//	en_output = new EnergyCard("test");
		//	assertSame(en_test, en_output);
		
		// attachEnergy()
		// TODO: test if the ID of card is the same
		//cm_test.attachEnergy(en_output,(PokemonCard)poke_output);
		
		
		
	}
	

}
