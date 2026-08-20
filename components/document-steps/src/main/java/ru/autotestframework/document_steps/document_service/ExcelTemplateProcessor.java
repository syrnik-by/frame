package ru.autotestframework.document_steps.document_service;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.jxls.util.TransformerFactory;
import org.springframework.stereotype.Component;
import ru.autotestframework.core.PlaceholderResolver;
import ru.autotestframework.core.context.Context;

/**
 * Processor of xls templates using context functions (StringSubstitutor)
 */
@Component
@RequiredArgsConstructor
public class ExcelTemplateProcessor {

    private final Context context;
    private final PlaceholderResolver resolver;

    /**
     * process xls exclusively using context variables
     * @param templatePath
     * @param outputPath
     */
    @SneakyThrows
    public void generate(String templatePath, String outputPath) {
        try (InputStream is = this.getClass().getResourceAsStream(templatePath);
                OutputStream os = new FileOutputStream(outputPath)) {

            org.jxls.common.Context jxlsContext = new org.jxls.common.Context(context.getAll());
            JxlsHelper.getInstance().processTemplate(is, os, jxlsContext);
        }
    }

    /**
     * process xls using context variables and context functions
     * @param templatePath
     * @param outputPath
     */
    @SneakyThrows
    public void customJexl(String templatePath, String outputPath) {
        try (InputStream is = this.getClass().getResourceAsStream(templatePath);
                OutputStream os = new FileOutputStream(outputPath)) {
            org.jxls.common.Context jxlsContext = new org.jxls.common.Context(context.getAll());

            Transformer transformer = TransformerFactory.createTransformer(is, os);
            transformer.getTransformationConfig().setExpressionEvaluator(new JxlsEngineEvaluator(resolver, context));
            new JxlsHelper().processTemplate(jxlsContext, transformer);
        }
    }
}
