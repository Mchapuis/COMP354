import java.util.ArrayList;
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

        ArrayList<PokemonCard> targetedPokemon = new ArrayList<>();
        switch(targetType){
            case OPPONENT_BENCH:
                if(hasChoice){
                    if(otherPlayer.getBench().size() > 0){
                        targetedPokemon.add(GameEngine.choosePokemonCard(player,targetType));
                    }
                }
                else{
                    for(PokemonCard p : otherPlayer.getBench()){
                        targetedPokemon.add(p);
                    }
                }
                break;
            case YOUR_BENCH:
                if(hasChoice){
                    if(sourcePlayer.getBench().size() > 0){
                        targetedPokemon.add(GameEngine.choosePokemonCard(player,targetType));
                    }
                }
                else{
                    for(PokemonCard p : sourcePlayer.getBench()){
                        targetedPokemon.add(p);
                    }
                }
                break;
            case OPPONENT_ACTIVE:
                targetedPokemon.add(otherPlayer.getActivePokemon());
                break;
            case YOUR_ACTIVE:
                targetedPokemon.add(sourcePlayer.getActivePokemon());
                break;
            case YOUR_POKEMON:
            case OPPONENT_POKEMON:
                targetedPokemon.add(GameEngine.choosePokemonCard(player,targetType)) ;
                break;
            case LAST:
                targetedPokemon.add(Ability.lastTargetedPokemon);
                break;
        }

        for(PokemonCard p : targetedPokemon){
            p.applyStatus(givenStatus);
            Ability.lastTargetedPokemon = p;
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
            hasChoice = true;
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
