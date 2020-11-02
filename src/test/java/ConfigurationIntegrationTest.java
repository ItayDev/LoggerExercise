import org.junit.Assert;
import org.junit.Test;
import org.os.learning.Appenders.Appender;
import org.os.learning.Appenders.AppenderFactory;
import org.os.learning.Appenders.ConsoleAppender;
import org.os.learning.ConfigLoader;
import org.os.learning.LogLevel;
import org.os.learning.LogManager;
import org.os.learning.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.fail;

public class ConfigurationIntegrationTest {
    @Test
    public void shouldParseCorrectly() {
        AppenderFactory factory = new AppenderFactory();
        factory.registerAppender("console", ConsoleAppender::new);

        ConfigLoader configLoader = new ConfigLoader(factory);

        try {
            List<Logger> loggers = configLoader.readAndBuildConfiguration("src/test/resources/exampleConf.xml");

            Assert.assertEquals(loggers.size(), 1);
            Logger logger = loggers.get(0);
            Assert.assertEquals(logger.getAppenders().size(), 1);
            ConsoleAppender consoleAppender = (ConsoleAppender) logger.getAppenders().get(0);
            Assert.assertEquals(consoleAppender.getColorMap().get(LogLevel.ERROR), "red");
        } catch (Exception exception){
            fail();
        }
    }

    @Test
    public void shouldGetTheCorrectLogger() throws ParserConfigurationException, SAXException, IOException {
        LogManager.setConfigurationLocation("src/test/resources/exampleConf.xml");

        Logger logger = LogManager.getLogger("main").orElseThrow(IllegalArgumentException::new);

        Assert.assertEquals(logger.getName(), "main");
    }
}
