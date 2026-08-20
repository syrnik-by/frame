package ru.autotestframework.document_steps;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.autotestframework.util.generator.FakerRU;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Employee {
    private String name = FakerRU.instance().name().fullName();
    private Date birthDate = FakerRU.instance().date().birthday();
    private Double payment = FakerRU.instance().number().randomDouble(2, 5000, 20000);
    private Double bonus = FakerRU.instance().number().randomDouble(2, 200, 2000);
}
