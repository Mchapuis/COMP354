import static org.junit.Assert.*;

import org.junit.Test;

public class PokemonCard_unitTest {

	@Test
	public void pokemonHasEnergy_test() {
		Attack attack = new Attack();
		PokemonCard pokemon1 = new PokemonCard();
		EnergyCard energy = new EnergyCard("COLORLESS");
	
		attack.addEnergyRequirement(energy, 1); 
		pokemon1.addAttack(attack);
	
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
