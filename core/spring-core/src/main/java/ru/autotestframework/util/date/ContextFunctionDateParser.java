package ru.autotestframework.util.date;

import static ru.autotestframework.util.date.DateUtils.dateToString;
import static ru.autotestframework.util.date.DateUtils.getLocalDateFromVar;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.lookup.StringLookup;
import org.springframework.stereotype.Component;
import ru.autotestframework.core.context.ContextFunctionsSupplier;
import ru.autotestframework.core.exception.AutotestException;

/**
 * Context function date parser.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ContextFunctionDateParser implements ContextFunctionsSupplier {

    @Override
    public HashMap<String, StringLookup> get() {
        var functions = new HashMap<String, StringLookup>();
        StringLookup dateParser = args -> {
            String result = args.startsWith("значение переменной:")
                    ? Arrays.stream(args.split(": "))
                            .skip(1)
                            .map(s -> s.split(" ", 2))
                            .map(v -> (v[0]) + " " + v[1])
                            .collect(Collectors.joining(" "))
                    : args;
            String[] parts = result.split("->");
            var date = dateToString(LocalDateTime.of(getLocalDateFromVar(parts[0]), LocalTime.now()), parts[1]);
            log.info("Из выражения '{}' в формате '{}' получено значение: {}", parts[0], parts[1], date);
            try {
                return date;
            } catch (AutotestException e) {
                throw new AutotestException("Ошибка при парсинге даты", e);
            }
        };
        functions.put("dateParser", dateParser);
        return functions;
    }
}
