package ru.autotestframework.core.matcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;
import ru.autotestframework.util.StringUtil;
import ru.autotestframework.util.Validator;

/**
 * Is equal file.
 */
@RequiredArgsConstructor
public class IsEqualFile extends TypeSafeMatcher<File> {

    private final File expectedFile;

    /**
     * Is equal file is equal file.
     *
     * @param expectedFilePath the expected file path
     * @return the is equal file
     */
    @Factory
    public static IsEqualFile isEqualFile(final File expectedFilePath) {
        return new IsEqualFile(expectedFilePath);
    }

    @Override
    protected boolean matchesSafely(final File actualFile) {
        boolean result;
        try (var actual = new FileInputStream(actualFile);
                var expected = new FileInputStream(expectedFile)) {
            result = IOUtils.contentEquals(actual, expected);
        } catch (IOException e) {
            throw Validator.exception("File comparison error", e);
        }
        return result;
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText(
                StringUtil.format("The contents of the file are equal to the contents of the file '{}'", expectedFile));
    }
}
