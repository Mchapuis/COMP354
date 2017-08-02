import static org.junit.Assert.*;

import org.junit.Test;

public class regressionTest_1 {

	@Test
	public void test() {
		AIPlayer AI_test = new AIPlayer("deck1.txt");
		PokemonCard StageOnePokemon = (PokemonCard) Parser.parseCard("Frogadier:pokemon:cat:stage-one:Froakie:cat:water:70:attacks:cat:colorless:2:13"
);
		AI_test.cardManager.getHand().add(StageOnePokemon);
		
		// The method in question
		AI_test.moveAllPokemonToBench();
		
		// the test. checking if the Stage One Pokemon added to the Hand was moved to the bench
		int index =0;
		while(index < AI_test.getBench().size()){
			PokemonCard.Category PokemonCat = AI_test.getBench().get(index).getCat();
			if(PokemonCat != PokemonCard.Category.BASIC) fail("Fail: a "+ PokemonCat +" Pokemon was moved to the hand") ;
			index++;
		}


	}

}
