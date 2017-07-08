import java.util.ArrayList;

public abstract class Player {

	//turn variables
	boolean hasPlacedEnergy = false;
	boolean hasPlayedSupportCard = false;
	boolean hasPlayedStadiumCard = false;
	boolean hasRetreatedActivePokemon = false;
	boolean turnOver = false;

	private void resetTurnVariables(){
		hasPlacedEnergy = false;
		hasPlayedSupportCard = false;
		hasPlayedStadiumCard = false;
		hasRetreatedActivePokemon = false;
		turnOver = false;
	}

	public void playTurn(){
		resetTurnVariables();

		this.drawCard();
        GameEngine.w.updatePlayerSide();

		while(!turnOver){
			takeActions();
			GameEngine.checkForKnockouts();
			GameEngine.w.updateAll();

			if(GameEngine.winnerFound()){
				turnOver = true;
			}
		}


	}

	public abstract void takeActions();

	protected CardManager cardManager;
	
	public abstract void attack(int attackIndex);
	
	public void moveCardFromHandToBottomOfDeck(Card card){
		this.cardManager.moveCardFromHandToBottomOfDeck(card);
	}
	
	public PokemonCard getActivePokemon(){
		return this.cardManager.getActivePokemon();
	}
	
	public ArrayList<Card> getHand(){
		return this.cardManager.getHand();
	}
	
	public ArrayList<PokemonCard> getBench(){
		return this.cardManager.getBench();
	}
	
	public void setActivePokemon(PokemonCard pokemon){
		this.cardManager.setActivePokemon(pokemon);
	}
	
	public void attachEnergy(EnergyCard energy, PokemonCard pokemon){
		this.cardManager.attachEnergy(energy, pokemon);
	}
	
	public void drawCard(){
		if(cardManager.getDeck().size() > 0){
			this.cardManager.addCardToHandFromDeck(0);
		}
	}
	
	public boolean movePokemonToBench(PokemonCard pokemon){
		return this.cardManager.movePokemonToBench(pokemon);
	}
	
	public ArrayList<Card> getDeck(){
		return this.cardManager.getDeck();
	}
	
	public ArrayList<Card> getDiscard(){
		return this.cardManager.getDiscard();
	}
	
	public ArrayList<Card> getPrizeCards(){
		return this.cardManager.getPrizeCards();
	}
	
	public void retreatPokemon(PokemonCard cardToSwapWith){
		if(! hasRetreatedActivePokemon){
			this.cardManager.retreatPokemon(cardToSwapWith);
			hasRetreatedActivePokemon = true;
		}
		else{
			//send failure message
		}
	}

	public abstract void setup();

	public abstract boolean chooseNewActivePokemon();


}