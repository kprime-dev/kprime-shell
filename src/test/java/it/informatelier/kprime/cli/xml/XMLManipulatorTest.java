package it.informatelier.kprime.cli.xml;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Created by nipe on 23/11/16.
 */
public class XMLManipulatorTest {

    @Test
    public void triggersAddElementPathOnProject() {
        XmlManipulator manipulator = new XmlManipulator();
        Document doc = mockDoc();
        List<String> path = Arrays.asList("project");
        Node node = manipulator.addElementPath(doc, path);
        assertEquals("project",node.getNodeName());
    }

    @Test
    public void triggersAddElementPathOnDependency() {
        XmlManipulator manipulator = new XmlManipulator();
        Document doc = mockDoc();
        List<String> path = Arrays.asList("project","dependencies","dependency");
        Node node = manipulator.addElementPath(doc, path);
        assertEquals("dependency",node.getNodeName());
    }

    private Document mockDoc() {
        Document document = null;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element project = document.createElement("project");
            document.appendChild(project);

            Element dependencies = document.createElement("dependencies");
            project.appendChild(dependencies);

            Element dependency = document.createElement("dependency");
            dependencies.appendChild(dependency);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return document;
    }

    @Test
    public void createXmlElementStartDocument() {
        StringWriter writer = new StringWriter();
        try {
            XMLStreamWriter xmlWriter = XMLOutputFactory.newFactory().createXMLStreamWriter(writer);
            xmlWriter.writeStartDocument();
            xmlWriter.flush();
            assertThat(writer.toString(),is("<?xml version=\"1.0\" ?>"));
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void createXmlElementStartElement() {
        StringWriter writer = new StringWriter();
        try {
            XMLStreamWriter xmlWriter = XMLOutputFactory.newFactory().createXMLStreamWriter(writer);
            xmlWriter.writeStartElement("plugin");
            xmlWriter.writeCharacters("jetty");
            xmlWriter.writeEndElement();
            xmlWriter.flush();
            assertThat(writer.toString(),is("<plugin>jetty</plugin>"));
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void createXmlElementStartElementWithAttribute() {
        StringWriter writer = new StringWriter();
        try {
            XMLStreamWriter xmlWriter = XMLOutputFactory.newFactory().createXMLStreamWriter(writer);
            xmlWriter.writeStartElement("plugin");
            xmlWriter.writeAttribute("name","org.jetty");
            xmlWriter.writeCharacters("jetty");
            xmlWriter.writeEndElement();
            xmlWriter.flush();
            assertThat(writer.toString(),is("<plugin name=\"org.jetty\">jetty</plugin>"));
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void updateTextContentElement() throws XPathExpressionException, ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder b = f.newDocumentBuilder();


        String exampleString = "<pom><build></build></pom>";
        InputStream stream = new ByteArrayInputStream(exampleString.getBytes(StandardCharsets.UTF_8));
        Document doc = b.parse(stream);

        XPath xPath = XPathFactory.newInstance().newXPath();
        Node startDateNode = (Node) xPath.compile("/pom/build").evaluate(doc, XPathConstants.NODE);
        startDateNode.setTextContent("29/07/2015");

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc),
                new StreamResult(writer));
        writer.flush();
        assertThat(writer.toString(),is("<pom>\n    <build>29/07/2015</build>\n</pom>\n"));

    }

    @Test
    public void updateElement() throws XPathExpressionException, ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder b = f.newDocumentBuilder();


        String exampleString = "<pom><build><plugins></plugins></build></pom>";
        InputStream stream = new ByteArrayInputStream(exampleString.getBytes(StandardCharsets.UTF_8));
        Document doc = b.parse(stream);

        XPath xPath = XPathFactory.newInstance().newXPath();
        Node startDateNode = (Node) xPath.compile("/pom/build/plugins").evaluate(doc, XPathConstants.NODE);
        Element pluginElement = doc.createElement("plugin");

        Element groupIdElement = doc.createElement("groupId");
        groupIdElement.setTextContent("org.eclipse.jetty");
        pluginElement.appendChild(groupIdElement);

        Element artifactIdElement = doc.createElement("artifactId");
        artifactIdElement.setTextContent("jetty-maven-plugin");
        pluginElement.appendChild(artifactIdElement);

        Element versionElement = doc.createElement("version");
        versionElement.setTextContent("9.4.0-SNAPSHOT");
        pluginElement.appendChild(versionElement);

        startDateNode.appendChild(pluginElement);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        StringWriter writer = new StringWriter();
        //StreamResult sr = new StreamResult(new File("Data.xml"));
        transformer.transform(new DOMSource(doc),
                new StreamResult(writer));
        writer.flush();
        assertThat(writer.toString(),is(
                "<pom>" +
                "\n    <build>" +
                "\n        <plugins>" +
                "\n            <plugin>" +
                "\n                <groupId>org.eclipse.jetty</groupId>" +
                "\n                <artifactId>jetty-maven-plugin</artifactId>" +
                "\n                <version>9.4.0-SNAPSHOT</version>" +
                "\n            </plugin>" +
                "\n        </plugins>" +
                "\n    </build>" +
                "\n</pom>\n"));

    }

    @Test
    public void test_sax_parse() {
        String xml = "    <dependency>\n     <groupId>com.google.code.gson</groupId>\n     <artifactId>gson</artifactId>\n     <version>2.2.4</version>\n</dependency>\n ";
        Document document = XmlManipulator.parseDocument(xml);
        assertNotNull(document);
    }

    @Test
    public void test_replace_all() {
        String xml = "    <dependency>\n     <groupId>com.google.code.gson</groupId>\n     ";
        assertThat(xml.replaceAll("\n",System.lineSeparator()),is("    <dependency>\n     <groupId>com.google.code.gson</groupId>\n     "));
    }

}
