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
		String[] splitString = line.split(":|,");
		
		if (splitString[0].equals("#")){
			return null;
		} else if (splitString[1].equals("pokemon")){
			PokemonCard poke = new PokemonCard(splitString[0], "Generic description", splitString[3], splitString[5], Integer.parseInt(splitString[6]), Integer.parseInt(splitString[10]));
			try{
				Ability ability1 = abilities.get(Integer.parseInt(splitString[15]) - 1);
				System.out.println(poke.getName() + ": " + ability1.name + " - " + ability1.getClass());
				EnergyCard energy1 = new EnergyCard(splitString[13]);
				ability1.addEnergyRequired(energy1, Integer.parseInt(splitString[14]));
				poke.addAbility(ability1);
			} catch (NumberFormatException e){
				Ability ability1 = abilities.get(Integer.parseInt(splitString[18]) - 1);
				System.out.println(poke.getName() + ": " + ability1.name + " - " + ability1.getClass());
				EnergyCard energy1 = new EnergyCard(splitString[13]);
				ability1.addEnergyRequired(energy1, Integer.parseInt(splitString[14]));
				EnergyCard energy2 = new EnergyCard(splitString[16]);
				ability1.addEnergyRequired(energy2, Integer.parseInt(splitString[17]));
				poke.addAbility(ability1);
			}
			
			boolean moreAbilities = false;
			try {
				if (splitString.length > 19){
					Integer.parseInt(splitString[19]);
					moreAbilities = true;
				}
			} catch (NumberFormatException e) {
				try {
					if (splitString.length > 22){
						Integer.parseInt(splitString[22]);
						moreAbilities = true;
					}
				} catch (NumberFormatException e2) {
					
				}
			}
			
			if (splitString.length > 16 && moreAbilities){
				try{
					Ability ability2 = abilities.get(Integer.parseInt(splitString[19]) - 1);
					System.out.println(poke.getName() + ": " + ability2.name + " - " + ability2.getClass());
					EnergyCard energy2 = new EnergyCard(splitString[17]);
					ability2.addEnergyRequired(energy2, Integer.parseInt(splitString[18]));
					poke.addAbility(ability2);
				} catch (NumberFormatException e){
					Ability ability2 = abilities.get(Integer.parseInt(splitString[22]) - 1);
					System.out.println(poke.getName() + ": " + ability2.name + " - " + ability2.getClass());
					EnergyCard energy2 = new EnergyCard(splitString[17]);
					ability2.addEnergyRequired(energy2, Integer.parseInt(splitString[18]));
					EnergyCard energy3 = new EnergyCard(splitString[20]);
					ability2.addEnergyRequired(energy3, Integer.parseInt(splitString[21]));
					poke.addAbility(ability2);
				}
			}
			return poke;
		} else if (splitString[1].equals("trainer")){
			TrainerCard trainer = new TrainerCard();
			return trainer;
		} else {
			EnergyCard energy = new EnergyCard(splitString[3]);
			return energy;
		}
	}
}