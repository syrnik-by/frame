package ru.autotestframework.java_elements;

import com.codeborne.selenide.WebDriverRunner;
import io.cucumber.java.Before;
import javax.swing.SwingUtilities;
import ru.autotestframework.java_elements.fake_app.Login;

public class JavaElementsTestHooks {

    private Login login;

    @Before(order = Integer.MIN_VALUE + 100002)
    public void testSetUp() {
        login = new Login();
        SwingUtilities.invokeLater(() -> login.setVisible(true));
        WebDriverRunner.getWebDriver().switchTo().window("Login");
    }
}
