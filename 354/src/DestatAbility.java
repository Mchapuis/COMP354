import java.util.HashMap;
import java.util.Map.Entry;

public class DestatAbility extends Ability {

	public DestatAbility(){
    	this.energyRequired = new HashMap<EnergyCard, Integer>();
    }
	
    public String realUse(Player player){
    	String resultString = "";
        CardManager sourcePlayer = null, otherPlayer = null;
        switch(player){
            case PLAYER:
                sourcePlayer = playerCardManager;
                otherPlayer = AICardManager;
                break;
            case AI:
                sourcePlayer = AICardManager;
                otherPlayer = playerCardManager;
                break;
        }
        
        resultString += "Status NORMAL was applied to ";

        switch(targetType){
            case OPPONENT_ACTIVE:
                otherPlayer.getActivePokemon().applyStatus(Status.NORMAL);
                resultString += "opponent's active pokemon. ";
                break;
            case YOUR_ACTIVE:
                sourcePlayer.getActivePokemon().applyStatus(Status.NORMAL);
                resultString += "your active pokemon. ";
                break;
            case OPPONENT_BENCH:
                //TODO: need to implement method to get selection
                break;
            case YOUR_BENCH:
                //TODO: need to implement method to get selection
                break;
            case YOUR_POKEMON:
                //TODO: need to implement method to get selection
                break;
            case OPPONENT_POKEMON:
                //TODO: need to implement method to get selection
                break;
        }

        return resultString;
    }

    DestatAbility(String[] description) throws UnimplementedException{
        int index = 0;

        //verify format: ability type
        if(description[index].equals("destat")){
            index++;
        }
        else{
            throw new UnimplementedException();
        }

        //verify format: target
        if(description[index].equals("target")){
            index++;
        }
        else{
            throw new UnimplementedException();
        }

        //set target
        if(description[index].equals("choice")){
            index++;
        }
        this.targetType = parseTarget(description[index++]);


    }
    
    public String getSimpleDescription(){
        return "Removes any status from " + targetType.toString();
    }
}
