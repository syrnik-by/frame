package ru.autotestframework.desktop_elements.desktop_driver;

import com.google.common.base.Throwables;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.CommandInfo;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.http.HttpMethod;

/**
 * {@link CommandExecutor} that understands DesktopDriver specific commands.
 */
public class DesktopDriverCommandExecutor extends HttpCommandExecutor {
    private static final Map<String, CommandInfo> DESKTOP_DRIVER_COMMAND_NAME_TO_URL;
    private final DesktopDriverService service;

    private static final String PATH = "/session/:sessionId/";

    static {
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL = new HashMap<String, CommandInfo>();
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put("executeInApp", new CommandInfo(PATH + "executeInApp", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "elementAttribute", new CommandInfo(PATH + "element/:id/attribute/:value", HttpMethod.GET));
        // region ComboBox
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "comboBoxCollapse", new CommandInfo(PATH + "element/:id/combobox/collapse", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "comboBoxExpand", new CommandInfo(PATH + "element/:id/combobox/expand", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "comboBoxSelect", new CommandInfo(PATH + "element/:id/combobox/select/:value", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "comboBoxSelectIndex",
                new CommandInfo(PATH + "element/:id/combobox/selectIndex/:value", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "comboBoxSetEditableText",
                new CommandInfo(PATH + "element/:id/combobox/setEditableText/:value", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "comboBoxIsEditable", new CommandInfo(PATH + "element/:id/combobox/isEditable", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "comboBoxIsReadOnly", new CommandInfo(PATH + "element/:id/combobox/isReadonly", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "comboBoxValue", new CommandInfo(PATH + "element/:id/combobox/value", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "comboBoxSelectedItems", new CommandInfo(PATH + "element/:id/combobox/selectedItems", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "comboBoxSelectedItem", new CommandInfo(PATH + "element/:id/combobox/selectedItem", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "comboBoxItems", new CommandInfo(PATH + "element/:id/combobox/items", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "comboBoxExpandCollapseState",
                new CommandInfo(PATH + "element/:id/combobox/expandCollapseState", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "comboBoxEditableText", new CommandInfo(PATH + "element/:id/combobox/editableText", HttpMethod.POST));
        // endregion
        // region CheckBox
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "checkBoxToggleState", new CommandInfo(PATH + "element/:id/checkbox/toggleState", HttpMethod.POST));
        // endregion
        // region Slider
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "sliderMinimum", new CommandInfo(PATH + "element/:id/slider/minimum", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "sliderMaximum", new CommandInfo(PATH + "element/:id/slider/maximum", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "sliderSmallChange", new CommandInfo(PATH + "element/:id/slider/smallChange", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "sliderLargeChange", new CommandInfo(PATH + "element/:id/slider/largeChange", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "sliderGetLargeIncreaseButton",
                new CommandInfo(PATH + "element/:id/slider/getLargeIncreaseButton", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "sliderGetLargeDecreaseButton",
                new CommandInfo(PATH + "element/:id/slider/getLargeDecreaseButton", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "sliderGetThumb", new CommandInfo(PATH + "element/:id/slider/getThumb", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "sliderIsOnlyValue", new CommandInfo(PATH + "element/:id/slider/isOnlyValue", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "sliderGetValue", new CommandInfo(PATH + "element/:id/slider/getValue", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "sliderSetValue", new CommandInfo(PATH + "element/:id/slider/setValue/:value", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "sliderSmallIncrement", new CommandInfo(PATH + "element/:id/slider/smallIncrement", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "sliderSmallDecrement", new CommandInfo(PATH + "element/:id/slider/smallDecrement", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "sliderLargeIncrement", new CommandInfo(PATH + "element/:id/slider/largeIncrement", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "sliderLargeDecrement", new CommandInfo(PATH + "element/:id/slider/largeDecrement", HttpMethod.POST));
        // endregion
        // region DataGridView
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "dataGridViewHasAddRow", new CommandInfo(PATH + "element/:id/dataGridView/hasAddRow", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "dataGridViewGetHeader", new CommandInfo(PATH + "element/:id/dataGridView/getHeader", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "dataGridViewGetRows", new CommandInfo(PATH + "element/:id/dataGridView/getRows", HttpMethod.POST));
        // endregion
        // region DataGridViewHeader
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "dataGridViewHeaderGetColumns",
                new CommandInfo(PATH + "element/:id/dataGridViewHeader/getColumns", HttpMethod.POST));
        // endregion
        // region DataGridViewRow
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "dataGridViewRowGetCells",
                new CommandInfo(PATH + "element/:id/dataGridViewRow/getCells", HttpMethod.POST));
        // endregion
        // region DataGridViewCell
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "dataGridViewCellGetValue",
                new CommandInfo(PATH + "element/:id/dataGridViewCell/getValue", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "dataGridViewCellSetValue",
                new CommandInfo(PATH + "element/:id/dataGridViewCell/setValue/:value", HttpMethod.POST));
        // endregion
        // region Grid
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridRowCount", new CommandInfo(PATH + "element/:id/grid/rowCount", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridColumnCount", new CommandInfo(PATH + "element/:id/grid/columnCount", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridColumnHeaders", new CommandInfo(PATH + "element/:id/grid/columnHeaders", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridRowHeaders", new CommandInfo(PATH + "element/:id/grid/rowHeaders", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridRowOrColumnMajor", new CommandInfo(PATH + "element/:id/grid/rowOrColumnMajor", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridGetHeader", new CommandInfo(PATH + "element/:id/grid/getHeader", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridGetRows", new CommandInfo(PATH + "element/:id/grid/getRows", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridSelectedItems", new CommandInfo(PATH + "element/:id/grid/selectedItems", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridSelectedItem", new CommandInfo(PATH + "element/:id/grid/selectedItem", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridSelect", new CommandInfo(PATH + "element/:id/grid/select/:index", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridSelectText", new CommandInfo(PATH + "element/:id/grid/selectText/:index/:text", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridAddToSelection",
                new CommandInfo(PATH + "element/:id/grid/addToSelection/:index", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridAddToSelectionText",
                new CommandInfo(PATH + "element/:id/grid/addToSelectionText/:index/:text", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridRemoveFromSelection",
                new CommandInfo(PATH + "element/:id/grid/removeFromSelection/:index", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridRemoveFromSelectionText",
                new CommandInfo(PATH + "element/:id/grid/removeFromSelectionText/:index/:text", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridGetRowByIndex", new CommandInfo(PATH + "element/:id/grid/getRowByIndex/:index", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridGetRowByValue",
                new CommandInfo(PATH + "element/:id/grid/getRowByValue/:index/:text", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridGetRowsByValue",
                new CommandInfo(PATH + "element/:id/grid/getRowsByValue/:index/:text/:count", HttpMethod.POST));
        // endregion
        // region GridCell
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridCellContainingGrid",
                new CommandInfo(PATH + "element/:id/gridCell/containingGrid", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridCellContainingRow", new CommandInfo(PATH + "element/:id/gridCell/containingRow", HttpMethod.POST));
        // endregion
        // region GridHeader
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridHeaderColumns", new CommandInfo(PATH + "element/:id/gridHeader/columns", HttpMethod.POST));
        // endregion
        // region GridRow
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridRowCells", new CommandInfo(PATH + "element/:id/gridRow/cells", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridRowHeader", new CommandInfo(PATH + "element/:id/gridRow/header", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridRowFindCellByText",
                new CommandInfo(PATH + "element/:id/gridRow/findCellByText/:value", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "gridRowScrollIntoView", new CommandInfo(PATH + "element/:id/gridRow/scrollIntoView", HttpMethod.POST));
        // endregion
        // region ScrollBarBase
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "scrollBarBaseValue", new CommandInfo(PATH + "element/:id/scrollBarBase/value", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "scrollBarBaseMinimumValue",
                new CommandInfo(PATH + "element/:id/scrollBarBase/minimumValue", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "scrollBarBaseMaximumValue",
                new CommandInfo(PATH + "element/:id/scrollBarBase/maximumValue", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "scrollBarBaseSmallChange",
                new CommandInfo(PATH + "element/:id/scrollBarBase/smallChange", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "scrollBarBaseLargeChange",
                new CommandInfo(PATH + "element/:id/scrollBarBase/largeChange", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "scrollBarBaseIsReadOnly",
                new CommandInfo(PATH + "element/:id/scrollBarBase/isReadOnly", HttpMethod.POST));
        // endregion
        // region HorizontalScrollBar
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "horizontalScrollBarScrollLeft",
                new CommandInfo(PATH + "element/:id/horizontalScrollBar/scrollLeft", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "horizontalScrollBarScrollRight",
                new CommandInfo(PATH + "element/:id/horizontalScrollBar/scrollRight", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "horizontalScrollBarScrollLeftLarge",
                new CommandInfo(PATH + "element/:id/horizontalScrollBar/scrollLeftLarge", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "horizontalScrollBarScrollRightLarge",
                new CommandInfo(PATH + "element/:id/horizontalScrollBar/scrollRightLarge", HttpMethod.POST));
        // endregion
        // region VerticalScrollBar
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "verticalScrollBarScrollUp",
                new CommandInfo(PATH + "element/:id/verticalScrollBar/scrollUp", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "verticalScrollBarScrollDown",
                new CommandInfo(PATH + "element/:id/verticalScrollBar/scrollDown", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "verticalScrollBarScrollUpLarge",
                new CommandInfo(PATH + "element/:id/verticalScrollBar/scrollUpLarge", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "verticalScrollBarScrollDownLarge",
                new CommandInfo(PATH + "element/:id/verticalScrollBar/scrollDownLarge", HttpMethod.POST));
        // endregion
        // region ProgressBar
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "progressBarMinimum", new CommandInfo(PATH + "element/:id/progressBar/minimum", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "progressBarMaximum", new CommandInfo(PATH + "element/:id/progressBar/maximum", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "progressBarValue", new CommandInfo(PATH + "element/:id/progressBar/value", HttpMethod.POST));
        // endregion
        // region ListBox
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "listBoxItems", new CommandInfo(PATH + "element/:id/listBox/items", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "listBoxSelectedItems", new CommandInfo(PATH + "element/:id/listBox/selectedItems", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "listBoxSelectedItem", new CommandInfo(PATH + "element/:id/listBox/selectedItem", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "listBoxSelectIndex",
                new CommandInfo(PATH + "element/:id/listBox/selectIndex/:index", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "listBoxSelectText", new CommandInfo(PATH + "element/:id/listBox/selectText/:value", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "listBoxAddToSelectionIndex",
                new CommandInfo(PATH + "element/:id/listBox/addToSelectionIndex/:index", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "listBoxAddToSelectionText",
                new CommandInfo(PATH + "element/:id/listBox/addToSelectionText/:value", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "listBoxRemoveFromSelectionIndex",
                new CommandInfo(PATH + "element/:id/listBox/removeFromSelectionIndex/:index", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "listBoxRemoveFromSelectionText",
                new CommandInfo(PATH + "element/:id/listBox/removeFromSelectionText/:value", HttpMethod.POST));
        // endregion
        // region ListBoxItem
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "listBoxItemScrollIntoView",
                new CommandInfo(PATH + "element/:id/listBoxItem/scrollIntoView", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "listBoxItemIsChecked", new CommandInfo(PATH + "element/:id/listBoxItem/isChecked", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "listBoxItemSetChecked",
                new CommandInfo(PATH + "element/:id/listBoxItem/setChecked/:value", HttpMethod.POST));
        // endregion
        // region Menu
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "menuItems", new CommandInfo(PATH + "element/:id/menu/items", HttpMethod.POST));
        // endregion
        // region MenuItem
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "menuItemItems", new CommandInfo(PATH + "element/:id/menuItem/items", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "menuItemInvoke", new CommandInfo(PATH + "element/:id/menuItem/invoke", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "menuItemExpand", new CommandInfo(PATH + "element/:id/menuItem/expand", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "menuItemCollapse", new CommandInfo(PATH + "element/:id/menuItem/collapse", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "menuItemIsChecked", new CommandInfo(PATH + "element/:id/menuItem/isChecked", HttpMethod.POST));
        // endregion
        // region Button
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "buttonInvoke", new CommandInfo(PATH + "element/:id/button/invoke", HttpMethod.POST));
        // endregion
        // region Spinner
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "spinnerMinimum", new CommandInfo(PATH + "element/:id/spinner/minimum", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "spinnerMaximum", new CommandInfo(PATH + "element/:id/spinner/maximum", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "spinnerSmallChange", new CommandInfo(PATH + "element/:id/spinner/smallChange", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "spinnerIsOnlyValue", new CommandInfo(PATH + "element/:id/spinner/isOnlyValue", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "spinnerGetValue", new CommandInfo(PATH + "element/:id/spinner/getValue", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "spinnerSetValue", new CommandInfo(PATH + "element/:id/spinner/setValue/:value", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "spinnerIncrement", new CommandInfo(PATH + "element/:id/spinner/increment", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "spinnerDecrement", new CommandInfo(PATH + "element/:id/spinner/decrement", HttpMethod.POST));
        // endregion
        // region Tab
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "tabSelectedTabItem", new CommandInfo(PATH + "element/:id/tab/selectedTabItem", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "tabSelectedTabItemIndex",
                new CommandInfo(PATH + "element/:id/tab/selectedTabItemIndex", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "tabTabItems", new CommandInfo(PATH + "element/:id/tab/tabItems", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "tabSelectTabItemIndex",
                new CommandInfo(PATH + "element/:id/tab/selectTabItemIndex/:index", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "tabSelectTabItemText",
                new CommandInfo(PATH + "element/:id/tab/selectTabItemText/:value", HttpMethod.POST));
        // endregion
        // region TabItem
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "tabItemSelect", new CommandInfo(PATH + "element/:id/tabItem/select", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "tabItemAddToSelection", new CommandInfo(PATH + "element/:id/tabItem/addToSelection", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "tabItemRemoveFromSelection",
                new CommandInfo(PATH + "element/:id/tabItem/removeFromSelection", HttpMethod.POST));
        // endregion
        // region TextBox
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "textBoxGetText", new CommandInfo(PATH + "element/:id/textBox/getText", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "textBoxSetText", new CommandInfo(PATH + "element/:id/textBox/setText/:value", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "textBoxIsReadOnly", new CommandInfo(PATH + "element/:id/textBox/isReadOnly", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "textBoxEnter", new CommandInfo(PATH + "element/:id/textBox/enter/:value", HttpMethod.POST));
        // endregion
        // region Thumb
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "thumbSlideHorizontally",
                new CommandInfo(PATH + "element/:id/thumb/slideHorizontally/:index", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "thumbSlideVertically",
                new CommandInfo(PATH + "element/:id/thumb/slideVertically/:index", HttpMethod.POST));
        // endregion
        // region TitleBar
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "titleBarMinimizeButton",
                new CommandInfo(PATH + "element/:id/titleBar/minimizeButton", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "titleBarMaximizeButton",
                new CommandInfo(PATH + "element/:id/titleBar/maximizeButton", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "titleBarRestoreButton", new CommandInfo(PATH + "element/:id/titleBar/restoreButton", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "titleBarCloseButton", new CommandInfo(PATH + "element/:id/titleBar/closeButton", HttpMethod.POST));
        // endregion
        // region ToggleButton
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "toggleButtonToggle", new CommandInfo(PATH + "element/:id/toggleButton/toggle", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "toggleButtonGetToggleState",
                new CommandInfo(PATH + "element/:id/toggleButton/getToggleState", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "toggleButtonSetToggleState",
                new CommandInfo(PATH + "element/:id/toggleButton/setToggleState/:value", HttpMethod.POST));
        // endregion
        // region Tree
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "treeSelectedTreeItem", new CommandInfo(PATH + "element/:id/tree/selectedTreeItem", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "treeItems", new CommandInfo(PATH + "element/:id/tree/items", HttpMethod.POST));
        // endregion
        // region TreeItem
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "treeItemItems", new CommandInfo(PATH + "element/:id/treeItem/items", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "treeItemGetText", new CommandInfo(PATH + "element/:id/treeItem/getText", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "treeItemExpandCollapseState",
                new CommandInfo(PATH + "element/:id/treeItem/expandCollapseState", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "treeItemExpand", new CommandInfo(PATH + "element/:id/treeItem/expand", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "treeItemCollapse", new CommandInfo(PATH + "element/:id/treeItem/collapse", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "treeItemSelect", new CommandInfo(PATH + "element/:id/treeItem/select", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "treeItemAddToSelection",
                new CommandInfo(PATH + "element/:id/treeItem/addToSelection", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "treeItemRemoveFromSelection",
                new CommandInfo(PATH + "element/:id/treeItem/removeFromSelection", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "treeItemIsChecked", new CommandInfo(PATH + "element/:id/treeItem/isChecked", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "treeItemSetChecked",
                new CommandInfo(PATH + "element/:id/treeItem/setChecked/:value", HttpMethod.POST));
        // endregion
        // region Window
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "windowTitle", new CommandInfo(PATH + "element/:id/window/title", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "windowIsModal", new CommandInfo(PATH + "element/:id/window/isModal", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "windowTitleBar", new CommandInfo(PATH + "element/:id/window/titleBar", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "windowModalWindows", new CommandInfo(PATH + "element/:id/window/modalWindows", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "windowPopup", new CommandInfo(PATH + "element/:id/window/popup", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "windowContextMenu", new CommandInfo(PATH + "element/:id/window/contextMenu", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "windowClose", new CommandInfo(PATH + "element/:id/window/close", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "windowMove", new CommandInfo(PATH + "element/:id/window/move/:x/:y", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "windowSetTransparency",
                new CommandInfo(PATH + "element/:id/window/setTransparency/:index", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "windowGetActiveWindow", new CommandInfo(PATH + "element/:id/window/getActiveWindow", HttpMethod.POST));
        // endregion
        // region Calendar
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "calendarSelectedDates", new CommandInfo(PATH + "element/:id/calendar/selectedDates", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "calendarSelectDate",
                new CommandInfo(PATH + "element/:id/calendar/selectDate/:dateTime", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "calendarAddToSelection",
                new CommandInfo(PATH + "element/:id/calendar/addToSelection/:dateTime", HttpMethod.POST));
        // endregion
        // region DateTimePicker
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "dateTimePickerGetDate", new CommandInfo(PATH + "element/:id/dateTimePicker/getDate", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "dateTimePickerSetDate",
                new CommandInfo(PATH + "element/:id/dateTimePicker/setDate/:dateTime", HttpMethod.POST));
        // endregion
        // region Other
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "customScreenshot", new CommandInfo(PATH + "customScreenshot/:format", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "elementScreenshot", new CommandInfo(PATH + "element/:id/elementScreenshot", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put("dragAndDrop", new CommandInfo(PATH + "dragAndDrop", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "getActiveWindow", new CommandInfo(PATH + "getActiveWindow", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "elementDragAndDrop", new CommandInfo(PATH + "element/:id/elementDragAndDrop", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "sendCharsToActiveElement", new CommandInfo(PATH + "sendCharsToActiveElement", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "getKeyboardLayout", new CommandInfo(PATH + "getKeyboardLayout", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "setKeyboardLayout", new CommandInfo(PATH + "setKeyboardLayout", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "elementMouseAction", new CommandInfo(PATH + "element/:id/elementMouseAction", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "getClipboardText", new CommandInfo(PATH + "getClipboardText", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "setClipboardText", new CommandInfo(PATH + "setClipboardText", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "keyCombination", new CommandInfo(PATH + "keyCombination", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "touchActionsTap", new CommandInfo(PATH + "touchActionsTap", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "touchActionsHold", new CommandInfo(PATH + "touchActionsHold", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "touchActionsPinch", new CommandInfo(PATH + "touchActionsPinch", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "touchActionsTransition", new CommandInfo(PATH + "touchActionsTransition", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "touchActionsDrag", new CommandInfo(PATH + "touchActionsDrag", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "touchActionsRotate", new CommandInfo(PATH + "touchActionsRotate", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put("actions", new CommandInfo(PATH + "actions", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "setRootElement", new CommandInfo(PATH + "setRootElement", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "changeProcess", new CommandInfo(PATH + "changeProcess", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "killProcesses", new CommandInfo(PATH + "killProcesses", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "fileOrDirectoryExists", new CommandInfo(PATH + "fileOrDirectoryExists", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put(
                "deleteFileOrDirectory", new CommandInfo(PATH + "deleteFileOrDirectory", HttpMethod.POST));
        DESKTOP_DRIVER_COMMAND_NAME_TO_URL.put("startApp", new CommandInfo(PATH + "startApp", HttpMethod.POST));
        // endregion
    }

    public DesktopDriverCommandExecutor(final DesktopDriverService driverService) {
        super(DESKTOP_DRIVER_COMMAND_NAME_TO_URL, driverService.getUrl());
        service = driverService;
    }

    public DesktopDriverCommandExecutor(final URL remoteUrl) {
        super(DESKTOP_DRIVER_COMMAND_NAME_TO_URL, remoteUrl);
        service = null;
    }

    /**
     * executes driver's command
     * @param command
     * @return
     * @throws IOException
     */
    @Override
    public Response execute(final Command command) throws IOException {
        if ((service != null) && (DriverCommand.NEW_SESSION.equals(command.getName()))) {
            service.start();
        }
        try {
            return super.execute(command);
        } catch (Exception t) {
            var rootCause = Throwables.getRootCause(t);
            if (rootCause instanceof ConnectException
                    && "Connection refused".equals(rootCause.getMessage())
                    && ((service == null) || (!service.isRunning()))) {
                throw new WebDriverException("The driver server has unexpectedly died!", t);
            }
            Throwables.throwIfUnchecked(t);
            throw new WebDriverException(t);
        } finally {
            if ((service != null) && (DriverCommand.QUIT.equals(command.getName()))) {
                service.stop();
            }
        }
    }
}
