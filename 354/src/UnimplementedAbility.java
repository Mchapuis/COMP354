import java.util.HashMap;

class UnimplementedAbility extends Ability{
  public boolean realUse(Player player){
    return false;
  }

  UnimplementedAbility(){
    this.name = "Unimplemented Ability";
    this.energyRequired = new HashMap<EnergyCard, Integer>();
  }
  
  public String getSimpleDescription(){
	  return "This part of the ability is not implemented :(";
  }
}
