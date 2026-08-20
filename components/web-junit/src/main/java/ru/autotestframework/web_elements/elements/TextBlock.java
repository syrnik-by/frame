package ru.autotestframework.web_elements.elements;

import org.openqa.selenium.WebElement;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.ifaces.IReadable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IVerifiable;
import ru.autotestframework.web_elements.elements.typified.TypifiedWebElement;

/**
 * Универсальный веб-элемент, который можно использовать для любых случаев, для которых
 * не подходят прочие специфичные элементы. Предоставляет возможность прочитать данные из элемента
 * и проверить их на соответствие ожидаемым
 */
public class TextBlock extends TypifiedWebElement implements IReadable, IVerifiable {
    public TextBlock(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    @Override
    public boolean isFixStateValue() {
        return false;
    }

    /**
     * returns value of TextBlock element
     * @return
     */
    @Override
    public String readValue() {
        return getSelenideElement().getText();
    }

    /**
     * verifies element's value
     * @param expected ожидаемое значение для проверки
     * @return
     */
    @Override
    public Verifier verify(final String expected) {
        return Verifier.of(this, expected);
    }
}
