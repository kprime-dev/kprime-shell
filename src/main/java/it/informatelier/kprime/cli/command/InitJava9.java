package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.fs.FsManipulator;

import java.util.HashMap;
import java.util.Map;

public class InitJava9 extends CommandRun {


    public InitJava9() {
        super();
        Map<String,String> args = new HashMap<>();
        setMustArgs(args);
    }

    @Override
    public String getLineDescription() {
        return "Create a new Java9 parent pom file.";
    }

    @Override
    public String getName() {
        return "init-java9";
    }

    @Override
    public void runBody() {
        FsManipulator.initJava9();
    }
}
