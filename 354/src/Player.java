import java.util.ArrayList;

public abstract class Player {
	protected CardManager cardManager;

	//turn variables
	boolean hasPlacedEnergy = false;
	boolean hasPlayedSupportCard = false;
	boolean hasPlayedStadiumCard = false;
	boolean hasRetreatedActivePokemon = false;
	boolean turnOver = false;

	public abstract void setup();

	//turn playing algorithm
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
	private void resetTurnVariables(){
		hasPlacedEnergy = false;
		hasPlayedSupportCard = false;
		hasPlayedStadiumCard = false;
		hasRetreatedActivePokemon = false;
		turnOver = false;
	}
	public abstract void takeActions();

	//Individual turn Actions
	public void drawCard(){
		if(cardManager.getDeck().size() > 0){
			this.cardManager.addCardToHandFromDeck(0);
		}
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
	public abstract boolean chooseNewActivePokemon();
	public void attack(int attackIndex){
		Ability ability = getActivePokemon().getAbilities().get(attackIndex);

		if(!getActivePokemon().hasEnoughEnergyForAttack(attackIndex)){
			GameEngine.w.updateInstructions(getActivePokemon().getName() + " does not have enough energy to attack.");
		}
		else if(getActivePokemon().getStatus() == Status.ASLEEP){
			GameEngine.w.updateInstructions(getActivePokemon().getName() + " is asleep and cannot attack.");
		}
		else if(getActivePokemon().getStatus() == Status.PARALYZED){
			GameEngine.w.updateInstructions(getActivePokemon().getName() + " is paralyzed and cannot attack.");
		}
		else{
			GameEngine.w.updateInstructions(getActivePokemon().getName() + " used ability " + ability.name);
			ability.use(GameEngine.getCurrentPlayer());
			turnOver = true;
		}
	}
	public void retreatActive(){
		if(! this.getActivePokemon().hasEnoughEnergyForRetreat()){
			GameEngine.w.updateInstructions(getActivePokemon().getName() + " does not have enough energy to retreat.");
		}
		else if(this.getActivePokemon().getStatus() ==  Status.ASLEEP){
			GameEngine.w.updateInstructions(getActivePokemon().getName() + " is asleep and can't retreat.");
		}
		else if(this.getActivePokemon().getStatus() == Status.PARALYZED){
			GameEngine.w.updateInstructions(getActivePokemon().getName() + " is paralyzed and can't retreat.");
		}
		else if(this.getActivePokemon().getStatus() == Status.STUCK){
			GameEngine.w.updateInstructions(getActivePokemon().getName() + " is stuck and can't retreat.");
		}
		else{
			PokemonCard replacement = GameEngine.choosePokemonCard(this, Ability.Target.YOUR_BENCH);
			GameEngine.w.updateInstructions(getActivePokemon().getName() + " has been replaced with " + replacement.getName());
			this.getActivePokemon().applyStatus(Status.NORMAL);
			this.retreatPokemon(replacement);
			hasRetreatedActivePokemon = true;
		}
	}

	//Getters
	public ArrayList<Card> getHand(){
		return this.cardManager.getHand();
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
	public ArrayList<PokemonCard> getBench(){
		return this.cardManager.getBench();
	}
	public PokemonCard getActivePokemon(){
		return this.cardManager.getActivePokemon();
	}

	//Setters
	public void setActivePokemon(PokemonCard pokemon){
		this.cardManager.setActivePokemon(pokemon);
	}

	//Other
	public boolean movePokemonToBench(PokemonCard pokemon){
		return this.cardManager.movePokemonToBench(pokemon);
	}
}