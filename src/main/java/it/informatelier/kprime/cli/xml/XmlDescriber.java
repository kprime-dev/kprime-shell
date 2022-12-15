package it.informatelier.kprime.cli.xml;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nipe on 10/02/17.
 */
public class XmlDescriber {

    String describeNode(Node node) {
        String description = "";//describeNodeName(node);
        description+= describeNodeAttributes(node);
//        description+= describeNodeValue(node);
//        if (node.getFirstChild()==null)
//            description+= describeNodeContent(node);
        List<Node> siblings = computeSiblings(null, node, node.getNodeName());
        for (Node sibling: siblings) {
            if (sibling.getNodeName().equals("dependency")) description += describeDependency(sibling);
            else description += describeNodeContent(sibling);
        }
        //description = cleanDescription(description);
        return description;
    }

    private List<Node> computeSiblings(List<Node> siblings, Node node, String nodeName) {
        if (siblings==null)
            siblings = new ArrayList<>();
        if (node.getNextSibling()==null) return siblings;
        if (node.getNodeName().equals(nodeName))
            siblings.add(node);
        return computeSiblings(siblings,node.getNextSibling(),nodeName);
    }
    private String describeDependency(Node node) {
        String description = "dependency {";
        for (int i = 0; i<node.getChildNodes().getLength(); i++) {
            Node child = node.getChildNodes().item(i);
            //description += "name" + child.getNodeName();
            //description += ":" + child.getNodeValue();
            description += "=" + child.getTextContent();
        }
        description +=" }";
        return description;
    }

    private String cleanDescription(String description) {
        description = description.replace('\n',' ');
        description = description.replaceAll("\\[","\n");
        description = description.replaceAll("]","\n");
        return description;
    }

    private String describeNodeAttributes(Node node) {
        String description = "";
        NamedNodeMap attributes = node.getAttributes();
        if (attributes==null) return description;
        for (int i = 0 ;i< attributes.getLength(); i++) {
            Node nodeDescription = attributes.item(i);
            description += " "+nodeDescription.getNodeName()+":"+nodeDescription.getNodeValue();
        }
        return description;
    }

    private String describeNodeName(Node node) {
        return (node.getNodeName()==null)? "": "{"+node.getNodeName()+"}";
    }

    private String describeNodeContent(Node node) {
        String textContent = node.getTextContent();
        if (textContent==null) return "";
//        textContent = textContent.replace('\n',' ');
//        if (textContent.trim().equals("")) return "";
        return "["+textContent+"]";
    }

    private String describeNodeValue(Node node) {
        return (node.getNodeValue()==null)? "": "<"+node.getNodeValue()+">";
    }


}
