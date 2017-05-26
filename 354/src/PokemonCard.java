import java.util.*;

public class PokemonCard extends Card {

	private enum Status {
		
	}
	
	private enum TypeCat { BASIC("basic"), STAGEONE("stage-one"), PSYCHIC("psychic"), FIGHTING("fighting");

		private String value;
		
		public String getValue() {
			return this.value;
		}
		
		private TypeCat(String value) {
			this.value = value;
		}
	}
	
	private Status status;
	private Type type;
	private String name;
	private String description;
	private int maxHP;
	private int currentHP;
	private List<Attack> attacks;
	private List<Ability> abilities;
	private List<EnergyCard> energy;
	private int[] retreatCost;
	
	public void attack(){
		
	}
	
	public void attachEnergy(EnergyCard e){
		energy.add(e);
	}
	
	
	//values
	
	public String cat;
	
	// set
	
	@Override
	public void setcardType(String type) {
		this.type = "energy";	
	}
	
	public void setCardTypeCat(String cat) {
		this.cat = cat;
	}
	
	// get
	public String cardTypeCat() {
		return cat;
	}
	
	// basic constructor

	public EnergyCard(String name) {
		super(name);
		setcardType("energy");
	}

	
}