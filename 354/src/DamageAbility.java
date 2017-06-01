import java.util.Arrays;

class DamageAbility extends Ability{
  private int damage;

  public void realUse(Player player){
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
            //TODO: need to implement method to get selection
            break;
        case YOUR_BENCH:
            //TODO: need to implement method to get selection
            break;
        case YOUR_POKEMON:
            //TODO: need to implement method to get selection
            break;
        case OPPONENT_POKEMON:
            //TODO: need to implement method to get selection
            break;
    }

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
}
































































































