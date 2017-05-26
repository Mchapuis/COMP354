
public abstract class Card {

	// my initial attempt at an enum of general card types
	
	enum Type { ENERGY("energy"), POKEMON("pokemon"), TRAINER("trainer");
	
		private String value;
		
		public String getValue() {
			return this.value;
		}
		
		private Type(String value) {
			this.value = value;
		}
	}
	
	// current values of the abstract card class
	
	protected static int cardnumber = 1;
	private final int cnum;
	public String name;
	public String type;
	
	// basic card constructor (?)
	
	public Card(String name) {
		cnum = cardnumber++;
		this.name = name;
	}
	
	public Card(){
		cnum = cardnumber++;
		name = "card name";
		type = "card type";
	}
	
	//set
	
	public abstract void setcardType(String type);
	
	public void setcardName(String name) {
		this.name = name;
	}
	
	//get

	public int cardNumber()	{
		return cnum;
	}
	
	public String cardName()	{
		return name;
	}
	
	public String cardType()	{
		return type;
	}
	
	// toString() method
	public String toString() {
		return ("Card number " + cnum + " is called " + cardName() + "and is an " + cardType() +" type card");
	}

	public void equals(Card c){
		// tbw
	}

}
