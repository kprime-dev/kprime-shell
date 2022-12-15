package it.informatelier.kprime.cli.command;

import java.util.Map;

public abstract class CommandRun extends Command{


    @Override
    public String getCommandLine(Map<String, String> args) {
        return null;
    }

    @Override
    public String getCommandLine() {
        return null;
    }

}
