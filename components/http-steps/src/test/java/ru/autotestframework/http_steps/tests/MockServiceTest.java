package ru.autotestframework.http_steps.tests;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.http_steps.components.MockService;

@Tag("@HttpSteps")
class MockServiceTest {
    static MockService mockService;
    static WireMockServer server;
    static WireMock client;

    @BeforeAll
    static void initMockService() {
        mockService = Mockito.mock(MockService.class);
        server = Mockito.mock(WireMockServer.class);
        client = Mockito.mock(WireMock.class);
        ReflectionTestUtils.setField(mockService, "client", client);
        ReflectionTestUtils.setField(mockService, "server", server);
    }

    @Test
    void startServerTest() {
        Mockito.doCallRealMethod().when(mockService).startServer();
        Mockito.doThrow(Error.class).when(server).start();
        Assert.assertThrows(Error.class, () -> mockService.startServer());
    }

    @Test
    void registerStubTest() {
        Mockito.doCallRealMethod().when(mockService).register(Mockito.any(StubMapping.class));
        Mockito.doThrow(Error.class).when(client).register(Mockito.any(StubMapping.class));
        Assert.assertThrows(Error.class, () -> mockService.register(Mockito.mock(StubMapping.class)));
    }

    @Test
    void registerStringTest() {
        Mockito.doCallRealMethod().when(mockService).register(Mockito.any(StubMapping.class));
        Mockito.doCallRealMethod().when(mockService).register(Mockito.anyString());
        Mockito.doThrow(Error.class).when(client).register(Mockito.any(StubMapping.class));
        Assert.assertThrows(
                Error.class,
                () -> mockService.register("  {\n" + "    \"request\": {\n"
                        + "      \"method\": \"GET\",\n"
                        + "      \"url\": \"/get-file\"\n"
                        + "    },\n"
                        + "    \"response\": {\n"
                        + "      \"status\": 200,\n"
                        + "      \"bodyFileName\": \"file.pdf\",\n"
                        + "      \"headers\": {\n"
                        + "        \"Content-Type\": \"application/pdf\",\n"
                        + "        \"Content-Disposition\": \"attachment;filename=\\\"file.pdf\\\"\"\n"
                        + "      }\n"
                        + "    }\n"
                        + "  }"));
    }

    @Test
    void cleanRequestsJournalTest() {
        Mockito.doCallRealMethod().when(mockService).cleanRequestsJournal();
        Mockito.doThrow(Error.class).when(client).resetRequests();
        Assert.assertThrows(Error.class, () -> mockService.cleanRequestsJournal());
    }

    @Test
    void cleanAllTest() {
        Mockito.doCallRealMethod().when(mockService).cleanAll();
        Mockito.doThrow(Error.class).when(client).removeMappings();
        Assert.assertThrows(Error.class, () -> mockService.cleanAll());
    }

    @Test
    void stopServerTest() {
        Mockito.doCallRealMethod().when(mockService).stopServer();
        Mockito.doThrow(Error.class).when(server).stop();
        Assert.assertThrows(Error.class, () -> mockService.stopServer());
    }
}
