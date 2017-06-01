
public abstract class Card {

/*	// my initial attempt at an enum of general card types
	
	enum Type { ENERGY("energy"), POKEMON("pokemon"), TRAINER("trainer");
	
		private String value;
		
		public String getValue() {
			return this.value;
		}
		
		private Type(String value) {
			this.value = value;
		}
	}*/
	
	// current values of the abstract card class
	
	protected static int cardnumber = 1;
	private final int cID;
	public String name;
	public String type;
	
	// basic card constructor (?)
	
	public Card(String name) {
		cID = cardnumber++;
		this.name = name;
	}
	
	public Card(){
		cID = cardnumber++;
		name = "card name";
	}
	
	public abstract void setCardType(String type);
	
	public void setCardName(String name) {
		this.name = name;
	}
	
	//get

	public int cardID()	{
		return cID;
	}
	
	public String cardName()	{
		return name;
	}
	
	public String getType()	{
		return type;
	}
	
/*	// toString() method
	public String toString() {
		return ("Card number " + cnum + " is called " + cardName() + "and is an " + cardType() +" type card");
	}
	*/
	
	// superclass toString() method
		public String toString() {
			return ("Card number " + cardID() + " is called " + cardName());
		}

	public void equals(Card c){
		// tbw
	}

}
