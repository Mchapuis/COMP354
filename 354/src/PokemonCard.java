import java.util.*;

public class PokemonCard extends Card {

	private enum Category {
		BASIC, STAGEONE
	}
	
	private enum Type {
		LIGHTNING, NORMAL, PSYCHIC, WATER
	}
	
	private int ID;
	private String name;
	private String description;
	private Category cat;
	private Type type;
	private int maxHP;
	private int energyToRetreat;
	
	private Status status;
	private int currentHP;
	private ArrayList<Ability> abilities;
	private ArrayList<EnergyCard> energy;
	private int numColorlessEnergy;
	private boolean knockedOut;

	private boolean hasBeenHealed = false;
	
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
		this.knockedOut = false;
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
	
	public ArrayList<Ability> getAbilities(){
		return this.abilities;
	}
	
	public int getID() {
		return this.ID;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getDescription(){
		String desc = "<html><body>";
		desc += this.description;
		desc += "<br/>";
		desc += "=================";
		desc += "<br/>";
		desc += "HP: ";
		desc += this.currentHP;
		desc += "/";
		desc += this.maxHP;
		desc += "<br/>";
		desc += "Status: ";
		desc += this.status;
		desc += "<br/>";
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
		desc += "<br/>";
		desc += "=================";
		desc += "<br/>";
		desc += "Attacks: ";
		if (this.abilities.size() == 0){
			desc += "None";
		} else {
			desc += "<br/>";
			desc += "-----------------";
			desc += "<br/>";
			int i = 0;
			for (Ability a : this.abilities){
				desc += a.getDescription();
				if (i != this.abilities.size()){
					desc += "<br/>";
					desc += "-----------------";
					desc += "<br/>";
				}
			}
		}
		desc += "</html></body>";		
		return desc;
	}
	
	public String getSimpleDescription(){
		return this.description;
	}
	
	public void attack(){
		
	}
	
	public void attachEnergy(EnergyCard e){
		energy.add(e);
		numColorlessEnergy++;
	}
	
	public void removeEnergy(EnergyCard e){
		energy.remove(e);
	}
	
	public ArrayList<EnergyCard> getEnergy(){
		return this.energy;
	}
	
	public void addAbility(Ability ability){
		abilities.add(ability);
	}

	public int getMaxHP(){
	    return maxHP;
    }

    public int getCurrentHP(){
	    return currentHP;
    }
	
	public void removeHP(int points){
		this.currentHP -= points;
		if (this.currentHP <= 0) {
			knockedOut = true;
			this.currentHP = 0;
		}
	}
	
	public boolean hasEnoughEnergyForAttack(int attackIndex){
		numColorlessEnergy = this.energy.size();
		Ability ability = this.abilities.get(attackIndex);
		
		boolean enough = true;
		HashMap<EnergyCard, Integer> energyRequired = ability.getEnergyRequired();
		
		for (Map.Entry<EnergyCard, Integer> entry : energyRequired.entrySet()) {
			EnergyCard.Type type = entry.getKey().getType();
			
			if (type == EnergyCard.Type.COLORLESS)
				continue;
			int amount = entry.getValue();
		
			int count = 0;
			for (EnergyCard energy : this.energy){
				EnergyCard.Type typeToCompare = energy.getType();
				if (typeToCompare == type){
					count++;
					numColorlessEnergy--;
					if (count == amount){
						break;
					}
				}
			}
			
			if (count < amount){
				enough = false;
			}
			
		}
		
		for (Map.Entry<EnergyCard, Integer> entry : energyRequired.entrySet()) {
			EnergyCard.Type type = entry.getKey().getType();
			if (type == EnergyCard.Type.COLORLESS){
				int amount = entry.getValue();
				if (numColorlessEnergy < amount){
					enough = false;
				}
			}
		}
		
		return enough;
	}
	
	public boolean hasEnoughEnergyForRetreat(){		
		return energy.size() <= energyToRetreat;
	}

	public void applyStatus(Status status){
		this.status = status;
	}
	//TODO: remove this method when it is no longer being used by methods being refactored out
	public void applyStatus(String status){
		if (status.equals("PARALYZED")){
			this.status = Status.PARALYZED;
		}
	}
	
	public void setID(int ID){
		this.ID = ID;
	}

	public boolean getHasBeenHealed(){
		return hasBeenHealed;
	}
	public void setHasBeenHealed(boolean healed){
		this.hasBeenHealed = healed;
	}
	
	public boolean getKnockedOut(){
		return this.knockedOut;
	}
	
}