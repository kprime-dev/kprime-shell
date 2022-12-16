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
        commandable.run();
        String message = extractMessage(commandable.getResult());
        List<String> options = extractOptions(commandable.getResult());
        String result = "\n--------\n"+message.replace("\\t",":::")+"\n----------\n";
        commandable.setOptsArgs(options);
        commandable.setResult(result);
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

    private String executeProcess(Commandable commandable) {
        String output = "";
        try {
            String[] env = {
                    "JAVA_HOME="+config.get("JAVA_HOME")
            };
            Process p = Runtime.getRuntime().exec(commandable.getCommandLine(), env);
            InputStream inputStream = p.getInputStream();
            InputStream errorStream = p.getErrorStream();
            OutputStream stdOutput = p.getOutputStream();

            ThreadedStreamHandler inputStreamHandler = new ThreadedStreamHandler(inputStream, stdOutput);
            ThreadedStreamHandler errorStreamHandler = new ThreadedStreamHandler(errorStream);
            inputStreamHandler.start();
            errorStreamHandler.start();
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            inputStreamHandler.interrupt();
            errorStreamHandler.interrupt();
            try {
                inputStreamHandler.join();
                errorStreamHandler.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            output += inputStreamHandler.getOutputBuffer().toString();
            output += errorStreamHandler.getOutputBuffer().toString();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return output;
    }
}
