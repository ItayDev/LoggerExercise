package org.os.learning.Appenders;

import lombok.Cleanup;
import org.os.learning.LogLevel;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class RollingFileAppender implements Appender {
    boolean splitByLevel;
    long maxSize;
    String basePath;

    @Override
    public void writeLog(String message, LogLevel level) {
        String logFileName = basePath + File.pathSeparator;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();

        logFileName += dtf.format(now);

        if(splitByLevel) {
            logFileName += String.format("-%s", level.name());
        }

        logFileName += ".log";

        logFileName = validateLogFileSize(logFileName);

        File file = new File(logFileName);

        try {
            file.createNewFile();

            @Cleanup PrintWriter writer = new PrintWriter(new FileOutputStream(file));
            writer.println(message);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void loadSettings(Element xmlSettings) {
        Element settings = (Element) xmlSettings.getElementsByTagName("settings");
        Element rollingSettings = (Element) settings.getElementsByTagName("rollingSettings");
        maxSize = Optional.of(Long.parseLong(rollingSettings.getAttribute("size"))).orElse(1000000L);
        splitByLevel = Optional.of(parseBooleanString(rollingSettings.getAttribute("splitLevels"))).orElse(false);
        basePath = Optional.ofNullable(rollingSettings.getAttribute("basePath")).orElse("logs");
    }

    private boolean parseBooleanString(String booleanSpecifier) {
        if (booleanSpecifier.equals("True") || booleanSpecifier.equals("true")) {
            return true;
        } else if(booleanSpecifier.equals("False") || booleanSpecifier.equals("false")) {
            return false;
        } else {
            throw new IllegalArgumentException(String.format("Boolean value was expected. Got %s", booleanSpecifier));
        }
    }

    private String validateLogFileSize(String logName) {
        String finalLogName = logName;
        int retryTimes = 1;

        long fileSize = (new File(finalLogName)).getTotalSpace();

        while (fileSize > maxSize) {
            finalLogName = logName.substring(0, logName.length() -4);
            finalLogName += retryTimes + ".log";

            fileSize = (new File(finalLogName)).getTotalSpace();
        }

        return finalLogName;
    }
}
