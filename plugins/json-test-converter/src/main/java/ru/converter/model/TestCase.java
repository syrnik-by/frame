package ru.converter.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TestCase {

    @JsonProperty("name")
    private String name;

    @JsonProperty("startURL")
    private String startURL;

    @JsonProperty("title")
    private String title;

    @JsonProperty("actions")
    private List<TestAction> actions;

    @JsonProperty("createdAt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date createdAt;

    @JsonProperty("id")
    private Long id;
}
