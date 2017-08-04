import static org.junit.Assert.*;

import org.junit.Test;

public class GameEngine_unitTest {
	// some errors will appear in the console related to the GUI but should not be 
	//	 our concern since this is just a test of the methods in GameEngine class

	@Test
	public void test() throws Exception {

		// initializing important element for GameEngine to run correctly
		GameWindow.lock = GameEngine.lock;
		GameWindow.queue = GameEngine.queue;
		GameEngine.w = new GameWindow(GameEngine.autoPlayer, GameEngine.player, GameEngine.displayGameInFullScreen);
		GameEngine.w.display();


		//+ handleMulligans
		if(GameEngine.player.cardManager.getFirstPokemon() != null) {
			while(GameEngine.player.cardManager.getFirstPokemon() != null) {
				GameEngine.player.cardManager.shuffleHandIntoDeck();
				for(int i = 0; i < CardManager.STARTING_HAND_SIZE; i++){
					GameEngine.player.drawCard();
				}
			}
			assertTrue(GameEngine.player.cardManager.getFirstPokemon() ==null);
		}
		// now there is no Pokemon in player's hand
		GameEngine.handleMulligans(); // handleMulligans is only set to protected for testing purposes
		assertTrue(GameEngine.player.cardManager.getFirstPokemon() !=null);



		//+ checkForKnockouts

		//setup of the gameboard
		GameEngine.autoPlayer.moveAllPokemonToBench();
		GameEngine.player.setActivePokemon(GameEngine.player.cardManager.getFirstPokemon());
		assertTrue(GameEngine.autoPlayer.getBench().size() >=2); // there is at least one Pokemon in the bench
		GameEngine.autoPlayer.selectActivePokemon();
		PokemonCard firstInBench = GameEngine.autoPlayer.getBench().get(0);
		int numOfPokemonInBench = GameEngine.autoPlayer.getBench().size();
		GameEngine.autoPlayer.getBench().get(0).removeHP(firstInBench.getMaxHP());

		//testing that the method will remove the card with 0 HP from the bench
		GameEngine.checkForKnockouts();
		assertTrue(GameEngine.autoPlayer.getBench().size()== numOfPokemonInBench -1);




	}
}
