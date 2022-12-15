package it.informatelier.kprime.cli.xml;

import it.informatelier.kprime.cli.command.Command;
import it.informatelier.kprime.cli.command.MockCommand;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XmlQueryTest {

    @Test
    public void computes_correct_query_path_for_project() {
        Command command = new MockCommand("query project");
        XmlQuery query = new XmlQuery();
        String queryPath = query.computeQueryPath(command.getArgTokens());
        assertEquals("/project",queryPath);
    }

    @Test
    public void computes_correct_query_path_for_project_alfa() {
        Command command = new MockCommand("query project alfa");
        XmlQuery query = new XmlQuery();
        String queryPath = query.computeQueryPath(command.getArgTokens());
        System.out.println(queryPath);
        assertEquals("/project/alfa",queryPath);
    }
}
