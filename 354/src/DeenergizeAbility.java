import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class DeenergizeAbility extends Ability{
    int amountToRemove;

    public DeenergizeAbility(){
    	this.energyRequired = new HashMap<EnergyCard, Integer>();
    }
    
    public String realUse(Player player){
        //TODO: find out if deenergized energy goes to discard or hand
        //until we find out. it is discarded
    	
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

        resultString += amountToRemove + " energy was removed from ";
        
        PokemonCard targetPokemon = null;
        switch(targetType){
            case OPPONENT_ACTIVE:
                targetPokemon = otherPlayer.getActivePokemon();
                resultString += "opponent's active pokemon. ";
                break;
            case YOUR_ACTIVE:
                targetPokemon = sourcePlayer.getActivePokemon();
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

        if(targetPokemon != null){
            ArrayList<EnergyCard> energy = targetPokemon.getEnergy();
            for(int i = 0; i < amountToRemove && !energy.isEmpty(); i++){
                EnergyCard e = energy.get(0);
                sourcePlayer.addToDiscard(e);
                energy.remove(e);
            }
        }

        return resultString;
    }

    DeenergizeAbility(String[] description) throws UnimplementedException{
        int index = 0;

        //verify format: ability name
        if(description[index].equals("deenergize")){
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

        //get target
        if(description[index].equals("choice")){
            index++;
        }
        targetType = parseTarget(description[index]);
        index++;

        //get amount
        try{
            amountToRemove = Integer.valueOf(description[index]);
        }catch(Exception e){
            throw new UnimplementedException();
        }
    }
    
    public String getDescription(){
    	String desc = "Name: " + this.name;
    	desc += "<br/>";
    	desc += "Number energy to remove: ";
    	desc += this.amountToRemove;
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
