package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.xml.XmlQuery;

public class QueryCommand extends CommandRun {
    @Override
    public String getName() {
        return "query";
    }

    @Override
    public String getLineDescription() {
        return "Query POM nodes.";
    }

    @Override
    public void runBody() {
        setResult(new XmlQuery().queryElement(getArgTokens()));

    }
}
