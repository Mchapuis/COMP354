import java.util.HashMap;
import java.util.Map.Entry;

public class DrawAbility extends Ability{
    int amountToDraw;

    public DrawAbility(){
    	this.energyRequired = new HashMap<EnergyCard, Integer>();
    }
    
    public String realUse(Player player){
    	String resultString = "";
        CardManager sourcePlayer = null;
        switch(player){
            case PLAYER:
                sourcePlayer = playerCardManager;
                resultString += "You ";
                break;
            case AI:
                sourcePlayer = AICardManager;
                resultString += "Opponent ";
                break;
        }

        //draw cards
        for(int i = 0; i < amountToDraw; i++){
            sourcePlayer.addCardToHandFromDeck(0);
        }

        resultString += "drew " + amountToDraw + " cards.";
        
        return resultString;
    }

    DrawAbility(String[] description) throws UnimplementedException{
        int index = 0;

        //verify format: ability name
        if(description[index].equals("draw")){
            index++;
        }
        else{
            throw new UnimplementedException();
        }

        //get amount to draw
        try{
            amountToDraw = Integer.valueOf(description[index]);
        }catch(Exception e){
            throw new UnimplementedException();
        }
    }
    
    public String getDescription(){
    	String desc = "Name: " + this.name;
    	desc += "<br/>";
    	desc += "Cards to draw: ";
    	desc += this.amountToDraw;
    	desc += "<br/>";
    	desc += "Energy required: ";
		desc += "<br/>";
		for (Entry<EnergyCard, Integer> entry : energyRequired.entrySet()){
			desc += "&nbsp;&nbsp;&nbsp;";
			desc += entry.getKey().getType();
			desc += ": ";
			desc += entry.getValue();
			desc += "<br/>";
		}
    	return desc;
    }
}
