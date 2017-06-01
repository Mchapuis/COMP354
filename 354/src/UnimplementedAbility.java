class UnimplementedAbility extends Ability{
  public boolean
  realUse(Player player){
    System.out.println("NOT IMPLEMENTED! OH NOOOOOOOOOOOOOO!");
    return true;
  }

  UnimplementedAbility(){
    this.name = "Unimplemented Ability";
  }
}
