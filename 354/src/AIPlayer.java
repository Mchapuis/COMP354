public class AIPlayer extends Player {

	public AIPlayer(){
		cardManager = new CardManager();
	}
	
	public void selectActivePokemon(MainWindow w){
		PokemonCard selectedPokemon = cardManager.getFirstPokemon();
		int index = cardManager.hand.indexOf(selectedPokemon);
		cardManager.setActivePokemon(selectedPokemon);
		w.AIActivePokemon = new GUICard(selectedPokemon);
		w.AIHand.remove(index);
		w.updateAIActivePokemon();
		w.updateAIHandContainer();
	}
	
	public void playTurn(){
		PokemonCard selectedPokemon = cardManager.getActivePokemon();
		EnergyCard selectedEnergy = cardManager.getFirstEnergy();
		cardManager.attachEnergy(selectedEnergy, selectedPokemon);
		cardManager.getActivePokemon().attack();
	}
	
}