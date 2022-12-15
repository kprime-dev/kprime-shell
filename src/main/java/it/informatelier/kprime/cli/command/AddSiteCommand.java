package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.fs.FsManipulator;
import it.informatelier.kprime.cli.xml.XmlManipulator;

public class AddSiteCommand extends CommandRun {
    @Override
    public String getName() {
        return "add-site";
    }

    @Override
    public String getLineDescription() {
        return "Adds site folders to project.";
    }

    @Override
    public void runBody() {
      new XmlManipulator().addReportPlugin();
      FsManipulator.addSite();
    }
}
