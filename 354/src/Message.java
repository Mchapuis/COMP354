
public class Message {
	public static enum Side{
		PLAYER, AI, NONE
	}
	
	public static enum ButtonType{
		HAND, 
		BENCH, 
		ACTIVE, 
		DECK, 
		DISCARD, 
		PRIZECARDS, 
		MAKEACTIVE, 
		ADDTOBENCH, 
		ATTACHENERGY, 
		ATTACK, 
		LETAIPLAY, 
		RETREAT, 
		PLAYITEM, 
		EVOLVE,
		CARDSELECTOR,
		SELECT,
		CHOICE
	}
	
	private Side side;
	private ButtonType type;
	private int index;	
	
	public Message(String side, String type, int index){
		if (side.equals("player")){
			this.side = Side.PLAYER;
		} else {
			this.side = Side.AI;
		}
		
		if (type.equals("active")){
			this.type = ButtonType.ACTIVE;
		} else if (type.equals("bench")){
			this.type = ButtonType.BENCH;
		} else if (type.equals("hand")){
			this.type = ButtonType.HAND;
		} else if (type.equals("deck")){ 
			this.type = ButtonType.DECK;
		} else if (type.equals("discard")){ 
			this.type = ButtonType.DISCARD;
		} else if (type.equals("prizecards")){ 
			this.type = ButtonType.PRIZECARDS;
		} else if (type.equals("makeactive")){
			this.type = ButtonType.MAKEACTIVE;
		} else if (type.equals("addtobench")){
			this.type = ButtonType.ADDTOBENCH;
		} else if (type.equals("attachenergy")){
			this.type = ButtonType.ATTACHENERGY;
		} else if (type.equals("attack")){
			this.type = ButtonType.ATTACK;
		} else if (type.equals("letAIplay")){
			this.type = ButtonType.LETAIPLAY;
		} else if (type.equals("retreat")){
			this.type = ButtonType.RETREAT;
		} else if (type.equals("playitem")){
			this.type = ButtonType.PLAYITEM;
		} else if (type.equals("evolve")){
			this.type = ButtonType.EVOLVE;
		} else if (type.equals("cardselector")){
			this.type = ButtonType.CARDSELECTOR;
		} else if (type.equals("select")){
			this.type = ButtonType.SELECT;
		} else if (type.equals("choice")){
			this.type = ButtonType.CHOICE;
		}

		
		this.index = index;
	}
	
	public Side getSide(){
		return this.side;
	}
	
	public ButtonType getType(){
		return this.type;
	}
	
	public int getIndex(){
		return this.index;
	}
}
