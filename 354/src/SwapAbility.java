
public class SwapAbility extends Ability{
    Target destinationTarget = null;

    SwapAbility(){

    }
    SwapAbility(String[] description) throws UnimplementedException{
        int index = 0;

        //verify format: ability name
        if(description[index].equals("swap")){
            index++;
        }
        else{
            throw new UnimplementedException();
        }

        //verify format: source
        if(description[index].equals("source")){
            index++;
        }
        else{
            throw new UnimplementedException();
        }

        //get source target
        if(description[index].equals("choice")){
            index++;
        }
        this.targetType = parseTarget(description[index++]);

        //verify format: destination
        if(description[index].equals("destination")){
            index++;
        }
        else{
            throw new UnimplementedException();
        }

        //get destination target
        if(description[index].equals("choice")){
            index++;
        }
        this.destinationTarget = parseTarget(description[index++]);
    }

    public boolean realUse(Player player){
        CardManager sourcePlayer = null;
        CardManager otherPlayer = null;
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

        PokemonCard sourceTargetPokemon = null;
        switch(targetType){
            case OPPONENT_ACTIVE:
                sourceTargetPokemon = otherPlayer.getActivePokemon();
                break;
            case YOUR_ACTIVE:
                sourceTargetPokemon = sourcePlayer.getActivePokemon();
                break;
            case OPPONENT_BENCH:
                if(otherPlayer.getBench().size() > 0){
                    sourceTargetPokemon = GameEngine.choosePokemonCard(player,targetType);
                }
                break;
            case YOUR_BENCH:
                if(sourcePlayer.getBench().size() > 0){
                    sourceTargetPokemon = GameEngine.choosePokemonCard(player,targetType);
                }
                break;
            case YOUR_POKEMON:
                sourceTargetPokemon = GameEngine.choosePokemonCard(player,targetType);
                break;
            case OPPONENT_POKEMON:
                sourceTargetPokemon = GameEngine.choosePokemonCard(player,targetType);
                break;
            case LAST:
                sourceTargetPokemon = Ability.lastTargetedPokemon;
                break;
        }
        PokemonCard destinationTargetPokemon = null;
        switch(destinationTarget){
            case OPPONENT_ACTIVE:
                destinationTargetPokemon = otherPlayer.getActivePokemon();
                break;
            case YOUR_ACTIVE:
                destinationTargetPokemon = sourcePlayer.getActivePokemon();
                break;
            case OPPONENT_BENCH:
                if(otherPlayer.getBench().size() > 0){
                    destinationTargetPokemon = GameEngine.choosePokemonCard(player,destinationTarget);
                }
                break;
            case YOUR_BENCH:
                if(sourcePlayer.getBench().size() > 0){
                    destinationTargetPokemon = GameEngine.choosePokemonCard(player,destinationTarget);
                }
                break;
            case YOUR_POKEMON:
                destinationTargetPokemon = GameEngine.choosePokemonCard(player,destinationTarget);
                break;
            case OPPONENT_POKEMON:
                destinationTargetPokemon = GameEngine.choosePokemonCard(player,destinationTarget);
                break;
            case LAST:
                destinationTargetPokemon = Ability.lastTargetedPokemon;
                break;
        }

        if(sourcePlayer != null && otherPlayer != null && sourceTargetPokemon != null && destinationTargetPokemon != null){
            Target source = null;
            Target destination = null;

            //remove sourcepokemon and record source
            if(sourceTargetPokemon == sourcePlayer.getActivePokemon()){
                sourceTargetPokemon.applyStatus(Status.NORMAL);
                sourcePlayer.removeActivePokemonWithoutDiscard();
                source = Target.YOUR_ACTIVE;
            }
            else if(sourceTargetPokemon == otherPlayer.getActivePokemon()){
                sourceTargetPokemon.applyStatus(Status.NORMAL);
                otherPlayer.removeActivePokemonWithoutDiscard();
                source = Target.OPPONENT_ACTIVE;
            }
            else if(sourcePlayer.getBench().contains(sourceTargetPokemon)){
                sourcePlayer.getBench().remove(sourceTargetPokemon);
                source = Target.YOUR_BENCH;
            }
            else if(otherPlayer.getBench().contains(sourceTargetPokemon)){
                otherPlayer.getBench().remove(sourceTargetPokemon);
                source = Target.OPPONENT_BENCH;
            }

            //remove destination pokemon and record destination
            if(destinationTargetPokemon == sourcePlayer.getActivePokemon()){
                destinationTargetPokemon.applyStatus(Status.NORMAL);
                sourcePlayer.removeActivePokemonWithoutDiscard();
                destination = Target.YOUR_ACTIVE;
            }
            else if(destinationTargetPokemon == otherPlayer.getActivePokemon()){
                destinationTargetPokemon.applyStatus(Status.NORMAL);
                otherPlayer.removeActivePokemonWithoutDiscard();
                destination = Target.OPPONENT_ACTIVE;
            }
            else if(sourcePlayer.getBench().contains(destinationTargetPokemon)){
                sourcePlayer.getBench().remove(destinationTargetPokemon);
                destination = Target.YOUR_BENCH;
            }
            else if(otherPlayer.getBench().contains(destinationTargetPokemon)){
                otherPlayer.getBench().remove(destinationTargetPokemon);
                destination = Target.OPPONENT_BENCH;
            }

            //add source pokemon to destination
            switch(destination){
                case OPPONENT_BENCH:
                    otherPlayer.getBench().add(sourceTargetPokemon);
                    break;
                case YOUR_BENCH:
                    sourcePlayer.getBench().add(sourceTargetPokemon);
                    break;
                case OPPONENT_ACTIVE:
                    otherPlayer.setActivePokemon(sourceTargetPokemon);
                    break;
                case YOUR_ACTIVE:
                    sourcePlayer.setActivePokemon(sourceTargetPokemon);
                    break;
            }

            //add destination pokemon to source
            switch(source){
                case OPPONENT_BENCH:
                    otherPlayer.getBench().add(destinationTargetPokemon);
                    break;
                case YOUR_BENCH:
                    sourcePlayer.getBench().add(destinationTargetPokemon);
                    break;
                case OPPONENT_ACTIVE:
                    otherPlayer.setActivePokemon(destinationTargetPokemon);
                    break;
                case YOUR_ACTIVE:
                    sourcePlayer.setActivePokemon(destinationTargetPokemon);
                    break;
            }

            return true;
        }
        return false;
    }

    public String getSimpleDescription(){
        return "Swap " + targetType + " with " + destinationTarget;
    }

    public Ability shallowCopy(){
        SwapAbility returnCard = new SwapAbility();

        returnCard.name = this.name;
        returnCard.targetType = this.targetType;
        returnCard.subsequentAbility  = this.subsequentAbility;

        returnCard.destinationTarget = this.destinationTarget;

        return returnCard;
    }

}
