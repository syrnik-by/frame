package ru.autotestframework.pages.local;

import org.openqa.selenium.support.FindBy;
import ru.autotestframework.ui_core.page_manager.AbstractPage;
import ru.autotestframework.ui_core.page_manager.Element;
import ru.autotestframework.ui_core.page_manager.PageEntry;
import ru.autotestframework.web_elements.elements.TextBlock;

@PageEntry(title = "Drag and Drop")
public class DragAndDrop extends AbstractPage {

    @Element("Drag left")
    @FindBy(xpath = "//*[@id='draggable1']")
    public TextBlock draggableLeft;

    @Element("Drag right")
    @FindBy(xpath = "//*[@id='draggable2']")
    public TextBlock draggableRight;

    @Element("Drop left")
    @FindBy(xpath = "//*[@id='droppable1']")
    public TextBlock droppableLeft;

    @Element("Drop right")
    @FindBy(xpath = "//*[@id='droppable2']")
    public TextBlock droppableRight;
}
