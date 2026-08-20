package ru.autotestframework.ui_core.services;

import com.codeborne.selenide.Selenide;

/**
 * Js executor.
 */
public class JSExecutor {

    /**
     * Execute java script string.
     *
     * @param script   the script
     * @param iElement the element
     * @return the string
     */
    public String executeJavaScript(final String script, Object... iElement) {
        return Selenide.executeJavaScript(script, iElement);
    }
}
