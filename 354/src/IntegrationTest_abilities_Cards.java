import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;


public class IntegrationTest_abilities_Cards {

	@Test
	public void test() {
//		fail("Not yet implemented");
							
		PokemonCard Glameow = new PokemonCard("Glameow", "pokemon", "basic","colorless", 60, 2);
		EnergyCard energy = new EnergyCard("FIGHT");
		
		int energyAttached = Glameow.getEnergy().size();
		assertSame(energyAttached, 0); // check there is no energy attached
		
		
		Ability ability = Ability.parseAbilitiesLine("Tierno:draw:3");
		ability.addEnergyRequired(EnergyCard.Type.FIGHT, 1);
		Glameow.addAbility(ability);
		Glameow.attachEnergy(energy);
		energyAttached = Glameow.getEnergy().size();
		assertSame(energyAttached, 1); // check if one energy is attached
		
		Ability attachedAbility = Glameow.getAbilities().get(0);
		
		assertTrue(ability instanceof DrawAbility); // check the ability to add is of the right type
		assertTrue(attachedAbility instanceof DrawAbility); // check if the ability added is of the same type
		
		
		HumanPlayer human = new HumanPlayer();
	
		
		human.setActivePokemon(Glameow);
		PokemonCard active = human.getActivePokemon();
		assertSame(active, Glameow);
		
		int deckSize = human.cardManager.getDeck().size();
		
		assertTrue(deckSize==47); //deck = 60 cards - 7 cards in hand - 6 Prize cards
		
		//set player cardManager
		Ability.playerCardManager=human.cardManager;
		
		// Apply Pokemon Ability
		
		human.getActivePokemon().getAbilities().get(0).use(Ability.Player.PLAYER);
		deckSize = human.cardManager.getDeck().size();
		assertTrue(deckSize==44); //deck = 47 cards - 3 Draw
		int handSize = human.cardManager.getHand().size();
		assertTrue(handSize == 10); // hand = 7 + 3 cards added
		
		
	}	

	
}
