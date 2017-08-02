import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

public class Ability_unitTest {

	@Test
	public void test() {
		
		String descLine = "dam:target:opponent-active:40,cond:flip:dam:target:opponent-active:40";
		String[] desc = descLine.split(":") ;
		
		Ability ab_test = null;
		
		//check makeAbility() method used only with a string array of all the description of a single ability without the name
		try {
			ab_test = Ability.makeAbility(desc);
			//check the created ability type
			assertTrue(ab_test instanceof DamageAbility);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// parseAbilitiesline() use the whole description of an ability including the name and the sequential ability
		descLine ="Fury Attack:dam:target:opponent-active:40,cond:flip:dam:target:opponent-active:40";
		try {
			ab_test = Ability.parseAbilitiesLine(descLine);
			//check the type of first ability
			assertTrue(ab_test instanceof DamageAbility);
			//check the type of subsequentAbility
			assertTrue(ab_test.subsequentAbility instanceof ConditionAbility);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// testing addEnergyRequired
		ab_test.addEnergyRequired(EnergyCard.Type.FIGHT, 2);
		HashMap<EnergyCard.Type, Integer> energyRequired = ab_test.getEnergyRequired();
	
		assertTrue(energyRequired.toString().equals( "{FIGHT=2}") );
	}

}

//test class to test abstract Ability
class test extends Ability{

	@Override
	public boolean use(Player player) {
		return false;
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean realUse(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String getSimpleDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ability shallowCopy() {
		// TODO Auto-generated method stub
		return null;
	}
}