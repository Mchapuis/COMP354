import java.util.ArrayList;

public class HumanPlayer extends Player {

	public HumanPlayer(){
		cardManager = new CardManager();
	}
	
	public String attack(int attackIndex, Player opponent){
		String resultString = "";
		Attack attack = getActivePokemon().getAttacks().get(attackIndex);
		AIPlayer op = (AIPlayer)opponent;
		PokemonCard activePokemon = getActivePokemon();
		
		if (!activePokemon.hasEnoughEnergy(attackIndex)){
			return resultString;
		}
		
		String target = attack.getTarget();
		if (target.equals("OPPONENTACTIVE")){
			PokemonCard targetObj = op.getActivePokemon();
			int damagePoints = attack.getDamagePoints();
			if (damagePoints > 0){
				targetObj.removeHP(damagePoints);
				resultString += "Opponent's active pokemon lost " + damagePoints + " HP. ";
			}
			if (attack.getFlipRequired()){
				boolean flip = RandomNumberGenerator.flipACoin();
				if (flip){
					resultString += "Coin flip returned heads. ";
					String statusToApply = attack.getStatusToApply();
					if (!statusToApply.equals("NONE")){
						resultString += "Status " + statusToApply + " was applied to opponent's active pokemon. ";
						targetObj.applyStatus(statusToApply);
					} else {
						String additionalTarget = attack.getAdditionalTarget();
						
						if (additionalTarget.equals("OPPONENTACTIVE")){
							PokemonCard additionalTargetObj = op.getActivePokemon();
							int additionalDamage = attack.getAdditionalDamagePoints();
							if (additionalDamage > 0){
								additionalTargetObj.removeHP(additionalDamage);
								resultString += "Opponent's active pokemon lost another " + additionalDamage + " points. ";
							}
						}						
					}
				} else {
					resultString += "Coin flip returned tails. ";
				}
			}
		} else if (target.equals("OPPONENTHAND")){
			if (attack.getDestination().equals("BOTTOMOFDECK")){
				op.moveCardFromHandToBottomOfDeck();
				resultString += "Opponent moved one card from their hand to the bottom of their deck.";
			}
		} else {
			Object targetObj = null;
		}
		
		return resultString;
	}

}