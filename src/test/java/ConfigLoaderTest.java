import org.junit.Assert;
import org.junit.Test;
import org.os.learning.Appenders.Appender;
import org.os.learning.Appenders.AppenderFactory;
import org.os.learning.Appenders.ConsoleAppender;
import org.os.learning.ConfigLoader;
import org.os.learning.LogLevel;
import org.os.learning.Logger;
import java.util.List;

import static org.junit.Assert.fail;

public class ConfigLoaderTest {
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
}
