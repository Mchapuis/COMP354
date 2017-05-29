
public class PokemonCard extends Card {
	
	/*// pokemon categories enum
	
	private enum TypeCat { BASIC("basic"), STAGEONE("stage-one");

		private String value;
		
		public String getValue() {
			return this.value;
		}
		
		private TypeCat(String value) {
			this.value = value;
		}
	}*/
	
	// values

	public String cat;
	public String stage;
	public String bname;
	public int bHP;
	
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
	
	public void setBName(String bname){
		this.bname = bname;
	}
	
	public void setbaseHP(int bHP)	{
		this.bHP = bHP;
	}
	
	// get
	
	public String cardType() {
		return type;
	}
	
	public String cardTypeCat() {
		return cat;
	}
	
	public String pStage() {
		return stage;
	}
	
	public String bName()	{
		return bname;
	}
	
	public int bHP()	{
		return bHP;
	}
	
	// basic constructor
	
	public PokemonCard(String name, String stage, String cat, int bHP) {
		super(name);
		setPStage(stage);
		setCardTypeCat(cat);
		setbaseHP(bHP);
		setCardType("pokemon");
	}
	
	// stage-one constructor
	
	public PokemonCard(String name, String stage, String bname, String cat, int bHP) {
		super(name);
		setPStage(stage);
		setBName(bname);	// basic evolution name
		setCardTypeCat(cat);
		setbaseHP(bHP);
		setCardType("pokemon");
	}
	
	// toString() method
	@Override
	public String toString() {
		return ("Card number " + cardNumber() + " is called " + cardName() + " and is a "+ pStage() +" "+cardTypeCat()+" "+cardType() +" card [HP: "+bHP()+"]");		
	}
}
