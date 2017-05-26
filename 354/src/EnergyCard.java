
public class EnergyCard extends Card {
	
	// energy categories enum
	
	enum TypeCat { COLORLESS("colorless"), WATER("water"), LIGHTNING("lightning");

		private String value;
		
		public String getValue() {
			return this.value;
		}
		
		private TypeCat(String value) {
			this.value = value;
		}
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
