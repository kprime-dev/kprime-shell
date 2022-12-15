package it.informatelier.kprime.cli.command;

import java.util.HashMap;
import java.util.Map;

/**
 * Command for create a new war.
 */
final public class NewWarCommand extends Command {

    public static final String MVN_BIN_PATH = "mvnBinPath";
    public static final String GROUP_ID = "groupId";
    public static final String ARTIFACT_ID = "artifactId";
    private static final NewWarCommand command = new NewWarCommand();

    private NewWarCommand() {
        Map<String, String> required = new HashMap<>();
        required.put(ARTIFACT_ID,"");
        required.put(GROUP_ID,"");
        setMustArgs(required);
    }

    public NewWarCommand(Map<String, String> args) {
        super(command.getCommandLine(args));
    }

    public static synchronized NewWarCommand getInstance() { return command; }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @Override
    public void runBody() {}

    @Override
    public String getCommandLine(Map<String, String> args) {
        if (!valid(args,getMustArgs())) return null;
        String commandLineTemplate = args.get(MVN_BIN_PATH) + "mvn archetype:generate " +
                "-DgroupId=%s " +
                "-DartifactId=%s " +
                "-DarchetypeArtifactId=maven-archetype-webapp " +
                "-DinteractiveMode=false";
        String format = String.format(commandLineTemplate, args.get(GROUP_ID), args.get(ARTIFACT_ID));
        setCommandLine(format);
        return format;
    }

    @Override
    public String getLineDescription() {
        return "Executes mvn create new war pom project.";
    }

    @Override
    public String getName() {
        return "new-war";
    }

}
