class UnimplementedAbility extends Ability{
  public void realUse(Player player){
    System.out.println("NOT IMPLEMENTED! OH NOOOOOOOOOOOOOO!");
  }

  UnimplementedAbility(){
    this.name = "Unimplemented Ability";
  }
}
