import static org.junit.Assert.*;

import org.junit.Test;

public class Ability_JUnit {

	@Test
	public void test() {
		
		String descLine = "dam:target:opponent-active:40,cond:flip:dam:target:opponent-active:40";
		String[] desc = descLine.split(":") ;
		
		Ability ab_test;
		
		//check makeAbility() method
		try {
			ab_test = Ability.makeAbility(desc);
			System.out.println(ab_test.getClass().getName());
			assertTrue(ab_test instanceof DamageAbility);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		descLine ="Fury Attack:dam:target:opponent-active:40,cond:flip:dam:target:opponent-active:40";
		try {
			ab_test = Ability.parseAbilitiesLine(descLine);
			System.out.println(ab_test.getClass().getName());
			assertTrue(ab_test instanceof DamageAbility);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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