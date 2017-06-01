
public class HealAbility extends Ability{
    public int healAmount = 0;

    public void realUse(Player player){
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
    }
}