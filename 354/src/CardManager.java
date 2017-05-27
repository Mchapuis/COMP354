import java.awt.Window.Type;
import java.util.*;

public class CardManager {

	private Deck deck;
	public ArrayList<Card> hand;
	private ArrayList<PokemonCard> bench;
	private ArrayList<Card> prizeCards;
	private ArrayList<Card> discardPile;
	private PokemonCard activePokemon;
	
	public CardManager(){
		buildDeck();
		selectHand();
		selectPrizeCards();
	}
	
	public void buildDeck(){
		deck = new Deck();
		
		for (int i = 0; i < 14; i++){
			EnergyCard energyCard = new EnergyCard("COLORLESS");
			deck.push(energyCard);
		}
		
		// hardcoding Pikachu card
		EnergyCard energyCard = new EnergyCard("COLORLESS");
		HashMap<EnergyCard, Integer> retreatMapPikachu = new HashMap<EnergyCard, Integer>();
		retreatMapPikachu.put(energyCard, 1);
		PokemonCard pikachu = new PokemonCard("Pikachu", "A lightning-type pokemon.", "BASIC", "LIGHTNING", 60, retreatMapPikachu);
		deck.push(pikachu);
		
		// hardcoding Glameow card
		energyCard = new EnergyCard("COLORLESS");
		HashMap<EnergyCard, Integer> retreatMapGlameow = new HashMap<EnergyCard, Integer>();
		retreatMapGlameow.put(energyCard, 2);
		PokemonCard glameow = new PokemonCard("Glameow", "A normal-type pokemon.", "BASIC", "NORMAL", 60, retreatMapGlameow);
		deck.push(glameow);
		
		/*System.out.println(deck.size());*/
		
		//last step: shuffle
		deck.shuffle();
	}
	
	public void selectHand(){
		hand = new ArrayList<Card>();
		
		for (int i = 0; i < 7; i++){
			Card card = deck.pop();
			hand.add(card);
		}
		
		while (getFirstPokemon() == null){
			Iterator<Card> it = hand.iterator();
			
			while (it.hasNext()){
				Card card = it.next();
				deck.push(card);
				it.remove();
			}
			
			deck.shuffle();
			for (int i = 0; i < 7; i++){
				Card card = deck.pop();
				hand.add(card);
			}
		}
	}
	
	public void selectPrizeCards(){
		prizeCards = new ArrayList<Card>(6);
		
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
	
	public void removeActivePokemon(){
		discardActivePokemon();
		activePokemon = null;
	}
	
	//returns the active pokemon
	public PokemonCard getActivePokemon(){
		return activePokemon;
	}
	
	public void discardActivePokemon(){
		ArrayList<EnergyCard> energy = activePokemon.getEnergy();
		for (EnergyCard card : energy){
			discardPile.add(card);
			activePokemon.removeEnergy(card);
		}
		discardPile.add(activePokemon);
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
	
	public void addCardToHandFromDeck(int index){
		Card card = deck.getCardAtIndex(index);
		hand.add(card);
		deck.removeCardAtIndex(index);
	}
	
	public void addCardToHandFromDiscard(int index){
		Card card = discardPile.get(index);
		hand.add(card);
		discardPile.remove(index);
	}
	
	public void discardFromHand(int index){
		Card card = hand.get(index);
		discardPile.add(card);
		hand.remove(index);
	}
	
	public void discardFromBench(PokemonCard pokemon){
		discardPile.add(pokemon);
		hand.remove(pokemon);
	}
	
	public void movePokemonToBench(PokemonCard pokemon){
		bench.add(pokemon);
		hand.remove(pokemon);
	}
	
	public void addPrizeCardToHand(Card card){
		hand.add(card);
		prizeCards.remove(card);
	}
}