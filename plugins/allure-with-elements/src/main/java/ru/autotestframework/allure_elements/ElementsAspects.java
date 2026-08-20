package ru.autotestframework.allure_elements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Allure;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.autotestframework.ui_core.typified_elements.IElement;

/**
 * Automation of allure step creation on interacting with SelenideElement or IElement (click/sendKeys/shouldBe)
 */
@Aspect
public class ElementsAspects {

    private static final Logger log = LoggerFactory.getLogger(ElementsAspects.class);

    @SneakyThrows
    @After("execution(* org.openqa.selenium.WebElement+.click())")
    public void click(final JoinPoint joinPoint) {
        String element = getNameFromTarget(joinPoint.getTarget());
        Allure.step("Нажимает на " + element);
    }

    @SneakyThrows
    @After("execution(* org.openqa.selenium.WebElement+.sendKeys(..))")
    public void sendKeys(final JoinPoint joinPoint) {
        String element = getNameFromTarget(joinPoint.getTarget());
        String collectedArgs =
                Arrays.stream(joinPoint.getArgs()).map(Object::toString).collect(Collectors.joining(" "));
        Allure.step("Отправляет " + collectedArgs + " в " + element);
    }

    @SneakyThrows
    @After("execution(* ru.autotestframework.ui_core.typified_elements.IElement+.shouldBe(..)) ||"
            + "execution(* com.codeborne.selenide.SelenideElement+.shouldBe(..))")
    public void shouldBe(final JoinPoint joinPoint) {
        String element = getNameFromTarget(joinPoint.getTarget());
        String condition = ((Condition) joinPoint.getArgs()[0]).getName();
        var notReversed = "";
        try {
            if (Boolean.FALSE.equals(joinPoint.getArgs()[1])) {
                notReversed = "не";
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        Allure.step("Элемент " + element + " " + notReversed + " должен быть " + condition);
    }

    private String getNameFromTarget(Object targetElement) {
        try {
            return ((SelenideElement) targetElement).getAlias();
        } catch (Exception e) {
            return ((IElement) targetElement).getTitle();
        }
    }
}
