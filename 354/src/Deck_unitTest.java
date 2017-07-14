import static org.junit.Assert.*;

import org.junit.Test;

//public class Deck_unitTest {
//
//	@Test
//	public void test() {
//		fail("Not yet implemented");
//	}
//
//}
//
//import org.junit.Test;
//
//public class Deck_unitTest {
//
//	@Test
//	public void test() {
//		Deck aDeck = new Deck();
//		
//		// testing if null
//		assertNotNull(aDeck);
//		
//		// FAILED TEST -- should be NotNull
//		// testing if return a Card when pop() called -- shouldn't be empty when Deck is created
//		Card aCard = aDeck.pop();
//		assertNull(aCard);
//		
//		// FAILED TEST -- should be equals
//		//Size should be 60 
//		int size = aDeck.size();
//		int requiredSize = 60;
//		assertNotEquals(requiredSize, size);
//		
//	}
//
//}

import static org.junit.Assert.*;

import org.junit.Test;

public class Deck_unitTest {

	@Test
	public void Deck_test() {
		PokemonCard pokemon = new PokemonCard();
		TrainerCard trainer = new TrainerCard();
		EnergyCard energy =new EnergyCard("colorless");
		
		Deck deck = new Deck();
		//check deckSize
		assertSame(deck.size(),0);
		
		deck.push(energy);
		deck.push(trainer);
		deck.push(pokemon);
		
		Card firstCard = deck.pop();
		
		assertSame(energy,firstCard);
		
		deck.push(energy);
		deck.shuffle();
		Card first = deck.pop();
		Card second = deck.pop();
		
		if((first==energy && second==trainer)||(first==trainer && second==energy)){
			assertSame(pokemon, deck.pop());
		}else if ((first==trainer && second==pokemon)|| (first==pokemon && second==trainer)){
			assertSame(energy, deck.pop());
		}else if ((first==energy && second==pokemon)|| (first==pokemon && second==energy)){
			assertSame(trainer, deck.pop());
		}else assertSame(null, deck.pop());
		
	}

}