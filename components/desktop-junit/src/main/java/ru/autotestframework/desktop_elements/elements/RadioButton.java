package ru.autotestframework.desktop_elements.elements;

import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.enums.FixState;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IVerifiable;

/**
 * Радиокнопка не подразумевает возможности установки нужного состояния,
 * например её нельзя выключить, кроме как кликнуть в другую радиокнопку.
 * Поэтому для включения радиокнопки по ней нужно кликнуть.
 */
public class RadioButton extends TypifiedDesktopElement implements IReadable, IVerifiable {

    public RadioButton(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    @Override
    public boolean isFixStateValue() {
        return true;
    }

    /**
     * returns RadioButton fix state
     * @return
     */
    @Override
    public FixState readState() {
        return isSelected() ? FixState.ON : FixState.OFF;
    }

    /**
     * verifies RadioButton's fix state
     * @param expected
     * @return
     */
    @Override
    public Verifier verifyFixState(final FixState expected) {
        return Verifier.of(this, expected);
    }
}
