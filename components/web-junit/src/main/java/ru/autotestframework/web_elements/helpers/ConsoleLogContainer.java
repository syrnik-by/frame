package ru.autotestframework.web_elements.helpers;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class ConsoleLogContainer {
    private Map<String, String> consoleLog = new HashMap<String, String>();

    /**
     * puts log in file
     * @param fileName
     * @param log
     */
    public void put(final String fileName, final String log) {
        consoleLog.put(fileName, log);
    }
}
