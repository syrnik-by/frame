import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.StandardEnvironment;
import ru.autotestframework.configuration.FrameworkProperties;
import ru.autotestframework.util.access_checker.AccessChecker;
import ru.autotestframework.util.access_checker.HostCheckUtil;
import ru.autotestframework.util.access_checker.HostInfo;

@Disabled
@Tag("@BackendCore")
class AccessCheckTests {
    private static final String BAD_HOST = "http://localhost:0";
    FrameworkProperties frameworkProperties = new FrameworkProperties();
    String dummyTagNegative = "@toFailure";
    String dummyTagPositive = "@toProceed";
    HostInfo negativeCheckHost = new HostInfo("dummy", BAD_HOST, dummyTagNegative);
    HostInfo positiveCheckHost = new HostInfo("dummy2", "localhost:80", dummyTagPositive);
    Properties prop = new Properties();

    {
        frameworkProperties.setAccessCheckEnabled(true);
    }

    {
        prop.setProperty(
                negativeCheckHost.getAccessTag().concat(".").concat(negativeCheckHost.getStandName()),
                negativeCheckHost.getHostAddress());
        prop.setProperty(
                positiveCheckHost.getAccessTag().concat(".").concat(positiveCheckHost.getStandName()),
                positiveCheckHost.getHostAddress());
    }

    @Test
    void builtinAccessCheckTest() {
        AccessChecker accessChecker = new AccessChecker(frameworkProperties, new StandardEnvironment());
        accessChecker.check();
        Assertions.assertTrue(accessChecker.getViolatedTags().contains("@clientApi"));
        Assertions.assertEquals(1, accessChecker.getViolatedTags().size());
    }

    @Test
    void taskRegimeTest() {
        AccessChecker accessChecker = new AccessChecker(frameworkProperties, new StandardEnvironment());
        accessChecker.setTaskRegime(true);
        accessChecker.check();
        List<String> violatedHosts = accessChecker.getUnavailableHosts().stream()
                .map(HostInfo::getHostAddress)
                .collect(Collectors.toList());
        Assertions.assertTrue(violatedHosts.contains("nonexistingurl.ru"));
        Assertions.assertTrue(violatedHosts.contains(BAD_HOST));
        Assertions.assertEquals(3, accessChecker.getUnavailableHosts().size());
    }

    @Test
    void checkTagResult() {
        AccessChecker accessChecker = new AccessChecker(frameworkProperties, new StandardEnvironment());
        accessChecker.proceedHostInfoList(List.of(positiveCheckHost, negativeCheckHost));
        Assertions.assertTrue(accessChecker.getViolatedTags().contains(dummyTagNegative));
        Assertions.assertEquals(1, accessChecker.getViolatedTags().size());
    }

    @Test
    void negativeCheckHostTest() {
        boolean result = HostCheckUtil.checkIfAvailable(negativeCheckHost.getHostAddress());
        Assertions.assertFalse(result);
    }

    @Test
    void positiveCheckHostTest() {
        boolean result = HostCheckUtil.checkIfAvailable(positiveCheckHost.getHostAddress());
        Assertions.assertTrue(result);
    }
}
