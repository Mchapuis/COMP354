import java.util.ArrayList;

public class ReEnergizeAbility extends Ability{
    Target destinationTargetType = null;
    boolean hasChoiceDestination = false;
    ComplexAmount toTake = null;
    ComplexAmount toGive = null;

    ReEnergizeAbility(){

    }
    ReEnergizeAbility(String [] description) throws UnimplementedException{
        int index = 0;

        //first token must be dam
        if(description[index].equals("reenergize")){
            index++;
        }
        else{
            throw new UnimplementedException();
        }

        //second token must be target
        if(description[index].equals("target")){
            index++;
        }
        else{
            throw new UnimplementedException();
        }

        //get target
        if(description[index].equals("choice")){
            index++;
            hasChoice = true;
        }
        this.targetType = parseTarget(description[index++]);

        //get amount
        this.toTake = new ComplexAmount(description[index++]);

        //next token must be target
        if(description[index].equals("target")){
            index++;
        }
        else{
            throw new UnimplementedException();
        }

        //get target
        if(description[index].equals("choice")){
            index++;
            hasChoiceDestination = true;
        }
        this.destinationTargetType = parseTarget(description[index++]);

        //get amount
        this.toGive = new ComplexAmount(description[index]);

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
            case LAST:
                targetPokemon = Ability.lastTargetedPokemon;
                break;
        }

        PokemonCard destinationPokemon = null;
        switch(destinationTargetType){
            case OPPONENT_ACTIVE:
                destinationPokemon = otherPlayer.getActivePokemon();
                break;
            case YOUR_ACTIVE:
                destinationPokemon = sourcePlayer.getActivePokemon();
                break;
            case OPPONENT_BENCH:
                if(otherPlayer.getBench().size() > 0){
                    destinationPokemon = GameEngine.choosePokemonCard(player,targetType);
                }
                break;
            case YOUR_BENCH:
                if(sourcePlayer.getBench().size() > 0){
                    destinationPokemon = GameEngine.choosePokemonCard(player,targetType);
                }
                break;
            case YOUR_POKEMON:
                destinationPokemon = GameEngine.choosePokemonCard(player,targetType);
                break;
            case OPPONENT_POKEMON:
                destinationPokemon = GameEngine.choosePokemonCard(player,targetType);
                break;
            case LAST:
                destinationPokemon = Ability.lastTargetedPokemon;
                break;
        }

        if(targetPokemon != null && destinationPokemon != null){
            int amountToRemove = toTake.evaluate(player);
            ArrayList<EnergyCard> transfer = new ArrayList<>();
            for(int i = 0; i < amountToRemove; i++){
                if(targetPokemon.getEnergy().size() > 0){
                    transfer.add(targetPokemon.getEnergy().remove(0));
                }
            }
            int amountToAdd = toGive.evaluate(player);
            for(int i = 0; i < amountToAdd; i++){
                if(transfer.size() > 0){
                    destinationPokemon.getEnergy().add(transfer.remove(i));
                }
            }

            //
            if(transfer.size() > 0){
                if(targetPokemon == sourcePlayer.getActivePokemon() || sourcePlayer.getBench().contains(targetPokemon)){
                    for(Card c : transfer){
                        sourcePlayer.addToDiscard(c);
                    }
                }
                else{
                    for(Card c : transfer){
                        otherPlayer.addToDiscard(c);
                    }
                }
            }

            Ability.lastTargetedPokemon = targetPokemon;
        }

        return true;
    }
    public String getSimpleDescription(){
        return "Take " + toTake.getDescription() + " energy from " + targetType + " and give " + toGive.getDescription() + " energy to " + destinationTargetType;
    }
    public Ability shallowCopy(){
        ReEnergizeAbility returnCard = new ReEnergizeAbility();

        returnCard.name = this.name;
        returnCard.targetType = this.targetType;
        returnCard.subsequentAbility  = this.subsequentAbility;
        returnCard.hasChoice = this.hasChoice;

        returnCard.destinationTargetType = this.destinationTargetType;
        returnCard.toTake = this.toTake;
        returnCard.toGive = this.toGive;

        return returnCard;
    }

}
