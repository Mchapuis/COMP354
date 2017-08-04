import static org.junit.Assert.*;

import org.junit.Test;

public class DeenergizeAbility_unitTest {

	@Test
	public void test() throws Exception {
		String abilityDesc = "deenergize:target:your-active:1";
		Ability Deenergize = Ability.makeAbility(abilityDesc.split(":"));
		Deenergize.addEnergyRequired(EnergyCard.Type.FIGHT, 1);
		assertTrue(Deenergize instanceof DeenergizeAbility);
		PokemonCard pokemon1 = new PokemonCard();
		PokemonCard pokemon2 = new PokemonCard();
		EnergyCard energy = new EnergyCard();
		pokemon1.addAbility(Deenergize);
		pokemon1.attachEnergy(energy);
		
		
		HumanPlayer player = new HumanPlayer("deck1.txt");
		AIPlayer autoplayer = new AIPlayer("deck2.txt");
		player.setActivePokemon(pokemon1);
		autoplayer.setActivePokemon(pokemon2);
		assertTrue(player.getActivePokemon().getEnergy().size()==1);
		
		Ability.AICardManager = autoplayer.cardManager;
		Ability.playerCardManager = player.cardManager;
		GameEngine.player = player;
		GameEngine.autoPlayer = autoplayer;

		player.getActivePokemon().getAbilities().get(0).use(Ability.Player.PLAYER);
		
		assertTrue(player.getActivePokemon().getEnergy().size()==0);
	}

}
