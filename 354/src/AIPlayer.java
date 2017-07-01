public class AIPlayer extends Player {

	public AIPlayer(){
		cardManager = new CardManager();
	}
	
	public PokemonCard getActivePokemon(){
		return this.cardManager.getActivePokemon();
	}
	
	public void selectActivePokemon(){
		for(Card c : getHand()){
			if(c instanceof PokemonCard){
				PokemonCard pc = (PokemonCard) c;
				if(pc.getCat() == PokemonCard.Category.BASIC){
					setActivePokemon(pc);
					break;
				}
			}
		}
	}
	
	public void moveAllPokemonToBench(){
		int index = 0;
		boolean success = true;
		while (success == true){
			PokemonCard nextPoke = cardManager.getNextPokemon(index);
			if (nextPoke != null) success = movePokemonToBench(nextPoke);
			else success = false;
		}
	}
	
	public void moveCardFromHandToBottomOfDeck(){
		Card firstCard = cardManager.getFirstCardOfHand();
		cardManager.moveCardFromHandToBottomOfDeck(firstCard);
	}

	public String attack(int attackIndex){
		String resultString = "";
		Ability ability = getActivePokemon().getAbilities().get(attackIndex);

		if(!getActivePokemon().hasEnoughEnergyForAttack(attackIndex)){
			GameEngine.w.updateInstructions(getActivePokemon().getName() + " does not have enough energy to attack.");
		}
		else if(getActivePokemon().getStatus() == Status.ASLEEP){
			GameEngine.w.updateInstructions(getActivePokemon().getName() + " is asleep and cannot attack.");
		}
		else if(getActivePokemon().getStatus() == Status.PARALYZED && RandomNumberGenerator.flipACoin()){
			GameEngine.w.updateInstructions(getActivePokemon().getName() + " is paralyzed and cannot attack.");
		}
		else{
			resultString = ability.use(Ability.Player.AI);
			turnOver = true;
		}
		return resultString;
	}

	public void takeActions(){
		//attach first energy in hand to active pokemon
		EnergyCard firstEnergy = cardManager.getFirstEnergy();
		if (firstEnergy != null && hasPlacedEnergy == false){
			cardManager.attachEnergy(firstEnergy, getActivePokemon());
			hasPlacedEnergy = true;
		}

		//move all pokemon available to bench
		moveAllPokemonToBench();

		//use first attack with available energy
		int numberOfAttacks = getActivePokemon().getAbilities().size();
		for (int i = numberOfAttacks - 1; i >= 0; i--){
			String resultString = attack(i);
			if (!resultString.equals("")){
				turnOver = true;
				break;
			}
		}

		turnOver = true;
	}

	public void setup(){
		this.selectActivePokemon();
		this.moveAllPokemonToBench();

		GameEngine.w.updateAISide();
	}

	public boolean chooseNewActivePokemon(){
		if(getBench().size() > 0){
		    PokemonCard newActive = getBench().remove(0);
		    setActivePokemon(newActive);
		    return true;
        }
        else{
		    return false;
        }
	}
}