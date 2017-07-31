import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class HealAbility extends Ability{
    ComplexAmount healAmount = null;

    public HealAbility(){
        this.energyRequired = new HashMap<EnergyCard.Type, Integer>();
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

        ArrayList<PokemonCard> targetedPokemon = new ArrayList<>();
        switch(targetType){
            case OPPONENT_BENCH:
                if(hasChoice){
                    if(otherPlayer.getBench().size() > 0){
                        targetedPokemon.add(GameEngine.choosePokemonCard(player,targetType));
                    }
                }
                else{
                    for(PokemonCard p : otherPlayer.getBench()){
                        targetedPokemon.add(p);
                    }
                }
                break;
            case YOUR_BENCH:
                if(hasChoice){
                    if(sourcePlayer.getBench().size() > 0){
                        targetedPokemon.add(GameEngine.choosePokemonCard(player,targetType));
                    }
                }
                else{
                    for(PokemonCard p : sourcePlayer.getBench()){
                        targetedPokemon.add(p);
                    }
                }
                break;
            case OPPONENT_ACTIVE:
                targetedPokemon.add(otherPlayer.getActivePokemon());
                break;
            case YOUR_ACTIVE:
                targetedPokemon.add(sourcePlayer.getActivePokemon());
                break;
            case YOUR_POKEMON:
            case OPPONENT_POKEMON:
                targetedPokemon.add(GameEngine.choosePokemonCard(player,targetType)) ;
                break;
            case LAST:
                targetedPokemon.add(Ability.lastTargetedPokemon);
                break;
        }

        for(PokemonCard targetPokemon : targetedPokemon){
            int maxHealAmount = targetPokemon.getMaxHP() - targetPokemon.getCurrentHP();
            int amountToHeal = Math.min(healAmount.evaluate(player), maxHealAmount);
            targetPokemon.removeHP(-amountToHeal);
            targetPokemon.setHasBeenHealed(true);

            Ability.lastTargetedPokemon = targetPokemon;
        }

        return true;
    }

    HealAbility(String[] description) throws UnimplementedException{
        int index = 0;

        //verify format: ability name
        if(description[index].equals("heal")){
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

        //set target
        if(description[index].equals("choice")){
            index++;
            hasChoice = true;
        }
        targetType = parseTarget(description[index]);

        //set heal amount
        index++;
        try{
            healAmount = new ComplexAmount(description[index]);
        }catch(Exception e){
            throw new UnimplementedException();
        }
    }
    
    public String getSimpleDescription(){
    	return "Heal up to " + healAmount.getDescription() + " HP on " + targetType.toString();
    }

    public Ability shallowCopy(){
        HealAbility returnCard = new HealAbility();

        returnCard.name = this.name;
        returnCard.targetType = this.targetType;
        returnCard.subsequentAbility  = this.subsequentAbility;

        returnCard.healAmount = this.healAmount;

        return returnCard;
    }
}
