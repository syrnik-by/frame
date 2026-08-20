package ru.autotestframework.ui_core.typified_elements.enums;

/**
 * The enum Keyboard layout.
 */
public enum KeyboardLayout {
    /**
     * Rus keyboard layout.
     */
    RUS("00000419"),
    /**
     * Eng keyboard layout.
     */
    ENG("00000409"),
    /**
     * Unknown keyboard layout.
     */
    UNKNOWN();

    private String layoutCode;

    /**
     * Gets layout code.
     *
     * @return the layout code
     */
    public String getLayoutCode() {
        return layoutCode;
    }

    private KeyboardLayout setLayoutCode(final String layoutCode) {
        this.layoutCode = layoutCode;
        return this;
    }

    KeyboardLayout() {}

    KeyboardLayout(final String layoutCode) {
        this.layoutCode = layoutCode;
    }

    /**
     * Gets keyboard layout.
     *
     * @param layoutCode the layout code
     * @return the keyboard layout
     */
    public static KeyboardLayout getKeyboardLayout(final String layoutCode) {
        switch (layoutCode) {
            case "00000419":
                return KeyboardLayout.RUS;
            case "00000409":
                return KeyboardLayout.ENG;
            default:
                return KeyboardLayout.UNKNOWN.setLayoutCode(layoutCode);
        }
    }
}
