import java.util.HashMap;
import java.util.Map.Entry;

public class DestatAbility extends Ability {

    public DestatAbility(){
        this.energyRequired = new HashMap<EnergyCard, Integer>();
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
                    targetPokemon = GameEngine.choosePokemonCard(player,targetType);
                }
                break;
            case YOUR_BENCH:
                if(sourcePlayer.getBench().size() > 0){
                    targetPokemon = GameEngine.choosePokemonCard(player,targetType);
                }
                break;
            case YOUR_POKEMON:
                targetPokemon = GameEngine.choosePokemonCard(player,targetType);
                break;
            case OPPONENT_POKEMON:
                targetPokemon = GameEngine.choosePokemonCard(player,targetType);
                break;
        }

        if(targetPokemon != null){
            targetPokemon.applyStatus(Status.NORMAL);
        }

        return true;
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

    public Ability shallowCopy(){
        DestatAbility returnCard = new DestatAbility();

        returnCard.name = this.name;
        returnCard.targetType = this.targetType;
        returnCard.subsequentAbility  = this.subsequentAbility;

        return returnCard;
    }
}
