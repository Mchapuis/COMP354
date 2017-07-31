import static org.junit.Assert.*;

import org.junit.Test;

public class HealAbility_unitTest {

	@Test
	public void test() throws Exception {
		String abilityDesc = "Lady:heal:target:choice:your:60";
		Ability healing = Ability.makeAbility(abilityDesc.split(":"));
		PokemonCard pokemon1 = new PokemonCard();
		
	}

}
