package ru.converter.pages;

import java.util.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.autotestframework.core.exception.AutotestException;

@Slf4j
@Getter
public class Store {

    private final Map<String, Page> pages;

    public Store() {
        this.pages = new HashMap<>();
    }

    public void addPage(Page page) {
        pages.put(page.getInfo().pageName, page);
    }

    public Page getPage(String pageName) {
        return pages.get(pageName);
    }

    /**
     * Сгенерированная страница
     */
    @Setter
    @Getter
    public static class Page {

        private Info info;

        private String filePath;

        public Page(Info info) {
            this.info = info;
        }

        public String getClassName() {
            return info.getClassName();
        }

        public String findElementFieldNameByLocator(String selector) {
            return info.getElements().stream()
                    .filter(elementInfo -> elementInfo.getSelector().equals(selector))
                    .findFirst()
                    .map(ElementInfo::getFieldName)
                    .orElseThrow(() -> new AutotestException("Ошибка получения элемента " + selector));
        }

        public String findElementNameByLocator(String selector) {
            return info.getElements().stream()
                    .filter(elementInfo -> elementInfo.getSelector().equals(selector))
                    .findFirst()
                    .map(ElementInfo::getName)
                    .orElseThrow(() -> new AutotestException("Ошибка получения элемента " + selector));
        }

        /**
         * Информация о странице
         */
        public static class Info {
            @Getter
            private final String pageName;

            @Getter
            private final String url;

            private final Map<String, ElementInfo> elements;

            public Info(String pageName, String url) {
                this.pageName = pageName;
                this.url = url;
                this.elements = new LinkedHashMap<>();
            }

            public void addElement(ElementInfo element) {
                String key = element.getName().toLowerCase();
                if (!elements.containsKey(key)) {
                    elements.put(key, element);
                } else {
                    // Если элемент с таким именем уже существует, добавляем суффикс
                    int counter = 1;
                    String newName;
                    do {
                        newName = element.getName() + counter;
                        key = newName.toLowerCase();
                        counter++;
                    } while (elements.containsKey(key));
                    element.setName(newName);
                    elements.put(key, element);
                }
            }

            public String getClassName() {
                return pageName + "Page";
            }

            public String getVariableName() {
                return Character.toLowerCase(pageName.charAt(0)) + pageName.substring(1) + "Page";
            }

            public Collection<ElementInfo> getElements() {
                return elements.values();
            }
        }

        /**
         * Информация об элементе
         */
        @Getter
        public static class ElementInfo {
            @Setter
            private String name;

            private final String selector;
            private final String locatorType;
            private final String locatorValue;
            private final String elementType;

            public ElementInfo(
                    String name, String selector, String locatorType, String locatorValue, String elementType) {
                this.name = name;
                this.selector = selector;
                this.locatorType = locatorType;
                this.locatorValue = locatorValue;
                this.elementType = elementType;
            }

            public String getFieldName() {
                if (name == null || name.isEmpty()) return "element";
                return Character.toLowerCase(name.charAt(0)) + name.substring(1);
            }

            public String getElementAnnotation() {
                // Аннотация @Element содержит имя элемента
                return "@Element(\"" + name + "\")";
            }

            public String getFindByAnnotation() {
                switch (locatorType) {
                    case "id":
                        return "@FindBy(id = \"" + escapeJava(locatorValue) + "\")";
                    case "className":
                        return "@FindBy(className = \"" + escapeJava(locatorValue) + "\")";
                    case "name":
                        return "@FindBy(name = \"" + escapeJava(locatorValue) + "\")";
                    case "tagName":
                        return "@FindBy(tagName = \"" + escapeJava(locatorValue) + "\")";
                    case "xpath":
                        return "@FindBy(xpath = \"" + escapeJava(locatorValue) + "\")";
                    case "css":
                    default:
                        return "@FindBy(css = \"" + escapeJava(locatorValue) + "\")";
                }
            }

            public String getFieldType() {
                return elementType; // Button, TextInput, Text, WebElement
            }

            private String escapeJava(String text) {
                if (text == null) return "";
                return text.replace("\\", "\\\\").replace("\"", "\\\"");
            }
        }
    }
}
