import java.util.*;

public class GameEngine {
	
	//instantiate players - this builds their decks, selects a hand and selects 6 prize cards
	private static AIPlayer autoPlayer = new AIPlayer();
	private static HumanPlayer player = new HumanPlayer();
	
	private static MainWindow w;
	private static Object lock = new Object();
    private static PriorityQueue<Message> queue = new PriorityQueue<>();
    
    private static boolean hasSelectedActive = false;
    private static boolean hasClickedAttach = false;
    private static boolean hasAttachedEnergy = false;
    private static boolean mustMoveCardToBottomOfDeck = false;
    private static boolean mustChoosePokemonToSwap = false;
    private static boolean mustClickOnDeck = false;
    private static boolean playerMulligan = false;

	public static void main(String[] args) {        
		MainWindow.lock = lock;
        MainWindow.queue = queue;
        
        w = new MainWindow(autoPlayer, player);
        
        while (autoPlayer.getFirstPokemon() == null && player.getFirstPokemon() == null) {
        	autoPlayer.selectHand();
        	player.selectHand();
        } 
        
        while (autoPlayer.getFirstPokemon() == null){
        	w.updateInstructions("AI player called for a mulligan. Click on your deck to draw an extra card.");
        
        	playerMulligan = false;
        	mustClickOnDeck = true;
        	waitForInput();
        	Card card = handleButtonPress();
        	while (card == null){
        		waitForInput();
        		card = handleButtonPress();
        	}
        	
        	autoPlayer.selectHand();
        }
        
        while (player.getFirstPokemon() == null) {
        	w.updateInstructions("Your hand has no pokemon cards. Click on your deck to call a mulligan.");
        	
        	playerMulligan = true;
        	mustClickOnDeck = true;
        	waitForInput();
        	Card card = handleButtonPress();
        	while (card == null){
        		waitForInput();
        		card = handleButtonPress();
        	}
        	
        	player.selectHand();
        }
        
        autoPlayer.selectActivePokemon();
		autoPlayer.moveAllPokemonToBench();
		w.updateAIActivePokemon();
		w.updateAIHand();
		w.updateAIBench();
		
		w.display();
		
        Ability.playerCardManager = player.cardManager;
        Ability.AICardManager = autoPlayer.cardManager;
		
		w.updateInstructions("Choose a pokemon to be your active pokemon.");
		
		while(true){
            if(queue.isEmpty()){
                waitForInput();
            }
            
            handleButtonPress();
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
	
	public static Card handleButtonPress(){
		Message msg = queue.remove();
		queue.clear();
		
        Card cardToDisplay;
        boolean showMakeActive = false;
        boolean showAttachToPokemon = false;
        boolean showAddToBench = false;
        boolean showAttacks = false;
        boolean showLetAIPlay = false;
        boolean showRetreat = false;
        
        /* if this move is supposed to choose a card from the hand to move to the deck */
        if (mustMoveCardToBottomOfDeck){
        	if (msg.getSide() == Message.Side.PLAYER && msg.getType() == Message.ButtonType.HAND){
        		mustMoveCardToBottomOfDeck = false;
        		return player.getHand().get(msg.getIndex());
        	}
        	return null;
		}
        
        if (mustChoosePokemonToSwap){
        	if (msg.getSide() == Message.Side.PLAYER && msg.getType() == Message.ButtonType.BENCH){
        		mustChoosePokemonToSwap = false;
        		return player.getBench().get(msg.getIndex());
        	}
        	return null;
        }
        
        if (mustClickOnDeck){
        	if (msg.getSide() == Message.Side.PLAYER && msg.getType() == Message.ButtonType.DECK){
        		if (playerMulligan) {
        			autoPlayer.drawCard();
        		} else {
        			player.drawCard();
        		}
        		mustClickOnDeck = false;
        	}
        }
        
        /* if player side clicked */
        if (msg.getSide() == Message.Side.PLAYER){
        	
        	/* if active pokemon clicked */
        	if (msg.getType() == Message.ButtonType.ACTIVE){
        		/* set the card that will be displayed */
        		cardToDisplay = player.getActivePokemon();
        		
        		/* set showAttacks to true so that attack buttons will be shown */
        		showAttacks = true;
        		showRetreat = true;
        		
        	/* if bench clicked */
        	} else if (msg.getType() == Message.ButtonType.BENCH){
        		/* set the card that will be displayed */
        		if (player.getBench().isEmpty())
        			cardToDisplay = null;
        		else 
        			cardToDisplay = player.getBench().get(msg.getIndex());
        		
        	/* if hand clicked */
        	} else if (msg.getType() == Message.ButtonType.HAND){
        		/* set the card that will be displayed */
        		cardToDisplay = player.getHand().get(msg.getIndex());
        		
        		/* if no active pokemon has been selected yet and the card that was clicked on is a pokemon card
        		 * from the hand, show the option to make this card the active pokemon */
        		if (!hasSelectedActive && cardToDisplay.getClass().toString().equals("class PokemonCard")){
        			showMakeActive = true;
        			
        		/* if energy has not yet been attached this turn and the card clicked on is an energy card, show the 
        		 * option to attach this energy card to a pokemon */
        		} else if (hasSelectedActive && cardToDisplay.getClass().toString().equals("class PokemonCard")){
        			showAddToBench = true;
        		} else if (!hasClickedAttach && !hasAttachedEnergy && cardToDisplay.getClass().toString().equals("class EnergyCard")){
        			showAttachToPokemon = true;
        			w.updateInstructions("Now click a pokemon you want to attach the energy card to.");
        		}
        		
        	/* if deck clicked */
        	} else if (msg.getType() == Message.ButtonType.DECK){
        		cardToDisplay = makeCardFromPile(player.getDeck());
        		
        	/* if "make active pokemon" button clicked */
        	} else if (msg.getType() == Message.ButtonType.MAKEACTIVE){
        		/* actually set the player's active pokemon */
        		player.setActivePokemon((PokemonCard)w.getDisplayedCard());
        		
        		/* update the view */
        		w.setPlayerActivePokemon();
        		w.updatePlayerHand();
        		w.updateInstructions("(Optional) Click on an energy card to select it. Then click \"Attach to a pokemon\" in the sidebar on the right. "
        				+ "If you don't want to attach energy, click on your active pokemon to see its attacks. You may also add po");
        		
        		/* set the card to display */
        		cardToDisplay = player.getActivePokemon();
        		
        		/* set some necessary values */
        		showMakeActive = false;
        		hasSelectedActive = true;
        		
        	/* if "attach to a pokemon clicked */
        	} else if (msg.getType() == Message.ButtonType.ADDTOBENCH){
        		cardToDisplay = player.getHand().get(msg.getIndex());
        		
        		player.movePokemonToBench((PokemonCard)w.getDisplayedCard());
        		
        		w.updatePlayerHand();
        		w.updatePlayerBench();
        	} else if (msg.getType() == Message.ButtonType.ATTACHENERGY){
        		/* get the energy card that was clicked on */
        		EnergyCard energy = (EnergyCard)w.getDisplayedCard();
        		
        		/* wait for more user input: they still have to click a pokemon to attach the energy to */
        		waitForInput();
        		
        		/* get the pokemon that was clicked */
        		PokemonCard pokemon = (PokemonCard)handleButtonPress();
        		
        		/* carry out the attach operation */
        		player.attachEnergy(energy, pokemon); 
        		
        		/* update view */
        		w.updatePlayerSide();
        		w.updateInstructions("Choose an attack to use.");
        		
        		/* set the card to display */
        		cardToDisplay = player.getActivePokemon();
        		
        		/* set some necessary values */
        		showAttachToPokemon = false;
        		hasAttachedEnergy = true;
        		showAttacks = true;
        		
        	/* if attack button clicked */
        	} else if (msg.getType() == Message.ButtonType.ATTACK){
        		String resultString;
        		
        		/* get the active pokemon */
        		cardToDisplay = autoPlayer.getActivePokemon();
        		
        		/* if the pokemon has enough energy for the attack that was clicked */
        		if (player.getActivePokemon().hasEnoughEnergyForAttack(msg.getIndex())){
        			/* set some necessary values */
        			showAttacks = false;
            		
            		/* carry out the attack */
        			resultString = player.attack(msg.getIndex());
        			
        			if (autoPlayer.getActivePokemon().getKnockedOut()){
        				player.collectPrizeCard();
        				resultString += " The opponent's active pokemon was knocked out! A prize card has been added to your hand.";
        				if (player.getPrizeCards().size() == 0) {
        					resultString += "<br/>YOU WON!!!!!!!";
        				}
        			}
        			
        			/* update the view */
        			w.updateInstructions("Your turn is done. " + resultString);
        		} else {
        			/* continue to show the attack buttons */
        			showAttacks = true;
        			
        			/* update the view to notify the user that their pokemon doesn't have enough energy */
        			w.updateInstructions("Your active pokemon doesn't have enough energy for this attack.");
        		}
        		
        		/* update the view */
        		w.updateAISide();
        		
        	/* if "let AI play" button clicked */
        	} else if (msg.getType() == Message.ButtonType.LETAIPLAY) {
        		/* get the result of AI playing a turn */
        		String resultString = autoPlayer.playTurn();
        		
        		if (player.getActivePokemon().getKnockedOut()) {
        			autoPlayer.collectPrizeCard();
        			resultString += "Your active pokemon was knocked out! Your opponent collected a prize card.";
        			
        			if (autoPlayer.getPrizeCards().size() == 0){
        				resultString += "<br/>You lost the game.";
        			}
        		}
        		
        		/* update the view */
        		w.updateAISide();
        		w.updatePlayerSide();
        		if (resultString.equals(""))
        			resultString = "No attack was carried out. ";
        		w.updateInstructions("AI's turn is done. " + resultString);
        		
        		/* hide the "let AI play" button */
        		showLetAIPlay = false;
        		
        		/* if AI's pokemon's attack forces the player to transfer a card to the bottom of their deck */
        		if (resultString.equals("You must put a card at the bottom of your deck. Click on a card from your hand to do so.")){
        			/* wait for further input: user must click on a card from their hand */
					waitForInput();
					
					/* set a necessary value & get the next card from the hand that's clicked */
					mustMoveCardToBottomOfDeck = true;
					Card cardToMoveToDeck = handleButtonPress();
					while (cardToMoveToDeck == null) {
	        			waitForInput();
	        			cardToMoveToDeck = handleButtonPress();
	        		}
					
					/* carry out the moving of the card */
					player.moveCardFromHandToBottomOfDeck(cardToMoveToDeck);
					
					/* update the view */
					w.updatePlayerSide();
					w.updateInstructions("(Optional) Click on an energy card to select it. Then click \"Attach to a pokemon\" in the sidebar on the right. If you don't want to attach energy, click on your active pokemon to see its attacks.");
				} else {
					/* update the view */
					w.updateInstructions("<html><body>" + w.getInstructions() + " (Optional) Click on an energy card to select it. Then click \"Attach to a pokemon\" in the sidebar on the right.<br/>If you don't want to attach energy, click on your active pokemon to see its attacks.</body></html>");
				}
        		
        		/* reset values */
        		hasClickedAttach = false;
        		hasAttachedEnergy = false;
        		cardToDisplay = null;
        		
        		/* draw a card */
        		player.drawCard();
        		w.updatePlayerHand();
        		
        	/* else if retreat button clicked */
        	} else {
        		String resultString;
        		
        		if (player.getActivePokemon().hasEnoughEnergyForRetreat()){
	        		resultString = "Now click a pokemon from the bench to swap places with the active pokemon.";
	        		w.updateInstructions(resultString);
	        		
	        		mustChoosePokemonToSwap = true;
	        		
	        		waitForInput();
	        		PokemonCard pokeToSwap = (PokemonCard) handleButtonPress();
	        		while (pokeToSwap == null) {
	        			waitForInput();
	        			pokeToSwap = (PokemonCard) handleButtonPress();
	        		}
	        		
	        		player.retreatPokemon(pokeToSwap);
	        		
	        		resultString = "Active pokemon and benched pokemon were swapped.";
	        		
	        		w.updatePlayerActivePokemon();
	        		w.updatePlayerBench();
        		} else {
        			resultString = "The active pokemon does not have enough energy to retreat.";
        		}
        		
        		cardToDisplay = player.getActivePokemon();
        		w.updateInstructions(resultString);
        	}
        
        /* if AI side clicked just display the card */
        } else {
        	if (msg.getType() == Message.ButtonType.ACTIVE){
        		cardToDisplay = autoPlayer.getActivePokemon();
        	} else if (msg.getType() == Message.ButtonType.BENCH){
        		if (autoPlayer.getBench().isEmpty())
        			cardToDisplay = null;
        		else 
        			cardToDisplay = autoPlayer.getBench().get(msg.getIndex());
        	} else {
        		cardToDisplay = autoPlayer.getHand().get(msg.getIndex());
        	}
        }
        
        /* if the player's turn is over, show the "let AI play" button */
        if (hasSelectedActive){
        	showLetAIPlay = true;
        }
        
        /* display the card that was clicked on */
        w.displayCard(cardToDisplay, showMakeActive, showAddToBench, showAttachToPokemon, showAttacks, showLetAIPlay, showRetreat);
        
        return cardToDisplay;
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
		
		if (p.equals(player)){
			waitForInput();
			cardToReturn = getChoiceOfCard(p, target);
			while (cardToReturn == null) {
				cardToReturn = getChoiceOfCard(p, target);
			}
		} else {
			if (target == Ability.Target.OPPONENT_BENCH){
				cardToReturn = player.getFirstCardOfBench();
			} else if (target == Ability.Target.YOUR_BENCH){
				cardToReturn = autoPlayer.getFirstCardOfBench();
			} else if (target == Ability.Target.OPPONENT_POKEMON){
				cardToReturn = player.getActivePokemon();
			} else {
				cardToReturn = autoPlayer.getFirstCardOfBench();
			}
		}
		return cardToReturn;
	}
	
	public static Card makeCardFromPile(ArrayList<Card> pile){
		Card card = new GenericCard(pile.size());
		return card;
	}
	
}