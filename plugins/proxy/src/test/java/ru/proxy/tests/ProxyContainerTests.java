package ru.proxy.tests;

import com.browserup.bup.BrowserUpProxy;
import com.browserup.harreader.model.Har;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.proxy.ProxyContainer;

public class ProxyContainerTests {

    public static ProxyContainer proxyContainer;

    @BeforeAll
    public static void setUp() {
        proxyContainer = ProxyContainer.getInstance();
        proxyContainer.init("1", "2");
    }

    @Test
    void initTest() {
        BrowserUpProxy proxy = proxyContainer.getProxy();
        Assertions.assertEquals(8, proxy.getHarCaptureTypes().size());
        Assertions.assertEquals(
                "load", proxy.getHar().getLog().getPages().get(0).getId());
    }

    @Test
    void getHarTest() throws Exception {
        BrowserUpProxy proxy = proxyContainer.getProxy();
        Har har = proxy.getHar();
        Har har1 = proxyContainer.getHar();
        Har har2 = proxyContainer.getHar();
        Assertions.assertEquals(har, har1);
        Assertions.assertNotEquals(har, har2);
    }
}
