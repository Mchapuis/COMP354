import static org.junit.Assert.*;

import org.junit.Test;
/**
 * the bug: the evaluation of the amount to draw in the DrawAbility when in it's a Complex amount 
 * 	like when it's the user hand was reevaluated each time a draw was done which caused problems 
 * 	such as infinite looping. 
 *
 */
public class regressionTest_2 {

	@Test
	public void test() throws UnimplementedException {
		String abilityDesc = "Tierno:draw:count(your-hand)";
		String[] descArray = abilityDesc.split(":");
		Ability draw_test = Ability.parseAbilitiesLine(abilityDesc);
		
		HumanPlayer playerA = new HumanPlayer("deck1.txt");
		int handBeforeDraw = playerA.cardManager.getHand().size();
		
		PokemonCard pokemonA = new PokemonCard("Glameow", "pokemon", "basic","colorless", 60, 2);
		draw_test.playerCardManager = playerA.cardManager;
		
		playerA.setActivePokemon(pokemonA);
		playerA.getActivePokemon().addAbility(draw_test);
	
		playerA.getActivePokemon().getAbilities().get(0).use(Ability.Player.PLAYER);
		
		int handAfterDraw = playerA.cardManager.getHand().size();
		System.out.println(handAfterDraw);
		//the amount to draw calculated should be equal to the hand. so the hand size should double
		assertEquals(handAfterDraw, (2*handBeforeDraw));
	}

}
