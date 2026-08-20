package ru.autotestframework.desktop_elements.enums;

public enum ExpandCollapseState {

    /**
     * No children are visible.
     */
    COLLAPSED,

    /**
     * All children are visible.
     */
    EXPANDED,

    /**
     * Some, but not all, children are visible.
     */
    PARTIALLY_EXPANDED,

    /**
     * The element does not expand or collapse.
     */
    LEAF_NODE;

    public static ExpandCollapseState getEnum(final String value) {
        switch (value) {
            case "Collapsed":
                return COLLAPSED;
            case "Expanded":
                return EXPANDED;
            case "PartiallyExpanded":
                return PARTIALLY_EXPANDED;
            case "LeafNode":
                return LEAF_NODE;
            default:
                return null;
        }
    }
}
