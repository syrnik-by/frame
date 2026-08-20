package ru.autotestframework.desktop_elements.elements;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.ifaces.IMultipleValueVerifiable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IMultipleValueWritable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IMutlipleValueReadable;
import ru.autotestframework.util.Validator;

@Slf4j
public class ListBox extends TypifiedDesktopElement
        implements IMutlipleValueReadable, IMultipleValueWritable, IMultipleValueVerifiable {

    public static final String LIST_ITEM_XPATH = "./ListItem";
    private static final String LIST_BOX_SELECTED_ITEM = "listBoxSelectedItem";
    private static final String LIST_BOX_REMOVE_FROM_SELECTION_TEXT = "listBoxRemoveFromSelectionText";
    public static final String ELEMENT_DOESN_T_CONTAINS_EXACT_VALUE_TRYING_TO_FIND_WITH_CONTAINS =
            "Element '{}' doesn't contains exact value '{}', trying to find with contains";
    public static final String ELEMENT_HAS_NOT_EXACTLY_ONE_OPTION_OF_VALUE_OPTIONS_COUNT =
            "Element '{}' has not exactly one option of value '{}', options count : {}";

    public ListBox(final WebElement wrappedElement) {
        super(wrappedElement, "no title");
    }

    public ListBox(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    /**
     * Returns all the list box items.
     *
     * @return list of all listbox items
     */
    public List<ListBoxItem> items() {
        return selenideItems().stream().map(ListBoxItem::new).collect(Collectors.toList());
    }

    /**
     * returns ListBox elements as selenide elements collection
     * @return
     */
    public ElementsCollection selenideItems() {
        return getSelenideElement().$$(By.xpath(LIST_ITEM_XPATH));
    }

    /**
     * Gets all selected items.
     *
     * @return list of all selected items
     */
    public List<ListBoxItem> selectedItems() {
        return items().stream().filter(ListBoxItem::isChecked).collect(Collectors.toList());
    }

    /**
     * Gets the first selected item or null otherwise.
     *
     * @return list with first selected item
     */
    public ListBoxItem selectedItem() {
        var response = callVoidCommand(LIST_BOX_SELECTED_ITEM);
        if (response == null) {
            return null;
        }
        return new ListBoxItem(createWebElementFromResponse(response));
    }

    /**
     * Selects an item by index.
     *
     * @param index specified index
     * @return item with specified index
     */
    public ListBoxItem select(final int index) {
        var item = new ListBoxItem(selenideItems()
                .shouldBe(CollectionCondition.sizeGreaterThan(index))
                .get(index));
        return item;
    }

    /**
     * Find a ListBoxItem item by text without select.
     *
     * @param value specified text
     * @return item with specified text
     */
    public ListBoxItem getItem(final String value) {

        ElementsCollection allItems = selenideItems().shouldBe(CollectionCondition.sizeGreaterThan(0));
        ElementsCollection foundOptions = allItems.filter(Condition.attribute("Name", value));

        if (foundOptions.size() == 0) {
            log.warn(ELEMENT_DOESN_T_CONTAINS_EXACT_VALUE_TRYING_TO_FIND_WITH_CONTAINS, getTitle(), value);
            foundOptions = allItems.filter(Condition.attributeMatching("Name", ".*" + value + ".*"));
        }
        Validator.assertThat(
                foundOptions.size() == 1,
                ELEMENT_HAS_NOT_EXACTLY_ONE_OPTION_OF_VALUE_OPTIONS_COUNT,
                getTitle(),
                value,
                foundOptions.size());

        return foundOptions.stream()
                .map(ListBoxItem::new)
                .collect(Collectors.toList())
                .get(0);
    }

    /**
     * Selects an item by text.
     *
     * @param value specified text
     * @return item with specified text
     */
    public ListBoxItem select(final String value) {
        ElementsCollection allItems = selenideItems().shouldBe(CollectionCondition.sizeGreaterThan(0));
        ElementsCollection foundOptions = allItems.filter(Condition.attribute("Name", value));
        if (foundOptions.size() == 0) {
            log.warn(ELEMENT_DOESN_T_CONTAINS_EXACT_VALUE_TRYING_TO_FIND_WITH_CONTAINS, getTitle(), value);
            foundOptions = allItems.filter(Condition.attributeMatching("Name", ".*" + value + ".*"));
        }
        Validator.assertThat(
                foundOptions.size() == 1,
                ELEMENT_HAS_NOT_EXACTLY_ONE_OPTION_OF_VALUE_OPTIONS_COUNT,
                getTitle(),
                value,
                foundOptions.size());

        foundOptions.get(0).click();

        return foundOptions.stream()
                .map(ListBoxItem::new)
                .collect(Collectors.toList())
                .get(0);
    }

    /**
     * Add a row to the selection by index.
     *
     * @param index specified index
     * @return item with specified index
     */
    @Deprecated
    public ListBoxItem addToSelection(final int index) {
        ListBoxItem item = selenideItems().shouldBe(CollectionCondition.sizeGreaterThan(index)).stream()
                .map(ListBoxItem::new)
                .collect(Collectors.toList())
                .get(index);
        item.click();

        return item;
    }

    /**
     * checks if element is editable
     * @return
     */
    @Override
    public boolean isEditable() {
        return isEnabled();
    }

    /**
     * Add a row to the selection by text.
     *
     * @param value specified text
     * @return item with specified text
     *
     * Deprecated Use {@link #select(String)} instead
     */
    @Deprecated
    public ListBoxItem addToSelection(final String value) {
        ElementsCollection allItems = selenideItems().shouldBe(CollectionCondition.sizeGreaterThan(0));
        ElementsCollection foundOptions = allItems.filter(Condition.attribute("Name", value));
        if (foundOptions.size() == 0) {
            log.warn(ELEMENT_DOESN_T_CONTAINS_EXACT_VALUE_TRYING_TO_FIND_WITH_CONTAINS, getTitle(), value);
            foundOptions = allItems.filter(Condition.attributeMatching("Name", ".*" + value + ".*"));
        }
        Validator.assertThat(
                foundOptions.size() == 1,
                ELEMENT_HAS_NOT_EXACTLY_ONE_OPTION_OF_VALUE_OPTIONS_COUNT,
                getTitle(),
                value,
                foundOptions.size());

        foundOptions.get(0).click();

        return foundOptions.stream()
                .map(ListBoxItem::new)
                .collect(Collectors.toList())
                .get(0);
    }

    /**
     * Remove a row from the selection by index.
     *
     * @param index specified index
     * @return removed item with specified index
     */
    public ListBoxItem removeFromSelection(final int index) {
        ListBoxItem item = selenideItems().shouldBe(CollectionCondition.sizeGreaterThan(index)).stream()
                .map(ListBoxItem::new)
                .collect(Collectors.toList())
                .get(index);
        item.click();
        // TODO Check if selected in all occurancies
        return item;
    }

    /**
     * Add a row to the selection by text.
     *
     * @param text specified text
     * @return item with specified text
     *
     * Deprecated Use {@link #select(String)} instead
     */
    @Deprecated
    public ListBoxItem removeFromSelection(final String text) {
        var response = callValueCommand(LIST_BOX_REMOVE_FROM_SELECTION_TEXT, text);
        if (response == null) {
            return null;
        }
        return new ListBoxItem(createWebElementFromResponse(response));
    }

    @Override
    public boolean isMultipleValues() {
        return true;
    }

    @Override
    public boolean isFixStateValue() {
        return false;
    }

    /**
     * returns all ListBox's selected item's values
     * @return
     */
    @Override
    public Collection<String> readMultipleValues() {
        return selectedItems().stream().map(x -> x.getAttribute("Name")).collect(Collectors.toList());
    }

    /**
     * selects multiple ListBox values
     * @param values
     * @throws ElementInteractionException
     */
    @Override
    public void writeMultiple(final Collection<String> values) throws ElementInteractionException {
        values.forEach(value -> {
            try {
                var itemNumber = Integer.parseInt(value);
                addToSelection(itemNumber);
            } catch (NumberFormatException e) {
                addToSelection(value);
            }
        });
    }

    /**
     * verifies ListBox
     * @param expected  список ожидаемых значений для проверки
     * @param fullCheck true если требуется выполнить полную сверку всех значений, false - проверить только на вхождение
     * @return
     */
    @Override
    public Verifier verifyMultiple(final Collection<String> expected, final boolean fullCheck) {
        return Verifier.of(this, fullCheck, expected);
    }
}
