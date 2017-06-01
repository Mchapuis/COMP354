
public class Attack extends PokemonCard{
	
	private int attackID;
	private int energyTokens;	// number of energy cards it takes
	private int abilityID;		// reference id 
	
	//private EnergyCard energyType;
	private String energyType; //?? should it be an energy card OR a string delineating type
	
	public Attack()	{
		this.attackID = attackID;	// ???
		this.energyTokens = energyTokens;
		this.energyType = energyType;
		this.abilityID = abilityID;
	}
	
	public Attack(int attackID, String energyType, int energyTokens, int abilityID)	{
		setAttackID(attackID);
		setEnergyTokens(energyTokens);
		setEnergyType(energyType);
		setAbilityID(abilityID);
	}
	
	public void setEnergyTokens(int energyTokens)	{
		this.energyTokens = energyTokens;
	}
	
	public void setEnergyType(String energyType)	{
		this.energyType = energyType;
	}
	
	public void setAbilityID(int abilityID)	{
		this.abilityID = abilityID;
	}
	
	public void setAttackID(int attackID)	{
		this.attackID = attackID;
	}
	
	public int getAttackID()	{
		return attackID;
	}
	
	public int getEnergyTokens(){
		return energyTokens;
	}
	
	public String getEnergyType()	{
		return energyType;
	}
	
	public int getAbilityID()	{
		return abilityID;
	}
	
	public String toString()	{
		
		return ("[AttackID: " +getAttackID()+" | energy type: "+getEnergyType()+" | energy number: "+getEnergyTokens()+" | abilityID: "+getAbilityID()+"]");
		
	}

}
