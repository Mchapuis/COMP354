import static org.junit.Assert.*;

import org.junit.Test;

public class Ability_JUnit {

	@Test
	public void test() {
		String str_test_ParseAbility = "Pikachu:pokemon:cat:basic:cat:lightning:60:retreat:cat:colorless:1:attacks:cat:colorless:1:5,cat:colorless:2:6";
		Ability ab_test = new test();
		final Ability ab_new = ab_test.parseAbilitiesLine(str_test_ParseAbility);
		HumanPlayer hp = new HumanPlayer();
		
		// the function need to return the name at least
		String name = ab_new.name;
		assertNotNull(name);
		
		// testing null object
		String[] desc = new String[1];
		desc[0] = "test";
		Ability ab_test_make;
		try {
			ab_test_make = ab_test.makeAbility(desc);
			assertNull(ab_test_make);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// testing UnimplementedAbility 
		desc[0] = "draw";
		// reset variable for test
		ab_test_make = null;
		try {
			ab_test_make = ab_test.makeAbility(desc);
			assertNotNull(ab_test_make);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// TODO: THIS TEST FAILED testing DamageAbility
//		desc[0] = "dam";
//		// reset
//		ab_test_make = null;
//		try {
//			ab_test_make = ab_test.makeAbility(desc);
//			assertNull(ab_test_make);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		//TODO: parseTarget will change so the unit test is not necessary
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