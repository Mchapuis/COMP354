public class AIPlayer extends Player {

	public AIPlayer(){
		cardManager = new CardManager();
	}
	
	public void selectActivePokemon(){
		PokemonCard selectedPokemon = cardManager.getFirstPokemon();
		cardManager.setActivePokemon(selectedPokemon);
	}
	
	public void moveCardFromHandToBottomOfDeck(){
		Card firstCard = cardManager.getFirstCardOfHand();
		cardManager.moveCardFromHandToBottomOfDeck(firstCard);
	}
	
	public String playTurn(Player opponent){
		EnergyCard firstEnergy = cardManager.getFirstEnergy();
		if (firstEnergy != null){
			cardManager.attachEnergy(firstEnergy, cardManager.activePokemon);
		}
		
		String resultString = "";
		int numberOfAttacks = cardManager.activePokemon.attacks.size();
		for (int i = numberOfAttacks - 1; i >= 0; i--){
			resultString = attack(i, opponent);
			if (!resultString.equals("")){
				break;
			}
		}
		
		return resultString;
	}
	
	public String attack(int attackIndex, Player opponent){
		String resultString = "";
		Attack attack = this.cardManager.activePokemon.attacks.get(attackIndex);
		HumanPlayer op = (HumanPlayer)opponent;
		PokemonCard activePokemon = this.cardManager.activePokemon;
		
		if (!activePokemon.hasEnoughEnergy(attackIndex)){
			return resultString;
		}
		
		String target = attack.getTarget();
		if (target.equals("OPPONENTACTIVE")){
			PokemonCard targetObj = op.cardManager.activePokemon;
			int damagePoints = attack.getDamagePoints();
			if (damagePoints > 0){
				targetObj.removeHP(damagePoints);
				resultString += "Your active pokemon lost " + damagePoints + " HP. ";
			}
			if (attack.getFlipRequired()){
				boolean flip = RandomNumberGenerator.flipACoin();
				if (flip){
					resultString += "Coin flip returned heads. ";
					String statusToApply = attack.getStatusToApply();
					if (!statusToApply.equals("NONE")){
						resultString += "Status " + statusToApply + " was applied to your active pokemon. ";
						targetObj.applyStatus(statusToApply);
					} else {
						String additionalTarget = attack.getAdditionalTarget();
						
						if (additionalTarget.equals("OPPONENTACTIVE")){
							PokemonCard additionalTargetObj = op.cardManager.activePokemon;
							int additionalDamage = attack.getAdditionalDamagePoints();
							if (additionalDamage > 0){
								additionalTargetObj.removeHP(additionalDamage);
								resultString += "Your active pokemon lost another " + additionalDamage + " points. ";
							}
						}						
					}
				} else {
					resultString += "Coin flip returned tails. ";
				}
			}
		} else if (target.equals("OPPONENTHAND")){
			if (attack.getDestination().equals("BOTTOMOFDECK")){
				resultString = "You must put a card at the bottom of your deck. Click on a card from your hand to do so.";
			}
		} else {
			Object targetObj = null;
		}
		
		return resultString;
	}
}