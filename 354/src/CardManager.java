import java.util.*;

public class CardManager {

	private Deck deck;
	private ArrayList<Card> hand;
	private ArrayList<PokemonCard> bench;
	private ArrayList<Card> prizeCards;
	private ArrayList<Card> discardPile;
	private PokemonCard activePokemon;
	
	public CardManager(){
		buildDeck();
		selectHand();
		selectPrizeCards();
		discardPile = new ArrayList<Card>();
		bench = new ArrayList<PokemonCard>();
	}
	
	public ArrayList<Card> getHand(){
		return this.hand;
	}
	
	public ArrayList<PokemonCard> getBench(){
		return this.bench;
	}
	
	public PokemonCard getActivePokemon(){
		return this.activePokemon;
	}
	
	public void buildDeck(){
		deck = new Deck();
		
		ArrayList<Integer> cardNumbers = Parser.readInDeck("deck1.txt");
		
		for (Integer num : cardNumbers){
			Card card = Parser.cards.get(num - 1);
			if (card != null)
				deck.push(card);
		}
		
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
	
	public boolean movePokemonToBench(PokemonCard pokemon){
		if (bench.size() < 5){
			bench.add(pokemon);
			hand.remove(pokemon);
			return true;
		}
		return false;
	}
	
	public void addPrizeCardToHand(Card card){
		hand.add(card);
		prizeCards.remove(card);
	}

	public void addToDiscard(Card card){
		//NOTE: DOES NOT REMOVE CARD FROM ANYTHING
		discardPile.add(card);
	}
	
	public Card getFirstCardOfHand(){
		return this.hand.get(0);
	}
	
	public void moveCardFromHandToBottomOfDeck(Card card){
		this.deck.push(card);
		this.hand.remove(card);
	}
	
	public PokemonCard getFirstCardOfBench(){
		return this.bench.get(0);
	}
	
	public PokemonCard getNextPokemon(int index){
		for (; index < hand.size(); index++){
			Card card = hand.get(index);
			if (card instanceof PokemonCard) return (PokemonCard) card;
		}
		return null;
	}
	
	public ArrayList<Card> getDiscard(){
		return this.discardPile;
	}
	
	public ArrayList<Card> getDeck(){
		return this.deck.getCards();
	}
	
	public ArrayList<Card> getPrizeCards(){
		return this.prizeCards;
	}
	
	public void retreatPokemon(PokemonCard cardToSwapWith){
		int index = bench.indexOf(cardToSwapWith);
		PokemonCard temp = activePokemon;
		setActivePokemon(cardToSwapWith);
		bench.remove(index);
		bench.add(index, temp);
	}
}