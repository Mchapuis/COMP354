import java.util.*;
import java.util.concurrent.LinkedTransferQueue;

public class GameEngine {
	
	public static AIPlayer autoPlayer = new AIPlayer();
	public static HumanPlayer player = new HumanPlayer();
	private static Player currentPlayer = player;
	
	public static MainWindow w;
	private static Object lock = new Object();
    public static Queue<Message> queue = new LinkedTransferQueue<>();
    
    private static boolean hasSelectedActive = false;
    private static boolean hasClickedAttach = false;
    private static boolean hasAttachedEnergy = false;
    private static boolean mustMoveCardToBottomOfDeck = false;
    private static boolean mustChoosePokemonToSwap = false;

	public static void main(String[] args) {
		//create and display the main game window
		MainWindow.lock = lock;
		MainWindow.queue = queue;
		w = new MainWindow(autoPlayer, player);
		w.display();

		//give Ability class access to card managers
		Ability.playerCardManager = player.cardManager;
		Ability.AICardManager = autoPlayer.cardManager;

		handleMulligans(); //ensures each player has at least one pokemon

		setupPhase(); //players choose their initial active and benched pokemon

		rollForFirstTurn(); //determines which player gets to start

		//play game until there is a winner
		boolean gameEnded = false;
		while(!gameEnded){
			currentPlayer.playTurn();

			//updateStatusEffects();

			//checkWinConditions();

			switchTurn();
		}
	}

	private static void switchTurn(){
		if(currentPlayer == player){
			currentPlayer = autoPlayer;
		}
		else{
			currentPlayer = player;
		}
	}

	private static void setupPhase(){
		player.setup();
		autoPlayer.setup();
	}

	private static void handleMulligans(){
		//player mulligans
		while(player.cardManager.getFirstPokemon() == null){
			//shuffle hand into deck
			player.cardManager.shuffleHandIntoDeck();

			//draw new hand
			for(int i = 0; i < CardManager.STARTING_HAND_SIZE; i++){
				player.drawCard();
			}

			//opponent draws 1 card
			autoPlayer.drawCard();
		}

		//ai mulligans
		while(autoPlayer.cardManager.getFirstPokemon() == null){
			//shuffle hand into deck
			autoPlayer.cardManager.shuffleHandIntoDeck();

			//draw new hand
			for(int i = 0; i < CardManager.STARTING_HAND_SIZE; i++){
				autoPlayer.drawCard();
			}

			//opponent draws 1 card
			player.drawCard();
		}
	}

	private static void rollForFirstTurn(){
		if(RandomNumberGenerator.flipACoin()){
			currentPlayer = player;
		}
		else{
			currentPlayer = autoPlayer;
		}
	}

	public static void waitForInput(){
        try{
            synchronized(lock){
                lock.wait();
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
	
	public static PokemonCard getChoiceOfCard(Player p, Ability.Target target){
		Message msg = queue.remove();
		PokemonCard cardToReturn;
		
		if (target == Ability.Target.OPPONENT_BENCH && msg.getSide() == Message.Side.AI && msg.getType() == Message.ButtonType.BENCH){
			cardToReturn = autoPlayer.getBench().get(msg.getIndex());
		} else if (target == Ability.Target.YOUR_BENCH && msg.getSide() == Message.Side.PLAYER && msg.getType() == Message.ButtonType.BENCH){
			cardToReturn = player.getBench().get(msg.getIndex());
		} else if (target == Ability.Target.OPPONENT_POKEMON && msg.getSide() == Message.Side.AI){
			if (msg.getType() == Message.ButtonType.ACTIVE){
				cardToReturn = autoPlayer.getActivePokemon();
			} else if (msg.getType() == Message.ButtonType.BENCH) {
				cardToReturn = autoPlayer.getBench().get(msg.getIndex());
			} else {
				cardToReturn = null;
			}
		} else if (target == Ability.Target.YOUR_POKEMON && msg.getSide() == Message.Side.PLAYER){
			if (msg.getType() == Message.ButtonType.ACTIVE){
				cardToReturn = player.getActivePokemon();
			} else if (msg.getType() == Message.ButtonType.BENCH) {
				cardToReturn = player.getBench().get(msg.getIndex());
			} else {
				cardToReturn = null;
			}
		} else {
			cardToReturn = null;
		}
		
		return cardToReturn;
	}

	public static PokemonCard choosePokemonCard(Player p, Ability.Target target){
		PokemonCard cardToReturn;
		
		if (p == player){
			waitForInput();
			cardToReturn = getChoiceOfCard(p, target);
			while (cardToReturn == null) {
				waitForInput();
				cardToReturn = getChoiceOfCard(p, target);
			}
		} else {
			if (target == Ability.Target.OPPONENT_BENCH){
				cardToReturn = player.cardManager.getFirstCardOfBench();
			} else if (target == Ability.Target.YOUR_BENCH){
				cardToReturn = autoPlayer.cardManager.getFirstCardOfBench();
			} else if (target == Ability.Target.OPPONENT_POKEMON){
				cardToReturn = player.cardManager.getActivePokemon();
			} else {
				cardToReturn = autoPlayer.cardManager.getFirstCardOfBench();
			}
		}
		return cardToReturn;
	}
	
}