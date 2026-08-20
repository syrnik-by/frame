package ru.autotestframework.screen_elements.driver_manager.drivers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static ru.autotestframework.screen_elements.driver_manager.drivers.DriverScreen.findSubRegion;
import static ru.autotestframework.screen_elements.driver_manager.drivers.DriverScreen.getRegion;

import java.util.ArrayList;
import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Screen;
import ru.autotestframework.core.exception.ConfigurationException;

/**
 * Driver screen test.
 */
@Tag("@ScreenElements")
class DriverScreenTest {

    private String value = Faker.instance().address().buildingNumber();
    /**
     * The Words.
     */
    ArrayList<Match> words;
    /**
     * The Match.
     */
    Match match = Mockito.mock(Match.class);

    /**
     * Before each.
     */
    @BeforeEach
    void beforeEach() {
        when(match.getText()).thenReturn(value);
        words = new ArrayList<>();
        words.add(match);
    }

    /**
     * Gets region with first search type test.
     *
     * @throws FindFailed the find failed
     */
    @Test
    void getRegionWithFirstSearchTypeTest() throws FindFailed {
        try (MockedConstruction<Screen> mockScreen = Mockito.mockConstruction(Screen.class, (mock, context) -> {
            when(mock.findText(anyString())).thenThrow(new FindFailed(""));
        })) {
            Assertions.assertThrows(ConfigurationException.class, () -> getRegion("test", 1, "RUR"));
        }
    }

    /**
     * Gets region with second search type test.
     *
     * @throws Exception the exception
     */
    @Test
    void getRegionWithSecondSearchTypeTest() throws Exception {
        try (MockedConstruction<Screen> mockScreen = Mockito.mockConstruction(Screen.class, (mock, context) -> {
            when(mock.findText(anyString())).thenReturn(match);
        })) {
            Assertions.assertNotNull(getRegion("test", 2, "RUR"));
        }
    }

    /**
     * Gets region with third search type test.
     */
    @Test
    void getRegionWithThirdSearchTypeTest() {
        try (MockedConstruction<Screen> mockScreen = Mockito.mockConstruction(Screen.class, (mock, context) -> {
            when(mock.findWord(anyString())).thenReturn(match);
        })) {
            Assertions.assertNotNull(getRegion("test", 3, "RUR"));
        }
    }

    /**
     * Find sub region with first search type test.
     */
    @Test
    void findSubRegionWithFirstSearchTypeTest() {
        try (MockedConstruction<Screen> mockScreen = Mockito.mockConstruction(Screen.class, (mock, context) -> {
            when(mock.findText(anyString())).thenReturn(match);
        })) {
            Assertions.assertThrows(ConfigurationException.class, () -> findSubRegion("test", 1, "RUR"));
        }
    }

    /**
     * Find sub region with second search type test.
     */
    @Test
    void findSubRegionWithSecondSearchTypeTest() {
        try (MockedConstruction<Screen> mockScreen = Mockito.mockConstruction(Screen.class, (mock, context) -> {
            when(mock.findLines()).thenReturn(words);
        })) {
            Assertions.assertNotNull(DriverScreen.findSubRegion(value, 2, "RUR"));
        }
    }

    /**
     * Find sub region with third search type test.
     */
    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void findSubRegionWithThirdSearchTypeTest() {
        try (MockedConstruction<Screen> mockScreen = Mockito.mockConstruction(Screen.class, (mock, context) -> {
            when(mock.findWords()).thenReturn(words);
        })) {
            Assertions.assertNotNull(DriverScreen.findSubRegion(value, 3, "RUR"));
        }
    }
}
