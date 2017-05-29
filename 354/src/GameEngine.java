import java.util.*;

public class GameEngine {
	
	private static MainWindow w;

	public static void main(String[] args) {        
		/*Parser.readInAbilities();*/
		
		//instantiate players - this builds their decks, selects a hand and selects 6 prize cards
		AIPlayer autoPlayer = new AIPlayer();
		HumanPlayer player = new HumanPlayer();
		
		w = new MainWindow(autoPlayer, player);
		
		w.instructions.setText("Choose a pokemon to be your active pokemon.");
		
		//have AI player select an active pokemon
		autoPlayer.selectActivePokemon();
		w.updateAIActivePokemon();
		
		w.display();
	}

}