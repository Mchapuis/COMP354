import java.io.*;
import java.util.*;

public class Parser {
	
	public static ArrayList<Card> cards = new ArrayList<Card>();
	public static ArrayList<Ability> abilities = new ArrayList<Ability>();
	
	public static ArrayList<Integer> readInDeck(String fileName){
		abilities.clear();
		abilities = readInAbilities();
		cards.clear();
		cards = readInCards();
		
		ArrayList<Integer> cardNumbers = new ArrayList<Integer>();
		
		String line = null;
		List<String> lines = new ArrayList<String>();
		
		try{
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while ((line = bufferedReader.readLine()) != null){
				lines.add(line);
			}
			
			bufferedReader.close();
		} catch (FileNotFoundException e){
			System.out.println("File " + fileName + " not found.");
		} catch (IOException e){
			e.printStackTrace();
		}
		
		for (String l : lines){
			if (l.equals("#"))
				continue;
			Integer i = Integer.parseInt(l);
			cardNumbers.add(i);
		}
		
		return cardNumbers;
	}
	
	public static ArrayList<Ability> readInAbilities(){
		String fileName = "abilities.txt";
		String line = null;
		List<String> lines = new ArrayList<String>();
		
		try{
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while ((line = bufferedReader.readLine()) != null){
				lines.add(line);
			}
			
			bufferedReader.close();
		} catch (FileNotFoundException e){
			System.out.println("File " + fileName + " not found.");
		} catch (IOException e){
			e.printStackTrace();
		}
		
		for (String l : lines){
			Ability a = Ability.parseAbilitiesLine(l);
			abilities.add(a);
		}
		
		return abilities;
	}
	
	public static ArrayList<Card> readInCards(){
		String fileName = "cards.txt";
		String line = null;
		List<String> lines = new ArrayList<String>();
		
		try{
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while ((line = bufferedReader.readLine()) != null){
				lines.add(line);
			}
			
			bufferedReader.close();
		} catch (FileNotFoundException e){
			System.out.println("File " + fileName + " not found.");
		} catch (IOException e){
			e.printStackTrace();
		}
		
		for (String l : lines){
			Card c = parseCard(l);
			cards.add(c);
		}
		
		return cards;
	}

	public static Card parseCard(String line){
		if(line.equals("")){
		    return null;
        }

	    String[] tokens = line.split(":|,");

		int index = 0;
		String cardName = tokens[index++];

		if(cardName.equals("#")){
			return null;
		}

		//parse pokémon card
		if(tokens[index].equals("pokemon")){
			PokemonCard pokemonCard = new PokemonCard();
			pokemonCard.setName(cardName);

			index++; //advance index past card type
			index++; //advance index past cat indicator

			//get pokémon evolution category
			switch (tokens[index]){
				case "basic":
					pokemonCard.setCat(PokemonCard.Category.BASIC);
					break;
				case "stage-one":
					pokemonCard.setCat(PokemonCard.Category.STAGEONE);
					index++; //advance index
					pokemonCard.setEvolvesFrom(tokens[index]);
					break;
			}

			index++; //advance index past pokemon category
			index++; //advance index past cat indicator

			//get pokémon elemental category
			switch (tokens[index]){
				case "colorless":
					pokemonCard.setType(PokemonCard.Type.NORMAL);
					break;
				case "water":
					pokemonCard.setType(PokemonCard.Type.WATER);
					break;
				case "lightning":
					pokemonCard.setType(PokemonCard.Type.LIGHTNING);
					break;
				case "psychic":
					pokemonCard.setType(PokemonCard.Type.PSYCHIC);
					break;
				case "fighting":
					pokemonCard.setType(PokemonCard.Type.FIGHTING);
					break;
			}

			index++; //advance index to hp

			//get max hp
			pokemonCard.setMaxHP(Integer.parseInt(tokens[index]));

			index += 4; //advance index to retreat cost
			pokemonCard.setEnergyToRetreat(Integer.parseInt(tokens[index]));

			index += 2; //advance index to first attack

            //add abilities
            while(index < tokens.length - 1){
                int colourlessCost = 0;
                int waterCost = 0;
                int lightningCost = 0;
                int psychicCost = 0;
                int fightingCost = 0;

                //get costs
                while(tokens[index].equals("cat")){
                    index++; //advance index to type of energy

                    switch(tokens[index]){
                        case "colorless":
                            index++;
                            colourlessCost = Integer.parseInt(tokens[index]);
                            break;
                        case "water":
                            index++;
                            waterCost = Integer.parseInt(tokens[index]);
                            break;
                        case "lightning":
                            index++;
                            lightningCost = Integer.parseInt(tokens[index]);
                            break;
                        case "psychic":
                            index++;
                            psychicCost = Integer.parseInt(tokens[index]);
                            break;
                        case "fight":
                            index++;
                            fightingCost = Integer.parseInt(tokens[index]);
                            break;
                    }
                    index++; //advance index to next cat or ability number
                }

                //add ability
                Ability abilityToAdd = abilities.get(Integer.parseInt(tokens[index]) - 1);
                pokemonCard.addAbility(abilityToAdd);

                index++;
            }

			return pokemonCard;
		}
		//parse trainer card
		else if(tokens[index].equals("trainer")){
			Ability trainerAbility = abilities.get(Integer.parseInt(tokens[tokens.length - 1]) - 1);
			return new TrainerCard(cardName, trainerAbility);
		}
		//parse energy card
		else if(tokens[index].equals("energy")){
			return new EnergyCard(tokens[3]);
		}

        return null;
	}
}