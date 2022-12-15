package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.xml.XmlManipulator;

public class AddFatJarCommand extends CommandRun {
    @Override
    public String getName() {
        return "add-fatjar-plugin";
    }

    @Override
    public String getLineDescription() {
        return "Adds JAR with dependencies plugin.";
    }

    @Override
    public void runBody() {
        new XmlManipulator().addFatJarPlugin();
    }
}
