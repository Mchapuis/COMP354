import static org.junit.Assert.*;

import org.junit.Test;

public class PokemonCard_unitTest {

	@Test
	public void pokemonHasEnergy_test() {
		DeenergizeAbility ability = new DeenergizeAbility();
		PokemonCard pokemon1 = new PokemonCard();
		EnergyCard energy = new EnergyCard("fight");
		System.out.println(energy.getType().toString());
	
		ability.addEnergyRequired(energy.getType(), 1); 
		pokemon1.addAbility(ability);
	
		boolean hasEnergy = pokemon1.hasEnoughEnergyForAttack(0);
		
		assertFalse(hasEnergy); //check that the pokemon doesn't have enough energy
		
		pokemon1.attachEnergy(energy);
		hasEnergy = pokemon1.hasEnoughEnergyForAttack(0);
		
		assertTrue(hasEnergy); // check if the energy was added
		
		// testing pokemon status change
		
		PokemonCard pokemon2 = new PokemonCard();
		Status currentStatus = pokemon2.getStatus();
		
		assertSame(currentStatus, Status.NORMAL);
		
		// changing Status
		pokemon2.applyStatus(Status.PARALYZED);
		currentStatus = pokemon2.getStatus();
		assertSame(currentStatus, Status.PARALYZED);
		
		
	}
	
	
		
		

}
