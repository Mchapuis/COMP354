import java.io.*;
import java.util.*;

public class Parser {
	
	
/* 
 * what does the parser do? 
   		- It cuts up and reads text files and turns them into useable objects 
   				--> currently the input card text file [cards.txt]
		- Currently arranges the various Card Objects listed in [cards.txt], 
				--> aka the total deck, via various ArrayLists 
	Abstract Card class contains super methods for the various Card types
				--> Energy; Pokemon; Trainer cards, and eventually their sub-categories
	For now am using equals() method to determine card type
*/
	
	// only changed one thing; buffered reader now catches null lines
	public static void readInAbilities(){
		String fileName = "abilities.txt";
		String line = null;
		List<String> abilityLines = new ArrayList<String>();
		
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
		
		for (String l : abilityLines){
			parseLine(l);
		}
	}

	// not being used as of yet
	public static void parseLine(String line){	
	}
	
	//------------------------------------------> HERE BEGINS ME CODE
	
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
		
		// two arrays; one contains card objects, the other is another string array without # lines and also catches potentially empty lines
		ArrayList<String> combLines = new ArrayList<String>();
		ArrayList<Card> newCards = new ArrayList<Card>(); 
		
		// makes sure that the only lines available have actual content
		for (String l : cardLines){
			if (l.equals("#") || l.isEmpty()) {
				System.out.println("Card does not exist");
			} else {
				combLines.add(l);
			}
		}

		// creates rudimentary cards of the three card types out of only some of the information
		for (String l : combLines){
			String[] cS = l.split("[,:\\n]");
			try {
				if (cS[1].equals("pokemon")) {
					newCards.add(new PokemonCard(cS[0]));
					System.out.println("New Pokemon card created.");
				}
				else if (cS[1].equals("trainer")){
					newCards.add(new TrainerCard(cS[0]));
					System.out.println("New Trainer card created.");
				}
				else if (cS[1].equals("energy")) {
					newCards.add(new EnergyCard(cS[0])); 
					System.out.println("New Energy card created.");
				}
				else if (l.isEmpty()){ // is this necessary idk
					System.out.println("Done!");
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


	/* Okay so.. this is very rudimentary but how are we going to continue parsing?
	 * idea 1: split the newly combed array combLines into three separate arrays of types of card info,
	 * 	then create three types of parsing methods for each card type,
	 * 	 which will then efficiently create new fully instantiated cards,
	 * idea 2: continue doing what i'm doing aka predicting what kind of content is in each parsing of each card type 
	 * 	this would be effective but also messier and maybe more complicated
	 * 	pros would be not having to restart as much (maybe)
	 * 
	 * TODO: Figure out how to change parsed string segments into enums
	 * 			(alternatively find out if the enums will be useful at all or whether they're excessive/unnecessary)
	 * 		 Separate readInCards() into multiple modular methods (for debugging, readability, and code compatibility)
	 * 		 Finish parsing the card info and fully instantiating card methods aka categories/attacks/abilities/etc of each card type
	 * 		 Eventually figure out how to relate all of this information into GUI versions of the cards
	 * 		 How does this relate to player decks?
	 */

	// -------------------------------------------MAIN--------------------------------------------------------
	
public static void main(String[]args){
	
	readInCards();	

		
	}
}