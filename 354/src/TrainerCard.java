import java.util.*;

public class TrainerCard extends Card {

	private enum Type{
		
	}
	
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
	
}