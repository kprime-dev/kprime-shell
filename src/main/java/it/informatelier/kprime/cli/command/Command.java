package it.informatelier.kprime.cli.command;

import java.util.*;

/**
 * Command produced by CommandLineParser.
 * Wraps che command line to execute by CommandExecutor.
 */
public abstract class Command implements Commandable {
    private boolean hasRun = false;
    private String result;
    private String commandLine;
    private List<String> tokens;
    private Map<String,String> mustArgs; // Key, Default
    private List<String> optsArgs;
    private Map<String, String> environment;

    protected Command() {}


    public Command(String commandLine) {
        this.commandLine = commandLine;
        if (commandLine!=null) {  this.tokens = Arrays.asList(commandLine.split(" ")); }
    }

    @Override
    public String getCommandLine() {
        return commandLine;
    }

    @Override
    public void setCommandLine(String commandLine) {
        this.commandLine = commandLine;
        if (commandLine!=null) {  this.tokens = Arrays.asList(commandLine.split(" ")); }
    }

    protected static boolean valid(Map<String, String> args,Map<String,String> requireds) {
        if (args == null) return false;
        Set<String> keyIterator = requireds.keySet();
        for (String key: keyIterator) {
            if (args.get(key)== null) return false;
        }
        return true;
    }

    @Override
    public Map<String,String> getMustArgs() {
        return mustArgs;
    }

    @Override
    public void setMustArgs(Map<String,String> args) {
        this.mustArgs = args;
    }

    @Override
    public List<String> getOptsArgs() {
        return optsArgs;
    }

    @Override
    public void setOptsArgs(List<String> args) {
        this.optsArgs = args;
    }

    @Override
    public void fillWithArgs(Map<String, String> args) {
        this.getCommandLine(args);
    }

    @Override
    public List<String> getTokens() { return Collections.unmodifiableList(this.tokens); }

    @Override
    public String getFirstToken() {
        if (tokens==null || tokens.size()==0) return "";
        return tokens.get(0);}

    @Override
    public List<String> getArgTokens() {
        if (tokens==null || tokens.size()==0) return new ArrayList();
        return tokens.subList(1,tokens.size());
    }

    @Override
    public void run() {
        runBody();
        hasRun = true;
    };

    @Override
    public boolean hasRun() { return hasRun; }

    public abstract void runBody();

    @Override
    public String getResult() { return result; }

    @Override
    public void setResult(String result) { this.result = result; }

    @Override
    public Map<String, String> getEnvironment() {
        return environment;
    }

    @Override
    public void setEnvironment(Map<String, String> environment) {
        this.environment = environment;
    }

    @Override
    public String toString() {
        return "Command{" +
                "hasRun=" + hasRun +
                ", result='" + result + '\'' +
                ", commandLine='" + commandLine + '\'' +
                ", tokens=" + tokens +
                ", mustArgs=" + mustArgs +
                ", optsArgs=" + optsArgs +
                ", environment=" + environment +
                '}';
    }
}
