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
import ru.autotestframework.desktop_elements.enums.ExpandCollapseState;
import ru.autotestframework.ui_core.typified_elements.Verifier;
import ru.autotestframework.ui_core.typified_elements.ifaces.IAccessible;
import ru.autotestframework.ui_core.typified_elements.ifaces.IMultipleValueVerifiable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IMultipleValueWritable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IMutlipleValueReadable;
import ru.autotestframework.util.Validator;

@Slf4j
public class ComboBox extends TypifiedDesktopElement
        implements IMultipleValueWritable, IMutlipleValueReadable, IMultipleValueVerifiable, IAccessible {

    public static final String LIST_LIST_ITEM_XPATH = "./List/ListItem";
    private static final String COMBO_BOX_COLLAPSE = "comboBoxCollapse";
    private static final String COMBO_BOX_IS_EDITABLE = "comboBoxIsEditable";
    private static final String COMBO_BOX_IS_READ_ONLY = "comboBoxIsReadOnly";
    private static final String COMBO_BOX_SELECTED_ITEM = "comboBoxSelectedItem";
    private static final String COMBO_BOX_EXPAND_COLLAPSE_STATE = "comboBoxExpandCollapseState";
    private static final String COMBO_BOX_EDITABLE_TEXT = "comboBoxEditableText";
    private static final String COMBO_BOX_ITEM_XPATH = "./ComboBoxItem";

    public ComboBox(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }
    /**
     * Collapses the element.
     */
    public void collapse() {
        callVoidCommand(COMBO_BOX_COLLAPSE);
    }

    /**
     * Expands the element.
     */
    public void expand() {
        click();
    }

    /**
     * returns combobox elements as selenide elements collection
     * @return
     */
    public ElementsCollection selenideListItems() {
        return getSelenideElement().$$(By.xpath(LIST_LIST_ITEM_XPATH));
    }

    /**
     * Select the first item which matches the given text.
     *
     * @param value The text to search for.
     */
    public void select(final String value) {
        expand();
        ElementsCollection allItems = selenideListItems().shouldBe(CollectionCondition.sizeGreaterThan(0));

        ElementsCollection foundOptions = allItems.filter(Condition.attribute("Name", value));

        if (foundOptions.isEmpty()) {
            log.warn("Element '{}' doesn't contains exact value '{}', trying to find with regex", getTitle(), value);
            foundOptions = allItems.filter(Condition.attributeMatching("Name", ".*" + value + ".*"));
        }
        Validator.assertThat(
                foundOptions.size() == 1,
                "Element '{}' has not exactly one option of value '{}', options counted: {}",
                getTitle(),
                value,
                foundOptions.size());

        foundOptions.get(0).click();
    }

    /**
     * Select an item by index.
     *
     * @param index The index to search for.
     */
    public void select(final int index) {
        expand();
        selenideListItems()
                .shouldBe(CollectionCondition.sizeGreaterThan(index))
                .get(index)
                .click();
        // TODO two different elements should be presented(to work with core steps) //selectComboBox //selectListBox
    }

    /**
     * Set the text of the editable element inside the combobox.
     * Only works if the combobox is editable.
     *
     * @param value value
     */
    public void setEditableText(final String value) {
        getSelenideElement().sendKeys(value);
    }

    /**
     * Flag which indicates, if the combobox is editable or not.
     *
     * @return editable status of the combobox
     */
    public boolean isEditable() {
        var response = callVoidCommand(COMBO_BOX_IS_EDITABLE);
        var res = response.getValue().toString();
        return Boolean.parseBoolean(res) && isEnabled();
    }

    /**
     * Flag which indicates, if the combobox is read-only or not.
     *
     * @return readable status of the combobox
     */
    public boolean isReadOnly() {
        var response = callVoidCommand(COMBO_BOX_IS_READ_ONLY);
        return Boolean.parseBoolean(response.getValue().toString());
    }

    /**
     * @return Selected value.
     */
    public String value() {
        return getText();
    }

    /**
     * returns ComboBox element's text
     * @return
     */
    @Override
    public String getText() {
        return getSelenideElement().getText();
    }

    /**
     * returns ComboBox value
     * @return
     */
    @Override
    public String readValue() {
        return value();
    }

    /**
     * returns selected combobox values
     * @return
     */
    @Override
    public Collection<String> readMultipleValues() {
        return selectedItems().stream().map(x -> x.getAttribute("Name")).collect(Collectors.toList());
    }

    /**
     * Gets the first selected item or null otherwise.
     *
     * @return ComboBoxItem which was selected
     */
    public ComboBoxItem selectedItem() {
        var response = callVoidCommand(COMBO_BOX_SELECTED_ITEM);
        if (response == null) {
            return null;
        }
        return new ComboBoxItem(createWebElementFromResponse(response));
    }

    /**
     * Gets all selected items.
     *
     * @return all ComboBoxItems which were selected in combobox
     */
    public List<ComboBoxItem> selectedItems() {
        expand();
        List<ComboBoxItem> comboBoxItems = selenideListItems().stream()
                .filter(WebElement::isSelected)
                .map(ComboBoxItem::new)
                .collect(Collectors.toList());
        collapse();
        return comboBoxItems;
    }

    /**
     * returns available combobox items
     * @return
     */
    public List<ListBoxItem> getListBoxItems() {
        expand();
        List<ListBoxItem> listBoxItems = listBoxItems();
        collapse();
        return listBoxItems;
    }

    private List<ListBoxItem> listBoxItems() {
        return selenideListItems().stream().map(ListBoxItem::new).collect(Collectors.toList());
    }

    private List<ComboBoxItem> comboBoxItems() {
        return getSelenideElement().$$(By.xpath(COMBO_BOX_ITEM_XPATH)).stream()
                .map(ComboBoxItem::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets all items.
     *
     * @return all ComboBoxItems which can be selected in combobox
     */
    public List<ComboBoxItem> items() {
        expand();
        List<ComboBoxItem> comboBoxItems = comboBoxItems();
        collapse();
        return comboBoxItems;
    }

    /**
     * Gets the ExpandCollapseState of the element.
     *
     * @return enum which specifies whether element can be expanded
     */
    public ExpandCollapseState expandCollapseState() {
        var response = callVoidCommand(COMBO_BOX_EXPAND_COLLAPSE_STATE);
        return ExpandCollapseState.getEnum(response.getValue().toString());
    }

    /**
     * The text of the editable element inside the combobox.
     * Only works if the combobox is editable.
     *
     * @return text of the editable combobox element
     */
    public String editableText() {
        var response = callVoidCommand(COMBO_BOX_EDITABLE_TEXT);
        return response.getValue().toString();
    }

    /**
     * checks if element is accessed
     * @return
     */
    @Override
    public boolean isAccessed() {
        return !isReadOnly();
    }

    /**
     * checks if element is visible
     * @return
     */
    @Override
    public boolean isVisible() {
        return isDisplayed();
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
     * selects multiple combobox values
     * @param values
     */
    @Override
    public void writeMultiple(final Collection<String> values) {
        values.forEach(value -> {
            try {
                var itemNumber = Integer.parseInt(value);
                select(itemNumber);
            } catch (NumberFormatException e) {
                select(value);
            }
        });
    }

    /**
     * Verifies ComboBox
     * @param expected  список ожидаемых значений для проверки
     * @param fullCheck true если требуется выполнить полную сверку всех значений, false - проверить только на вхождение
     * @return
     */
    @Override
    public Verifier verifyMultiple(final Collection<String> expected, final boolean fullCheck) {
        return Verifier.of(this, fullCheck, expected);
    }
}
