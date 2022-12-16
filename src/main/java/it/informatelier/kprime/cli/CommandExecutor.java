package it.informatelier.kprime.cli;

import it.informatelier.kprime.cli.command.Commandable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Execute on host operation system a Command.
 */
public class CommandExecutor {

    Config config;

    CommandExecutor(Config config) {
        this.config = config;
    }

    String execute(Commandable commandable) {
        if (commandable==null) return "no commandable.";
        String commandLine = commandable.getCommandLine();
        if (commandLine ==null) {
            commandable.run();
            String result = "\n--------\n"+commandable.getResult().replace("\\t",":::")+"\n----------\n";
            return result;
        }
        System.out.println("execute process ["+commandLine+"]");
        return executeProcess(commandable);
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
