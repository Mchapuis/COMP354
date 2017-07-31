import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

abstract class Ability {

	//card managers let ability classes directly modify the games cards
    public static CardManager playerCardManager;
    public static CardManager AICardManager;

    //enumerated types //should be moved to to separate files
	public enum Target{
		YOUR_ACTIVE, OPPONENT_ACTIVE, //targets the active pokemon
		YOUR_POKEMON, OPPONENT_POKEMON, //targets choice of pokemon active or bench
		YOUR_BENCH, OPPONENT_BENCH, //targets choice of pokemon on bench
		YOU, OPPONENT, //targets player in general
		LAST //the last pokemon that was targeted
	}
	public enum Player{
	    PLAYER,
        AI
    }

    //ability data
	public String name;
	Target targetType;
	boolean hasChoice = false;
	Ability subsequentAbility = null;
	protected HashMap<EnergyCard.Type, Integer> energyRequired = new HashMap<EnergyCard.Type, Integer>();

	static PokemonCard lastTargetedPokemon = null;

	//---Methods

	//use ability and helper method
	public boolean use(Player player){
	    boolean passed = realUse(player);
		if(subsequentAbility != null){
			boolean secondary;
	       	secondary = subsequentAbility.use(player);
	       	if(secondary == false){
	       		passed = false;
			}
        }
        return passed;
    }
    public abstract boolean realUse(Player player);

	//get description and helper methods
	public String getDescription(){
        return getBaseDescription() + "<br/>" + getRecursiveDescription();
	}
	private String getBaseDescription(){
        String desc = "Name: " + this.name;
        desc += "<br/>";
        desc += "Energy required: ";
        if(energyRequired.size() == 0){
        	desc += "None";
		}
		else{
			for (Entry<EnergyCard.Type, Integer> entry : energyRequired.entrySet()){
				desc += "<br/>";
				desc += "&nbsp;&nbsp;&nbsp;";
				desc += entry.getKey().toString();
				desc += ": ";
				desc += entry.getValue();
			}
		}
        return desc;
    }
	protected String getRecursiveDescription(){

	    return (subsequentAbility == null)? "<br/>"+getSimpleDescription():"<br/>"+getSimpleDescription()+" then "+subsequentAbility.getRecursiveDescription();

    }
    protected abstract String getSimpleDescription();

	//
	public void addEnergyRequired(EnergyCard.Type energyType, int amount){
		this.energyRequired.put(energyType, amount);
	}

	//getter and setters
	public void setName(String name){
		this.name = name;
	}
	public HashMap<EnergyCard.Type, Integer> getEnergyRequired(){
		return this.energyRequired;
	}
	public void setSubsequentAbility(Ability subAbility){
		this.subsequentAbility = subAbility;
	}

	//parsing methods //should be moved into parser class
	public static Ability parseAbilitiesLine(String line){
		//do nothing in case of removed ability line
		if(line.equals("#")){
			return new UnimplementedAbility();
		}

		//if parsing ever fails then return UnimplementedAbility
		try{
			//extract name from line
			int nameLimit = line.indexOf(':');
			String abilityName = line.substring(0, nameLimit);
			line = line.substring(nameLimit+1);

			//tokenize by comma
			String [] sequentialAbilities = line.split(",");

			//remerge parentheses
			sequentialAbilities = remergeParentheses(sequentialAbilities);

			//make first ability in sequence
			Ability firstAbility = makeAbility(sequentialAbilities[0].split(":"));
			firstAbility.setName(abilityName);

			//make subsequent abilites and attach them sequentialy
			Ability latestAbility = firstAbility;
			for(int i = 1; i < sequentialAbilities.length; i++){
				Ability newAbility = makeAbility(sequentialAbilities[i].split(":"));
				latestAbility.setSubsequentAbility(newAbility);
				latestAbility = newAbility;
			}

			//return the first ability
			return firstAbility;
		}
		catch(Exception e){
			return new UnimplementedAbility();
		}
	}
	private static String[] remergeCount(String [] description){
		ArrayList<String> newDescription = null;
		ArrayList<String> oldDescription = new ArrayList<>();

		for(int i = 0; i < description.length; i++){
			oldDescription.add(description[i]);
		}

		boolean done = false;
		while(!done){
			newDescription = new ArrayList<>();
			done = true;

			for(int i = 0; i < oldDescription.size(); i++){
				if(oldDescription.get(i).matches(".*count\\([\\w]+$")){
					done = false;
					String mergedString = oldDescription.get(i);
					for(int j = i+1; j < oldDescription.size(); j++){
						mergedString += ":" + oldDescription.get(j);
						i = j;
						if(oldDescription.get(j).contains(")")){
							break;
						}
					}
					newDescription.add(mergedString);
				}
				else{
					newDescription.add(oldDescription.get(i));
				}
			}

			oldDescription = newDescription;
		}



		String returnArray[] = new String[newDescription.size()];
		newDescription.toArray(returnArray);
		return returnArray;
	}
	protected static String[] remergeParentheses(String[] description){
		ArrayList<String> newDescription = null;
		ArrayList<String> oldDescription = new ArrayList<>();

		for(int i = 0; i < description.length; i++){
			oldDescription.add(description[i]);
		}

		boolean done = false;
		while(!done){
			newDescription = new ArrayList<>();
			done = true;

			for(int i = 0; i < oldDescription.size(); i++){
				if(oldDescription.get(i).contains("(")){
					String mergedString = oldDescription.get(i);

					if(oldDescription.get(i).endsWith(")") && (getParenthesesBalance(oldDescription.get(i))) == 1){
						//do nothing
					}
					else if((getParenthesesBalance(oldDescription.get(i))&1)==1){
						done = false;
						for(int j = i+1; j < oldDescription.size(); j++){
							mergedString += "," + oldDescription.get(j);
							i = j;
							if((countCharInString(oldDescription.get(j), ')')&1) == 1){
								break;
							}
						}
					}
					newDescription.add(mergedString);
				}
				else{
					newDescription.add(oldDescription.get(i));
				}
			}

			oldDescription = newDescription;
		}

		String returnArray[] = new String[newDescription.size()];
		newDescription.toArray(returnArray);
		return returnArray;
	}
	private static int getParenthesesBalance(String s){
		int length = s.length();
		int leftCount = 0;
		int rightCount = 0;
		for(int i = 0; i < length; i++){
			char c = s.charAt(i);
			if(c == '('){
				leftCount++;
			}
			else if(c == ')'){
				rightCount++;
			}
		}
		return leftCount - rightCount;
	}
	private static int countCharInString(String s, char c){
		int count = 0;
		int stringLength = s.length();
		for(int i = 0; i < stringLength; i++){
			if(s.charAt(i) == c){
				count++;
			}
		}
		return count;
	}
	protected static void removeStartEndParentheses(String[] description){
		int lastCharIndex = description.length - 1;
		if(description[0].startsWith("(") && description[lastCharIndex].endsWith(")")){
			//remove first character from first string
			description[0] = description[0].substring(1);
			//remove last character from last string
			description[lastCharIndex] = description[lastCharIndex].substring(0, description[lastCharIndex].length() - 1);
		}
	}


	public static Ability makeAbility(String[] description) throws Exception{
		description = remergeCount(description);

		Ability returnAbility = null;

		//branch based on ability type
		String abilityType = description[0];
		try{

		    switch (abilityType){
                case "dam":
                    returnAbility = new DamageAbility(description);
                    break;
                case "deck":
                    returnAbility = new DeckAbility(description);
                    break;
                case "draw":
                    returnAbility = new DrawAbility(description);
                    break;
                case "cond":
                    returnAbility = new ConditionAbility(description);
                    break;
                case "applystat":
                    returnAbility = new ApplyStatAbility(description);
                    break;
                case "destat":
                    returnAbility = new DestatAbility(description);
                    break;
                case "heal":
                    returnAbility = new HealAbility(description);
                    break;
                case "deenergize":
                    returnAbility = new DeenergizeAbility(description);
                    break;
                case "reenergize":
                    returnAbility = new ReEnergizeAbility(description);
                    break;
                case "redamage":
                    returnAbility = new RedamageAbility(description);
                    break;
                case "search":
                    returnAbility = new SearchAbility(description);
                    break;
                case "swap":
                    returnAbility = new SwapAbility(description);
                    break;
                case "add":
                    returnAbility = new AddAbility(description);
                    break;
				case "shuffle":
					returnAbility = new ShuffleAbility(description);
					break;
                default:
                    returnAbility = new UnimplementedAbility();
            }
        }catch (UnimplementedException e){
		    returnAbility = new UnimplementedAbility();
        }

		return returnAbility;
	}
	public static Target parseTarget(String token){
		switch(token) {
			case "your-active":
				return Target.YOUR_ACTIVE;
			case "opponent-active":
				return Target.OPPONENT_ACTIVE;
			case "you":
				return Target.YOU;
			case "them":
				return Target.OPPONENT;
			case "opponent":
				return Target.OPPONENT_POKEMON;
			case "your":
				return Target.YOUR_POKEMON;
			case "opponent-bench":
				return Target.OPPONENT_BENCH;
			case "your-bench":
				return Target.YOUR_BENCH;
			case "last":
				return Target.LAST;
			case "your-pokemon": //exception for wally card
				return Target.YOUR_POKEMON;
		}
		return Target.OPPONENT_ACTIVE;
	}

	//copy ability
	public abstract Ability shallowCopy();
}
