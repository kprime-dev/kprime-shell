package it.informatelier.kprime.cli.command;

import java.util.Map;

public class MockCommand extends Command {

    public MockCommand(String cmdLine) {
        setCommandLine(cmdLine);
    }

    @Override
    public void runBody() {}

    @Override
    public String getCommandLine(Map<String, String> args) {
        return null;
    }

    @Override
    public String getLineDescription() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
