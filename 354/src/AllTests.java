import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ Ability_JUnit.class, AIplayer_JUnit.class, CardManager_JUnit.class,
		Deck_unitTest.class, HumanPlayer_JUnit.class, PokemonCard_unitTest.class, RandomNumberGen_JUnit.class })



public class AllTests {

}
