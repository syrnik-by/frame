package ru.autotestframework.cucumber.step_defs;

import static ru.autotestframework.util.Validator.assertThat;
import static ru.autotestframework.util.Validator.checkThat;

import com.codeborne.selenide.Condition;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.autotestframework.core.FileLoaderImpl;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.core.exception.ExecutionException;
import ru.autotestframework.cucumber.page_manager.PageManager;
import ru.autotestframework.cucumber.parser.MatcherName;
import ru.autotestframework.cucumber.parser.MatcherParser;
import ru.autotestframework.cucumber.type.Triple;
import ru.autotestframework.cucumber.type.resolvable.ResolvableList;
import ru.autotestframework.cucumber.type.resolvable.ResolvableMap;
import ru.autotestframework.ui_core.conditions.Editable;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImpl;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.ui_core.services.image_comparison.ImageComparison;
import ru.autotestframework.ui_core.typified_elements.IElement;
import ru.autotestframework.ui_core.typified_elements.IElementData;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.enums.FixState;
import ru.autotestframework.ui_core.typified_elements.ifaces.ICleanable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.ISelectable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IVerifiable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IWritable;

/**
 * Steps ui.
 */
@Repository("Steps")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
@RequiredArgsConstructor
public class StepsUi implements IStepsUi {
    private final Context context;
    private final PageManager pageManager;
    private final DriverContainerImpl driverContainer;
    private final FileLoaderImpl fileLoader;

    private double pixelToleranceLevel;
    private double allowingPercentOfDifferentPixels;

    @Override
    public void closeApp() {
        driverContainer.remove();
    }

    @Override
    public void setCurrentPage(final String pageTitle) {
        pageManager.getPageByTitle(pageTitle);
    }

    @Override
    public void click(final String elementTitle) {
        pageManager.getCurrent().getElementByTitle(elementTitle).click();
    }

    @Override
    public void click(final int index, final String elementTitle) {
        pageManager
                .getCurrent()
                .getElementsList(elementTitle, WebElement.class)
                .get(index - 1)
                .click();
    }

    @Override
    public void doubleClick(final String elementTitle) {
        pageManager.getCurrent().getElementByTitle(elementTitle).doubleClick();
    }

    @Override
    public void doubleClick(final int index, final String elementTitle) {
        pageManager
                .getCurrent()
                .getElementsList(elementTitle, IElement.class)
                .get(index - 1)
                .doubleClick();
    }

    @Override
    public void rightClick(final String elementTitle) {
        pageManager.getCurrent().getElementByTitle(elementTitle).rightClick();
    }

    @Override
    public void rightClick(final int index, final String elementTitle) {
        pageManager
                .getCurrent()
                .getElementsList(elementTitle, IElement.class)
                .get(index - 1)
                .rightClick();
    }

    @Override
    public void hover(final String elementTitle) {
        pageManager.getCurrent().getElementByTitle(elementTitle).hover();
    }

    @Override
    public void hover(final int index, final String elementTitle) {
        pageManager
                .getCurrent()
                .getElementsList(elementTitle, IElement.class)
                .get(index - 1)
                .hover();
    }

    @Override
    public void clearField(final String elementTitle) {
        final ICleanable element = pageManager.getCurrent().getElement(elementTitle, ICleanable.class);
        element.clean();
    }

    @Override
    public void clearField(final int index, final String elementTitle) {
        final ICleanable element = pageManager
                .getCurrent()
                .getElementsList(elementTitle, ICleanable.class)
                .get(index - 1);
        element.clean();
    }

    @Override
    public void clearFields(final List<String> elementTitlesList) {
        for (String elementTitle : elementTitlesList) {
            clearField(elementTitle);
        }
    }

    @Override
    public void fillField(final String elementTitle, final String value) throws ElementInteractionException {
        clearField(elementTitle);
        final IWritable element = pageManager.getCurrent().getElement(elementTitle, IWritable.class);
        IElementData elementData = (() -> element.write(value));
        elementData.execute();
    }

    @Override
    public void fillField(final int index, final String elementTitle, final String value)
            throws ElementInteractionException {
        clearField(index, elementTitle);
        final IWritable element = pageManager
                .getCurrent()
                .getElementsList(elementTitle, IWritable.class)
                .get(index - 1);
        IElementData elementData = (() -> element.write(value));
        elementData.execute();
    }

    @Override
    public void fillFields(final ResolvableMap data) throws ElementInteractionException {
        for (final String element : data.keySet()) {
            fillField(element, data.get(element));
        }
    }

    @Override
    public void selectValue(final String elementTitle, final String value) throws ElementInteractionException {
        final ISelectable element = pageManager.getCurrent().getElement(elementTitle, ISelectable.class);
        if (element.isFixStateValue()) {
            selectFixStateValue(element, value).execute();
        } else {
            selectStringValue(element, value).execute();
        }
    }

    @Override
    public void selectValue(final int index, final String elementTitle, final String value)
            throws ElementInteractionException {
        final ISelectable element = pageManager
                .getCurrent()
                .getElementsList(elementTitle, ISelectable.class)
                .get(index - 1);
        if (element.isFixStateValue()) {
            selectFixStateValue(element, value).execute();
        } else {
            selectStringValue(element, value).execute();
        }
    }

    @Override
    public void appendField(final String elementTitle, final String value) throws ElementInteractionException {
        final IWritable element = pageManager.getCurrent().getElement(elementTitle, IWritable.class);
        IElementData elementData = (() -> element.append(value));
        elementData.execute();
    }

    @Override
    public void appendField(final int index, final String elementTitle, final String value)
            throws ElementInteractionException {
        final IWritable element = pageManager
                .getCurrent()
                .getElementsList(elementTitle, IWritable.class)
                .get(index - 1);
        IElementData elementData = (() -> element.append(value));
        elementData.execute();
    }

    @Override
    public void appendFields(final ResolvableMap data) throws ElementInteractionException {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            appendField(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void strongVerifyField(final String elementTitle, final String value) throws ElementInteractionException {
        final IVerifiable element = pageManager.getCurrent().getElement(elementTitle, IVerifiable.class);
        IElementData elementData = (() -> verify(element, elementTitle, value, true));
        elementData.execute();
    }

    @Override
    public void strongVerifyField(final int index, final String elementTitle, final String value)
            throws ElementInteractionException {
        final IVerifiable element = pageManager
                .getCurrent()
                .getElementsList(elementTitle, IVerifiable.class)
                .get(index - 1);
        IElementData elementData = (() -> verify(element, elementTitle, value, true));
        elementData.execute();
    }

    @Override
    public void verifyField(final String elementTitle, final String value) throws ElementInteractionException {
        final IVerifiable element = pageManager.getCurrent().getElement(elementTitle, IVerifiable.class);
        IElementData elementData = (() -> verify(element, elementTitle, value, false));
        elementData.execute();
    }

    @Override
    public void verifyField(final int index, final String elementTitle, final String value)
            throws ElementInteractionException {
        final IVerifiable element = pageManager
                .getCurrent()
                .getElementsList(elementTitle, IVerifiable.class)
                .get(index - 1);
        IElementData elementData = (() -> verify(element, elementTitle, value, false));
        elementData.execute();
    }

    @Override
    public void strongVerifyFields(final ResolvableMap data) throws ElementInteractionException {
        SoftAssertions.assertSoftly(softly -> data.forEach((elementTitle, value) -> softly.assertThatCode(() -> {
                    String resolvedValue = Optional.ofNullable(value).orElse("");
                    strongVerifyField(elementTitle, resolvedValue);
                })
                .doesNotThrowAnyException()));
    }

    @Override
    public void verifyFields(final ResolvableMap data) throws ElementInteractionException {
        SoftAssertions.assertSoftly(softly -> data.forEach(
                (elementTitle, value) -> softly.assertThatCode(() -> verifyField(elementTitle, data.get(elementTitle)))
                        .doesNotThrowAnyException()));
    }

    @Override
    public void verifyFields(final String elementTitle, final ResolvableList expectedValues)
            throws ElementInteractionException {
        verifyList(elementTitle, expectedValues, false);
    }

    @Override
    public void strongVerifyFields(final String elementTitle, final ResolvableList expectedValues)
            throws ElementInteractionException {
        verifyList(elementTitle, expectedValues, true);
    }

    @Override
    public void setWindow(final String windowName) {
        driverContainer.get().switchTo().window(windowName);
    }

    @Override
    public void verifyDisplayedElements(final Boolean isDisplayed, final List<String> elementTitlesList) {
        SoftAssertions.assertSoftly(softly -> elementTitlesList.forEach(
                elementTitle -> softly.assertThatCode(() -> verifyDisplayedElement(isDisplayed, elementTitle))
                        .doesNotThrowAnyException()));
    }

    @Override
    public void verifyDisplayedElement(final Boolean isDisplayed, final String elementTitle) {
        final IElement element = pageManager.getCurrent().getElementByTitle(elementTitle);
        element.shouldBe(Condition.visible, isDisplayed);
    }

    @Override
    public void verifyDisplayedElement(final Boolean isDisplayed, final int index, final String elementTitle) {
        final IElement element = pageManager
                .getCurrent()
                .getElementsList(elementTitle, IElement.class)
                .get(index - 1);
        element.shouldBe(Condition.visible, isDisplayed);
    }

    @Override
    public void verifyActiveElement(final String elementTitle, final Boolean isActive) {
        final IElement element = pageManager.getCurrent().getElementByTitle(elementTitle);
        element.shouldBe(Condition.enabled, isActive);
    }

    @Override
    public void verifyEditableElement(final String elementTitle, final Boolean isEditable) {
        final IElement element = pageManager.getCurrent().getElementByTitle(elementTitle);
        element.shouldBe(new Editable(), isEditable);
    }

    @Override
    public void verifyEditableElements(final Boolean isEditable, final List<String> elementTitlesList) {
        SoftAssertions.assertSoftly(softly -> elementTitlesList.forEach(
                elementTitle -> softly.assertThatCode(() -> verifyActiveElement(elementTitle, isEditable))
                        .doesNotThrowAnyException()));
    }

    @Override
    public void verifyActiveElement(final int index, final String elementTitle, final Boolean isActive) {
        final IElement element = pageManager
                .getCurrent()
                .getElementsList(elementTitle, IElement.class)
                .get(index - 1);
        element.shouldBe(Condition.enabled, isActive);
    }

    @Override
    public void verifyActiveElements(final Boolean isActive, final List<String> elementTitlesList) {
        SoftAssertions.assertSoftly(softly -> elementTitlesList.forEach(
                elementTitle -> softly.assertThatCode(() -> verifyActiveElement(elementTitle, isActive))
                        .doesNotThrowAnyException()));
    }

    @Override
    public void equalsNumbersOfElements(final String elementTitle, final Integer numbers) {
        int actual =
                pageManager.getCurrent().getElementsListByTitle(elementTitle).size();
        assertThat(
                actual == numbers,
                "The number of elements in the list {} don't match expected value:\n"
                        + " expected : {} \n"
                        + " actual : {}",
                elementTitle,
                numbers,
                actual);
    }

    @Override
    public void readValues(final ResolvableMap data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            readValue(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void readValue(final String variableName, final String elementTitle) {
        final IReadable element = pageManager.getCurrent().getElement(elementTitle, IReadable.class);
        String value = element.readValue();
        context.set(variableName, value);
    }

    @Override
    public void pressOnKeyBoard(final String keysCombination) {
        driverContainer.getActiveDriver().pressOnKeyBoard(keysCombination);
    }

    @Override
    public void takeScreenshotAndSave(final String path) {
        try (FileInputStream inputStream =
                new FileInputStream(driverContainer.getActiveDriver().takeScreenshot())) {
            fileLoader.createFileInAnyDir(path, inputStream);
        } catch (IOException e) {
            throw new ExecutionException("Error creating the screenshot.", e);
        }
    }

    @Override
    public void compareCurrentWindowWithScreenshot(final String resultPath, final String path) {
        new ImageComparison(
                        path, driverContainer.getActiveDriver().takeScreenshot().getAbsolutePath())
                .setPixelToleranceLevel(pixelToleranceLevel)
                .setAllowingPercentOfDifferentPixels(allowingPercentOfDifferentPixels)
                .compareImages()
                .writeResultTo(new File(resultPath));
    }

    @Override
    public void compareScreenshots(final String resultPath, final String path1, final String path2) {
        new ImageComparison(path1, path2)
                .setPixelToleranceLevel(pixelToleranceLevel)
                .setAllowingPercentOfDifferentPixels(allowingPercentOfDifferentPixels)
                .compareImages()
                .writeResultTo(new File(resultPath));
    }

    @Override
    public void hasAttribute(final String elementTitle, final String attribute) {
        pageManager.getCurrent().getElementByTitle(elementTitle).hasAttribute(attribute);
    }

    @Override
    public void checkAttributes(String elementTitle, List<Triple> rows) {
        SoftAssertions.assertSoftly(softly -> rows.forEach(row -> {
            var attributeName = row.getFirst();
            var matcherSymbol = row.getSecond();
            pageManager.getCurrent().getElementByTitle(elementTitle).hasAttribute(attributeName);
            var actualValue =
                    pageManager.getCurrent().getElementByTitle(elementTitle).getAttribute(attributeName);
            var expectedValue = row.getThird();
            var matcherName = MatcherName.getBy(matcherSymbol);

            var matcher = MatcherParser.getMatcher(matcherName, expectedValue);
            softly.assertThatCode(() -> {
                        checkThat(matcherName.isStringMatcher(), "Only String matchers" + " are supported currently");
                        assertThat(
                                actualValue,
                                matcher,
                                "Value of attribute '{}': '{}' doesn't match the expected `{} {}`",
                                attributeName,
                                actualValue,
                                matcherSymbol,
                                expectedValue);
                    })
                    .doesNotThrowAnyException();
        }));
    }

    private void verifyList(final String elementTitle, final ResolvableList expectedValues, boolean equals) {
        final List<String> actualFieldValues = new ArrayList<>();
        pageManager
                .getCurrent()
                .getElementsList(elementTitle, IReadable.class)
                .forEach(iElement -> actualFieldValues.add(iElement.readValue()));
        assertThat(
                actualFieldValues.containsAll(expectedValues)
                        && (!equals || expectedValues.containsAll(actualFieldValues)),
                "The list of elements {} don't match expected value:\n" + " expected : {} \n" + " actual : {}",
                elementTitle,
                expectedValues,
                actualFieldValues);
    }

    private void verify(
            final IVerifiable element, final String elementTitle, final String value, final Boolean strongVerify) {
        var verifier = selectVerify(element, value, strongVerify);
        assertThat(
                verifier.isCorrect(),
                "Field '{}' actual value is '{}' but expected '{}'",
                elementTitle,
                verifier.toString(),
                value);
    }

    private Verifier selectVerify(final IVerifiable element, final String expected, final boolean fullCheck) {
        return element.verify(expected, fullCheck);
    }

    private IElementData selectFixStateValue(final ISelectable element, final String value) {
        return (() -> element.select(FixState.determine(value)));
    }

    private IElementData selectStringValue(final ISelectable element, final String value) {
        return (() -> element.select(value));
    }
}
