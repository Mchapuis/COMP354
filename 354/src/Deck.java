import java.util.*;
import java.util.stream.Collectors;

public class Deck {

	private ArrayList<Card> cards;
	
	public Deck(){
		cards = new ArrayList<Card>();
	}
	
	public ArrayList<Card> getCards(){
		return this.cards;
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
	
	public int size(){
		return cards.size();
	}

	public boolean validate(){
		//1. each deck must have exactly 60 cards
		if(cards.size() != 60) {
			System.out.println("Deck does not contain exactly 60 cards.");
			return false;
		}

		//2. you may have any number of cards of type energy

		//3. you may have at most 4 of any card that is not of type:energy
		Map<Card, Long> nonEnergyCounts = cards.stream().filter(p -> {return !(p instanceof EnergyCard);}).collect(Collectors.groupingBy(c->c, Collectors.counting()));
		for(Map.Entry<Card,Long> entry : nonEnergyCounts.entrySet()){
			if(entry.getValue() > 4) {
				System.out.println("Deck contains more than four of a non-energy card.");
				return false;
			}
		}

		//4. each deck must have at least one type:pokemon card
		if(cards.stream().filter(p -> {return p instanceof PokemonCard;}).findAny().orElse(null) == null) return false;

		//5. all cards must be ”playable”
		//(a) a type:pokemon,cat:stage-one is ”playable” if the deck contains its corresponding type:pokemon,cat:basic
		List<Card> stageTwoPokemon = cards.stream().filter(p->{return p instanceof PokemonCard && ((PokemonCard)p).getCat() == PokemonCard.Category.STAGEONE;}).collect(Collectors.toList());
		List<Card> basicPokemon = cards.stream().filter(p->{return p instanceof PokemonCard && ((PokemonCard)p).getCat() == PokemonCard.Category.BASIC;}).collect(Collectors.toList());
		int amountSupported = 0;
		for(Card c : stageTwoPokemon){
			String nameToLookFor = ((PokemonCard)c).getEvolvesFrom();
			for(Card c2 : basicPokemon){
				if(c2.getName().equals(nameToLookFor)){
					amountSupported++;
					break;
				}
			}
		}
		if(amountSupported < stageTwoPokemon.size()){
			System.out.println("Deck does not contain a basic pokemon for each stage one pokemon.");
			return false;
		}

		//(b) a type:pokemon is ”playable” if your deck contains sufficient energy to use its abilities
		List<Card> energyCards = cards.stream().filter(p -> {return (p instanceof EnergyCard);}).collect(Collectors.toList());
		Map<EnergyCard.Type, Integer> energyHave = new HashMap<>();
		for(Card c : energyCards){
			EnergyCard.Type t = ((EnergyCard)c).getType();
			if(energyHave.containsKey(t)){
				energyHave.replace(t, energyHave.get(t)+1);
			}
			else{
				energyHave.put(t, 1);
			}
		}
		List<Card> allPokemon = cards.stream().filter(p->{return p instanceof PokemonCard;}).collect(Collectors.toList());
		for(Card c : allPokemon){
			PokemonCard p = (PokemonCard)c;
			for(Ability a : p.getAbilities()){
				Map<EnergyCard.Type, Integer> abilityCosts = a.getEnergyRequired();
				for(Map.Entry<EnergyCard.Type,Integer> e : abilityCosts.entrySet()){
					if(e.getKey() != EnergyCard.Type.COLORLESS){
						if(!energyHave.containsKey(e.getKey())){
							System.out.println("Deck does not have enough energy for every ability.");
							return false;
						}
						if(e.getValue() > energyHave.get(e.getKey())){
							System.out.println("Deck does not have enough energy for every ability.");
							return false;
						}
					}
					else{
						int sum = 0;
						for(Map.Entry<EnergyCard.Type,Integer> ee : energyHave.entrySet()){
							sum += ee.getValue();
						}
						if(e.getValue() > sum){
							System.out.println("Deck does not have enough energy for every ability.");
							return false;
						}
					}
				}
			}
		}

		return true;
	}
	
}