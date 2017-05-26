import java.util.*;

public class Deck {

	private ArrayList<Card> cards;
	
	public Deck(){
		cards = new ArrayList<Card>(60);
	}
	
	public void shuffle(){
		for (int i = 0; i < cards.size(); i++){
			int rand = (int)(Math.random()*(cards.size()));
			Card temp = cards.get(i);
			cards.set(i, cards.get(rand));
			cards.set(rand, temp);
		}
	}
	
	public void push(Card card){
		cards.add(card);
	}
	
	public Card pop(){
		if (!cards.isEmpty()){
			Card card = cards.remove(0);
			return card;
		}
		return null;
	}
	
	public Card getCardAtIndex(int index){
		return cards.get(index);
	}
	
	public void removeCardAtIndex(int index){
		cards.remove(index);
	}
	
}