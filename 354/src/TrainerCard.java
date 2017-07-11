
public class TrainerCard extends Card {

	private enum Type{
		
	}
	
	private int ID;
	private String name;
	private String description;
	private Type type;
	private Ability ability;

	TrainerCard(){
		this.name = "Unnamed";
		ability = null;
	}

	TrainerCard(String name, Ability ability){
		this.ability = ability;
		this.name = name;
	}

	

	
	public String getDescription() {
		return "<html>" + this.ability.getDescription() + "</html>";
	}
	public String getSimpleDescription(){
		return this.description;
	}
	
	public void setID(int ID){
		this.ID = ID;
	}



	public Card shallowCopy(){
		TrainerCard r = new TrainerCard();

		r.ID = this.ID;
		r.name = this.name;
		r.description = this.description;
		r.type = this.type;
		r.ability = this.ability;

		return r;
	}

	//Getters
	public int getID() {
		return this.ID;
	}
	public String getName() {
		return this.name;
	}
	public Ability getAbility(){
		return ability;
	}

}