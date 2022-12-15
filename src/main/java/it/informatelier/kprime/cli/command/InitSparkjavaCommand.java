package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.fs.FsManipulator;
import it.informatelier.kprime.cli.xml.XmlManipulator;

import java.util.HashMap;
import java.util.Map;

public class InitSparkjavaCommand extends CommandRun {

    public InitSparkjavaCommand() {
        super();
        Map<String,String> args = new HashMap<>();
        args.put(XmlManipulator.InitPomArgs.ARTIFACT_ID.name(),"my-artifact");
        args.put(XmlManipulator.InitPomArgs.GROUP_ID.name(),"my-group");
        args.put(XmlManipulator.InitPomArgs.VERSION.name(),"0.1.0-SNAPSHOT");
        setMustArgs(args);
    }
    @Override
    public String getLineDescription() {
        return "Create a sparkjava project.";
    }

    @Override
    public String getName() {
        return "init-sparkjava";
    }

    @Override
    public void runBody() {
        new XmlManipulator().initPom(getEnvironment());
        new XmlManipulator().addSparkjavaDependency();
        new XmlManipulator().addSlf4jDependency();
        new XmlManipulator().addJunit4Dependency();
        new XmlManipulator().addJava8Plugin();
        new XmlManipulator().addFatJarPlugin();
        FsManipulator.init();
        FsManipulator.addHelloSparkjava();
    }
}
