package ru.autotestframework.core.context;

import static java.util.Locale.ENGLISH;
import static ru.autotestframework.util.Validator.exception;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.experimental.UtilityClass;
import net.datafaker.service.FakeValuesService;
import net.datafaker.service.RandomService;
import org.apache.commons.text.lookup.StringLookup;
import ru.autotestframework.util.StringUtil;
import ru.autotestframework.util.generator.FakerRU;

/**
 * initialising base context functions to proceed templates
 */
@UtilityClass
public final class ContextFunctionFactory {
    /**
     * The constant SECRET_DUMMY.
     */
    public static final String SECRET_DUMMY = "{secretVariable}";

    /**
     * The constant FAKER_RU.
     */
    public static final StringLookup FAKER_RU = new FakerRu();
    /**
     * The constant REGEX_GENERATOR.
     */
    public static final StringLookup REGEX_GENERATOR = new RegexGenerator();
    /**
     * The constant RANDOM_LONG_GENERATOR.
     */
    public static final StringLookup RANDOM_LONG_GENERATOR = new RandomLongGenerator();
    /**
     * The constant BASE_64_ENCODER.
     */
    public static final StringLookup BASE_64_ENCODER = new Base64Encoder();
    /**
     * The constant BASE_64_DECODER.
     */
    public static final StringLookup BASE_64_DECODER = new Base64Decoder();
    /**
     * The constant ESCAPER.
     */
    public static final StringLookup ESCAPER = new Escaper();
    /**
     * The constant SECRET.
     */
    public static final StringLookup SECRET = new Secret();
    /**
     * The constant SECRET_STUB.
     */
    public static final StringLookup SECRET_STUB = new SecretStub();

    private static final RandomService RANDOM_SERVICE = new RandomService();
    private static final FakeValuesService FAKE_VALUES_SERVICE = new FakeValuesService(ENGLISH, new RandomService());

    /**
     * Regex generator.
     */
    public static class RegexGenerator implements StringLookup {
        @Override
        public String lookup(final String regex) {
            return FAKE_VALUES_SERVICE.regexify(regex);
        }
    }

    /**
     * Random long generator.
     */
    public static class RandomLongGenerator implements StringLookup {
        @Override
        public String lookup(final String range) {
            try {
                return String.valueOf(RANDOM_SERVICE.nextLong(Long.parseLong(range)));
            } catch (Exception exception) {
                throw exception("Ошибка генерации числа в диапазоне от 0-{}", range);
            }
        }
    }

    /**
     * Base 64 encoder.
     */
    public static class Base64Encoder implements StringLookup {
        @Override
        public String lookup(final String content) {
            return Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * Base 64 decoder.
     */
    public static class Base64Decoder implements StringLookup {
        @Override
        public String lookup(final String content) {
            return new String(Base64.getDecoder().decode(content), StandardCharsets.UTF_8);
        }
    }

    /**
     * Escaper.
     */
    public static class Escaper implements StringLookup {
        @Override
        public String lookup(final String unescaped) {
            return unescaped.replace("\"", "\\\"").replace("'", "\\'");
        }
    }

    /**
     * Faker ru.
     */
    public static class FakerRu implements StringLookup {
        @Override
        public String lookup(final String key) {
            return FakerRU.generate(key);
        }
    }

    /**
     * Secret.
     */
    public static class Secret implements StringLookup {
        @Override
        public String lookup(final String key) {
            return key;
        }
    }

    /**
     * Secret stub.
     */
    public static class SecretStub implements StringLookup {
        @Override
        public String lookup(final String key) {
            return SECRET_DUMMY;
        }
    }

    /**
     * Make arithmetic.
     */
    public static class MakeArithmetic implements StringLookup {
        @Override
        public String lookup(final String args) {
            var dataParts = args.split("&");
            var operation = dataParts[0];
            var format = dataParts[1];
            return StringUtil.makeArithmeticOperationFormat(operation, format);
        }
    }
}
