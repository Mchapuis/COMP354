
public class ShuffleAbility extends Ability{
    ShuffleAbility(){

    }
    ShuffleAbility(String[] description) throws UnimplementedException{
        int index = 0;

        //verify format: ability name
        if(description[index].equals("shuffle")){
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

        //get target if specified
        if(description[index].equals("opponent")){
            index++;
            targetType = Target.OPPONENT;
        }
        else{
            targetType = Target.YOU;
        }
    }

    public boolean realUse(Player player){
        CardManager sourcePlayer = null;
        switch(player){
            case PLAYER:
                if(targetType == Target.YOU){
                    sourcePlayer = playerCardManager;
                }
                else{
                    sourcePlayer = AICardManager;
                }
                break;
            case AI:
                if(targetType == Target.YOU){
                    sourcePlayer = AICardManager;
                }
                else{
                    sourcePlayer = playerCardManager;
                }
                break;
        }

        if(sourcePlayer != null){
            sourcePlayer.shuffleDeck();
            return true;
        }
        return false;
    }

    public String getSimpleDescription(){
        if(targetType == Target.OPPONENT){
            return "Shuffle opponent's deck";
        }
        else{
            return "Shuffle your deck";
        }
    }

    public Ability shallowCopy(){
        ShuffleAbility returnCard = new ShuffleAbility();

        returnCard.name = this.name;
        returnCard.targetType = this.targetType;
        returnCard.subsequentAbility  = this.subsequentAbility;
        returnCard.hasChoice = this.hasChoice;

        return returnCard;
    }

}
