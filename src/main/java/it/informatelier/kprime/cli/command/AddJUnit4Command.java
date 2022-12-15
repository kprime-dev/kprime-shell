package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.xml.XmlManipulator;

public class AddJUnit4Command extends CommandRun {
    @Override
    public String getName() {
        return "add-junit4";
    }

    @Override
    public String getLineDescription() {
        return "Adds JUnit4 dependency.";
    }

    @Override
    public void runBody() {
        new XmlManipulator().addJunit4Dependency();
    }
}
