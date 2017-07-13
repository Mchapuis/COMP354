import static org.junit.Assert.*;

import org.junit.Test;

public class IntegrationTest_abilities_Cards {

	@Test
	public void test() {
//		fail("Not yet implemented");
							
		PokemonCard Glameow = new PokemonCard("Glameow", "pokemon", "basic","colorless", 60, 2);
		EnergyCard energy = new EnergyCard();
		int energyAttached = Glameow.getEnergy().size();
		assertSame(energyAttached, 0); // check there is no energy attached
		
		Glameow.attachEnergy(energy);
		energyAttached = Glameow.getEnergy().size();
		assertSame(energyAttached, 1); // check if one energy is attached
		
		
		Ability ability = Ability.parseAbilitiesLine("Cut:dam:target:opponent-active:30");
		Glameow.addAbility(ability);
		Ability attachedAbility = Glameow.getAbilities().get(0);
		
		assertTrue(ability instanceof DamageAbility); // check the ability to add is of the right type
		assertTrue(attachedAbility instanceof DamageAbility); // check if the ability added is of the same type
		
		
		PokemonCard Pikachu = new PokemonCard("Pikachu", "pokemon","basic", "lightning",60, 2);
		
		HumanPlayer human = new HumanPlayer();
		AIPlayer AI = new AIPlayer();
		
		human.setActivePokemon(Glameow);
		AI.setActivePokemon(Pikachu);
		
		
		
	
		
	}	

	
}
