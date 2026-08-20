package ru.autotestframework.ui_core.typified_elements;

import java.lang.annotation.Annotation;
import lombok.Data;

/**
 * Base element.
 */
@Data
public abstract class BaseElement implements IElement {

    /**
     * The Title.
     */
    protected String title;
    /**
     * The Annotations.
     */
    protected Annotation[] annotations;
}
