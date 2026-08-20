package ru.autotestframework.testlistener;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.autotestframework.testdispatcher.Launcher;
import ru.autotestframework.testdispatcher.jms.BDDConsumer;
import ru.autotestframework.testdispatcher.jms.BDDProducer;

@Slf4j
public class FeatureExecutor extends Thread {
    static SecureRandom RANDOM;

    static {
        try {
            RANDOM = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String LISTENER_CLIENT_ID;
    private static final long WAIT_FOR_FEATURES_TIMEOUT = 1000L;
    private static final Path ALLURE_RESULTS_DIR = Paths.get("build//reports//allure-results");
    private static AtomicBoolean busy = new AtomicBoolean(false);
    private static BDDProducer producer;
    private static BDDConsumer consumer;
    private static FeatureExecutor INSTANCE;

    @SneakyThrows
    public static FeatureExecutor getInstance(String brokerUrl) {
        LISTENER_CLIENT_ID = "BDDListener" + InetAddress.getLocalHost().getHostName() + RANDOM.nextInt();

        if (INSTANCE == null) {
            INSTANCE = new FeatureExecutor(brokerUrl);
        }

        return INSTANCE;
    }

    private FeatureExecutor(String brokerUrl) {
        consumer = new BDDConsumer(brokerUrl, LISTENER_CLIENT_ID + "consumer", Launcher.FEATURES_QUEUE, null);
        producer = new BDDProducer(brokerUrl, LISTENER_CLIENT_ID + "producer", Launcher.RESULTS_QUEUE);
        log.info("Producer and consumer created");
    }

    @Override
    @SneakyThrows
    public synchronized void run() {
        while (true) {
            if (busy.get()) {
                Thread.sleep(5000);
                log.info("Executor busy");
                continue;
            }
            busy.set(true);
            log.info("Feature executor run");
            Map<String, String> message = new HashMap<>();
            try {
                message = consumer.receiveMap(WAIT_FOR_FEATURES_TIMEOUT);
                log.info("Message recieved");
            } catch (Exception e) {
                log.info("Error receiving message");
                log.error(e.getMessage());
            }
            if (message.isEmpty()) {
                log.info("Message is empty");
                break;
            }
            String feature = message.get("Feature");
            String runId = message.get("RunID");
            log.debug(feature + " got from queue" + runId);
            byte[] results = executeFeature(feature, runId);
            try {
                producer.sendBytes(results);
                log.debug("sent results of " + feature);
            } catch (Exception e) {
                log.error("Ошибка выполнения feature : {}", feature, e);
            }
            busy.set(false);
        }
    }

    private byte[] executeFeature(String feature, String runId) {
        long executionId = System.currentTimeMillis();
        ToolingAPI.executeFeatures(feature, "allure-results" + executionId, runId);
        byte[] res = getExecutionResults(ALLURE_RESULTS_DIR);
        busy.set(true);
        return res;
    }

    private byte[] getExecutionResults(Path resultsDir) {
        Path zip = Paths.get(resultsDir + ".zip");
        try (ZipOutputStream zipOutputStream =
                new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(zip, CREATE_NEW)))) {
            try (Stream<Path> paths = Files.walk(resultsDir)) {
                paths.filter(path -> path.toFile().isFile()).forEach(file -> addZipEntry(zipOutputStream, file));
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return zipToBytes(zip);
    }

    private void addZipEntry(ZipOutputStream zipOutputStream, Path file) {
        String fileName = file.getFileName().toString();
        try {
            zipOutputStream.putNextEntry(new ZipEntry(fileName));
            zipOutputStream.write(Files.readAllBytes(file));
            zipOutputStream.closeEntry();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private byte[] zipToBytes(Path zip) {
        try {
            return Files.readAllBytes(zip);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return new byte[0];
    }
}
