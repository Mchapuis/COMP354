public class AIPlayer extends Player {

	public AIPlayer(){
		cardManager = new CardManager();
	}
	
	public void selectActivePokemon(){
		PokemonCard selectedPokemon = cardManager.getFirstPokemon();
		cardManager.setActivePokemon(selectedPokemon);
	}
	
	public boolean attack(int attackIndex, Player opponent){
		return true;
	}
}