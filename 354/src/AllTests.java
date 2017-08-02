import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ Ability_unitTest.class, AIplayer_unitTest.class, CardManager_unitTest.class,
		Deck_unitTest.class, HumanPlayer_JUnit.class, PokemonCard_unitTest.class, RandomNumberGen_unitTest.class,
		IntegrationTest_abilities_Cards.class, regressionTest_1.class })



public class AllTests {

}
