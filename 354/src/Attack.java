import java.util.HashMap;
import java.util.Map;

public class Attack {

	private enum Target{
		OPPONENTACTIVE, UNDEFINED
	}
	
	private enum Status{
		PARALYZED, NONE
	}
	
	private String name;
	private Target target;
	private int damagePoints;
	private HashMap<EnergyCard, Integer> energyRequired;
	private boolean flip;
	private int additionalDamagePoints;
	private Status statusToApply;
	private Target additionalTarget;
	
	public Attack(){
		this.name = "";
		this.target = Target.UNDEFINED;
		this.damagePoints = 0;
		this.energyRequired = new HashMap<EnergyCard, Integer>();
		this.flip = false;
		this.additionalDamagePoints = 0;
		this.statusToApply = Status.NONE;
		this.additionalTarget = Target.UNDEFINED;
	}

	public String getDescription(){
		String desc = "<html><body>Name: " + name;
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
			desc += entry.getKey().getType();
			desc += ": ";
			desc += entry.getValue();
			desc += "<br/>";
		}
		desc += "Flip required: ";
		desc += flip;
		desc += "<br/>";
		desc += "Status to apply: ";
		if (statusToApply == Status.PARALYZED){
			desc += "Paralyzed";
		} else {
			desc += "None";
		}
		if (additionalDamagePoints > 0) {
			desc += "Potential additional damage: ";
			desc += additionalDamagePoints;
		}
		desc += "</body></html>";
		return desc;
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
}