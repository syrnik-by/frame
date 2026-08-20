package ru.autotestframework.desktop_elements.elements;

import com.codeborne.selenide.ElementsCollection;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;

public class Tree extends TypifiedDesktopElement {

    public static final String TREE_ITEM_XPATH = "./TreeItem";

    public Tree(final WebElement wrappedElement) {
        super(wrappedElement, WebElementExtensions.NO_TITLE);
    }

    public Tree(WebElement wrappedElement, String title) {
        super(wrappedElement, title);
    }

    /**
     * The currently selected "TreeItem".
     *
     * @return selected tree item
     */
    public ElementsCollection selenideItems() {
        return getSelenideElement().$$(By.xpath(TREE_ITEM_XPATH));
    }

    /**
     * returns first selected TreeItem
     * @return
     */
    public TreeItem selectedTreeItem() {
        return selenideItems().stream()
                .map(TreeItem::new)
                .filter(TypifiedDesktopElement::isSelected)
                .findFirst()
                .orElseThrow(() -> new ElementInteractionException("No item selected"));
    }

    /**
     * All child "TreeItem" objects from this "Tree".
     *
     * @return list of child tree items
     */
    public List<TreeItem> items() {
        return selenideItems().stream().map(TreeItem::new).collect(Collectors.toList());
    }
}
