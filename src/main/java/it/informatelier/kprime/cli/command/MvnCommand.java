package it.informatelier.kprime.cli.command;

import java.util.Map;

public class MvnCommand extends Command {

    public MvnCommand() {}

    public MvnCommand(String commandLine) {
        super(commandLine);
    }

    @Override
    public void runBody() {}

    @Override
    public String getCommandLine(Map<String, String> args) {
        return "mvn "+getCommandLine();
    }

    @Override
    public String getLineDescription() {
        return "Executes a maven command.";
    }

    @Override
    public String getName() {
        return "anyothercommand";
    }

}
