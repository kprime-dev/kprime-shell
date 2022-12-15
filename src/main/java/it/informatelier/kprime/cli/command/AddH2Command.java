package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.xml.XmlManipulator;

public class AddH2Command extends CommandRun {
    @Override
    public String getName() {
        return "add-h2";
    }

    @Override
    public String getLineDescription() {
        return "Adds H2 dependency.";
    }

    @Override
    public void runBody() {
        new XmlManipulator().addH2Dependency();
    }
}
