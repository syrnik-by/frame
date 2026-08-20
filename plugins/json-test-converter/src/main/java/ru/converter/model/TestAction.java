package ru.converter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TestAction {

    @JsonProperty("url")
    private String url;

    @JsonProperty("action")
    private String action;

    @JsonProperty("type")
    private String type;

    @JsonProperty("selector")
    private String selector;

    @JsonProperty("text")
    private String text;

    @JsonProperty("value")
    private String value;

    @JsonProperty("expected")
    private String expected;

    @JsonProperty("tagName")
    private String tagName;

    @JsonProperty("className")
    private String className;

    public String getInputText() {
        return text != null ? text : value;
    }
}
