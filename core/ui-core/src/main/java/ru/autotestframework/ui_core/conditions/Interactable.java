package ru.autotestframework.ui_core.conditions;

import com.codeborne.selenide.conditions.And;
import com.codeborne.selenide.conditions.CssValue;
import com.codeborne.selenide.conditions.Visible;
import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Interactable.
 */
@ParametersAreNonnullByDefault
public class Interactable extends And {

    /**
     * Instantiates a new Interactable.
     */
    public Interactable() {
        super("interactable", Arrays.asList(new Visible(), new CssValue("opacity", "0")));
    }

    @Nonnull
    @Override
    public String toString() {
        return getName();
    }
}
