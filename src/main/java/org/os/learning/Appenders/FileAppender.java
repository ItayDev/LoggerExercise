package org.os.learning.Appenders;

import lombok.Cleanup;
import org.os.learning.LogLevel;
import org.w3c.dom.Element;

import java.io.*;

public class FileAppender implements Appender {
    private String logPath;

    @Override
    public void writeLog(String message, LogLevel level) {
        File file = new File(logPath);
        try {
            file.createNewFile();
            @Cleanup PrintWriter writer = new PrintWriter(new FileOutputStream(file));
            writer.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadSettings(Element xmlSettings) {
        Element settings = (Element) xmlSettings.getElementsByTagName("settings");
        Element path = (Element) settings.getElementsByTagName("path");

        logPath = path.getAttribute("value");
    }
}
