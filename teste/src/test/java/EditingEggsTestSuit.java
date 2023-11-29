import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectPackages({ "qsmp" })
@IncludeTags({ "EditingSystemTests" })
@SuiteDisplayName("Editing eggs test cases")
public final class EditingEggsTestSuit {
}
