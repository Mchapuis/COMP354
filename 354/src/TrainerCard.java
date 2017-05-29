
public class TrainerCard extends Card {
	
	/*// trainer categories enum
	
	private enum TypeCat { STADIUM("stadium"), SUPPORTER("supporter"), ITEM("item");

		private String value;
		
		public String getValue() {
			return this.value;
		}
		
		private TypeCat(String value) {
			this.value = value;
		}
	}*/

	// values

	public String cat;
	public int num;
	
	// set
	
	@Override
	public void setCardType(String type) {
		this.type = "trainer";	
	}
	
	public void setCardTypeCat(String cat) {
		this.cat = cat;
	}
	
	public void setTNum(int num) {
		this.num = num;
	}
	
	// get
	
	public String cardType() {
		return type;
	}
	
	public String cardTypeCat() {
		return cat;
	}
	
	public int tNum() {
		return num;
	}
	
	// constructor

	public TrainerCard(String name, String cat, int num) {
		super(name);
		setCardTypeCat(cat);
		setCardType("trainer");
		setTNum(num);
	}

	// toString() method
	@Override
	public String toString() {
		return ("Card number " + cardNumber() + " is called " + cardName() + " and is a " +cardTypeCat()+" type "+ cardType() +" card [TNum: " + tNum() +"]");		
	}
}
