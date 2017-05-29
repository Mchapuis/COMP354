import java.util.*;

public class TrainerCard extends Card {

	private enum Type{
		
	}
	
	private int ID;
	private String name;
	private String description;
	private Type type;
	private List<Action> actions;
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getSimpleDescription(){
		return this.description;
	}
	
	public void setID(int ID){
		this.ID = ID;
	}

	public int getID() {
		return this.ID;
	}
	
}