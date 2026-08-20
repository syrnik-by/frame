package ru.converter.template;

import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

@Slf4j
public class TemplateProcessor {

    private final VelocityEngine velocityEngine;
    private final String templateDirectory;

    public TemplateProcessor() {
        this("templates");
    }

    public TemplateProcessor(String templateDirectory) {
        this.templateDirectory = templateDirectory;
        this.velocityEngine = initializeVelocityEngine();
    }

    private VelocityEngine initializeVelocityEngine() {
        try {
            VelocityEngine engine = new VelocityEngine();
            Properties props = new Properties();
            // Базовые настройки Velocity 1.7
            props.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath,file");
            // Classpath loader
            props.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            props.setProperty("classpath.resource.loader.cache", "false");
            props.setProperty("classpath.resource.loader.modificationCheckInterval", "0");
            // File loader
            props.setProperty(
                    "file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
            props.setProperty("file.resource.loader.path", "");
            props.setProperty("file.resource.loader.cache", "false");
            // Кодировка
            props.setProperty(RuntimeConstants.INPUT_ENCODING, "UTF-8");
            props.setProperty(RuntimeConstants.OUTPUT_ENCODING, "UTF-8");
            // ВАЖНО: Отключаем логирование Velocity или используем NullLogChute
            props.setProperty(
                    RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogChute");
            engine.init(props);
            log.info("Velocity Engine initialized without logging");
            return engine;
        } catch (Exception e) {
            log.error("Failed to initialize Velocity Engine: {}", e.getMessage());
            // Возвращаем простой движок
            return createSimpleVelocityEngine();
        }
    }

    private VelocityEngine createSimpleVelocityEngine() {
        try {
            VelocityEngine engine = new VelocityEngine();
            Properties props = new Properties();
            // Минимальная конфигурация
            props.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
            props.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, "");
            props.setProperty(RuntimeConstants.INPUT_ENCODING, "UTF-8");
            props.setProperty(RuntimeConstants.OUTPUT_ENCODING, "UTF-8");
            props.setProperty("file.resource.loader.cache", "false");
            // Отключаем логирование
            props.setProperty(
                    RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogChute");
            engine.init(props);
            log.warn("Using simple Velocity Engine");
            return engine;
        } catch (Exception e) {
            throw new RuntimeException("Cannot initialize Velocity Engine", e);
        }
    }

    public String processTemplate(String templateName, Map<String, Object> context) {
        try {
            String templatePath = templateDirectory + "/" + templateName;
            log.debug("Processing template: {}", templatePath);
            VelocityContext velocityContext = new VelocityContext();
            if (context != null) {
                context.forEach(velocityContext::put);
            }
            // Добавляем утилиты
            velocityContext.put("StringUtils", new StringUtils());
            velocityContext.put("DateUtils", new DateUtils());
            Template template = velocityEngine.getTemplate(templatePath, "UTF-8");
            StringWriter writer = new StringWriter();
            template.merge(velocityContext, writer);
            return writer.toString();
        } catch (Exception e) {
            log.error("Error processing template {}: {}", templateName, e.getMessage());
            return processTemplateSimple(templateName, context);
        }
    }

    private String processTemplateSimple(String templateName, Map<String, Object> context) {
        try {
            // Простая замена переменных без Velocity
            String templateContent = loadTemplateFile(templateName);
            return replaceVariables(templateContent, context);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process template: " + templateName, e);
        }
    }

    private String loadTemplateFile(String templateName) throws Exception {
        Path[] paths = {
            Paths.get("src/main/resources", templateDirectory, templateName),
            Paths.get("src/main/resources/templates", templateName),
            Paths.get(templateDirectory, templateName),
            Paths.get(templateName)
        };
        for (Path path : paths) {
            if (Files.exists(path)) {
                return Files.readString(path);
            }
        }
        throw new Exception("Template not found: " + templateName);
    }

    private String replaceVariables(String template, Map<String, Object> context) {
        String result = template;
        if (context != null) {
            for (Map.Entry<String, Object> entry : context.entrySet()) {
                String placeholder = "${" + entry.getKey() + "}";
                String value = entry.getValue() != null ? entry.getValue().toString() : "";
                result = result.replace(placeholder, value);
            }
        }
        return result;
    }

    public static class StringUtils {
        public String toCamelCase(String input) {
            if (input == null || input.isEmpty()) return input;
            String[] parts = input.split("[^a-zA-Z0-9]");
            StringBuilder result = new StringBuilder();
            for (String part : parts) {
                if (!part.isEmpty()) {
                    result.append(Character.toUpperCase(part.charAt(0)))
                            .append(part.substring(1).toLowerCase());
                }
            }
            return result.toString();
        }

        public String escapeJava(String input) {
            if (input == null) return "";
            return input.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
        }
    }

    public static class DateUtils {
        public String currentDate() {
            return new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        }
    }
}
