//TODO: add to subclasses ability to handle complicated [amount]
abstract class Ability {

	public static void main(String[] args){
		//this method doesn't matter it just let nelson do quick tests
		String damageAbility = "Misty's Determination:cond:ability:deck:destination:discard:target:choice:you:1:(search:target:you:source:deck:filter:top:8:1,shuffle:target:you)";

		Ability test = parseAbilitiesLine(damageAbility);

		System.out.println(test.name);
		if(test instanceof DamageAbility){
			System.out.println("yep");
		}
	}

    public static CardManager playerCardManager;
    public static CardManager AICardManager;

	public static enum Target{
		YOUR_ACTIVE, OPPONENT_ACTIVE, //targets the active pokemon
		YOUR_POKEMON, OPPONENT_POKEMON, //targets choice of pokemon active or bench
		YOUR_BENCH, OPPONENT_BENCH, //targets choice of pokemon on bench
		YOU, OPPONENT //targets player in general
	}
	public enum Player{
	    PLAYER,
        AI
    }

	public String name;
	Target targetType;
	Ability subsequentAbility = null;

	public boolean use(Player player){
	    realUse(player);

	    if(subsequentAbility != null){
	        subsequentAbility.use(player);
        }

        return true;
    }

    public abstract boolean realUse(Player player);

	public void setName(String name){
		this.name = name;
	}

	public void setSubsequentAbility(Ability subAbility){
		this.subsequentAbility = subAbility;
	}

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
                    returnAbility = new ConditionAbility(description); //TODO:
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
}
