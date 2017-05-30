import java.io.*;
import java.util.*;

public class Parser {

	/* 
	 * TODO: Figure out how to change parsed string segments into enums
	 * 			(alternatively find out if the enums will be useful at all or whether they're excessive/unnecessary)
	 * 		 Finish parsing the card info and fully instantiating card methods aka categories/attacks/abilities/etc of each card type
	 * 		 Eventually figure out how to relate all of this information into GUI versions of the cards
	 * 		 Create deck() method
	 *
	
/* v 1.0
 * what does the parser do? 
   		- It cuts up and reads text files and turns them into useable objects 
   				--> currently the input card text file [cards.txt]
		- Currently arranges the various Card Objects listed in [cards.txt], 
				--> aka the total deck, via various ArrayLists 
	Abstract Card class contains super methods for the various Card types
				--> Energy; Pokemon; Trainer cards, and eventually their sub-categories
	For now am using equals() method to determine card type
	
	x - x - x - x - x - x - x - x - x - x - x - x - x - x - x - x - x - x - x - x - x - x
	
	changes v1.1: 
		- Removed the combing array because it didn't address the needs of deck1/deck2.txt, 
					--> changed parser to produce blanks when encountering "#" and increment the card number allocator 
					--> now card objects are correctly instantiated and aligned with their line number placement in cards.txt
									
	x - x - x - x - x - x - x - x - x - x - x - x - x - x - x - x - x - x - x - x - x - x
	
	changes v1.2:
		- finished the trainer and energy constructors (more or less; at least relating to what is parsable via cards.txt)
		- created a nested loop in readInCards() for creating two types of pokemon cards depending on pokemon stage (uses two different constructors)
		- still in the process of roughly parsing the entirety of a pokemon card type / roughly finishing the constructor
											
	x - x - x - x - x - x - x - x - x - x - x - x - x - x - x - x - x - x - x - x - x - x
	 
	 changes v1.3:
	 	- readinabilities and parseabilitylines;
	 	- going to integrate nfrank's ability parsing
		
*/
	
	// Reads in cards.txt into an array list of strings. 
		// (mostly just copied and tweaked ana's code)
	public static void readInCards(){
		String fileName = "cards.txt";
		String line = null;
		ArrayList<String> cardLines = new ArrayList<String>();
		
		try{
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while (!(line = bufferedReader.readLine()).isEmpty()){ // changed because it wasn't catching null lines
				cardLines.add(line);
			}
			
			bufferedReader.close();
		} catch (FileNotFoundException e){
			System.out.println("File " + fileName + " not found.");
		} catch (IOException e){
			e.printStackTrace();
		}
		
		parseCardLine(cardLines);
	}

	public static void parseCardLine(ArrayList<String> cardLines){	
		
		// array which contains card objects
				ArrayList<Card> newCards = new ArrayList<Card>(); 

				// creates rudimentary cards of the three card types out of only some of the information located in cards.txt
				// if '#' sysprints blank and increments card number allocator
				for (String l : cardLines){
					String[] cS = l.split("[,:\\n]");
					try {
						if (cS[0].equals("#")){
							Card.cardnumber++;
							System.out.println("Blank");
						}
						else if (cS[1].equals("pokemon")) {
							// depending on the stage of the pokemon card, uses two different constructors, one of which references basic stage evolution via name
							if (cS[3].equals("basic")){
								newCards.add(new PokemonCard(cS[0], cS[3], cS[5], Integer.parseInt(cS[6])));
								System.out.println("New Basic Pokemon card created.");
							}
							else if (cS[3].equals("stage-one")){
								newCards.add(new PokemonCard(cS[0], cS[3], cS[4], cS[6], Integer.parseInt(cS[7])));
								System.out.println("New Stage-One Pokemon card created.");
							}
						}
						else if (cS[1].equals("trainer")){
							newCards.add(new TrainerCard(cS[0], cS[3], Integer.parseInt(cS[4])));
							System.out.println("New Trainer card created.");
						}
						else if (cS[1].equals("energy")) {
							newCards.add(new EnergyCard(cS[0], cS[3])); 
							System.out.println("New Energy card created.");
						}
					} catch (ArrayIndexOutOfBoundsException o){
							System.out.println("There was a problem parsing and creating cards");
							o.printStackTrace();
						}
				}
				
				// prints out tostrings of the cards that were created
				for (Card c : newCards) {
					System.out.println(c.toString());
				}
	}
	
	// only changed one thing; buffered reader now catches null lines
		public static void readInAbilities(){
			String fileName = "abilities.txt";
			String line = null;
			ArrayList<String> abilityLines = new ArrayList<String>();
			
			try{
				FileReader fileReader = new FileReader(fileName);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				
				while (!(line = bufferedReader.readLine()).isEmpty()){ // changed because it wasn't catching null lines
					abilityLines.add(line);
				}
				
				bufferedReader.close();
			} catch (FileNotFoundException e){
				System.out.println("File " + fileName + " not found.");
			} catch (IOException e){
				e.printStackTrace();
			}	
			
			parseAbilityLine(abilityLines);
		}
	
	// going to cut up what nfrank wrote for abilities and plug it into here
	public static void parseAbilityLine(ArrayList<String> abilityLines){
		
		// array which contains ability objects
		ArrayList<Ability> newAbilities = new ArrayList<Ability>(); 
		
		for (String l : abilityLines){
			//do the thing
			
		}
	}

	// -------------------------------------------MAIN--------------------------------------------------------
	
public static void main(String[]args){

	//readInAbilities();
	readInCards();	
	
	}
}
