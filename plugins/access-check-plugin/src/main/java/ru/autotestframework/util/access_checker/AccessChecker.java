package ru.autotestframework.util.access_checker;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.autotestframework.configuration.FrameworkProperties;
import ru.autotestframework.core.exception.ConfigurationException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessChecker implements InitializingBean {

    private static final String PROPERTIES_FILE = "mse.properties";
    private static final String COMMON_ENVIRONMENT = "COMMON";
    private static final String PROP_DELIMITER = "\\.";

    @Autowired
    private final FrameworkProperties frameworkProperties;

    @Autowired
    private final Environment environment;

    @Getter
    private final List<String> violatedTags = new ArrayList<>();

    @Getter
    private final List<HostInfo> unavailableHosts = new ArrayList<>();

    @Setter
    private boolean isTaskRegime = false;

    /**
     * main method in access check plugin
     */
    public void check() {
        if (!frameworkProperties.isAccessCheckEnabled() && !isTaskRegime) {
            log.warn("AccessCheck is disabled. No related additional filtering tag rules would be applied");
            return;
        }
        var properties = PropertiesHelper.getProperties(PROPERTIES_FILE);
        if (properties.size() == 0) {
            throw new ConfigurationException(
                    "Create appropriate {} " + "or set property framework.access.check.enabled=false", PROPERTIES_FILE);
        }
        List<HostInfo> list = filterHostInfo(properties);
        proceedHostInfoList(list);
    }

    /**
     * parses and filters properties
     * @param properties
     * @return
     */
    public List<HostInfo> filterHostInfo(final Properties properties) {
        List<String> profiles = List.of(environment.getActiveProfiles());

        return properties.entrySet().stream()
                .map(x -> {
                    String[] parsedKeys = x.getKey().toString().split(PROP_DELIMITER);
                    String tag = parsedKeys[0];
                    String standName = (parsedKeys.length > 1) ? parsedKeys[1] : COMMON_ENVIRONMENT;
                    return new HostInfo(standName, x.getValue().toString(), "@".concat(tag));
                })
                .filter(x -> {
                    String environmentName = x.getStandName();
                    return profiles.contains(environmentName)
                            || environmentName.equals(COMMON_ENVIRONMENT)
                            || environmentName.equals("local") && profiles.isEmpty()
                            || isTaskRegime;
                })
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * processes checks for each host
     * @param hostInfoList
     */
    public void proceedHostInfoList(final List<HostInfo> hostInfoList) {
        log.error("==== AccessCheck Report ====");
        var previousStandName = "";
        for (HostInfo currentHost : hostInfoList) {
            String currentStandName = currentHost.getStandName();
            if (!previousStandName.equals(currentStandName)) {
                log.error("");
                log.error("====== Stand : {} ======", currentStandName);
            }
            checkAccess(currentHost);
            previousStandName = currentHost.getStandName();
        }
        if (!getViolatedTags().isEmpty() && !isTaskRegime) {
            String violatedCucumberTags =
                    "(".concat(String.join(" or ", getViolatedTags())).concat(")");
            log.error("====== AccessCheck Brief Failures ======");
            log.error("Violated tags : " + violatedCucumberTags);
        }
    }

    private void checkAccess(final HostInfo hostInfo) {
        String tag = hostInfo.getAccessTag();
        String sHost = hostInfo.getHostAddress();
        log.error("======== CheckAccess for tag: {}, host: {}", tag, sHost);
        Boolean result = HostCheckUtil.checkIfAvailable(sHost);
        if (Boolean.FALSE.equals(result)) {
            getUnavailableHosts().add(hostInfo);
            log.error("Status: Failed - Access denied. "
                    + "Please make preparations (UKD, SZ etc) to get Access on this Stand");
            if (!isTaskRegime) {
                violatedTags.add(tag);
            }
        } else {
            log.error("Status: Succeed - Access provided");
        }
    }

    /**
     * InitializingBean implementation
     */
    @Override
    public void afterPropertiesSet() {
        check();
    }
}
