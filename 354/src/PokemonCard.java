import java.util.*;

public class PokemonCard extends Card {

	private enum Status {
		NORMAL
	}
	
	private enum Category {
		BASIC, STAGEONE
	}
	
	private enum Type {
		LIGHTNING, NORMAL
	}
	
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
	
	public String getName(){
		return this.name;
	}
	
	public String getDescription(){
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
	
}