
public abstract class Ability {
	
	// current values of the abstract ability class
	
	protected static int abilitynumber = 1;
	private final int anum;
	public String name;
	public String type;

	// basic ability constructor
	
	public Ability(String name) {
		anum = abilitynumber++;
		this.name = name;
	}
	
	public Ability(){
		anum = abilitynumber++;
		name = "ability name";
	}
	
	//set
	
	public abstract void setAbilityType(String type);
	
	public void setAbilityName(String name) {
		this.name = name;
	}
	
	//get

	public int abilityNumber()	{
		return anum;
	}
	
	public String abilityName()	{
		return name;
	}
	
/*	public String abilityType()	{
		return type;
	}*/
	
/*	// toString() method
	public String toString() {
		return ("Ability number " + anum + " is called " + abilityName() + "and is an " + abilityType() +" type ability");
	}
	*/
	
	// superclass toString() method
		public String toString() {
			return ("Ability number " + abilityNumber() + " is called " + abilityName());
		}

	public void equals(Ability a){
		// tbw
	}

}

