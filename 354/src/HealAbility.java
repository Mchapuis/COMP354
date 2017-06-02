import java.util.HashMap;
import java.util.Map.Entry;

public class HealAbility extends Ability{
    public int healAmount = 0;
    
    public HealAbility(){
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

        PokemonCard targetPokemon = null;
        switch(targetType){
            case OPPONENT_ACTIVE:
                targetPokemon = otherPlayer.getActivePokemon();
                resultString += "Opponent's active pokemon ";
                break;
            case YOUR_ACTIVE:
                targetPokemon = sourcePlayer.getActivePokemon();
                resultString += "Your active pokemon ";
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
            default:
                targetPokemon = otherPlayer.getActivePokemon();
        }

        int maxHealAmount = targetPokemon.getMaxHP() - targetPokemon.getCurrentHP();
        int amountToHeal = Math.min(healAmount, maxHealAmount);
        targetPokemon.removeHP(-amountToHeal);
        targetPokemon.setHasBeenHealed(true);
        
        resultString += "was healed (+" + healAmount + " pts).";

        return resultString;
    }

    HealAbility(String[] description) throws UnimplementedException{
        int index = 0;

        //verify format: ability name
        if(description[index].equals("heal")){
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
        targetType = parseTarget(description[index]);

        //set heal amount
        index++;
        try{
            healAmount = Integer.valueOf(description[index]);
        }catch(Exception e){
            throw new UnimplementedException();
        }
    }
    
    public String getDescription(){
    	String desc = "Name: " + this.name;
    	desc += "<br/>";
    	desc += "HP to add: ";
    	desc += this.healAmount;
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
