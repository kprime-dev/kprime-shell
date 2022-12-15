package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.xml.XmlManipulator;

public class AddSkinCommand extends CommandRun {
    @Override
    public String getName() {
        return "add-skin";
    }

    @Override
    public String getLineDescription() {
        return "Adds Skin Fluido to site.";
    }

    @Override
    public void runBody() {
        new XmlManipulator().addSkinFluido();
    }
}
