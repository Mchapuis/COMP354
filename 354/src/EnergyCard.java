
public class EnergyCard extends Card {
	
/*	// energy categories enum
	
	enum TypeCat { COLORLESS("colorless"), WATER("water"), LIGHTNING("lightning"), PSYCHIC("psychic"), FIGHTING("fighting");

		private String value;
		
		public String getValue() {
			return this.value;
		}
		
		private TypeCat(String value) {
			this.value = value;
		}
	}*/

	//values
	
	public String cat;
	
	// set
	
	@Override
	public void setCardType(String type) {
		this.type = "energy";	
	}
	
	public void setCardTypeCat(String cat) {
		this.cat = cat;
	}
	
	// get
	public String cardTypeCat() {
		return cat;
	}
	
	public String cardType() {
		return type;
	}
	
	// constructor

	public EnergyCard(String name, String cat) {
		super(name);
		setCardTypeCat(cat);
		setCardType("energy");
	}
	
	// toString() method
	@Override
	public String toString() {
		return ("Card number " + cardID() + " is called " + cardName() + " and is a " + cardTypeCat() +" type "+cardType()+" card");		
	}
	
}
