import com.sun.tools.javac.jvm.Gen;

public class GenericCard extends Card {

	public Card shallowCopy() {
		GenericCard r = new GenericCard("","");

		r.ID = this.ID;
		r.name = this.name;
		r.description = this.description;

		return r;
	}

	private int ID;
	private String name;
	private String description;

	public GenericCard(String titleString, String displayString){
		this.ID = 0;
		this.name = titleString;
		this.description = displayString;
	}

	public GenericCard(int sizeOfPile){
		this.ID = 0;
		this.name = sizeOfPile + " cards";
		this.description = "";
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return this.description;
	}

	public String getSimpleDescription() {
		return this.description;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}
}
