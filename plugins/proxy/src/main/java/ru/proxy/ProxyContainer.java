package ru.proxy;

import com.browserup.bup.BrowserUpProxy;
import com.browserup.bup.BrowserUpProxyServer;
import com.browserup.bup.filters.RequestFilter;
import com.browserup.bup.proxy.CaptureType;
import com.browserup.bup.util.HttpMessageContents;
import com.browserup.bup.util.HttpMessageInfo;
import com.browserup.harreader.model.Har;
import com.browserup.harreader.model.HarCookie;
import com.browserup.harreader.model.HarEntry;
import com.browserup.harreader.model.HarHeader;
import com.codeborne.selenide.WebDriverRunner;
import com.google.common.net.HttpHeaders;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.io.*;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.SneakyThrows;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;

public class ProxyContainer {
    private static ProxyContainer INSTANCE;

    @Getter
    private BrowserUpProxy proxy = new BrowserUpProxyServer();

    private ProxyContainer() {}

    /**
     * singleton ProxyContainer
     * @return
     */
    public static ProxyContainer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ProxyContainer();
        }
        return INSTANCE;
    }

    /**
     * initializes proxy by host and port
     * @param host
     * @param port
     */
    @SneakyThrows
    public void init(String host, String port) {
        var address = InetAddress.getByName(host);
        EnumSet<CaptureType> captureTypeSet = CaptureType.getAllContentCaptureTypes();
        captureTypeSet.addAll(CaptureType.getCookieCaptureTypes());
        captureTypeSet.addAll(CaptureType.getHeaderCaptureTypes());
        proxy.setHarCaptureTypes(captureTypeSet);
        proxy.enableHarCaptureTypes(CaptureType.getAllContentCaptureTypes());
        proxy.setMitmDisabled(false);

        // filter browser integration requests
        proxy.blocklistRequests(".*google.*", 404);
        proxy.blocklistRequests(".*yandex.*", 404);
        proxy.addRequestFilter(new UrlFilter(".*yandex.*"));
        proxy.addRequestFilter(new UrlFilter(".*google.*"));

        proxy.setTrustAllServers(true);
        proxy.newHar("load");
        new Thread(() -> {
                    proxy.start(Integer.parseInt(port)); // .start(Integer.parseInt(port));
                })
                .start();
    }

    private String stringify(String text) {
        byte[] bytes = text.getBytes(StandardCharsets.ISO_8859_1);
        return new String(bytes);
    }

    /**
     * returns har from proxy and starts new har
     * @return
     * @throws Exception
     */
    public Har getHar() throws Exception {
        Har har = getProxy().getHar();
        for (Iterator<HarEntry> iterator = har.getLog().getEntries().iterator(); iterator.hasNext(); ) {
            try {
                HarEntry e = iterator.next();

                for (Iterator<HarCookie> iterator2 = e.getRequest().getCookies().iterator(); iterator2.hasNext(); ) {
                    HarCookie c = iterator2.next();
                    c.setValue(stringify(c.getValue()));
                }
                for (Iterator<HarHeader> iterator2 = e.getRequest().getHeaders().iterator(); iterator2.hasNext(); ) {
                    HarHeader c = iterator2.next();
                    c.setValue(stringify(c.getValue()));
                }
            } catch (Exception e) {
                return har;
            }
        }

        getProxy().newHar();
        return har;
    }

    /**
     * saves har and performance log in file
     * @param fName
     */
    @SneakyThrows
    public void createArtifacts(String fName) {
        saveHar(fName);
        savePerfomanceLog(fName);
    }

    private static void saveHar(String fName) throws Exception {
        var har = ProxyContainer.getInstance().getHar();
        var harFile = new File(fName.concat(".har"));
        harFile.getParentFile().mkdirs();
        har.writeTo(harFile);
    }

    /**
     * saves performance log in file
     * @param fName
     * @throws IOException
     */
    public static void savePerfomanceLog(String fName) throws IOException {
        LogEntries les = WebDriverRunner.getWebDriver().manage().logs().get(LogType.PERFORMANCE);
        var fout = new File(fName.concat(".log"));
        var fos = new FileOutputStream(fout);
        try (var bw = new BufferedWriter(new OutputStreamWriter(fos))) {
            bw.write("[");
            var iter = les.iterator();
            while (iter.hasNext()) {
                var gson = new GsonBuilder().setPrettyPrinting().create();
                var jp = new JsonParser();
                JsonElement je = jp.parse(iter.next().getMessage());
                var prettyJsonString = gson.toJson(je.getAsJsonObject().get("message"));
                if (iter.hasNext()) {
                    bw.write(prettyJsonString.concat(","));
                }
                bw.newLine();
            }
            bw.write("]");
        }
    }

    private static class UrlFilter implements RequestFilter {
        private final String pattern;

        public UrlFilter(String pattern) {
            super();
            this.pattern = pattern;
        }

        /**
         * filters requests by pattern and returns default response
         * @param request The request object, including method, URI, headers, etc. Modifications to the request object will be reflected in the request sent to the server.
         * @param contents The request contents.
         * @param messageInfo Additional information relating to the HTTP message.
         * @return
         */
        @Override
        public HttpResponse filterRequest(
                HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo) {
            if (Pattern.compile(pattern).matcher(messageInfo.getOriginalUrl()).matches()) {
                final HttpResponse response =
                        new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.valueOf(200));
                response.headers().add(HttpHeaders.CONNECTION, "Close");
                return response;
            }
            return null;
        }
    }
}
