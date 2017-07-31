import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConditionAbility extends Ability{
    private enum ConditionType {
        FLIP,
        CHOICE,
        HEALED,
        ABILITY,
    }

    ConditionType condType;
    Ability conditionalAbility = null;
    Ability elseAbility = null;

    //used only for ConditionType.ABILITY
    Ability testAbility = null;
    
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
                //TODO: get anastasia to make a GUI prompt for this
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
                    elseAbility = makeAbility(newDescription);
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
                throw new UnimplementedException();
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

            removeStartEndParentheses(nextAbilityDescription);

            try {
                conditionalAbility = makeAbility(nextAbilityDescription);
            } catch (Exception e) {
                conditionalAbility = new UnimplementedAbility();
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
            String[] nextAbilityDescription = Arrays.copyOfRange(description, startIndex, endIndex);
            removeStartEndParentheses(nextAbilityDescription);

            try{
                conditionalAbility = makeAbility(nextAbilityDescription);
            }catch(Exception e){
                conditionalAbility = new UnimplementedAbility();
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

    private static void removeStartEndParentheses(String[] description){
        int lastCharIndex = description.length - 1;
        if(description[0].startsWith("(") && description[lastCharIndex].endsWith(")")){
            //remove first character from first string
            description[0] = description[0].substring(1);
            //remove last character from last string
            description[lastCharIndex] = description[lastCharIndex].substring(0, description[lastCharIndex].length() - 1);
        }
    }
    
    public String getSimpleDescription(){
    	String returnString = "";

    	switch (condType){
            case ABILITY:
                returnString += "If subsequent ability is used, then " + conditionalAbility.getRecursiveDescription();
                break;
            case CHOICE:
                returnString += "Player's choice to " + conditionalAbility.getRecursiveDescription();
                break;
            case FLIP:
                returnString += "50% chance to " + conditionalAbility.getRecursiveDescription();
                break;
            case HEALED:
                returnString += "If target has been healed, then " + conditionalAbility.getRecursiveDescription();
                break;
        }

        if(elseAbility != null){
    	    returnString += "<br/>Otherwise, " + elseAbility.getRecursiveDescription();
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
