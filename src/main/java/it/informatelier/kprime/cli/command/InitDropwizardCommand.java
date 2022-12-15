package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.fs.FsManipulator;
import it.informatelier.kprime.cli.xml.XmlManipulator;

import java.util.HashMap;
import java.util.Map;

public class InitDropwizardCommand extends CommandRun {
    public InitDropwizardCommand() {
        super();
        Map<String,String> args = new HashMap<>();
        args.put(XmlManipulator.InitPomArgs.ARTIFACT_ID.name(),"my-artifact");
        args.put(XmlManipulator.InitPomArgs.GROUP_ID.name(),"my-group");
        args.put(XmlManipulator.InitPomArgs.VERSION.name(),"0.1.0-SNAPSHOT");
        setMustArgs(args);
    }

    @Override
    public String getName() {
        return "init-dropwizard";
    }

    @Override
    public String getLineDescription() {
        return "Create a Dropwizard project.";
    }

    @Override
    public void runBody() {
        new XmlManipulator().initPom(getEnvironment());
        new XmlManipulator().addDropwizardDependency();
        new XmlManipulator().addJunit4Dependency();
        new XmlManipulator().addDropwizardPlugin();
        new XmlManipulator().addResources();
        FsManipulator.init();
        FsManipulator.addResources();
        FsManipulator.addHelloDropwizard();
    }
}
