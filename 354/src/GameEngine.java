import java.util.*;
import java.util.concurrent.LinkedTransferQueue;



public class GameEngine {
	/*
	*                                  Full Screen annoying as fuck?
	*                                         Disable here
	*                                            |=====|
	*                                            |=====|
	*                                            |=====|
	*                                            |=====|
	*                                            |=====|
	*                                            |=====|
	*                                           _|=====|_
	*                                           \|=====|/
	*                                            \=====/
	*                                             \===/
	*                                              \=/
	*                                               V
	*  */
	final static boolean displayGameInFullScreen = true;

	//GUI objects
	public static GameWindow w;
	public static Object lock = new Object();
	public static Queue<Message> queue = new LinkedTransferQueue<>();

	//players
	public static AIPlayer autoPlayer = new AIPlayer();
	public static HumanPlayer player = new HumanPlayer();

	//
	private static Player currentPlayer = player;
	private static Player winner = null;

	public static void main(String[] args){
		//create and display the main game window
		GameWindow.lock = lock;
		GameWindow.queue = queue;
		w = new GameWindow(autoPlayer, player, displayGameInFullScreen);
		w.display();

		//give Ability class access to card managers
		Ability.playerCardManager = player.cardManager;
		Ability.AICardManager = autoPlayer.cardManager;

		handleMulligans(); //ensures each player has at least one pokemon

		setupPhase(); //players choose their initial active and benched pokemon

		rollForFirstTurn(); //determines which player gets to start

		//play game until there is a winner
		while(true){

		    //check win (lose) condition of having no cards to draw
		    if(currentPlayer.getDeck().size() == 0){
		        if(currentPlayer == player){
		            declareWinner(Ability.Player.AI);
                }
                else{
		            declareWinner(Ability.Player.PLAYER);
                }
                break;
            }

			currentPlayer.playTurn();
			if(winnerFound()){ break; }

			updateStatusEffectsAll();
            checkForKnockouts();
            if(winnerFound()){ break; }

			switchTurn();
		}

		//announce win or loss
		GameOverWindow g = new GameOverWindow(getWinner(), displayGameInFullScreen);
		w.close();
		g.display();
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

	//initial game phases
	private static void setupPhase(){
		player.setup();
		autoPlayer.setup();
	}
	private static void handleMulligans(){
		//player mulligans
		while(player.cardManager.getFirstPokemon() == null){
			GameEngine.w.updateInstructions("You had a mulligan. You redraw your hand. Your opponent draws another card.");

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
			GameEngine.w.updateInstructions("Your opponent had a mulligan. Your opponent redraws their hand. You draw another card.");

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

	//game processes
	public static void checkForKnockouts(){
		ArrayList<PokemonCard> cardsToDiscard = new ArrayList<>();

		//check AI bench
		for(PokemonCard p : autoPlayer.cardManager.getBench()){
			if(p.getCurrentHP() <= 0){
				cardsToDiscard.add(p);

				if(player.getPrizeCards().size() == 1){
					declareWinner(Ability.Player.PLAYER);
					return;
				}
				else{
					player.cardManager.drawPrizeCard();
				}
			}
		}

		//discard dead bench pokemon
		for(PokemonCard p : cardsToDiscard){
			autoPlayer.cardManager.getBench().remove(p);
			autoPlayer.cardManager.addPokemonCardToDiscard(p);
		}
		cardsToDiscard.clear();

		//check player bench
		for(PokemonCard p : player.cardManager.getBench()){
			if(p.getCurrentHP() <= 0){
				cardsToDiscard.add(p);

				if(autoPlayer.getPrizeCards().size() == 1){
					declareWinner(Ability.Player.AI);
					return;
				}
				else{
					autoPlayer.cardManager.drawPrizeCard();
				}
			}
		}

		//discard dead bench pokemon
		for(PokemonCard p : cardsToDiscard){
			player.cardManager.getBench().remove(p);
			player.cardManager.addPokemonCardToDiscard(p);
		}
		cardsToDiscard.clear();

		//check AI active
		if(autoPlayer.cardManager.getActivePokemon().getCurrentHP() <= 0){
			autoPlayer.cardManager.removeActivePokemon();

			if(player.getPrizeCards().size() == 1){
				declareWinner(Ability.Player.PLAYER);
				return;
			}
			else{
				player.cardManager.drawPrizeCard();
			}

			if(! autoPlayer.chooseNewActivePokemon()){
				declareWinner(Ability.Player.PLAYER);
				return;
			}
		}

		//check player active
		if(player.cardManager.getActivePokemon().getCurrentHP() <= 0){
			player.cardManager.removeActivePokemon();

			if(autoPlayer.getPrizeCards().size() == 1){
				declareWinner(Ability.Player.AI);
			}
			else{
				autoPlayer.cardManager.drawPrizeCard();
			}

			if(! player.chooseNewActivePokemon()){
				declareWinner(Ability.Player.AI);
				return;
			}
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

	//update status effects
	private static void updateStatusEffectsAll(){
		//update statuses on player bench
		for(PokemonCard p : player.getBench()){
			updateStatusEffectsSingle(player, p);
		}

		//update statuses on player active
		updateStatusEffectsSingle(player, player.getActivePokemon());

		//update statuses on ai bench
		for(PokemonCard p : autoPlayer.getBench()){
			updateStatusEffectsSingle(autoPlayer, p);
		}

		//update statuses on ai active
		updateStatusEffectsSingle(autoPlayer, autoPlayer.getActivePokemon());
	}
	private static void updateStatusEffectsSingle(Player belongsTo, PokemonCard pokemonCard){
		Status s = pokemonCard.getStatus();

		switch(s){
			case ASLEEP:
				//50% chance to wake up
				if(RandomNumberGenerator.flipACoin()){
					pokemonCard.applyStatus(Status.NORMAL);
				}
				break;
			case NORMAL:
				//do nothing
				break;
			case PARALYZED:
				//100% chance to become unparalyzed after owner's turn
				if(currentPlayer == belongsTo){
					pokemonCard.applyStatus(Status.NORMAL);
				}
				break;
			case POISONED:
				//deals damage at end of owner's turn
				if(currentPlayer == belongsTo){
					pokemonCard.removeHP(10);
				}
				break;
			case STUCK:
				//100% chance to become unstuck after owner's turn
				if(currentPlayer == belongsTo){
					pokemonCard.applyStatus(Status.NORMAL);
				}
				break;
		}
	}

    //card selection
	//this should really be a set of abstract methods in Player, but why refactor when you can add new features?
	private static PokemonCard getChoiceOfCard(Ability.Target target){
		Message msg = null;
		PokemonCard cardToReturn = null;

		switch(target){
			case OPPONENT:
				//
				break;
			case OPPONENT_ACTIVE:
				cardToReturn = autoPlayer.getActivePokemon();
				break;
			case OPPONENT_BENCH:
				GameEngine.w.updateInstructions("Select a Pokémon on your opponent's bench.");
				waitForInput();
				msg = queue.remove();
				if(msg.getSide() == Message.Side.AI && msg.getType() == Message.ButtonType.BENCH){
					if(msg.getIndex() < autoPlayer.getBench().size()){
						cardToReturn = autoPlayer.getBench().get(msg.getIndex());
					}
				}
				break;
			case OPPONENT_POKEMON:
				GameEngine.w.updateInstructions("Select a Pokémon from your opponent's bench or active slot.");
				waitForInput();
				msg = queue.remove();
				if(msg.getSide() == Message.Side.AI && msg.getType() == Message.ButtonType.BENCH){
					if(msg.getIndex() < autoPlayer.getBench().size()){
						cardToReturn = autoPlayer.getBench().get(msg.getIndex());
					}
				}
				else if(msg.getSide() == Message.Side.AI && msg.getType() == Message.ButtonType.ACTIVE){
					cardToReturn = autoPlayer.getActivePokemon();
				}
				break;
			case YOU:
				//
				break;
			case YOUR_ACTIVE:
				cardToReturn = player.getActivePokemon();
				break;
			case YOUR_BENCH:
				GameEngine.w.updateInstructions("Select a Pokémon on your bench.");
				waitForInput();
				msg = queue.remove();
				if(msg.getSide() == Message.Side.PLAYER && msg.getType() == Message.ButtonType.BENCH){
					if(msg.getIndex() < player.getBench().size()){
						cardToReturn = player.getBench().get(msg.getIndex());
					}
				}
				break;
			case YOUR_POKEMON:
				GameEngine.w.updateInstructions("Select a Pokémon from your bench or active slot.");
				waitForInput();
				msg = queue.remove();
				if(msg.getSide() == Message.Side.PLAYER && msg.getType() == Message.ButtonType.BENCH){
					if(msg.getIndex() < player.getBench().size()){
						cardToReturn = player.getBench().get(msg.getIndex());
					}
				}
				else if(msg.getSide() == Message.Side.PLAYER && msg.getType() == Message.ButtonType.ACTIVE){
					cardToReturn = player.getActivePokemon();
				}
				break;
		}

		
		return cardToReturn;
	}
	public static PokemonCard choosePokemonCard(Player p, Ability.Target target){
		PokemonCard cardToReturn;
		
		if (p == player){
			cardToReturn = getChoiceOfCard(target);
			while (cardToReturn == null) {
				cardToReturn = getChoiceOfCard(target);
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
	public static PokemonCard choosePokemonCard(Ability.Player p, Ability.Target target){
		if(p == Ability.Player.PLAYER){
			return choosePokemonCard(player, target);
		}
		else{
			return choosePokemonCard(autoPlayer, target);
		}
	}
	public static Card chooseCardFromAHand(Ability.Player p, CardManager c){
		//this whole method is lazily designed. deal with it
		if(p == Ability.Player.PLAYER){
			boolean cardSelected = false;
			while(! cardSelected){
				Message.Side s = null;
				if(c == player.cardManager){
					GameEngine.w.updateInstructions("Please select a card from your hand.");
					s = Message.Side.PLAYER;
				}
				else{
					GameEngine.w.updateInstructions("Please select a card from your opponent's hand.");
					s = Message.Side.AI;
				}
				waitForInput();
				Message msg = queue.remove();

				if(msg.getSide() == s && msg.getType() == Message.ButtonType.HAND){
					return c.getHand().get(msg.getIndex());
				}

			}
		}
		else{
			return c.getHand().get(0);
		}
		return null;
	}

	//getters
	public static Ability.Player getCurrentPlayer(){
		if(currentPlayer == player){
			return Ability.Player.PLAYER;
		}
		else{
			return Ability.Player.AI;
		}
	}
	public static Ability.Player getWinner(){
		if(winner == player){
			return Ability.Player.PLAYER;
		}
		else{
			return Ability.Player.AI;
		}
	}
	public static void declareWinner(Ability.Player p){
		switch(p){
			case AI:
				winner = autoPlayer;
				break;
			case PLAYER:
				winner = player;
				break;
		}
	}
	public static boolean winnerFound(){
		return winner != null;
	}



}