import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class DeenergizeAbility extends Ability{
    int amountToRemove;

    public DeenergizeAbility(){
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
        }

        if(targetPokemon != null){
            ArrayList<EnergyCard> energy = targetPokemon.getEnergy();
            for(int i = 0; i < amountToRemove && !energy.isEmpty(); i++){
                EnergyCard e = energy.get(0);
                sourcePlayer.addToDiscard(e);
                energy.remove(e);
            }
        }

        return true;
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
    
    public String getSimpleDescription() {
        return "Remove " + amountToRemove + " energy from " + targetType.toString();
    }

    public Ability shallowCopy(){
        DeenergizeAbility returnCard = new DeenergizeAbility();

        returnCard.name = this.name;
        returnCard.targetType = this.targetType;
        returnCard.subsequentAbility  = this.subsequentAbility;

        returnCard.amountToRemove = this.amountToRemove;

        return returnCard;
    }
}
