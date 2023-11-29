import org.junit.platform.suite.api.*;

@Suite
@SelectPackages({ "qsmp" })
@IncludeTags({ "SystemTest" })
@ExcludeTags({ "RepetitionTest" })
@SuiteDisplayName("Non repeatable system tests")
public final class NonRepeatableSystemTests {
}
