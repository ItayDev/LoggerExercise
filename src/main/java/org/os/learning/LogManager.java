package org.os.learning;

import org.os.learning.Appenders.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class LogManager {
    private Map<String, Logger> loggers = null;
    private final ConfigLoader configLoader;
    private AppenderFactory appenderFactory;
    private String configurationLocation = "resources/logger.xml";
    private static final LogManager instance = new LogManager();

    private LogManager() {
        registerDefaultAppenders();

        configLoader = new ConfigLoader(appenderFactory);
    }

    public static void setConfigurationLocation(String path) {
        instance.configurationLocation = path;
    }

    public static void registerAppender(String appenderName, Supplier<Appender> supplier) throws IOException, SAXException, ParserConfigurationException {
        instance.appenderFactory.registerAppender(appenderName, supplier);
        instance.initializeLoggers();
    }

    public static Optional<Logger> getLogger(String loggerName) throws IOException, SAXException, ParserConfigurationException {
        if(instance.loggers == null) {
            instance.initializeLoggers();
        }

        return Optional.ofNullable(instance.loggers.get(loggerName));
    }

    private void registerDefaultAppenders() {
        appenderFactory = new AppenderFactory();
        appenderFactory.registerAppender("file", FileAppender::new);
        appenderFactory.registerAppender("console", ConsoleAppender::new);
        appenderFactory.registerAppender("http", HttpAppender::new);
        appenderFactory.registerAppender("rollingFile", RollingFileAppender::new);
    }

    private void initializeLoggers() throws ParserConfigurationException, SAXException, IOException {
        loggers = configLoader.readAndBuildConfiguration(instance.configurationLocation).stream()
        .collect(Collectors.toMap(Logger::getName, logger -> logger));

    }
}
