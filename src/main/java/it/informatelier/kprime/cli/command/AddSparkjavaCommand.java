package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.xml.XmlManipulator;

public class AddSparkjavaCommand extends CommandRun {
    @Override
    public String getName() {
        return "add-sparkjava";
    }

    @Override
    public String getLineDescription() {
        return "Adds Sparkjava dependency.";
    }

    @Override
    public void runBody() {
        new XmlManipulator().addSparkjavaDependency();
    }
}
