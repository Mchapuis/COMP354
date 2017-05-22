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
		
		//last step: shuffle
		deck.shuffle();
	}
	
	public void selectHand(){
		for (int i = 0; i < 7; i++){
			Card card = deck.pop();
			hand.add(card);
		}
		while (getFirstPokemon() == null){
			for (Card card : hand){
				deck.push(card);
				hand.remove(card);
			}
			deck.shuffle();
			for (int i = 0; i < 7; i++){
				Card card = deck.pop();
				hand.add(card);
			}
		}
	}
	
	public void selectPrizeCards(){
		for (int i = 0; i < 6; i++){
			Card card = deck.pop();
			prizeCards.add(card);
		}
	}
	
	//returns first pokemon in the hand
	public PokemonCard getFirstPokemon(){
		for (Card card : hand){
			if (card instanceof PokemonCard) return (PokemonCard) card;
		}
		return null;
	}
	
	public void setActivePokemon(PokemonCard pokemon){
		activePokemon = pokemon;
		hand.remove(pokemon);
	}
	
	//returns the active pokemon
	public PokemonCard getActivePokemon(){
		return activePokemon;
	}
	
	//returns first energy in the hand 
	public EnergyCard getFirstEnergy(){
		for (Card card : hand){
			if (card instanceof EnergyCard) return (EnergyCard) card;
		}
		return null;
	}
	
	public void attachEnergy(EnergyCard energy, PokemonCard pokemon){
		pokemon.attachEnergy(energy);
		hand.remove(energy);
	}
	
}