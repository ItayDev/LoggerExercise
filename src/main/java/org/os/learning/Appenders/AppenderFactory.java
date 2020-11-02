package org.os.learning.Appenders;

import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Supplier;

public class AppenderFactory {
    private final HashMap<String, Supplier<Appender>> appenderCreator = new HashMap<>();
    public void registerAppender(String appenderName, Supplier<Appender> appenderSupplier) {
        appenderCreator.put(appenderName, appenderSupplier);
    }

    public Appender createAppender(String appenderName, Element xmlConfiguration) {
        Appender appender = Optional.ofNullable(appenderCreator.get(appenderName)).orElseThrow(() ->
                new IllegalArgumentException(String.format("There is no registered appender named %s", appenderName)))
                .get();
        appender.loadSettings(xmlConfiguration);

        return appender;
    }
}
