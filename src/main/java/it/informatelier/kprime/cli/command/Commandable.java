package it.informatelier.kprime.cli.command;

import java.util.List;
import java.util.Map;

public interface Commandable extends Runnable {

    void setEnvironment(Map<String, String> environment);

    Map<String, String> getEnvironment();

    String getCommandLine(Map<String, String> args);

    String getCommandLine();

    void setCommandLine(String commandLine);

    default String getLineDescription() {
        return "no-description";
    }

    default String getName() {
        return "no-name";
    }

    Map<String,String> getMustArgs();

    List<String> getOptsArgs();

    List<String> getTokens();

    String getFirstToken();

    List<String> getArgTokens();

    void setMustArgs(Map<String,String> args);

    void setOptsArgs(List<String> args);

    void fillWithArgs(Map<String, String> mustArgs);

    String getResult();

    void setResult(String result);

    boolean hasRun();
}
