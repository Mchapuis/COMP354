import java.util.*;

public class GameEngine {
	
	private static HumanPlayer player;
	private static AIPlayer autoPlayer;

	public static void main(String[] args) {
		MainWindow w = new MainWindow();
        
		/*Parser.readInAbilities();*/
		
		//instantiate players - this builds their decks, selects a hand and selects 6 prize cards
		autoPlayer = new AIPlayer();
		ArrayList<Card> AIHand = autoPlayer.cardManager.hand;
		w.AIHand.clear();
		for (Card card : AIHand){
			w.AIHand.add(new GUICard(card));
		}
		w.updateAIHandContainer();
		
		player = new HumanPlayer();
		ArrayList<Card> playerHand = player.cardManager.hand;
		w.playerHand.clear();
		for (Card card : playerHand){
			w.playerHand.add(new GUICard(card));
		}
		w.updatePlayerHandContainer();
		
		w.instructions.setText("Choose a pokemon to be your active pokemon.");
		
		//have AI player select an active pokemon
		autoPlayer.selectActivePokemon(w);
		
		w.display();
		
		//have AI player play a turn
		/*autoPlayer.playTurn();
		
		//have user play a turn
		player.playTurn();*/
		
		/*boolean gameOver = false;
		while (!gameOver){
			player.playTurn();
			autoPlayer.playTurn();
		}*/
	}

}