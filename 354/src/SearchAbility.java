import org.omg.CORBA.BAD_CONTEXT;

import java.util.ArrayList;

public class SearchAbility extends Ability{
    Target wallyTargetPokemon = null;
    String source = "";
    ComplexAmount amountToSearch = null;

    enum FilterType{
        TOP,
        ENERGY,
        ITEM,
        POKEMON,
        BASIC_POKEMON,
        EVOLVES_FROM_LAST,
        NONE
    }
    FilterType filter = null;
    ComplexAmount topAmount = null;

    SearchAbility(){

    }

    SearchAbility(String[] description) throws UnimplementedException{
        int index = 0;

        //first token must be dam
        if(description[index].equals("search")){
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

        if(hasChoice){
            /*This exists only to make the Wally card work. Basically the description for Wally does not contain all of the information needed to parse
                * e.g.; it's missing which player's deck gets targeted. Target's can now be qualified by cat:basic? That's new. That's unspecified. That raises a whole lot of questions about how else it should be qualified.
                * It invents a new syntax that's inconsistent with the rest of the ability. You pretty much have to look up the card description online to know how it's supposed to work.
                * For it to make sense, it should be target:you (and not target:your!) and the "choice:your-pokemon:cat:basic" (which should be choice:your) should be part of the filter so that the filter is "filter:evolves-from:target:choice:your-pokemon:cat:basic"
                * With a full consistent description being: "Wally:search:target:you:source:deck:filter:evolves-from:target:choice:your:cat:basic:1,shuffle:target:you"
                * This would still be tricky to implement, but would be completely consistent with the syntax prescribed in the project description.
                * So the parsing is gonna be almost hard coded in because fuck the design of the abilities.txt file. It's like certain abilities were made by taking existing cards and making up a new syntax for each one.
                * Seriously, making something hard to parse is one thing. Making something inconsistent with itself is just ridiculous.
                * I really want to make an extremely flexible parser, but when bullshit like this comes up I just dgaf.*/
            wallyTargetPokemon = targetType;
            targetType = Target.YOU;
            index += 2;

        }

        //target can only be deck or discard
        switch(targetType){
            case YOUR_POKEMON:
                targetType = Target.YOU; //fix inconsistency with project description and abilities file
                break;
            case OPPONENT_POKEMON:
                targetType = Target.OPPONENT; //fix inconsistency with project description and abilities file
                break;
            case YOU:
            case OPPONENT:
                break;
            default:
                throw new UnimplementedException();
        }

        //next token must be source
        if(description[index].equals("source")){
            index++;
        }
        else{
            throw new UnimplementedException();
        }

        //get source
        source = description[index++];

        //source can only be deck or discard
        switch(source){
            case "deck":
            case "discard":
                break;
            default:
                throw new UnimplementedException();
        }

        //next token must be filter
        if(description[index].equals("filter")){
            index++;
        }
        else{
            filter = FilterType.NONE;
        }

        if(filter != FilterType.NONE){
            //get filter type
            String tmp = description[index++];
            switch(tmp){
                case "top":
                    filter = FilterType.TOP;
                    topAmount = new ComplexAmount(description[index]);
                    break;
                case "energy":
                    filter = FilterType.ENERGY;
                    break;
                case "cat":
                    if(description[index].equals("item")){
                        filter = FilterType.ITEM;
                    }
                    else{
                        throw new UnimplementedException();
                    }
                    break;
                case "pokemon":
                    if(description[index].equals("cat") && description[index+1].equals("basic")){
                        filter = FilterType.BASIC_POKEMON;
                    }
                    else{
                        filter = FilterType.POKEMON;
                    }
                    break;
                case "evolves-from":
                    filter = FilterType.EVOLVES_FROM_LAST;
                    break;
                default:
                    throw new UnimplementedException();
            }
        }

        //get amount to select
        try{
            this.amountToSearch = new ComplexAmount(description[description.length - 1]);
        }catch(Exception e){
            throw new UnimplementedException();
        }
    }

    public boolean realUse(Player player){
        CardManager sourcePlayer = null;
        CardManager otherPlayer = null;
        switch(player){
            case PLAYER:
                if(targetType == Target.YOU){
                    sourcePlayer = playerCardManager;
                    otherPlayer = AICardManager;
                }
                else{
                    sourcePlayer = AICardManager;
                    otherPlayer = playerCardManager;
                }
                break;
            case AI:
                if(targetType == Target.YOU){
                    sourcePlayer = AICardManager;
                    otherPlayer = playerCardManager;
                }
                else{
                    sourcePlayer = playerCardManager;
                    otherPlayer  = AICardManager;
                }
                break;
        }
        ArrayList<Card> targetDeck = null;
        if(source.equals("deck")){
            targetDeck = sourcePlayer.getDeck();
        }
        else{
            targetDeck = sourcePlayer.getDiscard();
        }

        //if it's Wally-like, get targeted pokemon and ensure that it's basic
        PokemonCard basePokemon = null;
        if(wallyTargetPokemon != null){
            boolean isBasic = false;
            while(! isBasic){
                switch(wallyTargetPokemon){
                    case OPPONENT_ACTIVE:
                        if(otherPlayer.getActivePokemon().getCat() == PokemonCard.Category.BASIC){
                            basePokemon = otherPlayer.getActivePokemon();
                        }
                        else{
                            return false;
                        }
                        break;
                    case YOUR_ACTIVE:
                        if(sourcePlayer.getActivePokemon().getCat() == PokemonCard.Category.BASIC){
                            basePokemon = sourcePlayer.getActivePokemon();
                        }
                        else{
                            return false;
                        }
                        break;
                    case OPPONENT_BENCH:
                        if(otherPlayer.getBench().size() > 0 && otherPlayer.benchHasBasicPokemon()){
                            basePokemon = GameEngine.choosePokemonCard(player,wallyTargetPokemon);
                        }
                        else{
                            return false;
                        }
                        break;
                    case YOUR_BENCH:
                        if(sourcePlayer.getBench().size() > 0 && sourcePlayer.benchHasBasicPokemon()){
                            basePokemon = GameEngine.choosePokemonCard(player,wallyTargetPokemon);
                        }
                        else{
                            return false;
                        }
                        break;
                    case YOUR_POKEMON:
                        if(sourcePlayer.getActivePokemon().getCat() == PokemonCard.Category.BASIC || sourcePlayer.benchHasBasicPokemon()){
                            basePokemon = GameEngine.choosePokemonCard(player,wallyTargetPokemon);
                        }
                        else{
                            return false;
                        }
                        break;
                    case OPPONENT_POKEMON:
                        if(otherPlayer.getActivePokemon().getCat() == PokemonCard.Category.BASIC || otherPlayer.benchHasBasicPokemon()){
                            basePokemon = GameEngine.choosePokemonCard(player,wallyTargetPokemon);
                        }
                        else{
                            return false;
                        }
                        break;
                    case LAST:
                        if(Ability.lastTargetedPokemon.getCat() == PokemonCard.Category.BASIC){
                            basePokemon = Ability.lastTargetedPokemon;
                        }
                        else{
                            return false;
                        }
                        break;
                }
                if(basePokemon.getCat() == PokemonCard.Category.BASIC){
                    isBasic = true;
                }
                else{
                    GameEngine.log("You must select a basic pokemon.");
                }
            }
        }

        //filter cards
        ArrayList<Card> filteredCards = new ArrayList<>();
        switch(filter){
            case BASIC_POKEMON:
                for(Card c : targetDeck){
                    if(c instanceof PokemonCard){
                        if(((PokemonCard)c).getCat() == PokemonCard.Category.BASIC){
                            filteredCards.add(c);
                        }
                    }
                }
                break;
            case ENERGY:
                for(Card c : targetDeck){
                    if(c instanceof EnergyCard){
                        filteredCards.add(c);
                    }
                }
                break;
            case ITEM:
                for(Card c : targetDeck){
                    if(c instanceof TrainerCard){
                        filteredCards.add(c);
                    }
                }
                break;
            case POKEMON:
                for(Card c : targetDeck){
                    if(c instanceof PokemonCard){
                        filteredCards.add(c);
                    }
                }
                break;
            case TOP:
                int a = topAmount.evaluate(player);
                for(int i = 0; i < a; i++){
                    filteredCards.add(targetDeck.get(i));
                }
                break;
            case EVOLVES_FROM_LAST:
                for(Card c : targetDeck){
                    if(c instanceof PokemonCard){
                        if(((PokemonCard)c).getEvolvesFrom().equals(basePokemon.getName())){
                            filteredCards.add(c);
                        }
                    }
                }
                break;
            case NONE:
                for(Card c : targetDeck){
                    filteredCards.add(c);
                }
                break;
            default:
                return false;
        }

        //view and select cards
        if(filteredCards.size() > 0){
            int numberCardsToSelect = amountToSearch.evaluate(player);
            if(numberCardsToSelect == 0){
                //show card picker but make no selections
                CardPickerWindow  cardPickerWindow = new CardPickerWindow("You can view these cards, but not select any", filteredCards, GameEngine.displayGameInFullScreen);
                GameEngine.w.close();
                cardPickerWindow.display();
                while(true){
                    GameEngine.waitForInput();
                    Message msg = GameEngine.queue.remove();
                    if(msg.getType() == Message.ButtonType.CARDSELECTOR){
                        cardPickerWindow.close();
                        GameEngine.w.display();
                        break;
                    }
                }
            }
            else{
                CardPickerWindow  cardPickerWindow = new CardPickerWindow("You can select up to " + numberCardsToSelect + " cards to add to your hand.", filteredCards, GameEngine.displayGameInFullScreen);
                GameEngine.w.close();
                cardPickerWindow.display();
                ArrayList<Card> cardsToAddToHand = new ArrayList<>();
                for(int i = 0; i < numberCardsToSelect; i++){
                    GameEngine.waitForInput();
                    Message msg = GameEngine.queue.remove();
                    if(msg.getType() != Message.ButtonType.CARDSELECTOR){
                        i--;
                    }
                    else{
                        if(msg.getIndex() == -1){
                            break;
                        }
                        else{
                            cardsToAddToHand.add(filteredCards.get(msg.getIndex()));
                        }
                    }
                }
                cardPickerWindow.close();
                GameEngine.w.display();

                //add cards to hand
                for(Card c : cardsToAddToHand){
                    sourcePlayer.getHand().add(c);
                }

                //remove cards from source deck
                for(Card c : cardsToAddToHand){
                    targetDeck.remove(c);
                }
            }
        }
        else{
            GameEngine.log("No cards were found matching that filter.");
        }
        return true;
    }

    public String getSimpleDescription(){
        String returnString = "Add up to " + amountToSearch.getDescription() + " cards to your hand from " + ((targetType==Target.YOU)?"your ":"their ") + source;
        switch(filter){
            case TOP:
                returnString += " from the top " + topAmount.getDescription();
                break;
            case POKEMON:
                returnString += " that are pokemon";
                break;
            case BASIC_POKEMON:
                returnString += " that are basic pokemon";
                break;
            case ENERGY:
                returnString += " that are energy";
                break;
            case ITEM:
                returnString += " that are items";
                break;
            case EVOLVES_FROM_LAST:
                returnString += " that evolve from the previously selected pokemon";
                break;
            default:
                break;
        }
        return returnString;
    }

    public Ability shallowCopy(){
        SearchAbility returnCard = new SearchAbility();

        returnCard.name = this.name;
        returnCard.targetType = this.targetType;
        returnCard.subsequentAbility  = this.subsequentAbility;

        returnCard.wallyTargetPokemon = this.wallyTargetPokemon;
        returnCard.amountToSearch = this.amountToSearch;
        returnCard.topAmount = this.topAmount;
        returnCard.filter = this.filter;
        returnCard.source = this.source;

        return returnCard;
    }
}
