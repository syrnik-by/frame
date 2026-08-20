package ru.autotestframework.ui_core.typified_elements.ifaces;

/**
 * The interface is designed for web elements with an internal link to a source or target.
 * for example:
 * &lt;a href="цель"&gt;anchor&lt;/a&gt;
 * &lt;img src="источник"/&gt;
 * &lt;button type="button" formaction="цель"&gt;
 * &lt;form action="цель"&gt;&lt;/form&gt;
 * &lt;iframe src="источник"&gt;&lt;/iframe&gt;
 */
public interface ISourceable {
    /**
     * Gets source.
     *
     * @return the source
     */
    String getSource();
}
