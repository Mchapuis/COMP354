import java.util.ArrayList;

public abstract class Player {
	protected CardManager cardManager;

	//turn Avariables
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
        GameEngine.updateGUI();

		while(!turnOver){
			takeActions();
			GameEngine.checkForKnockouts();
			GameEngine.updateGUI();

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
			GameEngine.log(getActivePokemon().getName() + " does not have enough energy to attack.");
		}
		else if(getActivePokemon().getStatus() == Status.ASLEEP){
			GameEngine.log(getActivePokemon().getName() + " is asleep and cannot attack.");
		}
		else if(getActivePokemon().getStatus() == Status.PARALYZED){
			GameEngine.log(getActivePokemon().getName() + " is paralyzed and cannot attack.");
		}
		else{

			GameEngine.log(getActivePokemon().getName() + " used ability " + ability.name);

			ability.use(GameEngine.getCurrentPlayer());
			turnOver = true;
		}
	}
	public void retreatActive(){
		if(! this.getActivePokemon().hasEnoughEnergyForRetreat()){
			GameEngine.log(getActivePokemon().getName() + " does not have enough energy to retreat.");
		}
		else if(this.getActivePokemon().getStatus() ==  Status.ASLEEP){
			GameEngine.log(getActivePokemon().getName() + " is asleep and can't retreat.");
		}
		else if(this.getActivePokemon().getStatus() == Status.PARALYZED){
			GameEngine.log(getActivePokemon().getName() + " is paralyzed and can't retreat.");
		}
		else if(this.getActivePokemon().getStatus() == Status.STUCK){
			GameEngine.log(getActivePokemon().getName() + " is stuck and can't retreat.");
		}
		else{
			PokemonCard replacement = GameEngine.choosePokemonCard(this, Ability.Target.YOUR_BENCH);
			GameEngine.log(getActivePokemon().getName() + " has been replaced with " + replacement.getName());
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