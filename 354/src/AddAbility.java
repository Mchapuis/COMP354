import java.util.Arrays;

public class AddAbility extends Ability{
    Ability abilityToAdd = null;
    String triggerTurn = null;

    AddAbility(){

    }
    AddAbility(String[] description)  throws UnimplementedException{
        int index = 0;

        //first token must be add
        if(description[index].equals("add")){
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

        //check for trigger
        if(description[index++].equals("trigger")){
            triggerTurn = description[index++];

            //verify allowed trigger turns
            switch(triggerTurn){
                case "opponent":
                case "your":
                    break;
                case "you":
                    triggerTurn = "your";
                    break;
                    default: throw new UnimplementedException();
            }
            //next token must be turn-end
            if(description[index].equals("turn-end")){
                index++;
            }
            else{
                throw new UnimplementedException();
            }
        }
        else{
            index--;
        }



        //get ability
        String[] newDescription = Arrays.copyOfRange(description, index, description.length);
        //make the ability
        removeStartEndParentheses(newDescription);
        String d = "";
        boolean first = true;
        for(String s : newDescription){
            if(! first){
                d += ":" + s;
            }
            else{
                d += s;
                first = false;
            }
        }
        //tokenize by comma
        String [] sequentialAbilities = d.split(",");

        //remerge parentheses
        sequentialAbilities = remergeParentheses(sequentialAbilities);

        try{
            //make first ability in sequence
            abilityToAdd = makeAbility(sequentialAbilities[0].split(":"));

            //make subsequent abilities and attach them sequentially
            Ability latestAbility = abilityToAdd;
            for(int j = 1; j < sequentialAbilities.length; j++){
                Ability newAbility = makeAbility(sequentialAbilities[j].split(":"));
                latestAbility.setSubsequentAbility(newAbility);
                latestAbility = newAbility;
            }
        }catch(Exception e){
            throw new UnimplementedException();
        }
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
            if(triggerTurn != null){
                if(triggerTurn.equals("opponent")){
                    targetPokemon.abilitiesTriggeredOpponentTurnEnd.add(abilityToAdd);
                }
                else if (triggerTurn.equals("your")){
                    targetPokemon.abilitiesTriggeredOwnTurnEnd.add(abilityToAdd);
                }
                else return false;
            }
            else{
                targetPokemon.addAbility(abilityToAdd);
            }
            Ability.lastTargetedPokemon = targetPokemon;
        }

        return true;
    }

    public String getSimpleDescription(){
        return "Add ability [" + abilityToAdd.getRecursiveDescription() + "] to " + targetType + (triggerTurn==null?"":" that triggers at end of " + triggerTurn + " turn");
    }

    public Ability shallowCopy(){
        AddAbility returnCard = new AddAbility();

        returnCard.name = this.name;
        returnCard.targetType = this.targetType;
        returnCard.subsequentAbility  = this.subsequentAbility;

        returnCard.triggerTurn = this.triggerTurn;
        returnCard.abilityToAdd = this.abilityToAdd;



        return returnCard;
    }
}
