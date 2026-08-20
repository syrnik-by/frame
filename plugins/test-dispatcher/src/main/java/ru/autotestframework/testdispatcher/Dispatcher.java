package ru.autotestframework.testdispatcher;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ru.autotestframework.testdispatcher.jms.BDDConsumer;
import ru.autotestframework.testdispatcher.jms.BDDProducer;
import ru.autotestframework.testdispatcher.rmi.RMILifeCycleManager;
import ru.autotestframework.testdispatcher.rmi.RMIListener;

@Slf4j
public class Dispatcher {
    static SecureRandom RANDOM;

    static {
        try {
            RANDOM = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String DISPATCHER_CLIENT_ID = "BDDDispatcher";
    //    private static final Path FEATURE_FILES_DIR = Paths.get("plugins//test-dispatcher//src//test//resources");
    private static final Path FEATURE_FILES_DIR = Paths.get("plugins/test-dispatcher/", "src//test//java");
    private static final Path ALLURE_RESULTS_DIR = Paths.get("build//reports//allure-results");
    private static final long WAIT_FOR_RESULTS_TIMEOUT = 120000L;

    private String brokerUrl;
    private String features;
    private RMILifeCycleManager rmiManager;
    private int executionQueueSize;

    public Dispatcher(String brokerUrl, String features) {
        this.brokerUrl = brokerUrl;
        this.features = features;
        this.rmiManager = new RMILifeCycleManager();
    }

    public void launch() throws Exception {
        createResultsDir();
        initExecutionQueue();
        Collection<RMIListener> listeners = rmiManager.initHosts(true);

        rmiManager.setExecutionNodes();

        ExecutorService EXEC = Executors.newCachedThreadPool();

        String projectDir = System.getProperty("projectDir");
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (RMIListener listener : listeners) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(
                    () -> {
                        try {
                            log.info("Start processing listener " + listener + "\n" + listener.healthCheck() + "\n"
                                    + System.currentTimeMillis());
                            listener.initProcessing(brokerUrl, projectDir);
                        } catch (RemoteException e) {
                            log.error("listener processing error ");
                            log.error(e.getMessage());
                        }
                        log.info("End processing listener " + listener + "\n" + System.currentTimeMillis());
                        try {
                            TimeUnit.SECONDS.sleep(5);
                        } catch (InterruptedException e) {
                            log.error("interr", e);
                        }
                    },
                    EXEC);
            futures.add(future);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        consumeResults();
    }

    private void createResultsDir() {
        try {
            Files.createDirectories(ALLURE_RESULTS_DIR);
        } catch (IOException e) {
            log.error("Unable to create resDir", e);
        }
    }

    private void initExecutionQueue() {
        List<String> executionQueue = getFeatures(FEATURE_FILES_DIR.resolve(features));

        try (BDDProducer bddProducer = new BDDProducer(brokerUrl, DISPATCHER_CLIENT_ID, Launcher.FEATURES_QUEUE)) {
            executionQueue.forEach(feature -> {
                //                String featureName = feature.subpath(FEATURE_FILES_DIR.getNameCount(),
                // feature.getNameCount()).toString();
                bddProducer.sendFeature(feature, String.valueOf(RANDOM.nextInt()));
                log.info(feature + " sent to queue.");
            });
        }
        executionQueueSize = executionQueue.size();
    }

    private List<String> getFeatures(Path executionPath) {
        List<String> executionQueue = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(executionPath)) {
            paths.filter(p -> p.toFile().isFile()).forEach(path -> {
                try {
                    executionQueue.addAll(getFeatureTagFromFile(path));
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            });
        } catch (IOException e) {
            log.error("unable to getFeatures", e);
        }
        return executionQueue;
    }

    private List<String> getFeatureTagFromFile(Path path) throws IOException {
        Pattern pattern = Pattern.compile("^@ExternalId=\\S*");
        List<String> strings = Files.lines(path)
                .map(String::trim)
                .filter(pattern.asPredicate())
                .collect(Collectors.toList());
        pattern = Pattern.compile("^@WorkItemIds\\(\"[0-9]+\"\\)\\S*");
        strings.addAll(Files.lines(path)
                .map(String::trim)
                .filter(pattern.asPredicate())
                .map(string -> StringUtils.substringBetween(string, "\""))
                .collect(Collectors.toList()));
        return strings;
    }

    private void consumeResults() throws Exception {
        try (BDDConsumer consumer = new BDDConsumer(brokerUrl, DISPATCHER_CLIENT_ID, Launcher.RESULTS_QUEUE, null)) {
            int resultsCount = 0;
            while (resultsCount != executionQueueSize) {
                byte[] results = consumer.receiveBytes(WAIT_FOR_RESULTS_TIMEOUT);
                if (results.length != 0) {
                    resultsCount++;
                    processResults(results);
                } else {
                    rmiManager.healthCheck();
                }
            }
        }
    }

    private void processResults(byte[] results) throws IOException {
        byte[] buffer = new byte[2048];
        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(results))) {
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                extractZipEntry(zipInputStream, zipEntry, buffer);
                zipInputStream.closeEntry();
            }
        }
    }

    private void extractZipEntry(ZipInputStream zipInputStream, ZipEntry file, byte[] buffer) throws IOException {
        String fileName = file.getName();
        try (FileOutputStream fileOutputStream = new FileOutputStream(
                        ALLURE_RESULTS_DIR.resolve(fileName).toString());
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, buffer.length)) {
            int length;
            while ((length = zipInputStream.read(buffer)) > 0) {
                bufferedOutputStream.write(buffer, 0, length);
            }
        }
    }
}
