package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.xml.XmlManipulator;

public class AddJava8Command extends CommandRun {
    @Override
    public String getName() {
        return "add-java8";
    }

    @Override
    public String getLineDescription() {
        return "Adds Java8 source and targets.";
    }

    @Override
    public void runBody() {
        new XmlManipulator().addJava8Plugin();
    }
}
