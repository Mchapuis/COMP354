import java.util.ArrayList;

public abstract class Player {

	protected CardManager cardManager;
	
	public abstract String attack(int attackIndex, Player opponent);
	
	public void moveCardFromHandToBottomOfDeck(Card card){
		this.cardManager.moveCardFromHandToBottomOfDeck(card);
	}
	
	public PokemonCard getActivePokemon(){
		return this.cardManager.getActivePokemon();
	}
	
	public ArrayList<Card> getHand(){
		return this.cardManager.getHand();
	}
	
	public ArrayList<PokemonCard> getBench(){
		return this.cardManager.getBench();
	}
	
	public void setActivePokemon(PokemonCard pokemon){
		this.cardManager.setActivePokemon(pokemon);
	}
	
	public void attachEnergy(EnergyCard energy, PokemonCard pokemon){
		this.cardManager.attachEnergy(energy, pokemon);
	}
	
}