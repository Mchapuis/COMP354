import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ Ability_unitTest.class, AIplayer_unitTest.class, CardManager_unitTest.class,
		Deck_unitTest.class,DeenergizeAbility_unitTest.class, GameEngine_unitTest.class, 
		HealAbility_unitTest.class, HumanPlayer_unitTest.class, PokemonCard_unitTest.class,
		RandomNumberGen_unitTest.class, IntegrationTest_abilities_Cards.class, 
		regressionTest_1.class, regressionTest_2.class, regressionTest_3.class })



public class AllTests {

}
