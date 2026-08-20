package ru.autotestframework.http_steps.components;

import static com.github.tomakehurst.wiremock.client.WireMock.create;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

public class MockService {

    private final WireMock client;
    private final WireMockServer server;

    public MockService(final MockServiceProperties properties) {
        server = new WireMockServer(options().httpsPort(properties.getPort()).needClientAuth(false));
        client = create().host("localhost").https().port(properties.getPort()).build();
    }

    /**
     * starts mock service
     */
    public void startServer() {
        server.start();
    }

    /**
     * configures wiremock client
     */
    public void configureClient() {
        WireMock.configureFor(client);
    }

    /**
     * registers stubs
     * @param mappingSpecJson
     */
    public void register(final String mappingSpecJson) {
        var stubMapping = StubMapping.buildFrom(mappingSpecJson);
        register(stubMapping);
    }

    /**
     * registers stubs
     * @param stubMapping
     */
    public void register(final StubMapping stubMapping) {
        client.register(stubMapping);
    }

    /**
     * resets request journal
     */
    public void cleanRequestsJournal() {
        client.resetRequests();
    }

    /**
     * removes all mappings
     */
    public void cleanAll() {
        client.removeMappings();
    }

    /**
     * stops server
     */
    public void stopServer() {
        server.stop();
    }
}
