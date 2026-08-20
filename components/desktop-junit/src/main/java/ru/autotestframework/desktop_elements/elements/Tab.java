package ru.autotestframework.desktop_elements.elements;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.util.Validator;

@Slf4j
public class Tab extends TypifiedDesktopElement {

    private static final String TAB_SELECTED_TAB_ITEM_INDEX = "tabSelectedTabItemIndex";
    private static final String TAB_ITEM_XPATH = "./TabItem";

    public Tab(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    /**
     * The currently selected TabItem.
     *
     * @return selected tab item
     */
    public ElementsCollection selenideTabItems() {
        return getSelenideElement().$$(By.xpath(TAB_ITEM_XPATH));
    }

    /**
     * returns first selected TabItem
     * @return
     */
    public TabItem selectedTabItem() {
        return selenideTabItems().filter(Condition.selected).stream()
                .map(TabItem::new)
                .findFirst()
                .orElseThrow(() -> new ElementInteractionException("No element selected"));
    }

    /**
     * The index of the currently selected TabItem.
     *
     * @return index of the selected tab item
     */
    public int selectedTabItemIndex() {
        var response = callVoidCommand(TAB_SELECTED_TAB_ITEM_INDEX);
        return Integer.parseInt(response.getValue().toString());
    }

    /**
     * All TabItem objects from this Tab.
     *
     * @return list of all tab items
     */
    public List<TabItem> tabItems() {
        return selenideTabItems().shouldBe(CollectionCondition.sizeGreaterThan(0)).stream()
                .map(TabItem::new)
                .collect(Collectors.toList());
    }

    /**
     * Selects a TabItem by index.
     *
     * @param index specified index
     * @return item with specified index
     */
    public TabItem selectTabItem(final int index) {
        TabItem item = selenideTabItems().shouldBe(CollectionCondition.sizeGreaterThan(index)).stream()
                .map(TabItem::new)
                .collect(Collectors.toList())
                .get(index);
        item.click();
        return item;
    }

    /**
     * Selects a TabItem by a given text (name property).
     *
     * @param text specified text
     * @return item with specified text
     */
    public TabItem selectTabItem(final String text) {
        ElementsCollection allItems = selenideTabItems().shouldBe(CollectionCondition.sizeGreaterThan(0));
        ElementsCollection foundOptions = allItems.filter(Condition.attribute("Name", text));
        if (foundOptions.size() == 0) {
            log.warn("Element '{}' doesn't contains exact value '{}', trying to find with contains", getTitle(), text);
            foundOptions = allItems.filter(Condition.attributeMatching("Name", ".*" + text + ".*"));
        }
        Validator.assertThat(
                foundOptions.size() == 1,
                "Element '{}' has not exactly one option of value '{}', options count : {}",
                getTitle(),
                text,
                foundOptions.size());

        foundOptions.get(0).click();
        return foundOptions.stream()
                .map(TabItem::new)
                .collect(Collectors.toList())
                .get(0);
    }
}
