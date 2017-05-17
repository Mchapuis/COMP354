import java.util.*;

public class PokemonCard extends Card {

	private enum Status {
		
	}
	
	private enum Type {
		
	}
	
	private Status status;
	private Type type;
	private String name;
	private String description;
	private int maxHP;
	private int currentHP;
	private List<Attack> attacks;
	private List<Ability> abilities;
	private List<EnergyCard> energy;
	private int[] retreatCost;
	
	
}