import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectPackages({ "qsmp" })
@IncludeTags({ "DeletingSystemTests" })
@SuiteDisplayName("Deleting eggs test cases")
public final class DeletingEggsTestSuit {
}
