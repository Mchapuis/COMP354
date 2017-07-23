import java.util.HashMap;
import java.util.Map.Entry;

public class DrawAbility extends Ability{
    ComplexAmount amountToDraw;

    public DrawAbility(){
        this.energyRequired = new HashMap<EnergyCard.Type, Integer>();
    }

    public boolean realUse(Player player){
        CardManager sourcePlayer = null;
        switch(player){
            case PLAYER:
                if(targetType == Target.YOU){
                    sourcePlayer = playerCardManager;
                }
                else{
                    sourcePlayer = AICardManager;
                }
                break;
            case AI:
                if(targetType == Target.YOU){
                    sourcePlayer = AICardManager;
                }
                else{
                    sourcePlayer = playerCardManager;
                }
                break;
        }

        //draw cards
        for(int i = 0; i < amountToDraw.evaluate(player); i++){
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

        //get target if specified
        if(description[index].equals("opponent")){
            index++;
            targetType = Target.OPPONENT;
        }
        else{
            targetType = Target.YOU;
        }

        //get amount to draw
        try{
            amountToDraw = new ComplexAmount(description[index]);
        }catch(Exception e){
            throw new UnimplementedException();
        }
    }
    
    public String getSimpleDescription(){
    	if(targetType == Target.YOU){
            return "Draw " + amountToDraw.getDescription() + " cards";
        }
        else{
    	    return "Opponent draws " + amountToDraw.getDescription() + " cards";
        }
    }

    public Ability shallowCopy(){
        DrawAbility returnCard = new DrawAbility();

        returnCard.name = this.name;
        returnCard.targetType = this.targetType;
        returnCard.subsequentAbility  = this.subsequentAbility;

        returnCard.amountToDraw = this.amountToDraw;

        return returnCard;
    }
}
