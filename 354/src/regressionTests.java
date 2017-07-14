import static org.junit.Assert.*;

import org.junit.Test;

public class regressionTests {

	@Test
	public void test() {
		AIPlayer AI_test = new AIPlayer();
		PokemonCard StageOnePokemon = (PokemonCard) Parser.parseCard("Frogadier:pokemon:cat:stage-one:Froakie:cat:water:70:attacks:cat:colorless:2:13"
);
		AI_test.cardManager.getHand().add(StageOnePokemon);
		
		// The method in question
		AI_test.moveAllPokemonToBench();
		
		// the test
		int index =0;
		while(index < AI_test.getBench().size()){
			PokemonCard.Category PokemonCat = AI_test.getBench().get(index).getCat();
			assertTrue(PokemonCat == PokemonCard.Category.BASIC);
			index++;
		}


	}

}
