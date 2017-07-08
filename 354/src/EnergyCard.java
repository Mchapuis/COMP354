public class EnergyCard extends Card {
	
	public enum Type{
		COLORLESS, FIGHT, PSYCHIC, LIGHTNING, WATER
	}
	
	private int ID;
	private String name;
	private String description;
	private Type type;

	public EnergyCard(){

	}

	public EnergyCard(String type){
		this.ID = 0;
		this.name = "Energy";
		
		if (type.equals("colorless")){
			this.type = Type.COLORLESS;
			this.description = "Colorless";
		} else if (type.equals("fight")) {
			this.type = Type.FIGHT;
			this.description = "Fight";
		} else if (type.equals("psychic")) {
			this.type = Type.PSYCHIC;
			this.description = "Psychic";
		} else if (type.equals("lightning")) {
			this.type = Type.LIGHTNING;
			this.description = "Lightning";
		} else if (type.equals("water")) {
			this.type = Type.WATER;
			this.description = "Water";
		}
	}
	
	public int getID() {
		return this.ID;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public String getSimpleDescription(){
		return this.description;
	}
	
	public Type getType(){
		return this.type;
	}
	
	public void setID(int ID){
		this.ID = ID;
	}

	public Card shallowCopy(){
		EnergyCard r = new EnergyCard();

		r.ID = this.ID;
		r.name = this.name;
		r.description = this.description;
		r.type = this.type;

		return r;
	}
	
}