import java.util.*;


public class PokemonCard extends Card {
	
	// pokemon categories enum
	
	private enum Status { NORMAL, PARALYZED, ASLEEP;	} // among others.. tbused at a later iteration i think
	
	// values

	private String cat;
	private String stage;
	private String evolvesFrom;
	private int baseHP;
	private int retreatToken;
	
	private int currentHP;
	private Status status;
	public ArrayList<Attack> attacks;
	private ArrayList<EnergyCard> energy;
	public Attack attack;
	
	// set
	
	@Override
	public void setCardType(String type) {
		this.type = "pokemon";
	}
	
	public void setCardTypeCat(String cat) {
		this.cat = cat;
	}
	
	public void setPStage(String stage) {
		this.stage = stage;
	}
	
	public void setEvolvesFrom(String evolvesFrom){
		this.evolvesFrom = evolvesFrom;
	}
	
	public void setbaseHP(int baseHP)	{
		this.baseHP = baseHP;
	}
	
	public void setRetreat(int retreatToken){
		
		this.retreatToken = retreatToken;
	}
	
	public void setStatus(Status status)	{
		
		this.status = status;
	}
	
	public void setAttacks(ArrayList<Attack> attacks){
		this.attacks = attacks;
	}
	
	// get
	
	public String getType() {
		return type;
	}
	
	public String getCat() {
		return cat;
	}
	
	public String getpStage() {
		return stage;
	}
	
	public String evolvesFrom()	{
		return evolvesFrom;
	}
	
	public int getBaseHP()	{
		return baseHP;
	}
	
	public int retreatToken()	{
		return retreatToken;
	}
	
	public int currentHP()	{
		return currentHP;
	}
	
	public ArrayList<Attack> getAttacks()	{
		return attacks;
	}
	
	public PokemonCard(){
		
		this.name = "nope";
		this.cat = "colorless";
		this.type = "pokemon";
		this.stage = "basic";
		this.status = Status.NORMAL;
		this.retreatToken = 0;
		this.currentHP = baseHP;
		this.attacks = new ArrayList<Attack>();
		this.energy = new ArrayList<EnergyCard>();
	}
	
	
	// basic constructor
	
	public PokemonCard(String name, String stage, String cat, int baseHP, int retreatToken) {
		super(name);
		setPStage(stage);
		setCardTypeCat(cat);
		setbaseHP(baseHP);
		setCardType("pokemon");
		setRetreat(retreatToken);
		setStatus(status);
		this.attacks = new ArrayList<Attack>();
	}
	
	// stage-one constructor
	
	public PokemonCard(String name, String stage, String evolvesFrom, String cat, int baseHP, int retreatToken) {
		super(name);
		setPStage(stage);
		setEvolvesFrom(evolvesFrom);	// basic evolution name
		setCardTypeCat(cat);
		setbaseHP(baseHP);
		setCardType("pokemon");
		setRetreat(retreatToken);
		setStatus(status);
		this.attacks = new ArrayList<Attack>();
	}
	
	// toString() method
	@Override
	public String toString() {
		return ("Card number " + cardID() + " is called " + cardName() + " and is a "+ getpStage() +" "+getCat()+" "+getType() +" card [HP: "+getBaseHP()+"]");		
	}
	
	public void attack(){

	}

	public void attachEnergy(EnergyCard e){
		energy.add(e);
	}

	public void removeEnergy(EnergyCard e){
		energy.remove(e);
	}

	public ArrayList<EnergyCard> getEnergy(){
		return this.energy;
	}

	public void addAttack(Attack attack){
		attacks.add(attack);
	}
	
	public void modifyHealth(int amount){
		this.currentHP += amount;
	}
}
