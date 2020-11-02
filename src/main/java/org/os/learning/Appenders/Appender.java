package org.os.learning.Appenders;

import org.os.learning.LogLevel;
import org.w3c.dom.Element;

public interface Appender {
    void writeLog(String message, LogLevel level);
    void loadSettings(Element xmlSettings);
}
