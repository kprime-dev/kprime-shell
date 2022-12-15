package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.fs.FsManipulator;
import it.informatelier.kprime.cli.xml.XmlManipulator;

import java.util.HashMap;
import java.util.Map;

public class InitKotlinCommand extends CommandRun {


    public InitKotlinCommand() {
        super();
        Map<String,String> args = new HashMap<>();
        args.put(XmlManipulator.InitPomArgs.ARTIFACT_ID.name(),"my-artifact");
        args.put(XmlManipulator.InitPomArgs.GROUP_ID.name(),"my-group");
        args.put(XmlManipulator.InitPomArgs.VERSION.name(),"0.1.0-SNAPSHOT");
        setMustArgs(args);
    }

    @Override
    public String getLineDescription() {
        return "Create a new kotlin pom file.";
    }

    @Override
    public String getName() {
        return "init-kotlin";
    }

    @Override
    public void runBody() {
        FsManipulator.initKotlinPom();
        FsManipulator.initKotlinTree();
        FsManipulator.initKotlinSample();
    }
}
