import java.util.*;

public class TrainerCard extends Card {

	private Type type;
	private List<Action> actions;
	
		// trainer categories enum
	
	private enum TypeCat { STADIUM("stadium"), SUPPORTER("supporter"), ITEM("item");

		private String value;
		
		public String getValue() {
			return this.value;
		}
		
		private TypeCat(String value) {
			this.value = value;
		}
	}

	// values

	public String cat;
	
	// set
	
	@Override
	public void setcardType(String type) {
		this.type = "trainer";	
	}
	
	public void setCardTypeCat(String cat) {
		this.cat = cat;
	}
	
	// get
	
	public String cardTypeCat() {
		return cat;
	}
	
	// basic constructor

	public TrainerCard(String name) {
		super(name);
		setcardType("trainer");
	}
}

