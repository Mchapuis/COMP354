
public class ApplyStatAbility extends Ability{


    private Status givenStatus;

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

        switch(targetType){
            case OPPONENT_ACTIVE:
                otherPlayer.getActivePokemon().applyStatus(givenStatus);
                break;
            case YOUR_ACTIVE:
                sourcePlayer.getActivePokemon().applyStatus(givenStatus);
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

}
