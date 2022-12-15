package it.informatelier.kprime.cli.xml;

import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.PrintStream;

/**
 * Created by nipe on 10/02/17.
 */
public class XmlPrinter {
    int indent = 0;
    final String basicIndent = " ";
    private void outputIndentation() {
        for (int i = 0; i < indent; i++) {
            System.out.print(basicIndent);
        }
    }

    public void print(Node node) {

        outputIndentation();
        int type = node.getNodeType();

        PrintStream out = System.out;
        switch (type) {
            case Node.ATTRIBUTE_NODE:
                out.print("ATTR:");
                printlnCommon(node);
                break;

            case Node.CDATA_SECTION_NODE:
                out.print("CDATA:");
                printlnCommon(node);
                break;

            case Node.COMMENT_NODE:
                out.print("COMM:");
                printlnCommon(node);
                break;

            case Node.DOCUMENT_FRAGMENT_NODE:
                out.print("DOC_FRAG:");
                printlnCommon(node);
                break;

            case Node.DOCUMENT_NODE:
                out.print("DOC:");
                printlnCommon(node);
                break;

            case Node.DOCUMENT_TYPE_NODE:
                out.print("DOC_TYPE:");
                printlnCommon(node);
                NamedNodeMap nodeMap = ((DocumentType)node).getEntities();
                indent += 2;
                for (int i = 0; i < nodeMap.getLength(); i++) {
                    Entity entity = (Entity)nodeMap.item(i);
                    print(entity);
                }
                indent -= 2;
                break;

            case Node.ELEMENT_NODE:
                out.print("ELEM:");
                printlnCommon(node);

                NamedNodeMap atts = node.getAttributes();
                indent += 2;
                for (int i = 0; i < atts.getLength(); i++) {
                    Node att = atts.item(i);
                    print(att);
                }
                indent -= 2;
                break;

            case Node.ENTITY_NODE:
                out.print("ENT:");
                printlnCommon(node);
                break;

            case Node.ENTITY_REFERENCE_NODE:
                out.print("ENT_REF:");
                printlnCommon(node);
                break;

            case Node.NOTATION_NODE:
                out.print("NOTATION:");
                printlnCommon(node);
                break;

            case Node.PROCESSING_INSTRUCTION_NODE:
                out.print("PROC_INST:");
                printlnCommon(node);
                break;

            case Node.TEXT_NODE:
                out.print("TEXT:");
                printlnCommon(node);
                break;

            default:
                out.print("UNSUPPORTED NODE: " + type);
                printlnCommon(node);
                break;
        }

        indent++;
        for (Node child = node.getFirstChild(); child != null;
             child = child.getNextSibling()) {
        }
        indent--;
        if (node.getNextSibling()!=null) print (node.getNextSibling());
    }

    private void printlnCommon(Node n) {
        PrintStream out = System.out;
        out.print(" nodeName=\"" + n.getNodeName() + "\"");

        String val = n.getNamespaceURI();
        if (val != null) {
            out.print(" uri=\"" + val + "\"");
        }

        val = n.getPrefix();

        if (val != null) {
            out.print(" pre=\"" + val + "\"");
        }

        val = n.getLocalName();
        if (val != null) {
            out.print(" local=\"" + val + "\"");
        }

        val = n.getNodeValue();
        if (val != null) {
            out.print(" nodeValue=");
            if (val.trim().equals("")) {
                // Whitespace
                //out.print("[WS]");
            }
            else {
                out.print("\"" + n.getNodeValue() + "\"");
            }
        }
        out.println();
    }
}
