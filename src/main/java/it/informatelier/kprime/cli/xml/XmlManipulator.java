package it.informatelier.kprime.cli.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Manipulate XML
 */
public class XmlManipulator {


    public static final String DEPENDENCY_SCOPE_TEST = "test";
    public static final String DEPENDENCY_SCOPE_ALL = null;
    private final XmlFileManager xmlFileManager = new XmlFileManager();

    public enum InitPomArgs {
        ARTIFACT_ID,GROUP_ID,VERSION;

    }

    public void initPom(Map<String,String> args) {
        try {
            Document doc =  (DocumentBuilderFactory.newInstance()).newDocumentBuilder().newDocument();
            Element rootElement = doc.createElement("project");
            addElement(doc, rootElement, "modelVersion", "4.0.0");
            addElement(doc, rootElement, "groupId", args.get(InitPomArgs.GROUP_ID.name()));
            addElement(doc, rootElement, "artifactId", args.get(InitPomArgs.ARTIFACT_ID.name()));
            addElement(doc, rootElement, "version", args.get(InitPomArgs.VERSION.name()));
            doc.appendChild(rootElement);

            Element propsElement = doc.createElement("properties");
            addElement(doc, propsElement, "project.build.sourceEncoding", "UTF-8");
            addElement(doc, propsElement, "project.reporting.outputEncoding", "UTF-8");
            rootElement.appendChild(propsElement);

            saveDoc(doc);
            printDoc(doc);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void addJettyPlugin() {
        importPlugin("plugin-jetty.xml");
    }

    public void addJava8Plugin() {
        importPlugin("plugin-java8.xml");
    }

    public void addPlugin(String xmlFragment) {
        importPlugin(xmlFragment);
    }

    public void addFatJarPlugin() {
        importPlugin("plugin-fatjar.xml");
    }
    public void addSpringBootPlugin() {
        importPlugin("springboot/plugin-springboot.xml");
    }

    public void addReportPlugin() {
      importPlugin("plugin-report.xml");
    }

    public void addDropwizardPlugin() {
        importPlugin("dropwizard/plugin-dropwizard.xml");
    }

    private void importPlugin(String filename) {
        try {
            FileInputStream pomfile = xmlFileManager.silentOpenPomFile();
            Document pomdoc = getDocument(pomfile);
            InputStream pluginfile = XmlManipulator.class.getClassLoader().getResourceAsStream(filename);
            Document pluginDoc = getDocument(pluginfile);
            Node pomPlugins = addElementPath(pomdoc,Arrays.asList("project","build", "plugins"));
            Node firstChild = pluginDoc.getFirstChild();
            Node nodeImported = pomdoc.importNode(firstChild, true);
            pomPlugins.appendChild(nodeImported);
            saveDoc(pomdoc);
            printDoc(pomdoc);
            pomfile.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void addResources() {
        try {
            String filename = "resources.xml";
            FileInputStream pomfile = xmlFileManager.silentOpenPomFile();
            Document pomdoc = getDocument(pomfile);
            InputStream pluginfile = XmlManipulator.class.getClassLoader().getResourceAsStream(filename);
            Document pluginDoc = getDocument(pluginfile);
            Node pomPlugins = addElementPath(pomdoc,Arrays.asList("project","build"));
            Node firstChild = pluginDoc.getFirstChild();
            Node nodeImported = pomdoc.importNode(firstChild, true);
            pomPlugins.appendChild(nodeImported);
            saveDoc(pomdoc);
            printDoc(pomdoc);
            pomfile.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void addFilteredResources() {

    }

    public void addSkinFluido() {
        try {
            FileInputStream file = xmlFileManager.silentOpenPomFile();
            Document doc = getDocument(file);
            Node project = addElementPath(doc, Arrays.asList("project"));
            Element skinElement = doc.createElement("skin");
            addElement(doc, skinElement, "groupId", "org.apache.maven.skins");
            addElement(doc, skinElement, "artifactId", "maven-fluido-skin");
            addElement(doc, skinElement, "version", "1.6");
            project.appendChild(skinElement);
            saveDoc(doc);
            printDoc(doc);
            file.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void addJunit4Dependency() {
        addDependency("junit", "junit", "4.12", DEPENDENCY_SCOPE_TEST);
    }

    public void addDddDependency() {
        addDependency("it.nipe.ddd", "ddd", "2.0.0", DEPENDENCY_SCOPE_ALL);
    }

    public void addH2Dependency() {
        addDependency("com.h2database", "h2", "1.4.193", DEPENDENCY_SCOPE_ALL);
    }
    public void addPdfBoxDependency() {
        addDependency("org.apache.pdfbox", "pdfbox", "2.0.4", DEPENDENCY_SCOPE_ALL);
    }

    public void addFreeMarkerDependency() {
        addDependency("org.freemarker", "freemarker", "2.3.23", DEPENDENCY_SCOPE_ALL);
    }

    public void addSparkjavaDependency() {
        addDependency("com.sparkjava","spark-core","2.6.0", DEPENDENCY_SCOPE_ALL);
    }

    public void addHandelbarsDependency() {
        addDependency("com.sparkjava","spark-template-handlebars","2.5.5", DEPENDENCY_SCOPE_ALL);
    }

    public void addSlf4jDependency() {
        addDependency("org.slf4j","slf4j-simple","1.7.13", DEPENDENCY_SCOPE_ALL);
    }

    public void addDropwizardDependency() { importDependency("dropwizard/dependency-dropwizard.xml"); }

    public void addSpringBootDependency() { importDependency("springboot/dependency-springboot.xml"); }

    public void addDependency(String groupId, String artifactId, String version, String scope) {
        try {
            FileInputStream file = xmlFileManager.silentOpenPomFile();
            Document doc = getDocument(file);
            addElementPath(doc, Arrays.asList("project","dependencies"));
            Element pluginElement = doc.createElement("dependency");
            addElement(doc, pluginElement, "groupId", groupId);
            addElement(doc, pluginElement, "artifactId", artifactId);
            addElement(doc, pluginElement, "version", version);
            if (scope!=null) {
                addElement(doc, pluginElement, "scope", scope);
            }
            appendDependencyElement(doc, pluginElement);
            saveDoc(doc);
            printDoc(doc);
            file.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void importDependency(Document dependencyDoc) {
        try {
            FileInputStream pomfile = xmlFileManager.silentOpenPomFile();
            Document pomdoc = getDocument(pomfile);
            Node pomDependencies = addElementPath(pomdoc,Arrays.asList("project","dependencies"));
            Node firstChild = dependencyDoc.getFirstChild();
            Node nodeImported = pomdoc.importNode(firstChild, true);
            pomDependencies.appendChild(nodeImported);
            saveDoc(pomdoc);
            printDoc(pomdoc);
            pomfile.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void importDependency(String filename) {
        try {
            InputStream dependencyFile = XmlManipulator.class.getClassLoader().getResourceAsStream(filename);
            Document dependencyDoc = getDocument(dependencyFile);
            importDependency(dependencyDoc);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void addSpringBootParent() { importParent("springboot/parent-springboot.xml"); }

    private void importParent(String filename) {
        try {
            FileInputStream pomfile = xmlFileManager.silentOpenPomFile();
            Document pomdoc = getDocument(pomfile);
            InputStream dependencyFile = XmlManipulator.class.getClassLoader().getResourceAsStream(filename);
            Document dependencyDoc = getDocument(dependencyFile);
            Node pomDependencies = addElementPath(pomdoc,Arrays.asList("project"));
            Node firstChild = dependencyDoc.getFirstChild();
            Node nodeImported = pomdoc.importNode(firstChild, true);
            pomDependencies.appendChild(nodeImported);
            saveDoc(pomdoc);
            printDoc(pomdoc);
            pomfile.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private Document getDocument(InputStream file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder b = f.newDocumentBuilder();
        return b.parse(file);
    }

    private void appendPluginElement(Document doc, Element pluginElement) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        Node startDateNode = (Node) xPath.compile("/project/build/plugins").evaluate(doc, XPathConstants.NODE);
        startDateNode.appendChild(pluginElement);
    }

    private void appendDependencyElement(Document doc, Element pluginElement) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        Node startDateNode = (Node) xPath.compile("/project/dependencies").evaluate(doc, XPathConstants.NODE);
        startDateNode.appendChild(pluginElement);
    }

    private void addElement(Document doc, Element pluginElement, String groupId, String textContent) {
        Element groupIdElement = doc.createElement(groupId);
        groupIdElement.setTextContent(textContent);
        pluginElement.appendChild(groupIdElement);
    }

    private void printDoc(Document doc) throws TransformerException {
        Transformer transformer = getTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        System.out.print(writer.toString());
    }

    private void saveDoc(Document doc) throws TransformerException {
        Transformer transformer = getTransformer();
        StreamResult sr = new StreamResult(new File("pom.xml"));
        transformer.transform(new DOMSource(doc),sr);
    }

    private Transformer getTransformer() throws TransformerConfigurationException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        return transformer;
    }

    Node addElementPath(Document doc, List<String> path) {
        Node parent=null;
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            parent = (Node) xPath.compile("/"+path.get(0)).evaluate(doc, XPathConstants.NODE);
            String strPath = "/"+path.get(0);
            path = path.subList(1,path.size());
            for (String token : path) {
                strPath += "/"+token;
                Node node = (Node) xPath.compile(strPath).evaluate(doc, XPathConstants.NODE);
                if (node==null) {
                    Element elem = doc.createElement(token);
                    parent.appendChild(elem);
                    parent = elem;
                    System.out.println("parent:"+parent.getNodeName());
                } else {
                    parent = node;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return parent;
    }

    public static Document parseDocument(String gist) {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder b = null;
        try {
            b = f.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document dependencyDoc=null;
        InputSource inputSource = new InputSource(new StringReader(gist));
        try {
            dependencyDoc = b.parse(inputSource);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dependencyDoc;
    }

}
