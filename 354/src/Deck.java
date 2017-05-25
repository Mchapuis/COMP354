import java.util.*;

public class Deck {

	private List<Card> cards;
	
	public void shuffle(){
		
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
	
}