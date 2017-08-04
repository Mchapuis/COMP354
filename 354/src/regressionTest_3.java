import static org.junit.Assert.*;

import org.junit.Test;

public class regressionTest_3 {

	@Test
	public void test() throws Exception {
		String abilityDesc = "heal:target:your-active:60";
		Ability healing = Ability.makeAbility(abilityDesc.split(":"));
		healing.addEnergyRequired(EnergyCard.Type.FIGHT, 1);
		assertTrue(healing instanceof HealAbility);
		
		PokemonCard pokemon1 = new PokemonCard();
		PokemonCard pokemon2 = new PokemonCard();
		EnergyCard energy = new EnergyCard();
		pokemon1.addAbility(healing);
		pokemon1.attachEnergy(energy);

		HumanPlayer player = new HumanPlayer("deck1.txt");
		AIPlayer autoplayer = new AIPlayer("deck2.txt");
		player.setActivePokemon(pokemon1);
		autoplayer.setActivePokemon(pokemon2);
		Ability.AICardManager = autoplayer.cardManager;
		Ability.playerCardManager = player.cardManager;
		GameEngine.player = player;
		GameEngine.autoPlayer = autoplayer;
		
		player.getActivePokemon().setMaxHP(80);
		player.getActivePokemon().removeHP(50);
		assertTrue(player.getActivePokemon().getCurrentHP() == 30);//check the change on currentHP happened

		player.getActivePokemon().getAbilities().get(0).use(Ability.Player.PLAYER);
		
		assertTrue(player.getActivePokemon().getHasBeenHealed());//check the status is healed
		assertTrue(player.getActivePokemon().getCurrentHP() == 80);// check the currentHP is back to max
//		assertFalse(player.getActivePokemon().getCurrentHP() == 90);//true if bug is not fixed
	}

}
