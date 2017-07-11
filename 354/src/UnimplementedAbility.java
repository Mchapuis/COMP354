import java.util.HashMap;

class UnimplementedAbility extends Ability{
  public boolean realUse(Player player){
    return false;
  }

  UnimplementedAbility(){
    this.name = "Unimplemented Ability";
    this.energyRequired = new HashMap<EnergyCard.Type, Integer>();
  }
  
  public String getSimpleDescription(){
	  return "This part of the ability is not implemented :(";
  }

  public Ability shallowCopy(){
    UnimplementedAbility returnCard = new UnimplementedAbility();

    returnCard.name = this.name;
    returnCard.targetType = this.targetType;
    returnCard.subsequentAbility  = this.subsequentAbility;

    return returnCard;
  }
}
