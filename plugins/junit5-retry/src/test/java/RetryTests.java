import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class RetryTests {

    @Test
    @Tag("Retry3")
    @Disabled // Need for testing purposes
    void retryTest() {
        Assertions.assertTrue(false);
    }
}
