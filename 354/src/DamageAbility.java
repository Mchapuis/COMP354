import java.util.Arrays;

class DamageAbility extends Ability{
  private int damage;

  public void use(Player player){
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
        case OPPONENT_BENCH:
            //TODO: need to implement method to get selection
            break;
        case YOUR_ACTIVE:
            sourcePlayer.getActivePokemon().removeHP(damage);
            break;
        case YOUR_BENCH:
            //TODO: need to implement method to get selection
            break;
    }

  }

  DamageAbility(String [] description){
    //parse damage
    damage = Integer.valueOf(description[description.length-1]);

    targetType = parseTarget(Arrays.copyOfRange(description, 1, description.length));
  }
}
