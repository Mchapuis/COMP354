
public class GenericCard extends Card {

	private int ID;
	private String name;
	private String description;
	
	public GenericCard(int sizeOfPile){
		this.ID = 0;
		this.name = "";
		this.description = sizeOfPile + " cards";
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
