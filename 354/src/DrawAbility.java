
public class DrawAbility extends Ability{
    int amountToDraw;

    public void realUse(Player player){
        CardManager sourcePlayer = null;
        switch(player){
            case PLAYER:
                sourcePlayer = playerCardManager;
                break;
            case AI:
                sourcePlayer = AICardManager;
                break;
        }

        //draw cards
        for(int i = 0; i < amountToDraw; i++){
            sourcePlayer.addCardToHandFromDeck(0);
        }
    }

    DrawAbility(String[] description) throws UnimplementedException{
        int index = 0;

        //verify format: ability name
        if(description[index].equals("draw")){
            index++;
        }
        else{
            throw new UnimplementedException();
        }

        //get amount to draw
        try{
            amountToDraw = Integer.valueOf(description[index]);
        }catch(Exception e){
            throw new UnimplementedException();
        }
    }
}
