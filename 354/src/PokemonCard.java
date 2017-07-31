import java.util.*;

public class PokemonCard extends Card {

	public enum Category {
		BASIC, STAGEONE
	}
	
	public enum Type {
		LIGHTNING, NORMAL, PSYCHIC, WATER, FIGHTING
	}
	
	private int ID;
	private String name;
	private String description;
	private Category cat;
	private String elementalType;
	private Type type;
	private int maxHP;
	private int energyToRetreat;
	
	private Status status;
	private int currentHP;
	private ArrayList<Ability> abilities;
	public ArrayList<EnergyCard> energy;
	private int numColorlessEnergy;

	private String evolvesFrom = "";
	private PokemonCard evolvedFrom = null;

	private boolean hasBeenHealed = false;

	public ArrayList<Ability> abilitiesTriggeredOpponentTurnEnd = new ArrayList<>();
	public ArrayList<Ability> abilitiesTriggeredOwnTurnEnd = new ArrayList<>();

	public Card shallowCopy(){
		PokemonCard returnCard = new PokemonCard();

		returnCard.ID = this.ID;
		returnCard.name = this.name;
		returnCard.description = this.description;
		returnCard.cat = this.cat;
		returnCard.elementalType = this.elementalType;
		returnCard.evolvesFrom = this.evolvesFrom;
		returnCard.type = this.type;
		returnCard.maxHP = this.maxHP;
		returnCard.energyToRetreat = this.energyToRetreat;
		returnCard.status = this.status;
		returnCard.currentHP = this.currentHP;
		returnCard.abilities = this.abilities;
		returnCard.numColorlessEnergy = this.numColorlessEnergy;
		returnCard.hasBeenHealed = this.hasBeenHealed;

		return returnCard;
	}

	//Constructors
	public PokemonCard(){
		this.ID = 0;
		this.name = "Undefined";
		this.description = "No description";
		this.cat = Category.BASIC;
		this.type = Type.NORMAL;
		this.maxHP = 0;
		this.energyToRetreat = 0;
		this.status = Status.NORMAL;
		this.currentHP = 0;
		this.abilities = new ArrayList<Ability>();
		this.energy = new ArrayList<EnergyCard>();
		this.numColorlessEnergy = 0;
	}
	public PokemonCard(String name, String description, String cat, String type, int maxHP, int energyToRetreat){
		this.name = name;
		this.description = description;
		
		if (cat.equals("basic")){
			this.cat = Category.BASIC;
		} else {
			this.cat = Category.STAGEONE;
		}
		
		if (type.equals("lightning")) {
			this.type = Type.LIGHTNING;
		} else if (type.equals("psychic")) {
			this.type = Type.PSYCHIC;
		} else if (type.equals("water")) {
			this.type = Type.WATER;
		} else if (type.equals("fighting")) {
			this.type = Type.FIGHTING;
		} else {
			this.type = Type.NORMAL;
		}
		
		this.maxHP = maxHP;
		this.energyToRetreat = energyToRetreat;
		
		this.status = Status.NORMAL;
		this.currentHP = maxHP;
		this.abilities = new ArrayList<Ability>();
		this.energy = new ArrayList<EnergyCard>();		
	}

	public String getDescription(){
		String desc = "<html><body>";
		desc += "<br/>=================<br/>";
		desc += "HP: " + this.currentHP +"/" + this.maxHP + "<br/>";
		if(!evolvesFrom.equals("")){
			desc += "Evolves from: " + evolvesFrom + "<br/>";
		}
		desc += "Retreat Cost: " + energyToRetreat + "<br/>";
		desc += "Status: " + this.status + "<br/>";
		desc += "Energy attached: ";
		if (this.energy.size() == 0){
			desc += "None";
		} else {
			int i = 0;
			for (EnergyCard e : this.energy){
				if (e.getType() == EnergyCard.Type.COLORLESS)
					continue;
				desc += e.getDescription();
				if (i + 1 < this.energy.size()){
					desc += ", ";
				}
			}
		}
		desc += "<br/>=================<br/>";
		desc += "<br/>Attacks: ";
		if (this.abilities.size() == 0){
			desc += "None";
		} else {
			desc += "<br/>-----------------<br/>";
			int i = 0;
			for (Ability a : this.abilities){
				desc += a.getDescription();
				if (i != this.abilities.size()){
					desc += "<br/>-----------------<br/>";
				}
			}
		}
		desc += "</html></body>";		
		return desc;
	}

	public void attachEnergy(EnergyCard e){
		energy.add(e);
		numColorlessEnergy++;
	}
	
	public void removeEnergy(EnergyCard e){
		energy.remove(e);
	}
	
	public void addAbility(Ability ability){
		abilities.add(ability);
	}

	public void removeHP(int points){
		this.currentHP -= points;
	}
	
	public boolean hasEnoughEnergyForAttack(int attackIndex){

		HashMap<EnergyCard.Type, Integer> energyRequired = abilities.get(attackIndex).getEnergyRequired();

		int amountFightEnergy = 0;
		int amountPsychicEnergy = 0;
		int amountWaterEnergy = 0;
		int amountLightningEnergy = 0;

		for(EnergyCard e : energy){
			EnergyCard.Type t = e.getType();
			switch (t){
				case FIGHT:
					amountFightEnergy++;
					break;
				case LIGHTNING:
					amountLightningEnergy++;
					break;
				case PSYCHIC:
					amountPsychicEnergy++;
					break;
				case WATER:
					amountWaterEnergy++;
					break;
				default:
			}
		}

		int sum = 0;
		if(energyRequired.containsKey(EnergyCard.Type.FIGHT)){
			int needed = energyRequired.get(EnergyCard.Type.FIGHT);
			if(needed > amountFightEnergy){
				return false;
			}
			sum += needed;
		}
		if(energyRequired.containsKey(EnergyCard.Type.LIGHTNING)){
			int needed = energyRequired.get(EnergyCard.Type.LIGHTNING);
			if(needed > amountLightningEnergy){
				return false;
			}
			sum += needed;
		}
		if(energyRequired.containsKey(EnergyCard.Type.PSYCHIC)){
			int needed = energyRequired.get(EnergyCard.Type.PSYCHIC);
			if(needed > amountPsychicEnergy){
				return false;
			}
			sum += needed;
		}
		if(energyRequired.containsKey(EnergyCard.Type.WATER)){
			int needed = energyRequired.get(EnergyCard.Type.WATER);
			if(needed > amountWaterEnergy){
				return false;
			}
			sum += needed;
		}
		if(energyRequired.containsKey(EnergyCard.Type.COLORLESS)){
			int needed = energyRequired.get(EnergyCard.Type.COLORLESS);
			int extra = (amountFightEnergy + amountLightningEnergy + amountPsychicEnergy + amountWaterEnergy) - sum;
			if(needed > extra){
				return false;
			}
		}

		return true;
	}
	
	public boolean hasEnoughEnergyForRetreat(){		
		return energy.size() >= energyToRetreat;
	}

	//Getters
	public PokemonCard getEvolvedFrom(){
		return evolvedFrom;
	}
	public int getEnergyToRetreat(){
		return energyToRetreat;
	}
	public Status getStatus(){
		return status;
	}
	public int getMaxHP(){
		return maxHP;
	}
	public int getCurrentHP(){
		return currentHP;
	}
	public Category getCat(){
		return cat;
	}
	public String getSimpleDescription(){
		return this.description;
	}
	public ArrayList<Ability> getAbilities(){
		return this.abilities;
	}
	public int getID() {
		return this.ID;
	}
	public String getName(){
		return this.name;
	}
	public String getEvolvesFrom(){
		return evolvesFrom;
	}
	public boolean getHasBeenHealed(){
		return hasBeenHealed;
	}
	public ArrayList<EnergyCard> getEnergy(){
		return this.energy;
	}

	//Setters
	public void setType(Type t){
		this.type = t;
	}
	public void setMaxHP(int max){
		this.maxHP = max;
		this.currentHP = this.maxHP;
	}
	public void setEnergyToRetreat(int cost){
		this.energyToRetreat = cost;
	}
	public void setCat(Category category){
		this.cat = category;
	}
	public void setEvolvesFrom(String evolvesFrom){
		this.evolvesFrom = evolvesFrom;
	}
	public void setName(String name){
		this.name = name;
	}
	public void applyStatus(Status status){
		this.status = status;
	}
	public void setID(int ID){
		this.ID = ID;
	}
	public void setHasBeenHealed(boolean healed){
		this.hasBeenHealed = healed;
	}
	public void setEvolvedFrom(PokemonCard evolvedFrom){
		this.evolvedFrom = evolvedFrom;
	}

}