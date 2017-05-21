import java.util.*;

public class CardManager {

	private Deck deck;
	private List<Card> hand;
	private List<PokemonCard> bench;
	private List<Card> prizeCards;
	private List<Card> discardPile;
	private PokemonCard activePokemon;
	
	public CardManager(){
		buildDeck();
		selectHand();
		selectPrizeCards();
	}
	
	public void buildDeck(){
		
	}
	
	public void selectHand(){
		
	}
	
	public void selectPrizeCards(){
		
	}
	
	//returns first pokemon in the hand
	public PokemonCard getFirstPokemon(){
		
	}
	
	public void setActivePokemon(PokemonCard pokemon){
		
	}
	
	//returns the active pokemon
	public PokemonCard getActivePokemon(){
		
	}
	
	//returns first energy in the hand 
	public EnergyCard getFirstEnergy(){
		
	}
	
	public void attachEnergy(EnergyCard energy, PokemonCard pokemon){
		
	}
	
}