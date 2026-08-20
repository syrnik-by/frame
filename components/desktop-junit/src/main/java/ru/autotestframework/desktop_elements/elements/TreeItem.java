package ru.autotestframework.desktop_elements.elements;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;
import ru.autotestframework.desktop_elements.enums.ExpandCollapseState;

public class TreeItem extends TypifiedDesktopElement {

    public static final String TREE_ITEM_EXPAND_COLLAPSE_STATE = "treeItemExpandCollapseState";
    public static final String TREE_ITEM_EXPAND = "treeItemExpand";
    public static final String TREE_ITEM_COLLAPSE = "treeItemCollapse";
    public static final String TREE_ITEM_SELECT = "treeItemSelect";
    public static final String TREE_ITEM_ADD_TO_SELECTION = "treeItemAddToSelection";
    public static final String TREE_ITEM_REMOVE_FROM_SELECTION = "treeItemRemoveFromSelection";
    public static final String TREE_ITEM_IS_CHECKED = "treeItemIsChecked";
    public static final String TREE_ITEM_XPATH = "./TreeItem";

    public TreeItem(final WebElement wrappedElement) {
        super(wrappedElement, WebElementExtensions.NO_TITLE);
    }

    /**
     * All child "TreeItem" objects from this"TreeItem".
     *
     * @return list of child tree items
     */
    public List<TreeItem> items() {
        return getSelenideElement().$$(By.xpath(TREE_ITEM_XPATH)).stream()
                .map(TreeItem::new)
                .collect(Collectors.toList());
    }

    /**
     * The text of the "TreeItem".
     *
     * @return text of the tree item
     */
    @Override
    public String getText() {
        return getSelenideElement().getAttribute("Name");
    }

    /**
     * Gets the current expand / collapse state.
     *
     * @return expand-collapse state of the element
     */
    public ExpandCollapseState expandCollapseState() {
        var response = callVoidCommand(TREE_ITEM_EXPAND_COLLAPSE_STATE);
        return ExpandCollapseState.getEnum(response.getValue().toString());
    }

    /**
     * Expands the element.
     */
    public void expand() {
        callVoidCommand(TREE_ITEM_EXPAND);
    }

    /**
     * Collapses the element.
     */
    public void collapse() {
        callVoidCommand(TREE_ITEM_COLLAPSE);
    }

    /**
     * Selects the element.
     */
    public void select() {
        callVoidCommand(TREE_ITEM_SELECT);
    }

    /**
     * Add the element to the selection.
     *
     * @return tree element that was added
     */
    public TreeItem addToSelection() {
        var response = callVoidCommand(TREE_ITEM_ADD_TO_SELECTION);
        if (response == null) {
            return null;
        }
        return new TreeItem(createWebElementFromResponse(response));
    }

    /**
     * Remove the element from the selection.
     *
     * @return removed tree item element
     */
    public TreeItem removeFromSelection() {
        var response = callVoidCommand(TREE_ITEM_REMOVE_FROM_SELECTION);
        if (response == null) {
            return null;
        }
        return new TreeItem(createWebElementFromResponse(response));
    }

    /**
     * Gets if the tree item is checked, if checking is supported.
     *
     * @return check status of the tree item
     */
    public boolean isChecked() {
        var response = callVoidCommand(TREE_ITEM_IS_CHECKED);
        return Boolean.parseBoolean(response.getValue().toString());
    }

    /**
     * Sets if the tree item is checked, if checking is supported.
     *
     * @param checked specified status that should be set to the tree element
     */
    public void setChecked(final boolean checked) {
        if (isChecked() != checked) {
            getSelenideElement().click();
        }
    }
}
