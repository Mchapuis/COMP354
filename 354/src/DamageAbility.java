import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

class DamageAbility extends Ability{
  private ComplexAmount damage;

  public DamageAbility(){
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

    for(PokemonCard p : targetedPokemon){
        p.removeHP(damage.evaluate(player));
        Ability.lastTargetedPokemon = p;
    }

    return true;
  }

  DamageAbility(String [] description) throws UnimplementedException{
      int index = 0;

      //first token must be dam
      if(description[index].equals("dam")){
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

      try{
          this.damage = new ComplexAmount(description[index]);
      }catch(Exception e){
          throw new UnimplementedException();
      }
  }
  
  public String getSimpleDescription(){
  	return "Deal " + damage.getDescription() + " damage to " + targetType.toString();
  }

  public Ability shallowCopy(){
        DamageAbility returnCard = new DamageAbility();

        returnCard.name = this.name;
        returnCard.targetType = this.targetType;
        returnCard.subsequentAbility  = this.subsequentAbility;
        returnCard.hasChoice = this.hasChoice;

        returnCard.damage = this.damage;

        return returnCard;
  }
}