package ru.autotestframework.ui_core.conditions;

import com.codeborne.selenide.collections.ExactTextsCaseSensitiveInAnyOrder;
import com.codeborne.selenide.impl.Html;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.WebElement;

/**
 * Exact text contents in any order.
 */
public class ExactTextContentsInAnyOrder extends ExactTextsCaseSensitiveInAnyOrder {
    /**
     * Instantiates a new Exact text contents in any order.
     *
     * @param exactTexts the exact texts
     */
    public ExactTextContentsInAnyOrder(List<String> exactTexts) {
        super(exactTexts);
    }

    @Override
    public boolean test(List<WebElement> elements) {
        if (elements.size() == this.expectedTexts.size()) {
            List<String> elementsTexts = (List)
                    elements.stream().map(x -> x.getAttribute("textContent")).collect(Collectors.toList());
            Iterator var3 = this.expectedTexts.iterator();

            boolean found;
            do {
                if (!var3.hasNext()) {
                    return true;
                }

                String expectedText = (String) var3.next();
                found = false;
                Iterator var6 = elementsTexts.iterator();

                while (var6.hasNext()) {
                    String elementText = (String) var6.next();
                    if (Html.text.equalsCaseSensitive(elementText, expectedText)) {
                        found = true;
                    }
                }
            } while (found);
        }
        return false;
    }
}
