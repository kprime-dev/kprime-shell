package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.xml.XmlQuery;

public class PrintCommand extends CommandRun {

    @Override
    public String getName() {
        return "print";
    }

    @Override
    public String getLineDescription() {
        return "Prints a POM node.";
    }

    @Override
    public void runBody() {
        setResult(new XmlQuery().printElement(this.getArgTokens()));
    }
}
