abstract class Ability {
    public static CardManager playerCardManager;
    public static CardManager AICardManager;

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

	public String name;
	Target targetType;
	Ability subsequentAbility = null;

	public abstract void use(Player player);

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
		if(abilityType.equals("dam")){
			returnAbility = new DamageAbility(description);
		}
		else if(abilityType.equals("deck")){
			returnAbility = new UnimplementedAbility();
		}
		else if(abilityType.equals("draw")){
			returnAbility = new UnimplementedAbility();
		}
		else if(abilityType.equals("cond")){
			returnAbility = new UnimplementedAbility();
		}
		else if(abilityType.equals("applystat")){
			returnAbility = new UnimplementedAbility();
		}
		else if(abilityType.equals("heal")){
			returnAbility = new UnimplementedAbility();
		}
		else if(abilityType.equals("deenergize")){
			returnAbility = new UnimplementedAbility();
		}
		else if(abilityType.equals("reenergize")){
			returnAbility = new UnimplementedAbility();
		}
		else if(abilityType.equals("redamage")){
			returnAbility = new UnimplementedAbility();
		}
		else if(abilityType.equals("search")){
			returnAbility = new UnimplementedAbility();
		}
		else if(abilityType.equals("swap")){
			returnAbility = new UnimplementedAbility();
		}

		return returnAbility;
	}

	public static Target parseTarget(String [] description){
		if(description.length < 3){
			if(description[1].equals("your-active")){
				return Target.YOUR_ACTIVE;
			}
			else if(description[1].equals("opponent-active")){
				return Target.OPPONENT_ACTIVE;
			}
			else if(description[1].equals("you")){
				return Target.YOU;
			}
			else if(description[1].equals("them")){
				return Target.OPPONENT;
			}
		}
		else{
			if(description[1].equals("your-active")){
				return Target.YOUR_ACTIVE;
			}
			else if(description[1].equals("opponent-active")){
				return Target.OPPONENT_ACTIVE;
			}
			else if(description[1].equals("you")){
				return Target.YOU;
			}
			else if(description[1].equals("them")){
				return Target.OPPONENT;
			}
			else if(description[1].equals("opponent")){
				return Target.OPPONENT;
			}
			else if(description[1].equals("your")){
				return Target.YOU;
			}
			else if(description[1].equals("opponent-bench")){
				return Target.OPPONENT_BENCH;
			}
			else if(description[1].equals("your-bench")){
				return Target.YOUR_BENCH;
			}
			else if(description[1].equals("choice")){
				if(description[2].equals("opponent")){
					return Target.OPPONENT;
				}
				if(description[2].equals("your")){
					return Target.YOU;
				}
				if(description[2].equals("opponent-bench")){
					return Target.OPPONENT_BENCH;
				}
				if(description[2].equals("your-bench")){
					return Target.YOUR_BENCH;
				}
			}
		}
		return Target.OPPONENT_ACTIVE;
	}
}
