import java.util.HashMap;
import java.util.Map.Entry;

public class ApplyStatAbility extends Ability{

    private Status givenStatus;
    
    public ApplyStatAbility(){
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

        switch(targetType){
            case OPPONENT_ACTIVE:
                otherPlayer.getActivePokemon().applyStatus(givenStatus);
                if (sourcePlayer.equals(playerCardManager))
                	resultString += "Status " + givenStatus + " applied to opponent's active pokemon. ";
                else 
                	resultString += "Status " + givenStatus + " applied to your active pokemon. ";
                break;
            case YOUR_ACTIVE:
                sourcePlayer.getActivePokemon().applyStatus(givenStatus);
                if (sourcePlayer.equals(playerCardManager))
                	resultString += "Status " + givenStatus + " applied to your active pokemon. ";
                else 
                	resultString += "Status " + givenStatus + " applied to opponent's active pokemon. ";
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

    ApplyStatAbility(String [] description) throws UnimplementedException{
        int index = 0;

        //verify the format

        //-starts with applystat
        if(description[index].equals("applystat")){
            index++;
        }
        else{
            throw new UnimplementedException();
        }

        //-next token is status
        if(description[index].equals("status")){
           index++;
        }
        else{
            throw new UnimplementedException();
        }

        //next token is the status
        switch(description[index]){
            case "paralyzed":
                givenStatus = Status.PARALYZED;
                break;
            case "stuck":
                givenStatus = Status.STUCK;
                break;
            case "poisoned":
                givenStatus = Status.POISONED;
                break;
            case "asleep":
                givenStatus = Status.ASLEEP;
                break;
            default:
                givenStatus = Status.NORMAL;
        }

        //get target
        index++;

        if(description[index].equals("choice")){
            index++;
        }
        this.targetType = parseTarget(description[index++]);

    }
    
    public String getDescription(){
    	String desc = "Name: " + this.name;
    	desc += "<br/>";
    	desc += "Status to apply: ";
    	desc += this.givenStatus;
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
