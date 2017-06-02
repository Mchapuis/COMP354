import java.util.ArrayList;

public class HumanPlayer extends Player {

	public HumanPlayer(){
		cardManager = new CardManager();
	}
	
	public String attack(int attackIndex){
		String resultString = "";
		Ability ability = getActivePokemon().getAbilities().get(attackIndex);
		resultString = ability.use(Ability.Player.PLAYER);
		return resultString;
	}

}