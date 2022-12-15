package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.xml.XmlManipulator;

public class AddSlf4jCommand extends CommandRun {
    @Override
    public String getName() {
        return "add-slf4j";
    }

    @Override
    public String getLineDescription() {
        return "Adds Slf4j dependency.";
    }

    @Override
    public void runBody() {
        new XmlManipulator().addSlf4jDependency();
    }
}
