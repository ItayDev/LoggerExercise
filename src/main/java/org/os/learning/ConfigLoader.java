package org.os.learning;

import lombok.AllArgsConstructor;
import lombok.Cleanup;
import org.os.learning.Appenders.AppenderFactory;
import org.os.learning.Appenders.Appender;
import org.os.learning.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@AllArgsConstructor
public class ConfigLoader {
    private final AppenderFactory appenderFactory;

    List<Logger> readAndBuildConfiguration(String path) throws IOException, ParserConfigurationException, SAXException {
        @Cleanup InputStream in = new FileInputStream(path);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(in);
        document.getDocumentElement().normalize();

        Element loggers = (Element) document.getElementsByTagName("loggers").item(0);

        return iterateNodeList(loggers, "logger")
                .map(loggerDoc -> parseLoggerXmlElement((Element) loggerDoc))
                .collect(Collectors.toList());
    }

    private Logger parseLoggerXmlElement(Element xmlElement) {
        String loggerName = Optional.ofNullable(xmlElement.getAttribute("name")).orElseThrow();
        List<Appender> appenders = iterateNodeList(xmlElement, "appender")
                .map(xmlConfig -> appenderFactory.createAppender(xmlConfig.getAttribute("name"), xmlConfig))
                .collect(Collectors.toList());

        return new Logger(loggerName, appenders);
    }

    private Stream<Element> iterateNodeList(Element rootElement, String tagName) {
        NodeList elements = rootElement.getElementsByTagName(tagName);
        return IntStream.range(0, elements.getLength()).mapToObj(elements::item).map(obj -> (Element) obj);
    }
}
