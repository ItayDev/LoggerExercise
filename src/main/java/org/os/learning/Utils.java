package org.os.learning;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Utils {
    public static Stream<Element> iterateXmlNodeList(Element rootElement, String tagName) {
        NodeList elements = rootElement.getElementsByTagName(tagName);
        return IntStream.range(0, elements.getLength()).mapToObj(elements::item).map(obj -> (Element) obj);
    }
}
