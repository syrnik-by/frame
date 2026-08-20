package ru.autotestframework.ui_core.conditions;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Driver;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.openqa.selenium.WebElement;
import ru.autotestframework.ui_core.typified_elements.ifaces.IWritable;

/**
 * Editable.
 */
@ParametersAreNonnullByDefault
public class Editable extends Condition {

    /**
     * Instantiates a new Editable.
     */
    public Editable() {
        super("editable");
    }

    @Nonnull
    @Override
    public String toString() {
        return getName();
    }

    @Nonnull
    @Override
    public CheckResult check(final Driver driver, final WebElement element) {
        boolean isEditable = ((IWritable) element).isEditable();
        return new CheckResult(isEditable, isEditable ? "editable" : "readonly");
    }
}
