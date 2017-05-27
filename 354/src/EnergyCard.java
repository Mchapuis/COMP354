public class EnergyCard extends Card {
	
	private enum Type{
		COLORLESS, OTHER
	}
	
	private String name;
	private String description;
	private Type type;

	public EnergyCard(String type){
		this.name = "Energy";
		
		if (type.equals("COLORLESS")){
			this.type = Type.COLORLESS;
			this.description = "Colorless";
		} else {
			this.type = Type.OTHER;
			this.description = "Other";
		}
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public Type getType(){
		return this.type;
	}
	
}