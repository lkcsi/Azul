import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({GameTest.class, GameTestNegative.class})
public class Tests {
}
