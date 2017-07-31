import java.util.*;


//TODO add card picker support to deenergize and retreat costs
//TODO make AI slightly smarter

public class GameEngine {
	/* 	For testers'/teacher's benefit
	*   Some quick settings can be changed here.
	*
	*       |=====|
	*       |=====|
	*       |=====|
	*       |=====|
	*       |=====|
	*       |=====|
	*      _|=====|_
	*      \|=====|/
	*       \=====/
	*        \===/
	*         \=/
	*          V
	* */
	final static boolean displayGameInFullScreen = true;
	final static String AI_DECK = "deck2.txt";
	final static String PLAYER_DECK = "deck1.txt";
	final static public boolean shuffleDecksAtGameStart = false;
	final static public boolean validateDecks = false;
	/*
	*          A
	*         /=\
	*        /===\
	*       /=====\
	*      /|=====|\
	*       |=====|
	*       |=====|
	*       |=====|
	*       |=====|
	*       |=====|
	*       |=====|
	*       |=====|
	*
	*  */


	//GUI objects
	public static GameWindow w;
	public static Object lock = new Object();
	public static Queue<Message> queue = new LinkedTransferQueue<>();

	//players
	public static AIPlayer autoPlayer = new AIPlayer(AI_DECK);
	public static HumanPlayer player = new HumanPlayer(PLAYER_DECK);


	public static void main(String[] args){
		//create and display the main game window
		GameWindow.lock = lock;
		GameWindow.queue = queue;
		w = new GameWindow(autoPlayer, player, displayGameInFullScreen);
		w.display();



	public static GameWindow w;



	private static Object lock = new Object();



	public static Queue<Message> queue = new LinkedTransferQueue<>();


			useTurnTriggerAbilities();
			updateStatusEffectsAll();
            checkForKnockouts();
            if(winnerFound()){ break; }


		//announce win or loss
		GameOverWindow g = new GameOverWindow(getWinner(), displayGameInFullScreen);
		w.close();
		g.display();
    }



	public static AIPlayer autoPlayer = new AIPlayer();

	//initial game phases
	private static void setupPhase(){
		player.setup();
		autoPlayer.setup();
	}
	private static void handleMulligans(){
		//player mulligans
		while(player.cardManager.getFirstPokemon() == null){
			GameEngine.log("You had a mulligan. You redraw your hand. Your opponent draws another card.");


	public static HumanPlayer player = new HumanPlayer();


			//opponent draws 1 card
			autoPlayer.drawCard();

			//update gui
			GameEngine.updateGUI();
		}

		//ai mulligans
		while(autoPlayer.cardManager.getFirstPokemon() == null){
			GameEngine.log("Your opponent had a mulligan. Your opponent redraws their hand. You draw another card.");


	//


			//opponent draws 1 card
			player.drawCard();

			//update gui
			GameEngine.updateGUI();
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



	private static Player winner = null;



	public static void main(String[] args) {



		//create and display the main game window



		GameWindow.lock = lock;



		GameWindow.queue = queue;



		w = new GameWindow(autoPlayer, player);

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
	private static void useTurnTriggerAbilities(){
		Player player1 = currentPlayer, otherPlayer = null;
		Ability.Player op = null;
		if(currentPlayer == player){
			otherPlayer = autoPlayer;
			op = Ability.Player.AI;
		}
		else{
			otherPlayer = player;
			op = Ability.Player.PLAYER;
		}

		//current own triggers
		for(PokemonCard p : currentPlayer.getBench()){
			for(Ability a : p.abilitiesTriggeredOwnTurnEnd){
				a.use(getCurrentPlayer());
			}
		}
		for(Ability a : currentPlayer.getActivePokemon().abilitiesTriggeredOwnTurnEnd){
			a.use(getCurrentPlayer());
		}

		//other player opponent triggers
		for(PokemonCard p : otherPlayer.getBench()){
			for(Ability a : p.abilitiesTriggeredOpponentTurnEnd){
				a.use(op);
			}
		}
		for(Ability a : currentPlayer.getActivePokemon().abilitiesTriggeredOpponentTurnEnd){
			a.use(op);
		}

	}

	//update status effects
	private static void updateStatusEffectsAll(){
		//update statuses on player bench
		for(PokemonCard p : player.getBench()){
			updateStatusEffectsSingle(player, p);
		}







		//give Ability class access to card managers

    //card selection
	//this should really be a set of abstract methods in Player, but why refactor when you can add new features?
	private static PokemonCard getChoiceOfCard(Ability.Target target){
		PokemonCard cardToReturn = null;

		//show prompt //or for actives, return card immediately
		switch(target){
			case OPPONENT_BENCH:
				GameEngine.log("Select a Pokémon on your opponent's bench.");
				break;
			case OPPONENT_POKEMON:
				GameEngine.log("Select a Pokémon from your opponent's bench or active slot.");
				break;
			case YOUR_BENCH:
				GameEngine.log("Select a Pokémon on your bench.");
				break;
			case YOUR_POKEMON:
				GameEngine.log("Select a Pokémon from your bench or active slot.");
				break;
			case OPPONENT_ACTIVE:
				return autoPlayer.getActivePokemon();
			case YOUR_ACTIVE:
				return player.getActivePokemon();
		}

		waitForInput();
		Message msg = queue.remove();

		Message.ButtonType mb = msg.getType();
		Message.Side ms = msg.getSide();
		int index = msg.getIndex();

		CardManager sourceCardManager = null;
		CardManager otherCardManager = null;
		if(ms == Message.Side.PLAYER){
			sourceCardManager = player.cardManager;
			otherCardManager = autoPlayer.cardManager;
		}
		else{
			sourceCardManager = autoPlayer.cardManager;
			otherCardManager = player.cardManager;
		}

		boolean showSelectButton = false;
		Card cardToBeDisplayed = null;
		switch(mb){
			case ACTIVE:
				cardToBeDisplayed = sourceCardManager.getActivePokemon();
				showSelectButton = ((target == Ability.Target.OPPONENT_ACTIVE || target == Ability.Target.OPPONENT_POKEMON) && ms == Message.Side.AI)
						|| ((target == Ability.Target.YOUR_ACTIVE || target == Ability.Target.YOUR_POKEMON) && ms == Message.Side.PLAYER);
				w.displayCard(cardToBeDisplayed, false, false, false, false, false, false, false, false, showSelectButton);
				break;
			case BENCH:
				if(index < sourceCardManager.getBench().size()){
					cardToBeDisplayed = sourceCardManager.getBench().get(index);
					showSelectButton = ((target == Ability.Target.OPPONENT_BENCH || target == Ability.Target.OPPONENT_POKEMON) && ms == Message.Side.AI)
							|| ((target == Ability.Target.YOUR_BENCH || target == Ability.Target.YOUR_POKEMON) && ms == Message.Side.PLAYER);
					w.displayCard(cardToBeDisplayed, false, false, false, false, false, false, false, false, showSelectButton);
				}
				break;
			case HAND:
				if(index < sourceCardManager.getHand().size()){
					cardToBeDisplayed = sourceCardManager.getHand().get(index);
					w.displayCard(cardToBeDisplayed, false, false, false, false, false, false, false, false, false);
				}
				break;
			case SELECT:
				break;
			default:
				cardToBeDisplayed = w.createCardFromMessage(msg);
				w.displayCard(cardToBeDisplayed, false, false, false, false, false, false, false, false, false);
				break;
		}

		if(mb == Message.ButtonType.SELECT){
			PokemonCard selectedCard = (PokemonCard) w.getDisplayedCard();
			switch(target){
				case OPPONENT_BENCH:
					if(otherCardManager.getBench().contains(selectedCard)){
						return selectedCard;
					}
					break;
				case OPPONENT_POKEMON:
					if(otherCardManager.getBench().contains(selectedCard) || otherCardManager.getActivePokemon() == selectedCard){
						return selectedCard;
					}
					break;
				case YOUR_BENCH:
					if(sourceCardManager.getBench().contains(selectedCard)){
						return selectedCard;
					}
					break;
				case YOUR_POKEMON:
					if(sourceCardManager.getBench().contains(selectedCard) || sourceCardManager.getActivePokemon() == selectedCard){
						return selectedCard;
					}
					break;
			}
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
					GameEngine.log("Please select a card from your hand.");
					s = Message.Side.PLAYER;
				}
				else{
					GameEngine.log("Please select a card from your opponent's hand.");
					s = Message.Side.AI;
				}
				waitForInput();
				Message msg = queue.remove();

				Message.ButtonType mb = msg.getType();
				Message.Side ms = msg.getSide();
				int index = msg.getIndex();

				CardManager sourceCardManager = null;
				if(ms == Message.Side.PLAYER){
					sourceCardManager = player.cardManager;
				}
				else{
					sourceCardManager = autoPlayer.cardManager;
				}

				Card cardToBeDisplayed = null;
				switch(mb){
					case ACTIVE:
						cardToBeDisplayed = sourceCardManager.getActivePokemon();
						w.displayCard(cardToBeDisplayed, false, false, false, false, false, false, false, false, false);
						break;
					case BENCH:
						if(index < sourceCardManager.getBench().size()){
							cardToBeDisplayed = sourceCardManager.getBench().get(index);
							w.displayCard(cardToBeDisplayed, false, false, false, false, false, false, false, false, false);
						}
						break;
					case HAND:
						if(index < sourceCardManager.getHand().size()){
							cardToBeDisplayed = sourceCardManager.getHand().get(index);
							w.displayCard(cardToBeDisplayed, false, false, false, false, false, false, false, false, true);
						}
						break;
					case SELECT:
						if(sourceCardManager.getHand().contains(w.getDisplayedCard())){
							return w.getDisplayedCard();
						}
						break;
					default:
						cardToBeDisplayed = w.createCardFromMessage(msg);
						w.displayCard(cardToBeDisplayed, false, false, false, false, false, false, false, false, false);
						break;
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

	//gui updaters
	public static void log(String text){
		System.out.println(text);
		GameEngine.w.updateInstructions(text);
	}
	public static void updateGUI(){
		w.updateAll();
	}



}