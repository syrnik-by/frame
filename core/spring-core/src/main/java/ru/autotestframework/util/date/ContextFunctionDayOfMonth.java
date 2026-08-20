package ru.autotestframework.util.date;

import static ru.autotestframework.util.date.DateUtils.getFirstOrLastDayOfMonth;

import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.lookup.StringLookup;
import org.springframework.stereotype.Component;
import ru.autotestframework.core.context.ContextFunctionsSupplier;
import ru.autotestframework.core.exception.AutotestException;

/**
 * Context function day of month.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ContextFunctionDayOfMonth implements ContextFunctionsSupplier {
    @Override
    public HashMap<String, StringLookup> get() {
        var functions = new HashMap<String, StringLookup>();
        StringLookup dayOfMonth = args -> {
            String[] parts = args.split("->");
            try {
                var date = getFirstOrLastDayOfMonth(parts[0], parts[1]);
                log.info("Получен '[{}]' день месяца из даты '[{}]' : [{}]", parts[1], parts[0], date);
                return date;
            } catch (AutotestException e) {
                throw new AutotestException("Ошибка получения " + parts[1] + " дня месяца", e);
            }
        };
        functions.put("dayOfMonth", dayOfMonth);
        return functions;
    }
}
