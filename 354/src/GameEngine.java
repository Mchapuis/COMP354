import java.util.*;

public class GameEngine {
	
	private static HumanPlayer player;
	private static AIPlayer autoPlayer;

	public static void main(String[] args) {        
		/*Parser.readInAbilities();*/
		
		//instantiate players - this builds their decks, selects a hand and selects 6 prize cards
		autoPlayer = new AIPlayer();
		player = new HumanPlayer();
		
		MainWindow w = new MainWindow(autoPlayer, player);
		
		w.instructions.setText("Choose a pokemon to be your active pokemon.");
		
		//have AI player select an active pokemon
		autoPlayer.selectActivePokemon();
		w.updateAIActivePokemon();
		
		w.display();
	}

}