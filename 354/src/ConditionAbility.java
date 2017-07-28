import com.sun.org.glassfish.gmbal.GmbalException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConditionAbility extends Ability{
    private enum ConditionType {
        FLIP,
        CHOICE,
        HEALED,
        ABILITY,
        BOOLEAN
    }

    ConditionType condType;
    Ability conditionalAbility = null;
    Ability elseAbility = null;

    //used only for ConditionType.ABILITY
    Ability testAbility = null;

    //used only for ConditionType.BOOLEAN
    BooleanExpression booleanExpression = null;
    
    public ConditionAbility(){
    	this.energyRequired = new HashMap<EnergyCard.Type, Integer>();
    }

    public boolean realUse(Player player){
        boolean conditionPassed = false;

        switch(condType){
            case FLIP:
                conditionPassed = RandomNumberGenerator.flipACoin();
                break;
            case CHOICE:
                ChoiceWindow c = new ChoiceWindow("Do you want to apply the ability [" + conditionalAbility.getRecursiveDescription() + "]" + (elseAbility==null?"":" otherwise ability ["+elseAbility.getRecursiveDescription()+"] will be applied."));
                c.display();
                GameEngine.w.close();
                while(true){
                    GameEngine.waitForInput();
                    Message msg = GameEngine.queue.remove();
                    if(msg.getType() == Message.ButtonType.CHOICE){
                        if(msg.getIndex() == 1){
                            conditionPassed = true;
                            break;
                        }
                        else{
                            conditionPassed = false;
                            break;
                        }
                    }
                }
                GameEngine.w.display();
                c.close();
                break;
            case HEALED:
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
                    default:
                        targetPokemon = otherPlayer.getActivePokemon();
                }

                conditionPassed = targetPokemon.getHasBeenHealed();
                break;
            case ABILITY:
                conditionPassed = testAbility.use(player);
                break;
            case BOOLEAN:
                conditionPassed = booleanExpression.evaluate(player);
                break;
        }


        if(conditionPassed){
            conditionalAbility.use(player);
        }else if(!conditionPassed && elseAbility != null){
            elseAbility.use(player);
        }
        else{
            return false;
        }

        return true;
    }

    ConditionAbility(String[] description) throws UnimplementedException{
        int index = 0;

        //verify format: ability name
        if(description[index].equals("cond")){
            index++;
        }
        else{
            throw new UnimplementedException();
        }

        //set else ability if applicable
        int indexOfElse = -1;
        for(int i = 0; i < description.length; i++){
            if(description[i].equals("else")){
                indexOfElse = i+1;
                try{
                    String[] newDescription = Arrays.copyOfRange(description, indexOfElse, description.length);
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
                        elseAbility = makeAbility(sequentialAbilities[0].split(":"));

                        //make subsequent abilites and attach them sequentialy
                        Ability latestAbility = elseAbility;
                        for(int j = 1; j < sequentialAbilities.length; j++){
                            Ability newAbility = makeAbility(sequentialAbilities[j].split(":"));
                            latestAbility.setSubsequentAbility(newAbility);
                            latestAbility = newAbility;
                        }
                    }catch(Exception e){
                        throw new UnimplementedException();
                    }
                }catch(Exception e){
                    elseAbility = new UnimplementedAbility();
                }
            }
        }

        //set condition type
        switch(description[index]){
            case "healed":
                condType = ConditionType.HEALED;
                break;
            case "flip":
                condType = ConditionType.FLIP;
                break;
            case "ability":
                condType = ConditionType.ABILITY;
                break;
            case "choice":
                condType = ConditionType.CHOICE;
                break;
            default:
                condType = ConditionType.BOOLEAN;
                booleanExpression = new BooleanExpression(description[index]);
        }
        index++;

        if(condType == ConditionType.HEALED){
            //verify format: target
            if(description[index].equals("target")){
                index++;
            }
            else{
                throw new UnimplementedException();
            }

            targetType = parseTarget(description[index]);
            index++;
        }

        if(condType != ConditionType.ABILITY){
            String[] nextAbilityDescription;
            if(elseAbility == null){
                nextAbilityDescription = Arrays.copyOfRange(description, index, description.length);
            }
            else{
                nextAbilityDescription = Arrays.copyOfRange(description, index, indexOfElse);
            }

            //make the ability
            removeStartEndParentheses(nextAbilityDescription);
            String d = "";
            boolean first = true;
            for(String s : nextAbilityDescription){
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
                conditionalAbility = makeAbility(sequentialAbilities[0].split(":"));

                //make subsequent abilites and attach them sequentialy
                Ability latestAbility = conditionalAbility;
                for(int i = 1; i < sequentialAbilities.length; i++){
                    Ability newAbility = makeAbility(sequentialAbilities[i].split(":"));
                    latestAbility.setSubsequentAbility(newAbility);
                    latestAbility = newAbility;
                }
            }catch(Exception e){
                throw new UnimplementedException();
            }
        }
        else if(condType == ConditionType.ABILITY){
            //find the start and end of the conditional ability
            //the end is always at the end to essentially just find the start
            int endIndex;
            int startIndex = -1;
            if(elseAbility == null){
                endIndex = description.length-1;
            }
            else{
                endIndex = indexOfElse - 1;
            }

            for(int i = endIndex - 1; i >= 0; i--){
                if(description[i].startsWith("(")){
                    startIndex = i;
                    break;
                }
            }

            //make the ability
            String[] nextAbilityDescription = Arrays.copyOfRange(description, startIndex, endIndex+1);
            removeStartEndParentheses(nextAbilityDescription);
            String d = "";
            boolean first = true;
            for(String s : nextAbilityDescription){
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
                conditionalAbility = makeAbility(sequentialAbilities[0].split(":"));

                //make subsequent abilites and attach them sequentialy
                Ability latestAbility = conditionalAbility;
                for(int i = 1; i < sequentialAbilities.length; i++){
                    Ability newAbility = makeAbility(sequentialAbilities[i].split(":"));
                    latestAbility.setSubsequentAbility(newAbility);
                    latestAbility = newAbility;
                }
            }catch(Exception e){
                throw new UnimplementedException();
            }


            //make the ability that gets tested for success
            nextAbilityDescription = Arrays.copyOfRange(description, index, startIndex);
            removeStartEndParentheses(nextAbilityDescription);
            try{
                testAbility = makeAbility(nextAbilityDescription);
            }catch(Exception e){
                testAbility = new UnimplementedAbility();
            }

        }

    }


    
    public String getSimpleDescription(){
    	String returnString = "";

    	switch (condType){
            case ABILITY:
                returnString += "If ability [" + testAbility.getRecursiveDescription() + "] is used, then [" + conditionalAbility.getRecursiveDescription()+"]";
                break;
            case CHOICE:
                returnString += "Player's choice to [" + conditionalAbility.getRecursiveDescription()+"]";
                break;
            case FLIP:
                returnString += "50% chance to [" + conditionalAbility.getRecursiveDescription()+"]";
                break;
            case HEALED:
                returnString += "If target has been healed, then [" + conditionalAbility.getRecursiveDescription()+"]";
                break;
            case BOOLEAN:
                returnString += "If [" + booleanExpression.getDescription()+"]" + ", then [" + conditionalAbility.getRecursiveDescription()+"]";
                break;
        }

        if(elseAbility != null){
    	    returnString += "<br/>Otherwise, [" + elseAbility.getRecursiveDescription()+"]";
        }

        return returnString;
    }

    public Ability shallowCopy(){
        ConditionAbility returnCard = new ConditionAbility();

        returnCard.name = this.name;
        returnCard.targetType = this.targetType;
        returnCard.subsequentAbility  = this.subsequentAbility;

        returnCard.condType = this.condType;
        returnCard.conditionalAbility = this.conditionalAbility;
        returnCard.elseAbility = this.elseAbility;
        returnCard.testAbility = this.testAbility;

        return returnCard;
    }
}
