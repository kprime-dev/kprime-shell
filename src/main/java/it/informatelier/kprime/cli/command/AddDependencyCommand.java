package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.xml.XmlManipulator;

import java.util.HashMap;
import java.util.Map;

public class AddDependencyCommand extends Command {

    public static final String GROUP_ID = "groupId";
    public static final String ARTIFACT_ID = "artifactId";
    public static final String VERSION = "version";
    public static final String SCOPE = "scope";
    private String groupId;
    private String artifactId;
    private String scope;
    private String version;

    private static AddDependencyCommand command = new AddDependencyCommand();

    public AddDependencyCommand(Map<String, String> args) {
        super(command.getCommandLine(args));
        setMustArgs(getMustArgs());
    }

    public AddDependencyCommand() {
        setMustArgs(getMustArgsMap());
    }

    private Map<String, String> getMustArgsMap() {
        Map<String, String> required = new HashMap<>();
        required.put(ARTIFACT_ID,artifactId);
        required.put(GROUP_ID,groupId);
        required.put(VERSION,version);
        return required;
    }

    public static AddDependencyCommand getInstance() { return command; }

    @Override
    public void runBody() {
        new XmlManipulator().addDependency(groupId, artifactId, version, scope);
    }

    @Override
    public String getCommandLine() {
        return null;
    }

    @Override
    public String getCommandLine(Map<String, String> args) {
        this.getCommandLine();
        groupId = args.get(GROUP_ID);
        artifactId = args.get(ARTIFACT_ID);
        version = args.get(VERSION);
        scope = args.get(SCOPE);
        if (scope ==null) {
            scope = "compile";
        }
        return null;
    }

    @Override
    public void setCommandLine(String commandLine) {
        // extracts tokens
        // compute groupid, artifactid, version
        // sets single arg
    }

    @Override
    public String getLineDescription() {
        return "Add a dependency to current pom.";
    }

    @Override
    public String getName() {
        return "add-dependency";
    }


}
