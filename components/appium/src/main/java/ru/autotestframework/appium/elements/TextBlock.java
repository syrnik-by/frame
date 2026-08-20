package ru.autotestframework.appium.elements;

import org.openqa.selenium.WebElement;
import ru.autotestframework.appium.elements.typified.TypifiedMobileElement;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IVerifiable;

/**
 * Универсальный веб-элемент, который можно использовать для любых случаев, для которых
 * не подходят прочие специфичные элементы. Предоставляет возможность прочитать данные из элемента
 * и проверить их на соответствие ожидаемым
 */
public class TextBlock extends TypifiedMobileElement implements IReadable, IVerifiable {
    public TextBlock(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    @Override
    public boolean isFixStateValue() {
        return false;
    }

    /**
     * returns TextBlock value
     * @return
     */
    @Override
    public String readValue() {
        return getSelenideElement().getText();
    }

    /**
     * verifies TextBlock
     * @param expected value to verifie
     * @return
     */
    @Override
    public Verifier verify(final String expected) {
        return Verifier.of(this, expected);
    }
}
