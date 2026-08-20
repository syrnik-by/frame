package ru.autotestframework.appium.elements;

import com.codeborne.selenide.SelenideElement;
import java.util.Optional;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.autotestframework.appium.elements.typified.TypifiedMobileElement;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.enums.FixState;
import ru.autotestframework.ui_core.typified_elements.ifaces.IAccessible;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.ISelectable;

public class ClassicCheckBox extends TypifiedMobileElement implements IReadable, ISelectable, IAccessible {
    private static final String OVERRIDE_MESSAGE =
            "Default CheckBox '{}' logic not applicable, extend this Class and override appropriate methods";

    public ClassicCheckBox(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    @Override
    public boolean isFixStateValue() {
        return true;
    }

    /**
     * returns ClassicCheckBox fix state
     * @return
     */
    @Override
    public FixState readState() {
        return FixState.determine(
                Optional.ofNullable(getInput().getAttribute("checked")).orElse("false"));
    }

    /**
     * verifies ClassicCheckBox fix state
     * @param expected
     * @return
     */
    @Override
    public Verifier verifyFixState(final FixState expected) {
        return Verifier.of(this, expected);
    }

    /**
     * selects ClassicCheckBox fix state
     * @param value
     */
    @Override
    public void select(final FixState value) {
        final WebElement input = getInput();
        FixState current = readState();
        int counter = FixState.values().length - 1;
        while (value != current && counter > 0) {
            input.click();
            counter--;
            current = readState();
        }
        if (value != current) {
            throw new ElementInteractionException(OVERRIDE_MESSAGE, getClass().getSimpleName());
        }
    }

    private SelenideElement getInput() {
        return getSelenideElement().$(By.xpath("descendant-or-self::input[@type='checkbox']"));
    }

    /**
     * checks if element is accessed
     * @return
     */
    @Override
    public boolean isAccessed() {
        return getInput().isEnabled();
    }

    /**
     * checks if element is visible
     * @return
     */
    @Override
    public boolean isVisible() {
        return getInput().isDisplayed();
    }
}
