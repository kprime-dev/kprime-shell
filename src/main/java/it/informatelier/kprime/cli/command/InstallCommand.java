package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.fs.FsManipulator;

import java.util.HashMap;
import java.util.Map;

public class InstallCommand extends CommandRun {
    public InstallCommand() {
        super();
        Map<String,String> args = new HashMap<>();
        args.put("source","/home/nipe/Tmp");
        setMustArgs(args);
    }

    @Override
    public String getName() {
        return "install";
    }

    @Override
    public String getLineDescription() {
        return "Copy source dir in local dir.";
    }

    @Override
    public void runBody() {
        String sourceDir = getEnvironment().get("source");
        FsManipulator.installDir(sourceDir);
    }
}
