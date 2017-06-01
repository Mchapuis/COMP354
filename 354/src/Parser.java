import java.io.*;
import java.util.*;

public class Parser {
	
/* v 1.0
 * what does the parser do? 
   		- It cuts up and reads text files and turns them into useable objects 
   				--> currently the input card text file [cards.txt]
		- Currently arranges the various Card Objects listed in [cards.txt], 
				--> aka the total deck, via various ArrayLists 
		- Abstract Card class contains super methods for the various Card types
				--> Energy; Pokemon; Trainer cards, and eventually their sub-categories
		- For now am using a String equals() method to determine card type
	
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
	 	- modulated readincards so that part of it exists in parsecardlines
	 	- created parseattacks method, which creates attack objects and adds them to an array list of attack objects called totalAttackList
	 	- created method called addattacks which adds attacks to the cards' instantiated blank attack array list
	 	- TODO: integrate nfrank's ability parsing
	 		    figure out how to relate all of this information into GUI versions of the cards
	 		    change so that an uninstantiated card is created instead of manually incrementing card count id in readInCards when faced with a blank line '#' (this is good praxis)
		
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
			
			while (!(line = bufferedReader.readLine()).isEmpty()){ 
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
				int count = 0;
				
				// creates rudimentary cards of the three card types out of only some of the information located in cards.txt
				// if '#' sysprints blank and increments card number allocator
				
				for (String l : cardLines){
					
					String[] cS = l.split(":");
					
					try {
						
						if (cS[0].equals("#")){
							
							Card.cardnumber++;
							System.out.println("Blank");
						}
						
						else if (cS[1].equals("pokemon")) {
							
							// depending on the stage of the pokemon card, uses two different constructors, one of which references basic stage evolution via name
						
							if (cS[3].equals("basic")){
								
								newCards.add(new PokemonCard(cS[0], cS[3], cS[5], Integer.parseInt(cS[6]), Integer.parseInt(cS[10])));
								System.out.println("New Basic Pokemon card created.");
							}	
							else if (cS[3].equals("stage-one")){	
									
								newCards.add(new PokemonCard(cS[0], cS[3], cS[4], cS[6], Integer.parseInt(cS[7]), Integer.parseInt(cS[11])));
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
				
				parseAttacks(newCards, cardLines);
	}
	
	// parses Attacks from cards.txt, creates Attack objects which are related to cardID (parsed as attackID and should be equal), and adds them to a totalAttackList array list
	
	public static void parseAttacks(ArrayList<Card> newCards, ArrayList<String> cardLines)	{
		
		ArrayList<Attack> totalAttackList = new ArrayList<Attack>();
		int attackID = 1;

		for (String l : cardLines)	{
			
			String[] cS = l.split(":");
			
			if (l.equals("#"))	{
				attackID++;
				continue;
				}
			
			else if (cS[1].equals("pokemon"))	{
				
				String[] attacksString = l.split("attacks:");
				String[] multipleAttacksString = attacksString[1].split(",");
				String[] multipleAttacksArray = new String[4];
				
				for (String s : multipleAttacksString){
					
					String maa3 = "0";
					multipleAttacksArray = s.split(":");
					
					if (multipleAttacksArray.length == 4)	{ 
						
						maa3 = multipleAttacksArray[3];
						}
					
					totalAttackList.add(new Attack(attackID, multipleAttacksArray[1], Integer.parseInt(multipleAttacksArray[2]), Integer.parseInt(maa3)));
					System.out.println("Attack successfully created. Attack is related to CarID number "+attackID);
				}
			}
			attackID++;
		}
		
		for (Attack a : totalAttackList) {
			
			System.out.println(a.toString());
		}
		
		addAttacks(newCards, totalAttackList);
	}
	
	public static void addAttacks (ArrayList<Card> newCards, ArrayList<Attack> totalAttackList)	{
		
			for (Card c : newCards)	{	
				
				int compareID = c.cardID();
				
				for(Attack a : totalAttackList) {
						
					if (c.getType() == "pokemon" && a.getAttackID() == compareID) {		
						
						((PokemonCard) c).addAttack(a);
						}
					}
				}
			
			for (Card c : newCards){
				
				System.out.println(c.toString());
				System.out.println(((PokemonCard) c).getAttacks());
			}
		}
			

		public static void readInAbilities(){
			String fileName = "abilities.txt";
			String line = null;
			ArrayList<String> abilityLines = new ArrayList<String>();
			
			try{
				FileReader fileReader = new FileReader(fileName);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				
				while (!(line = bufferedReader.readLine()).isEmpty()){ 
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
