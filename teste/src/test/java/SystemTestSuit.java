import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectPackages({ "qsmp" })
@IncludeTags({ "SystemTest" })
@SuiteDisplayName("All system tests suit")
public final class SystemTestSuit {
}
