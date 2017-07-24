import java.util.ArrayList;

public class DeckAbility extends Ability{
    ComplexAmount amountToDeck = null;
    String destination = null;
    String topOrBottom = "bottom"; //default to bottom
    String choice = "random"; //default to random; the only abilities provided using this move all cards anyways

    DeckAbility(){}

    DeckAbility(String[] description) throws UnimplementedException{
        int index = 0;

        //first token must be dam
        if(description[index].equals("deck")){
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
        targetType = parseTarget(description[index++]);

        //target can only be "opponent or you"
        switch(targetType){
            case OPPONENT_POKEMON://the provided abilities uses "opponent" instead of "them" which is inconsistent with the project description; this is to handle either
                targetType = Target.OPPONENT;
                break;
            case YOUR_POKEMON:
                targetType = Target.YOU; //same thing
                break;
            case OPPONENT:
            case YOU:
                break;
            default:
                throw new UnimplementedException();
        }

        //fourth token must be destination
        if(description[index].equals("destination")){
            index++;
        }
        else{
            throw new UnimplementedException();
        }

        //get destination
        destination = description[index++];

        //destination can only be deck or discard
        switch(destination){
            case "deck":
            case "discard":
                break;
            default:
                throw new UnimplementedException();
        }

        //get topOrBottom
        topOrBottom = description[index++];

        //can only be top or bottom
        switch(topOrBottom){
            case "top":
            case "bottom":
                break;
            default: //or it can be unspecified and we just read an unrelated field
                topOrBottom = "bottom"; //reset to default
                index--; //back up a bit
        }

        //get choice if specified
        if(description[index++].equals("choice")){
            //get choice
            choice = description[index++];

            //choice can only be you, them, or random
            switch(choice){
                case "you":
                case "them":
                case "random":
                    break;
                default:
                    throw new UnimplementedException();
            }
        }
        else{
            index--;
        }

        //get amount to deck
        this.amountToDeck = new ComplexAmount(description[index]);

        System.out.println();
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

        ArrayList<Card> deckToMoveTo = null;
        if(destination.equals("deck")){
            deckToMoveTo = sourcePlayer.getDeck();
        }
        else if(destination.equals("discard")){
            deckToMoveTo = sourcePlayer.getDiscard();
        }

        if(sourcePlayer != null && deckToMoveTo != null && sourcePlayer.getHand().size() > 0){
            int numberOfTimes = amountToDeck.evaluate(player);
            for(int i = 0; i < numberOfTimes && sourcePlayer.getHand().size() > 0; i++){
                Card cardToMove = null;
                //choose card
                if(choice.equals("you")){
                    cardToMove = GameEngine.chooseCardFromAHand(player, sourcePlayer);
                }
                else if(choice.equals("them")){
                    if(player == Player.PLAYER){
                        cardToMove = GameEngine.chooseCardFromAHand(Player.AI, sourcePlayer);
                    }
                    else{
                        cardToMove = GameEngine.chooseCardFromAHand(Player.PLAYER, sourcePlayer);
                    }
                }
                else if(choice.equals("random")){
                    cardToMove = sourcePlayer.getHand().get(0);
                }

                if(cardToMove != null){
                    if(topOrBottom.equals("top")){
                        deckToMoveTo.add(0,cardToMove);
                    }
                    else if(topOrBottom.equals("bottom")){
                        deckToMoveTo.add(cardToMove);
                    }

                    //remove card from hand
                    sourcePlayer.getHand().remove(cardToMove);
                }
                else{
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    public String getSimpleDescription(){
        return targetType.toString() + " moves " + amountToDeck.getDescription() + " card (s) from hand to " + topOrBottom + " of their " + destination + " chosen by " + choice;
    }
    public Ability shallowCopy(){
        DeckAbility returnCard = new DeckAbility();

        returnCard.name = this.name;
        returnCard.targetType = this.targetType;
        returnCard.subsequentAbility  = this.subsequentAbility;

        returnCard.amountToDeck = this.amountToDeck;
        returnCard.destination = this.destination;
        returnCard.topOrBottom = this.topOrBottom;

        return returnCard;
    }

}
