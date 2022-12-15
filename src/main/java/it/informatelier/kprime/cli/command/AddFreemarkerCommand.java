package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.xml.XmlManipulator;

public class AddFreemarkerCommand extends CommandRun {
    @Override
    public String getName() {
        return "add-freemarker";
    }

    @Override
    public String getLineDescription() {
        return "Adds FreeMarker dependency.";
    }

    @Override
    public void runBody() {
        new XmlManipulator().addFreeMarkerDependency();
    }
}
