package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.fs.FsManipulator;
import it.informatelier.kprime.cli.xml.XmlManipulator;

import java.util.HashMap;
import java.util.Map;

public class InitGarageCommand extends CommandRun {

    public InitGarageCommand() {
        super();
        Map<String,String> args = new HashMap<>();
        args.put(XmlManipulator.InitPomArgs.ARTIFACT_ID.name(),"my-artifact");
        args.put(XmlManipulator.InitPomArgs.GROUP_ID.name(),"my-group");
        args.put(XmlManipulator.InitPomArgs.VERSION.name(),"0.1.0-SNAPSHOT");
        setMustArgs(args);
    }
    @Override
    public String getLineDescription() {
        return "Create a garage project.";
    }

    @Override
    public String getName() {
        return "init-garage";
    }

    @Override
    public void runBody() {
        new XmlManipulator().initPom(getEnvironment());
        new XmlManipulator().addDddDependency();
        new XmlManipulator().addJunit4Dependency();
        new XmlManipulator().addJava8Plugin();
        new XmlManipulator().addResources();
        new XmlManipulator().addReportPlugin();
        FsManipulator.init();
        FsManipulator.addSite();
        FsManipulator.addGarage();
    }
}
