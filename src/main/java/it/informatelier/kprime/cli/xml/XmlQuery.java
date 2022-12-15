package it.informatelier.kprime.cli.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nipe on 10/02/17.
 */
public class XmlQuery {

    public String queryElement(List<String> path) {
        path = computePathAlias(path);
        String nodepath = computeQueryPath(path);
        String result = "["+nodepath+"]=";
        XmlDescriber describer = new XmlDescriber();
        try {
            FileInputStream file = (new XmlFileManager()).silentOpenPomFile();
            Document doc = getDocument(file);
            XPath xPath = XPathFactory.newInstance().newXPath();
            Node node = (Node) xPath.compile(nodepath).evaluate(doc, XPathConstants.NODE);
            result += (node==null)? "not found." : describer.describeNode(node);
        } catch(Exception e)
        {
            // silent
            result += e.getMessage();
        }
        return result;
    }

    public String printElement(List<String> path) {
        path = computePathAlias(path);
        String nodepath = computeQueryPath(path);
        String result = "["+nodepath+"]=";
        XmlPrinter printer = new XmlPrinter();
        try {
            FileInputStream file = (new XmlFileManager()).silentOpenPomFile();
            Document doc = getDocument(file);
            XPath xPath = XPathFactory.newInstance().newXPath();
            Node node = (Node) xPath.compile(nodepath).evaluate(doc, XPathConstants.NODE);
            printer.print(node);
        } catch(Exception e)
        {
            // silent
            result += e.getMessage();
        }
        return result;
    }

    private List<String> computePathAlias(List<String> path) {
        String alias = path.get(0);
        if (alias.equals("dependencies")) return Arrays.asList("project","dependencies","dependency");
        return path;
    }

    String computeQueryPath(List<String> path) {
        String nodepath = "";
        for(String step: path) {
            nodepath += "/"+step;
        }
        return nodepath;
    }

    private Document getDocument(FileInputStream file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder b = f.newDocumentBuilder();
        return b.parse(file);
    }

}