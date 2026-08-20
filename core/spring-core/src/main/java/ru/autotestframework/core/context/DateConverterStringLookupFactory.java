package ru.autotestframework.core.context;

import static java.time.Instant.now;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.HashMap;
import org.apache.commons.text.lookup.StringLookup;
import org.springframework.stereotype.Component;
import ru.autotestframework.core.exception.AutotestException;

/**
 * Converts a date from one format to another.
 * Example: ${{dateConverter;20190128121212{@literal &}z1{@literal &}z2}}.
 * where: z1 = yyyyMMddHHmmss is the old format, z2 = yyyy-MM-dd'T'HH:mm:ss is the new format
 */
@Component
public class DateConverterStringLookupFactory implements ContextFunctionsSupplier {
    @Override
    public HashMap<String, StringLookup> get() {
        var functions = new HashMap<String, StringLookup>();
        var dateConverter = new StringLookup() {
            @Override
            public String lookup(String args) {
                var dataParts = args.split("&");
                var dataValue = dataParts[0];
                var oldFormat = dataParts[1];
                var newFormat = dataParts[2];

                DateFormat defaultDateFormat = new SimpleDateFormat(oldFormat);
                DateFormat targetDateFormat = new SimpleDateFormat(newFormat);
                try {
                    var date = defaultDateFormat.parse(dataValue);
                    return targetDateFormat.format(date);

                } catch (ParseException e) {
                    throw new AutotestException("Произошла ошибка при конвертации даты:\n", e);
                }
            }
        };

        var timestampNow = new StringLookup() {
            @Override
            public String lookup(String key) {
                return Timestamp.from(now()).toString();
            }
        };

        var shiftDate = new StringLookup() {
            @Override
            public String lookup(String args) {
                var dataParts = args.split("&");
                var datePeriod = dataParts[0];
                var dateFormat = dataParts[1];

                var oldDate = LocalDateTime.now();
                var period = Period.parse(datePeriod);
                LocalDateTime newDate = oldDate.plus(period);

                return DateTimeFormatter.ofPattern(dateFormat).format(newDate);
            }
        };

        var quarterNow = new StringLookup() {
            @Override
            public String lookup(String args) {
                var myLocal = LocalDate.now();
                int quarter = myLocal.get(IsoFields.QUARTER_OF_YEAR);
                return String.valueOf(quarter);
            }
        };

        var quarter = new StringLookup() {
            @Override
            public String lookup(String args) {

                var myLocal = LocalDate.parse(args);
                int quarter = myLocal.get(IsoFields.QUARTER_OF_YEAR);
                return String.valueOf(quarter);
            }
        };

        functions.put("quarterNow", quarterNow);
        functions.put("quarterAt", quarter);
        functions.put("shiftNow", shiftDate);
        functions.put("timestampNow", timestampNow);
        functions.put("dateConverter", dateConverter);

        return functions;
    }
}
