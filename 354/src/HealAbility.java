import java.util.HashMap;
import java.util.Map.Entry;

public class HealAbility extends Ability{
    public int healAmount = 0;

    public HealAbility(){
        this.energyRequired = new HashMap<EnergyCard.Type, Integer>();
    }

    public boolean realUse(Player player){
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
                break;
            case YOUR_ACTIVE:
                targetPokemon = sourcePlayer.getActivePokemon();
                break;
            case OPPONENT_BENCH:
                if(otherPlayer.getBench().size() > 0){
                    targetPokemon = GameEngine.choosePokemonCard(player, targetType);
                }
                break;
            case YOUR_BENCH:
                if(sourcePlayer.getBench().size() > 0){
                    targetPokemon = GameEngine.choosePokemonCard(player, targetType);
                }
                break;
            case YOUR_POKEMON:
                targetPokemon = GameEngine.choosePokemonCard(player, targetType);
                break;
            case OPPONENT_POKEMON:
                targetPokemon = GameEngine.choosePokemonCard(player, targetType);
                break;
            default:
                targetPokemon = otherPlayer.getActivePokemon();
        }

        if (targetPokemon != null){
            int maxHealAmount = targetPokemon.getMaxHP() - targetPokemon.getCurrentHP();
            int amountToHeal = Math.min(healAmount, maxHealAmount);
            targetPokemon.removeHP(-amountToHeal);
            targetPokemon.setHasBeenHealed(true);
        }

        return true;
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
    
    public String getSimpleDescription(){
    	return "Heal up to " + healAmount + " HP on " + targetType.toString();
    }

    public Ability shallowCopy(){
        HealAbility returnCard = new HealAbility();

        returnCard.name = this.name;
        returnCard.targetType = this.targetType;
        returnCard.subsequentAbility  = this.subsequentAbility;

        returnCard.healAmount = this.healAmount;

        return returnCard;
    }
}
