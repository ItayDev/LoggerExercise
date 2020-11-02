package org.os.learning.Appenders;

import org.os.learning.LogLevel;
import org.os.learning.Utils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ConsoleAppender implements Appender {
    private final Map<LogLevel, String> colorMap = initializeDefaultColors();

    @Override
    public void writeLog(String message, LogLevel level) {
        System.out.println(message);
    }

    @Override
    public void loadSettings(Element xmlSettings) {
        Element mappings = (Element) xmlSettings.getElementsByTagName("mapping").item(0);
        Element maps = (Element) mappings.getElementsByTagName("map");
        Utils.iterateXmlNodeList(maps, "map").forEach(map -> {
            LogLevel level = LogLevel.valueOf(map.getAttribute("level"));
            String color = Optional.ofNullable(map.getAttribute("value")).orElseThrow();

            colorMap.put(level, color);
        });
    }

    private Map<LogLevel, String > initializeDefaultColors() {
        return Arrays.stream(LogLevel.values()).collect(Collectors.toMap(logLevel -> logLevel, logLevel -> "white"));
    }
}
