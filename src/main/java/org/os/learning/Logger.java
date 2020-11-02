package org.os.learning;

import lombok.Value;
import org.os.learning.Appenders.Appender;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Value
public class Logger {
    String name;
    List<Appender> appenders;

    public void debug(String message) {
        writeLog(LogLevel.DEBUG, message);
    }
    public void info(String message) {
        writeLog(LogLevel.LEVEL, message);
    }
    public void warning(String message) {
        writeLog(LogLevel.WARNING, message);
    }
    public void error(String message) {
        writeLog(LogLevel.ERROR, message);
    }
    public void fatal(String message) {
        writeLog(LogLevel.FATAL, message);
    }

    private void writeLog(LogLevel level, String message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String logMessage = String.format("[%s] %s: %s", formatter.format(now), level.name(), message);

        appenders.forEach(appenders -> appenders.writeLog(logMessage, level));
    }
}
