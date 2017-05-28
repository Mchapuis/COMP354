import java.util.HashMap;
import java.util.Map;

public class Attack {

	private enum Target{
		OPPONENTACTIVE, OPPONENTHAND, UNDEFINED
	}
	
	private enum Status{
		PARALYZED, NONE
		//OMG SUCH A BAD PUSH. MUST REVERTTTTTTTTTTTTTTTTTTTTTT
	}
	
	private enum Destination{
		BOTTOMOFDECK, NONE
	}
	
	private String name;
	private Target target;
	private int damagePoints;
	private HashMap<EnergyCard, Integer> energyRequired;
	private boolean flip;
	private int additionalDamagePoints;
	private Status statusToApply;
	private Target additionalTarget;
	private Destination destination;
	
	public Attack(){
		this.name = "";
		this.target = Target.UNDEFINED;
		this.damagePoints = 0;
		this.energyRequired = new HashMap<EnergyCard, Integer>();
		this.flip = false;
		this.additionalDamagePoints = 0;
		this.statusToApply = Status.NONE;
		this.additionalTarget = Target.UNDEFINED;
		this.destination = Destination.NONE;
	}

	public String getDescription(){
		String desc = "Name: " + name;
		desc += "<br/>";
		desc += "Target: ";
		desc += target;
		desc += "<br/>";
		desc += "Damage points: ";
		desc += damagePoints;
		desc += "<br/>";
		desc += "Energy required: ";
		desc += "<br/>";
		for (Map.Entry<EnergyCard, Integer> entry : energyRequired.entrySet()){
			desc += "&nbsp;&nbsp;&nbsp;";
			desc += entry.getKey().getType();
			desc += ": ";
			desc += entry.getValue();
			desc += "<br/>";
		}
		desc += "Flip required: ";
		desc += flip;
		if (statusToApply == Status.PARALYZED){
			desc += "<br/>Status to apply: Paralyzed";
		}
		if (additionalDamagePoints > 0) {
			desc += "<br/>";
			desc += "Potential additional damage: ";
			desc += additionalDamagePoints;
		}
		if (destination != Destination.NONE){
			desc += "<br/>";
			desc += "Destination: ";
			desc += destination;
		}
		return desc;
	}
	
	public String getTarget(){
		return this.target.toString();
	}
	
	public int getDamagePoints(){
		return this.damagePoints;
	}
	
	public HashMap<EnergyCard, Integer> getEnergyRequired(){
		return this.energyRequired;
	}
	
	public boolean getFlipRequired(){
		return this.flip;
	}
	
	public String getStatusToApply(){
		return this.statusToApply.toString();
	}
	
	public String getAdditionalTarget(){
		return this.additionalTarget.toString();
	}
	
	public int getAdditionalDamagePoints(){
		return this.additionalDamagePoints;
	}
	
	public String getDestination(){
		return this.destination.toString();
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setFlip(boolean flag){
		if (flag){
			this.flip = true;
		} else {
			this.flip = false;
		}
	}
	
	public void setTarget(String target){
		if (target.equals("opponent-active")){
			this.target = Target.OPPONENTACTIVE;
		} else if (target.equals("opponent-hand")) {
			this.target = Target.OPPONENTHAND;
		} else {
			this.target = Target.UNDEFINED;
		}
	}
	
	public void setApplyStatus(String status){
		if (status.equals("paralyzed")){
			statusToApply = Status.PARALYZED;
		} else {
			statusToApply = Status.NONE;
		}
	}
	
	public void setDamagePoints(int points){
		this.damagePoints = points;
	}
	
	public void setAdditionalDamagePoints(int points){
		this.additionalDamagePoints = points;
	}
	
	public void setAdditionalTarget(String target){
		if (target.equals("opponent-active")){
			this.additionalTarget = Target.OPPONENTACTIVE;
		} else {
			this.additionalTarget = Target.UNDEFINED;
		}
	}
	
	public void addEnergyRequirement(EnergyCard energy, int amount){
		this.energyRequired.put(energy, amount);
	}
	
	public void setDestination(String dest){
		if (dest.equals("deck-bottom")){
			this.destination = Destination.BOTTOMOFDECK;
		} else {
			this.destination = Destination.NONE;
		}
	}
}