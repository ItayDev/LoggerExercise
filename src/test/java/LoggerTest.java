import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.os.learning.Appenders.Appender;
import org.os.learning.LogLevel;
import org.os.learning.Logger;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class LoggerTest {
    private Logger logger;
    @Captor
    ArgumentCaptor<String> logMessageCaptor;
    @Captor
    ArgumentCaptor<LogLevel> logLevelCaptor;
    private Appender mockedAppender;
    private static final String logMessage = "Testing logger";
    private static final String loggerName = "Test";

    @Before
    public void init() {
        mockedAppender = Mockito.mock(Appender.class);
        logger = new Logger(loggerName, Arrays.asList(mockedAppender));
    }

    @Test
    public void loggerNameShouldBeCorrect() {
        Assert.assertEquals(logger.getName(), loggerName);
    }

    @Test
    public void loggerShouldAppendTheLogLevelInfoToMessage() {
        logger.info(logMessage);
        Mockito.verify(mockedAppender).writeLog(logMessageCaptor.capture(), logLevelCaptor.capture());
        String logLevelName = retrieveLevel(logMessageCaptor.getValue());
        LogLevel logLevel = logLevelCaptor.getValue();

        Assert.assertEquals(logLevel, LogLevel.INFO);
        Assert.assertEquals(logLevelName, LogLevel.INFO.name());
    }

    @Test
    public void loggerShouldAppendTheLogLevelWarningToMessage() {
        logger.warning(logMessage);
        Mockito.verify(mockedAppender).writeLog(logMessageCaptor.capture(), logLevelCaptor.capture());
        String logLevelName = retrieveLevel(logMessageCaptor.getValue());
        LogLevel logLevel = logLevelCaptor.getValue();

        Assert.assertEquals(logLevel, LogLevel.WARNING);
        Assert.assertEquals(logLevelName, LogLevel.WARNING.name());
    }

    @Test
    public void loggerShouldAppendTheLogLevelDebugToMessage() {
        logger.debug(logMessage);
        Mockito.verify(mockedAppender).writeLog(logMessageCaptor.capture(), logLevelCaptor.capture());
        String logLevelName = retrieveLevel(logMessageCaptor.getValue());
        LogLevel logLevel = logLevelCaptor.getValue();

        Assert.assertEquals(logLevel, LogLevel.DEBUG);
        Assert.assertEquals(logLevelName, LogLevel.DEBUG.name());
    }

    @Test
    public void loggerShouldAppendTheLogLevelErrorToMessage() {
        logger.error(logMessage);
        Mockito.verify(mockedAppender).writeLog(logMessageCaptor.capture(), logLevelCaptor.capture());
        String logLevelName = retrieveLevel(logMessageCaptor.getValue());
        LogLevel logLevel = logLevelCaptor.getValue();

        Assert.assertEquals(logLevel, LogLevel.ERROR);
        Assert.assertEquals(logLevelName, LogLevel.ERROR.name());
    }

    @Test
    public void loggerShouldAppendTheLogLevelFatalToMessage() {
        logger.fatal(logMessage);
        Mockito.verify(mockedAppender).writeLog(logMessageCaptor.capture(), logLevelCaptor.capture());
        String logLevelName = retrieveLevel(logMessageCaptor.getValue());
        LogLevel logLevel = logLevelCaptor.getValue();

        Assert.assertEquals(logLevel, LogLevel.FATAL);
        Assert.assertEquals(logLevelName, LogLevel.FATAL.name());
    }


    private String retrieveLevel(String logMessage) {
        String level = logMessage.split(" ")[2];

        return level.substring(0, level.length() -1);
    }
}
