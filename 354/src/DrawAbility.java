import java.util.HashMap;
import java.util.Map.Entry;

public class DrawAbility extends Ability{
    int amountToDraw;

    public boolean realUse(Player player){
        CardManager sourcePlayer = null;
        switch(player){
            case PLAYER:
                sourcePlayer = playerCardManager;
                break;
            case AI:
                sourcePlayer = AICardManager;
                break;
        }

        //draw cards
        for(int i = 0; i < amountToDraw; i++){
            if(sourcePlayer.getDeck().size() > 0){
                sourcePlayer.addCardToHandFromDeck(0);
            }
        }
        
        return true;
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
    
    public String getSimpleDescription(){
    	return "Draw " + amountToDraw + " cards";
    }
}
