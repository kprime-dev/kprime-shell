package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.fs.FsManipulator;

import java.util.HashMap;
import java.util.Map;

public class AddToolchains extends CommandRun {


    public AddToolchains() {
        super();
        Map<String,String> args = new HashMap<>();
        setMustArgs(args);
    }

    @Override
    public String getLineDescription() {
        return "Adds maven toolchains file.";
    }

    @Override
    public String getName() {
        return "add-toolchains";
    }

    @Override
    public void runBody() {
        FsManipulator.addToolchains();
    }
}
