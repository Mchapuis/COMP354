import java.util.*;

public class GameEngine {
	
	private static AIPlayer autoPlayer = new AIPlayer();
	private static HumanPlayer player = new HumanPlayer();
	
	private static MainWindow w;
	private static Object lock = new Object();
    private static PriorityQueue<Message> queue = new PriorityQueue<>();
    
    private static boolean hasSelectedActive = false;
    private static boolean hasClickedAttach = false;
    private static boolean hasAttachedEnergy = false;
    private static boolean turnOver = false;
    private static boolean mustMoveCardToBottomOfDeck = false;
    private static int energySourceIndex;

	public static void main(String[] args) {        
		/*Parser.readInAbilities();*/
		
		w.lock = lock;
        w.queue = queue;
		
		//instantiate players - this builds their decks, selects a hand and selects 6 prize cards
		
		w = new MainWindow(autoPlayer, player);
		
		w.instructions.setText("Choose a pokemon to be your active pokemon.");
		
		//have AI player select an active pokemon
		autoPlayer.selectActivePokemon();
		w.updateAIActivePokemon();
		
		w.display();
		
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
        Card cardToDisplay;
        boolean showMakeActive = false;
        boolean showAttachToPokemon = false;
        boolean showAttacks = false;
        boolean showLetAIPlay = false;
        
        if (msg.getSide() == Message.Side.PLAYER){
        	if (msg.getType() == Message.ButtonType.ACTIVE){
        		cardToDisplay = player.getActivePokemon();
        		showAttacks = true;
        	} else if (msg.getType() == Message.ButtonType.BENCH){
        		if (player.getBench().isEmpty())
        			cardToDisplay = null;
        		else 
        			cardToDisplay = player.getBench().get(msg.getIndex());
        	} else if (msg.getType() == Message.ButtonType.HAND){
        		cardToDisplay = player.getHand().get(msg.getIndex());
        		if (!hasSelectedActive && cardToDisplay.getClass().toString().equals("class PokemonCard")){
        			showMakeActive = true;
        		} else if (!hasClickedAttach && !hasAttachedEnergy && cardToDisplay.getClass().toString().equals("class EnergyCard")){
        			showAttachToPokemon = true;
        			w.updateInstructions("Now click a pokemon you want to attach the energy card to.");
        		} else if (mustMoveCardToBottomOfDeck){
        			return cardToDisplay;
        		}
        	} else if (msg.getType() == Message.ButtonType.MAKEACTIVE){
        		player.setActivePokemon((PokemonCard)w.getDisplayedCard());
        		w.setPlayerActivePokemon();
        		w.updatePlayerHand();
        		cardToDisplay = player.getActivePokemon();
        		showMakeActive = false;
        		hasSelectedActive = true;
        		w.updateInstructions("(Optional) Click on an energy card to select it. Then click \"Attach to a pokemon\" in the sidebar on the right. If you don't want to attach energy, click on your active pokemon to see its attacks.");
        	} else if (msg.getType() == Message.ButtonType.ATTACHENERGY){
        		EnergyCard energy = (EnergyCard)w.getDisplayedCard();
        		waitForInput();
        		PokemonCard pokemon = (PokemonCard)handleButtonPress();
        		player.attachEnergy(energy, pokemon); 
        		w.updatePlayerSide();
        		cardToDisplay = player.getActivePokemon();
        		showAttachToPokemon = false;
        		hasAttachedEnergy = true;
        		showAttacks = true;
        		w.updateInstructions("Choose an attack to use.");
        	} else if (msg.getType() == Message.ButtonType.ATTACK){
        		String resultString;
        		w.updateAISide();
        		cardToDisplay = autoPlayer.getActivePokemon();
        		if (player.getActivePokemon().hasEnoughEnergy(msg.getIndex())){
        			showAttacks = false;
            		turnOver = true;
        			resultString = player.attack(msg.getIndex(), autoPlayer);
        			w.updateInstructions("Your turn is done. " + resultString);
        		} else {
        			showAttacks = true;
        			w.updateInstructions("Your active pokemon doesn't have enough energy for this attack.");
        		}
        	} else {
        		String resultString = autoPlayer.playTurn(player);
        		w.updateAISide();
        		w.updatePlayerSide();
        		showLetAIPlay = false;
        		w.updateInstructions("AI's turn is done. " + resultString);
        		if (resultString.equals("You must put a card at the bottom of your deck. Click on a card from your hand to do so.")){
					waitForInput();
					mustMoveCardToBottomOfDeck = true;
					Card cardToMoveToDeck = handleButtonPress();
					player.moveCardFromHandToBottomOfDeck(cardToMoveToDeck);
					w.updatePlayerSide();
					w.updateInstructions("(Optional) Click on an energy card to select it. Then click \"Attach to a pokemon\" in the sidebar on the right. If you don't want to attach energy, click on your active pokemon to see its attacks.");
				} else {
					w.updateInstructions("<html><body>" + w.getInstructions() + " (Optional) Click on an energy card to select it. Then click \"Attach to a pokemon\" in the sidebar on the right.<br/>If you don't want to attach energy, click on your active pokemon to see its attacks.</body></html>");
				}
        		hasClickedAttach = false;
        		hasAttachedEnergy = false;
        		turnOver = false;
        		cardToDisplay = null;
        	}
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
        if (turnOver){
        	showLetAIPlay = true;
        }
        w.displayCard(cardToDisplay, showMakeActive, showAttachToPokemon, showAttacks, showLetAIPlay);
        
        return cardToDisplay;
	}

}