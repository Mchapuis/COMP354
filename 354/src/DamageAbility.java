import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

class DamageAbility extends Ability{
  private int damage;

  public DamageAbility(){
        this.energyRequired = new HashMap<EnergyCard, Integer>();
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

    switch(targetType){
        case OPPONENT_ACTIVE:
            otherPlayer.getActivePokemon().removeHP(damage);
            break;
        case YOUR_ACTIVE:
            sourcePlayer.getActivePokemon().removeHP(damage);
            break;
        case OPPONENT_BENCH:
            if(otherPlayer.getBench().size() > 0){
                GameEngine.choosePokemonCard(player,targetType).removeHP(damage);
            }
            break;
        case YOUR_BENCH:
            if(sourcePlayer.getBench().size() > 0){
                GameEngine.choosePokemonCard(player,targetType).removeHP(damage);
            }
            break;
        case YOUR_POKEMON:
            GameEngine.choosePokemonCard(player,targetType).removeHP(damage);
            break;
        case OPPONENT_POKEMON:
            GameEngine.choosePokemonCard(player,targetType).removeHP(damage);
            break;
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
      }
      this.targetType = parseTarget(description[index++]);
      if(description[index].equals("choice")){
          index++;
      }

      try{
          this.damage = Integer.valueOf(description[index]);
      }catch(Exception e){
          throw new UnimplementedException();
      }
  }
  
  public String getSimpleDescription(){
  	return "Deal " + damage + " damage to " + targetType.toString();
  }

  public Ability shallowCopy(){
        DamageAbility returnCard = new DamageAbility();

        returnCard.name = this.name;
        returnCard.targetType = this.targetType;
        returnCard.subsequentAbility  = this.subsequentAbility;

        returnCard.damage = this.damage;

        return returnCard;
  }
}