import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

class DamageAbility extends Ability{
  private int damage;

  public DamageAbility(){
  	this.energyRequired = new HashMap<EnergyCard, Integer>();
  }
  
  public String realUse(Player player){
	String resultString = "";
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
            if (sourcePlayer.equals(playerCardManager))
            	resultString += "Opponent's pokemon lost " + damage + " HP. ";
            else 
            	resultString += "Your pokemon lost " + damage + " HP. ";
            break;
        case YOUR_ACTIVE:
            sourcePlayer.getActivePokemon().removeHP(damage);
            if (sourcePlayer.equals(playerCardManager))
            	resultString += "Your pokemon lost " + damage + " HP. ";
            else 
            	resultString += "Opponent's pokemon lost " + damage + " HP. ";
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

    return resultString;
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
  
  public String getDescription(){
  	String desc = "Name: " + this.name;
  	desc += "<br/>";
  	desc += "Damage to inflict: ";
  	desc += this.damage;
  	desc += "<br/>";
  	desc += "Energy required: ";
	desc += "<br/>";
	for (Entry<EnergyCard, Integer> entry : energyRequired.entrySet()){
		desc += "&nbsp;&nbsp;&nbsp;";
		desc += entry.getKey().getType();
		desc += ": ";
		desc += entry.getValue();
		desc += "<br/>";
	}
  	return desc;
  }
}