package it.informatelier.kprime.cli;

import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestShell {

    @Test
    public void test_get_project_exist_tag() throws Exception {
        Project project = getProject("<project>valore</project>");
        assertTrue(project.getTokens().contains("project"));
    }

    @Test
    public void test_get_project_exist_value() throws Exception {
        Project project = getProject("<project>valore</project>");
        assertEquals(project.getValuesLine("project"), "valore");
    }

    @Test(expected = NullPointerException.class)
    public void test_get_project_by_null_content() throws Exception {
        Project project = getProject(null);
        assertTrue(project.isEmpty());
    }

    @Test
    public void change_value_of_tag() throws Exception {
        Project project = getProject("<project>valore</project>");
        project.changeValue("project", "nuovo_valore");

    }

    private Project getProject(String content) throws XMLStreamException {
        Shell shell = new Shell();
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        XMLStreamReader myXml = shell.getXmlStreamReader(inputStream);
        return shell.parseProjectTokens(myXml);
    }

}
