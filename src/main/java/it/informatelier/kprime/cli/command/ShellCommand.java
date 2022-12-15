package it.informatelier.kprime.cli.command;

import java.util.Map;

public class ShellCommand extends Command {

    public ShellCommand() {}

    public ShellCommand(String commandLine) {
        super(commandLine);
    }

    @Override
    public void runBody() {}

    @Override
    public String getCommandLine(Map<String, String> args) {
        return getCommandLine();
    }

    @Override
    public String getLineDescription() {
        return "Executes a shell command.";
    }

    @Override
    public String getName() {
        return "anyothercommand";
    }

}
