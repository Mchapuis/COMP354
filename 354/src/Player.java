import java.util.ArrayList;

public abstract class Player {

	protected CardManager cardManager;
	
	public abstract String attack(int attackIndex);
	
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
	
	public void drawCard(){
		this.cardManager.addCardToHandFromDeck(0);
	}
	
	public boolean movePokemonToBench(PokemonCard pokemon){
		return this.cardManager.movePokemonToBench(pokemon);
	}
	
	public ArrayList<Card> getDeck(){
		return this.cardManager.getDeck();
	}
	
	public ArrayList<Card> getDiscard(){
		return this.cardManager.getDiscard();
	}
	
	public ArrayList<Card> getPrizeCards(){
		return this.cardManager.getPrizeCards();
	}
	
	public void retreatPokemon(PokemonCard cardToSwapWith){
		this.cardManager.retreatPokemon(cardToSwapWith);
	}
	
	public void collectPrizeCard(){
		this.cardManager.collectPrizeCard();
	}
	
	public PokemonCard getFirstPokemon(){
		return this.cardManager.getFirstPokemon();
	}
	
	public void selectHand(){
		this.cardManager.selectHand();
	}
	
	public PokemonCard getFirstCardOfBench(){
		return this.cardManager.getFirstCardOfBench();
	}
}