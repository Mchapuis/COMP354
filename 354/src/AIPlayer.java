public class AIPlayer extends Player {

	public AIPlayer(){
		cardManager = new CardManager();
	}
	
	public void selectActivePokemon(){
		PokemonCard selectedPokemon = cardManager.getFirstPokemon();
		cardManager.setActivePokemon(selectedPokemon);
	}
	
	public void playTurn(){
		/*PokemonCard selectedPokemon = cardManager.getActivePokemon();
		EnergyCard selectedEnergy = cardManager.getFirstEnergy();
		cardManager.attachEnergy(selectedEnergy, selectedPokemon);
		cardManager.getActivePokemon().attack();*/
	}
	
}