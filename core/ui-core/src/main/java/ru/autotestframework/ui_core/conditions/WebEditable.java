package ru.autotestframework.ui_core.conditions;

import com.codeborne.selenide.conditions.And;
import com.codeborne.selenide.conditions.Enabled;
import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Web editable.
 */
@ParametersAreNonnullByDefault
public class WebEditable extends And {

    /**
     * Selenide 6.5.0 Condition for web.
     */
    public WebEditable() {
        super("editable", Arrays.asList(new Interactable(), new Enabled(), attribute("readonly")));
    }

    @Nonnull
    @Override
    public String toString() {
        return getName();
    }
}
