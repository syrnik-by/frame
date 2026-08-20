package ru.autotestframework.document_steps;

import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.autotestframework.core.PlaceholderResolver;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.document_steps.document_service.ExcelTemplateProcessor;
import ru.autotestframework.junit.BaseSpringJunitTest;

@Tag("@DocumentSteps")
public class ExcelTemplateTest extends BaseSpringJunitTest {
    @Autowired
    private Context context;

    @Autowired
    private PlaceholderResolver resolver;

    @Test
    void setSheetListPositiveTest() {
        String template = "/data/files/ExcelTemplateSimple.xlsx";
        String output = "build/ExcelTemplateResultSimple.xlsx";
        List employees = List.of(new Employee(), new Employee());
        context.set("employees", employees);
        context.set("var1", "eklmn");
        context.set("var2", "eprst");
        new ExcelTemplateProcessor(context, resolver).generate(template, output);
    }

    @Test
    void setSheetListCustom() {
        String template = "/data/files/ExcelTemplate.xlsx";
        String output = "build/ExcelTemplateResult.xlsx";
        List employees = List.of(new Employee(), new Employee());
        context.set("employees", employees);
        context.set("var1", "eklmn");
        context.set("var2", "eprst");
        new ExcelTemplateProcessor(context, resolver).customJexl(template, output);
    }

    @Test
    void setSheetListCombo() {
        String template = "/data/files/ExcelTemplateCombo.xlsx";
        String output = "build/ExcelTemplateResultCombo.xlsx";
        List employees = List.of(new Employee(), new Employee());
        context.set("employees", employees);
        context.set("var1", "eklmn");
        context.set("var2", "eprst");
        new ExcelTemplateProcessor(context, resolver).customJexl(template, output);
    }
}
