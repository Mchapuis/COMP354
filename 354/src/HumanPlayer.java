import java.util.ArrayList;

public class HumanPlayer extends Player {

	public HumanPlayer(){
		cardManager = new CardManager();
	}
	
	public boolean attack(int attackIndex, Player opponent){
		Attack attack = this.cardManager.activePokemon.attacks.get(attackIndex);
		AIPlayer op = (AIPlayer)opponent;
		PokemonCard activePokemon = this.cardManager.activePokemon;
		
		if (!activePokemon.hasEnoughEnergy(attackIndex)){
			return false;
		}
		
		String target = attack.getTarget();
		if (target.equals("OPPONENTACTIVE")){
			PokemonCard targetObj = op.cardManager.activePokemon;
			int damagePoints = attack.getDamagePoints();
			if (damagePoints > 0){
				targetObj.removeHP(damagePoints);
			}
		} else if (target.equals("OPPONENTHAND")){
			ArrayList<Card> targetObj = op.cardManager.hand;
		} else {
			Object targetObj = null;
		}
		
		return true;
	}

}