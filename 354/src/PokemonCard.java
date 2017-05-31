import java.util.*;

public class PokemonCard extends Card {

	private enum Status {
		NORMAL, PARALYZED
	}
	
	private enum Category {
		BASIC, STAGEONE
	}
	
	private enum Type {
		LIGHTNING, NORMAL
	}
	
	private int ID;
	private String name;
	private String description;
	private Category cat;
	private Type type;
	private int maxHP;
	private HashMap<EnergyCard, Integer> retreat;
	
	private Status status;
	private int currentHP;
	private ArrayList<Attack> attacks;
	private ArrayList<EnergyCard> energy;
	
	public PokemonCard(){
		this.ID = 0;
		this.name = "Undefined";
		this.description = "No description";
		this.cat = Category.BASIC;
		this.type = Type.NORMAL;
		this.maxHP = 0;
		this.retreat = new HashMap<EnergyCard, Integer>();
		this.status = Status.NORMAL;
		this.currentHP = 0;
		this.attacks = new ArrayList<Attack>();
		this.energy = new ArrayList<EnergyCard>();
	}
	
	public PokemonCard(String name, String description, String cat, String type, int maxHP, HashMap<EnergyCard, Integer> retreatMap){
		this.name = name;
		this.description = description;
		
		if (cat.equals("BASIC")){
			this.cat = Category.BASIC;
		} else {
			this.cat = Category.STAGEONE;
		}
		
		if (type.equals("LIGHTNING")) {
			this.type = Type.LIGHTNING;
		} else {
			this.type = Type.NORMAL;
		}
		
		this.maxHP = maxHP;
		this.retreat = retreatMap;
		
		this.status = Status.NORMAL;
		this.currentHP = maxHP;
		this.attacks = new ArrayList<Attack>();
		this.energy = new ArrayList<EnergyCard>();		
	}
	
	public ArrayList<Attack> getAttacks(){
		return this.attacks;
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
		if (this.attacks.size() == 0){
			desc += "None";
		} else {
			desc += "<br/>";
			desc += "-----------------";
			desc += "<br/>";
			int i = 0;
			for (Attack a : this.attacks){
				desc += a.getDescription();
				if (i != this.attacks.size()){
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
	}
	
	public void removeEnergy(EnergyCard e){
		energy.remove(e);
	}
	
	public ArrayList<EnergyCard> getEnergy(){
		return this.energy;
	}
	
	public void addAttack(Attack attack){
		attacks.add(attack);
	}
	
	public void removeHP(int points){
		this.currentHP -= points;
	}
	
	public boolean hasEnoughEnergy(int attackIndex){
		Attack attack = this.attacks.get(attackIndex);
		
		boolean enough = true;
		HashMap<EnergyCard, Integer> energyRequired = attack.getEnergyRequired();
		for (Map.Entry<EnergyCard, Integer> entry : energyRequired.entrySet()) {
			String type = entry.getKey().getType();
			
			int count = 0;
			for (EnergyCard energy : this.energy){
				String typeToCompare = energy.getType();
				if (typeToCompare.equals(type)){
					count++;
				}
			}
			
			int amount = entry.getValue();
			if (count < amount){
				enough = false;
			}
		}
		
		return enough;
	}
	
	public void applyStatus(String status){
		if (status.equals("PARALYZED")){
			this.status = Status.PARALYZED;
		}
	}
	
	public void setID(int ID){
		this.ID = ID;
	}
	
}