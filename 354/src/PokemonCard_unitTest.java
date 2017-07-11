import static org.junit.Assert.*;

import org.junit.Test;

public class PokemonCard_unitTest {

	@Test
	public void pokemonHasEnergy_test() {
		DeenergizeAbility ability = new DeenergizeAbility();
		PokemonCard pokemon1 = new PokemonCard();
		EnergyCard energy = new EnergyCard("COLORLESS");
	
		ability.addEnergyRequired(energy, 1); 
		pokemon1.addAbility(ability);
	
		boolean hasEnergy = pokemon1.hasEnoughEnergyForAttack(0);
		
		assertFalse(hasEnergy);
		
		pokemon1.attachEnergy(energy);
		hasEnergy = pokemon1.hasEnoughEnergyForAttack(0);
		
		assertTrue(hasEnergy);
	}
	
//	public void pokemonStatusChange(){
//		PokemonCard pokemon2 = new PokemonCard();
//		pokemon2.applyStatus("PARALYZED");
//		
//		
//	}

}
