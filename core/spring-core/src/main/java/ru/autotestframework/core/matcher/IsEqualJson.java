package ru.autotestframework.core.matcher;

import lombok.RequiredArgsConstructor;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;

/**
 * Utility class to Compare two Json
 */
@RequiredArgsConstructor
public class IsEqualJson extends TypeSafeMatcher<String> {

    private final String expectedJson;
    private final JSONCompareMode compareMode;

    /**
     * Is equal json is equal json.
     *
     * @param expectedJson the expected json
     * @param compareMode  the compare mode
     * @return the is equal json
     */
    @Factory
    public static IsEqualJson isEqualJson(final String expectedJson, final JSONCompareMode compareMode) {
        return new IsEqualJson(expectedJson, compareMode);
    }

    @Override
    protected boolean matchesSafely(final String actualJson) {
        JSONCompareResult result;
        try {
            result = JSONCompare.compareJSON(expectedJson, actualJson, compareMode);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return result.passed();
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText(expectedJson);
    }
}
