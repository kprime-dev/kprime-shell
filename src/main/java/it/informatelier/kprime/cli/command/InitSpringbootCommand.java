package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.fs.FsManipulator;
import it.informatelier.kprime.cli.xml.XmlManipulator;

import java.util.HashMap;
import java.util.Map;

public class InitSpringbootCommand extends CommandRun {

    public InitSpringbootCommand() {
        super();
        Map<String,String> args = new HashMap<>();
        args.put(XmlManipulator.InitPomArgs.ARTIFACT_ID.name(),"my-artifact");
        args.put(XmlManipulator.InitPomArgs.GROUP_ID.name(),"my-group");
        args.put(XmlManipulator.InitPomArgs.VERSION.name(),"0.1.0-SNAPSHOT");
        setMustArgs(args);
    }

    @Override
    public String getLineDescription() {
        return "Create a springboot project.";
    }

    @Override
    public String getName() {
        return "init-springboot";
    }

    @Override
    public void runBody() {
        new XmlManipulator().initPom(getEnvironment());
        new XmlManipulator().addSpringBootParent();
        new XmlManipulator().addSpringBootDependency();
        new XmlManipulator().addJunit4Dependency();
        new XmlManipulator().addJava8Plugin();
        new XmlManipulator().addSpringBootPlugin();
        FsManipulator.init();
        FsManipulator.addHelloSpringBoot();
    }
}
