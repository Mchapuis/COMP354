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
		YOU, OPPONENT //targets player in general
	}
	public enum Player{
	    PLAYER,
        AI
    }

    //ability data
	public String name;
	Target targetType;
	Ability subsequentAbility = null;
	protected HashMap<EnergyCard.Type, Integer> energyRequired = new HashMap<EnergyCard.Type, Integer>();

	//---Methods

	//use ability and helper method
	public boolean use(Player player){
	    realUse(player);
		if(subsequentAbility != null){
	       return subsequentAbility.use(player);
        }
        return false;
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
	    return (subsequentAbility == null)? "<br/>"+getSimpleDescription():"<br/>"+getSimpleDescription()+subsequentAbility.getRecursiveDescription();
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
	public static Ability makeAbility(String[] description) throws Exception{
		Ability returnAbility = null;

		//branch based on ability type
		String abilityType = description[0];
		try{

		    switch (abilityType){
                case "dam":
                    returnAbility = new DamageAbility(description);
                    break;
                case "deck":
                    returnAbility = new UnimplementedAbility(); //TODO:
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
                    returnAbility = new UnimplementedAbility(); //TODO:
                    break;
                case "redamage":
                    returnAbility = new UnimplementedAbility(); //TODO:
                    break;
                case "search":
                    returnAbility = new UnimplementedAbility(); //TODO:
                    break;
                case "swap":
                    returnAbility = new UnimplementedAbility(); //TODO:
                    break;
                case "add":
                    returnAbility = new UnimplementedAbility(); //TODO:
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
		}
		return Target.OPPONENT_ACTIVE;
	}

	//copy ability
	public abstract Ability shallowCopy();
}
