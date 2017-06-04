public class AIPlayer extends Player {

	public AIPlayer(){
		cardManager = new CardManager();
	}
	
	public PokemonCard getActivePokemon(){
		return this.cardManager.getActivePokemon();
	}
	
	public void selectActivePokemon(){
		PokemonCard selectedPokemon = cardManager.getFirstPokemon();
		cardManager.setActivePokemon(selectedPokemon);
	}
	
	public void moveCardFromHandToBottomOfDeck(){
		Card firstCard = cardManager.getFirstCardOfHand();
		cardManager.moveCardFromHandToBottomOfDeck(firstCard);
	}
	
	public String playTurn(){
		EnergyCard firstEnergy = cardManager.getFirstEnergy();
		if (firstEnergy != null){
			cardManager.attachEnergy(firstEnergy, getActivePokemon());
		}
		
		String resultString = "";
		int numberOfAttacks = getActivePokemon().getAbilities().size();
		for (int i = numberOfAttacks - 1; i >= 0; i--){
			resultString = attack(i);
			if (!resultString.equals("")){
				break;
			}
		}
		
		return resultString;
	}

	
	
	public String attack(int attackIndex){
		String resultString = "";
		Ability ability = getActivePokemon().getAbilities().get(attackIndex);
		if (getActivePokemon().hasEnoughEnergy(attackIndex)){
			resultString = ability.use(Ability.Player.AI);
		}
		return resultString;
	}
}