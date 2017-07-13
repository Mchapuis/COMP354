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
			PokemonCard nextPoke = cardManager.getNextPokemon(index++);
			if (nextPoke != null) {
			    if(nextPoke.getCat() == PokemonCard.Category.BASIC){
			    	success = movePokemonToBench(nextPoke);
				}
            }
			else {
				success = false;
			}
		}
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
		boolean attacked = false;
		int numberOfAttacks = getActivePokemon().getAbilities().size();
		for (int i = numberOfAttacks - 1; i >= 0; i--){
			if (getActivePokemon().hasEnoughEnergyForAttack(i)){
				attack(i);
				turnOver = true;
				attacked = true;
				break;
			}
		}

		if(!attacked){
			GameEngine.w.updateInstructions(getActivePokemon().getName() + " does not have enough energy for any attack and is forced to pass.");
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