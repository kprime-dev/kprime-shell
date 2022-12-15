package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.xml.XmlManipulator;

public class AddJettyCommand extends CommandRun {
    @Override
    public String getName() {
        return "add-jetty";
    }

    @Override
    public String getLineDescription() {
        return "Adds Jetty plugin.";
    }

    @Override
    public void runBody() {
        new XmlManipulator().addJettyPlugin();
    }
}
