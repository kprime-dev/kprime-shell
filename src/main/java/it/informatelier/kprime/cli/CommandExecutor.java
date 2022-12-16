package it.informatelier.kprime.cli;

import it.informatelier.kprime.cli.command.Commandable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Execute on host operation system a Command.
 */
public class CommandExecutor {

    Config config;

    CommandExecutor(Config config) {
        this.config = config;
    }

    Commandable execute(Commandable commandable) {
        if (commandable==null) return null;
        String commandLine = commandable.getCommandLine();
        try {
            commandable.run();
            String commandResult = commandable.getResult();
            if (commandResult!=null) {
                String message = extractMessage(commandResult);
                List<String> options = extractOptions(commandResult);
                String result = "\n--------\n" + message.replace("\\t", ":::") + "\n----------\n";
                commandable.setOptsArgs(options);
                commandable.setResult(result);
            } else commandable.setResult("No result.");
        } catch (Exception e) {
            commandable.setResult(e.getMessage());
        }
        return commandable;
    }

    private List<String> extractOptions(String result) {
        int msgStart = result.indexOf("options:[")+9;
        int msgEnd = result.indexOf("],ok:");
        List<String> options = null;
        if (msgStart>7 && msgEnd>msgStart) options = Arrays.asList(result.substring(msgStart,msgEnd).split(","));
        else options = new ArrayList<>();
        return options;
    }

    private String extractMessage(String result) {
        int msgStart = result.indexOf("message:")+8;
        int msgEnd = result.indexOf(",messageType:");
        String message = null;
        if (msgStart>7 && msgEnd>msgStart) message = result.substring(msgStart,msgEnd);
        else message = result;
        return message;
    }

}
