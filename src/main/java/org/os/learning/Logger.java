package org.os.learning;

import lombok.Value;
import org.os.learning.Appenders.Appender;

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
        // Will implemnent formatting later
        appenders.forEach(appenders -> appenders.writeLog(message, level));
    }
}
