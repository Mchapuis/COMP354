
public class RedamageAbility extends Ability{
    ComplexAmount timesToMove = null;
    Target destinationTargetType = null;

    RedamageAbility(){}
    RedamageAbility(String [] description) throws UnimplementedException{
        int index = 0;

        //first token must be dam
        if(description[index].equals("redamage")){
            index++;
        }
        else{
            throw new UnimplementedException();
        }

        //second token must be source
        if(description[index].equals("source")){
            index++;
        }
        else{
            throw new UnimplementedException();
        }

        //get target
        if(description[index].equals("choice")){
            index++;
        }
        this.targetType = parseTarget(description[index++]);

        //third token must be destination
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
        this.destinationTargetType = parseTarget(description[index++]);

        //get amount of times
        timesToMove = new ComplexAmount(description[index]);

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

        if(targetPokemon != null){
            int times = timesToMove.evaluate(player);
            for(int i = 0; i < times; i++){
                PokemonCard destinationCard = null;
                switch(destinationTargetType){
                    case OPPONENT_ACTIVE:
                        destinationCard = otherPlayer.getActivePokemon();
                        break;
                    case YOUR_ACTIVE:
                        destinationCard = sourcePlayer.getActivePokemon();
                        break;
                    case OPPONENT_BENCH:
                        if(otherPlayer.getBench().size() > 0){
                            destinationCard = GameEngine.choosePokemonCard(player,targetType);
                        }
                        break;
                    case YOUR_BENCH:
                        if(sourcePlayer.getBench().size() > 0){
                            destinationCard = GameEngine.choosePokemonCard(player,targetType);
                        }
                        break;
                    case YOUR_POKEMON:
                        destinationCard = GameEngine.choosePokemonCard(player,targetType);
                        break;
                    case OPPONENT_POKEMON:
                        destinationCard = GameEngine.choosePokemonCard(player,targetType);
                        break;
                    case LAST:
                        destinationCard = Ability.lastTargetedPokemon;
                        break;
                }

                int damageToMove = Integer.max(targetPokemon.getMaxHP()-targetPokemon.getCurrentHP(),10);
                targetPokemon.removeHP(-damageToMove);
                destinationCard.removeHP(damageToMove);
            }

            Ability.lastTargetedPokemon = targetPokemon;
        }
        return true;
    }

    public String getSimpleDescription(){
        return "Move 10 damage from " + targetType + " to " + destinationTargetType + " " + timesToMove.getDescription() + " times";
    }
    public Ability shallowCopy(){
        RedamageAbility returnCard = new RedamageAbility();

        returnCard.name = this.name;
        returnCard.targetType = this.targetType;
        returnCard.subsequentAbility  = this.subsequentAbility;
        returnCard.hasChoice = this.hasChoice;

        returnCard.timesToMove = this.timesToMove;
        returnCard.destinationTargetType = this.destinationTargetType;

        return returnCard;
    }

}
