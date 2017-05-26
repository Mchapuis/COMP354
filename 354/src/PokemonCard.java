import java.util.*;

public class PokemonCard extends Card {

	private enum Status {
		NORMAL
	}
	
	private enum Category {
		BASIC, STAGEONE
	}
	
	private enum Type {
		LIGHTNING, UNDEFINED
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
	
	public PokemonCard(String name, String description, String cat, String type, int maxHP, HashMap retreatMap){
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
			this.type = Type.UNDEFINED;
		}
		
		this.maxHP = maxHP;
		this.retreat = retreatMap;
		
		this.status = Status.NORMAL;
		this.currentHP = maxHP;
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