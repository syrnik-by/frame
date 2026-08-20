package ru.autotestframework.ui_core.typified_elements;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import java.lang.annotation.Annotation;
import java.time.Duration;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.Locatable;
import ru.autotestframework.util.Validator;

/**
 * Element.
 */
public interface IElement extends WebElement, WrapsElement, Locatable, TakesScreenshot {
    /**
     * Gets title.
     *
     * @return the title
     */
    String getTitle();

    /**
     * Double click.
     */
    void doubleClick();

    /**
     * Right click.
     */
    void rightClick();

    /**
     * Hover.
     */
    void hover();

    /**
     * Has attribute.
     *
     * @param attribute the attribute
     */
    void hasAttribute(String attribute);

    /**
     * Sets annotations.
     *
     * @param annotations the annotations
     */
    void setAnnotations(Annotation[] annotations);

    /**
     * Should be.
     *
     * @param condition   the condition
     * @param notReversed the not reversed
     * @param time        the time
     */
    default void shouldBe(Condition condition, boolean notReversed, Duration time) {
        Condition conditionToCheck = notReversed ? condition : condition.negate();
        Validator.tryOrAssertion(
                () -> $(this).shouldBe(conditionToCheck, time),
                "Element '{}' doesn't match the expected {} status (expected '{}')",
                getTitle(),
                conditionToCheck.getName(),
                notReversed);
    }

    /**
     * Should be.
     *
     * @param condition   the condition
     * @param notReversed the not reversed
     */
    default void shouldBe(Condition condition, boolean notReversed) {
        Condition conditionToCheck = notReversed ? condition : condition.negate();
        Validator.tryOrAssertion(
                () -> $(this).shouldBe(conditionToCheck),
                "Element '{}' doesn't match the expected {} status (expected '{}')",
                getTitle(),
                conditionToCheck.getName(),
                notReversed);
    }
}
