import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ Ability_JUnit.class, AIplayer_JUnit.class, CardManager_JUnit.class,
		Deck_unitTest.class, HumanPlayer_JUnit.class, PokemonCard_unitTest.class, RandomNumberGen_JUnit.class,
		IntegrationTest_abilities_Cards.class, regressionTests.class })



public class AllTests {

}
