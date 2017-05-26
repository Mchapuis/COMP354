public class EnergyCard extends Card {
	
	private enum Type{
		COLORLESS, OTHER
	}
	
	private Type type;

	public EnergyCard(String type){
		if (type.equals("COLORLESS")){
			this.type = Type.COLORLESS;
		} else {
			this.type = Type.OTHER;
		}
	}
	
}