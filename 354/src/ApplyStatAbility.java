import java.util.HashMap;
import java.util.Map.Entry;

public class ApplyStatAbility extends Ability{

    private Status givenStatus;
    
    public ApplyStatAbility(){
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

        PokemonCard targetedPokemon = null;

        switch(targetType){
            case OPPONENT_ACTIVE:
                targetedPokemon = otherPlayer.getActivePokemon();
                break;
            case YOUR_ACTIVE:
                targetedPokemon = sourcePlayer.getActivePokemon();
                break;
            case OPPONENT_BENCH:
                if(otherPlayer.getBench().size() > 0){
                    targetedPokemon = GameEngine.choosePokemonCard(player,targetType);
                }
                break;
            case YOUR_BENCH:
                if(sourcePlayer.getBench().size() > 0){
                    targetedPokemon = GameEngine.choosePokemonCard(player,targetType);
                }
                break;
            case YOUR_POKEMON:
                targetedPokemon = GameEngine.choosePokemonCard(player,targetType);
                break;
            case OPPONENT_POKEMON:
                targetedPokemon = GameEngine.choosePokemonCard(player,targetType);
                break;
            case LAST:
                targetedPokemon = Ability.lastTargetedPokemon;
                break;
        }

        if(targetedPokemon != null){
            targetedPokemon.applyStatus(givenStatus);
            Ability.lastTargetedPokemon = targetedPokemon;
        }
        return true;
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
    
    public String getSimpleDescription(){
        return "Apply status: " + givenStatus.toString() + " to " + targetType.toString();
    }

    public Ability shallowCopy(){
        ApplyStatAbility returnCard = new ApplyStatAbility();

        returnCard.name = this.name;
        returnCard.targetType = this.targetType;
        returnCard.subsequentAbility  = this.subsequentAbility;

        returnCard.givenStatus = this.givenStatus;

        return returnCard;
    }

}
